package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;
import org.bds.util.Gpr;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForLoopList extends StatementWithScope {

	// Note:	It is important that 'begin' node is type-checked before the others in order to
	//			add variables to the scope before ForCondition, ForEnd or Statement uses them.
	//			So the field name should be alphabetically sorted before the other (that's why
	//			I call it 'begin' and not 'init').
	//			Yes, it's a horrible hack.
	VarDeclaration beginVarDecl;
	Expression expression;
	Statement statement;
	String iterableListName;
	String iterableCountName;

	public ForLoopList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Variable declaration (Loop initialization)
	 */
	protected ScopeSymbol initBeginDecl(BdsThread bdsThread) {
		bdsThread.run(beginVarDecl);
		String varName = beginVarDecl.getVarInit()[0].getVarName();
		ScopeSymbol varSym = bdsThread.getScope().getSymbol(varName);
		return varSym;
	}

	/**
	 * Iterable counter (current position in iterator)
	 */
	protected ScopeSymbol initIterableCounter(BdsThread csThread) {
		// Are we recovering state from a checkpoint file?
		if (csThread.isCheckpointRecover()) {
			ScopeSymbol ssIterableCount = csThread.getScope().getSymbol(iterableCountName);
			return ssIterableCount;
		}

		// Create counter
		iterableCountName = ScopeSymbol.INTERNAL_SYMBOL_START + "iterableCount." + getFileName() + "." + getLineNum() + "." + getCharPosInLine();
		Type iterableCountType = Type.INT;
		ScopeSymbol ssIterableCount = new ScopeSymbol(iterableCountName, iterableCountType, 0L);
		csThread.getScope().add(ssIterableCount);
		return ssIterableCount;
	}

	/**
	 * Iterable values (list of elements to iterate)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ArrayList initIterableValues(BdsThread bdsThread, ScopeSymbol varSym) {
		// Evaluate list
		bdsThread.run(expression);

		// Are we recovering state from a checkpoint file?
		if (bdsThread.isCheckpointRecover()) {
			ScopeSymbol ssIterableList = bdsThread.getScope().getSymbol(iterableListName);
			return (ArrayList) ssIterableList.getValue();
		}

		//---
		// Find (or create) a collection we can iterate on
		//---
		Object res = bdsThread.pop();
		ArrayList iterableValues = new ArrayList();
		if (res instanceof List) iterableValues.addAll((List) res);
		else if (res instanceof Map) {
			// Create a sorted list of values
			iterableValues.addAll(((Map) res).values());
			Collections.sort(iterableValues);
		} else {
			// Single object
			iterableValues.add(res);
		}

		//---
		// Iterable list
		//---
		iterableListName = ScopeSymbol.INTERNAL_SYMBOL_START + "iterableList." + getFileName() + "." + getLineNum() + "." + getCharPosInLine();
		Type iterableListType = TypeList.get(varSym.getType());
		ScopeSymbol ssIterableList = new ScopeSymbol(iterableListName, iterableListType, iterableValues);
		bdsThread.getScope().add(ssIterableList);

		return iterableValues;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;

		if (isTerminal(tree, idx, "for")) idx++; // 'for'
		if (isTerminal(tree, idx, "(")) idx++; // '('
		if (!isTerminal(tree, idx, ":")) beginVarDecl = (VarDeclaration) factory(tree, idx++); // Is this a 'for:beginVarDecl'?
		if (isTerminal(tree, idx, ":")) idx++; // ':'
		if (!isTerminal(tree, idx, ";")) expression = (Expression) factory(tree, idx++); // Is this a 'for:expression'?
		if (isTerminal(tree, idx, ")")) idx++; // ')'

		statement = (Statement) factory(tree, idx++);
	}

	@Override
	public Type returnType(Scope scope) {
		return expression.returnType(scope);
	}

	/**
	 * Run
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void runStep(BdsThread bdsThread) {
		ScopeSymbol varSym = initBeginDecl(bdsThread);
		ArrayList iterableValues = initIterableValues(bdsThread, varSym);
		ScopeSymbol iterableCount = initIterableCounter(bdsThread);

		// First element to iterate.
		// Note: This could be set by a checkpoint recovery, so we have to read it from the scope
		long interStart = (Long) iterableCount.getValue(); //

		// Iterate on collection
		for (int iter = (int) interStart; iter < iterableValues.size(); iter++) {
			iterableCount.setValue(iter); // Update scope symbol (so that checkpoints can save state)

			// Get the element we are iterating on
			Object o = iterableValues.get(iter);
			varSym.setValue(varSym.getType().cast(o));

			bdsThread.run(statement); // Loop statement

			switch (bdsThread.getRunState()) {
			case OK:
			case CHECKPOINT_RECOVER:
				break;

			case BREAK: // Break from loop
				bdsThread.setRunState(RunState.OK);
				return;

			case CONTINUE: // Continue: Nothing to do, just continue with the next iteration
				bdsThread.setRunState(RunState.OK);
				break;

			case FATAL_ERROR:
			case RETURN: // Return
			case EXIT: // Exit program
				return;

			default:
				throw new RuntimeException("Unhandled RunState: " + bdsThread.getRunState());
			}
		}
	}

	@Override
	public String toString() {
		return "for( " + beginVarDecl + " : " + expression + " ) {\n" //
				+ Gpr.prependEachLine("\t", statement.toString()) //
				+ "}" //
		;
	}

	@Override
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		Type exprType = returnType(scope);

		if (statement == null) compilerMessages.add(this, "Empty for statement", MessageType.ERROR);

		if (exprType != null) {
			if (!exprType.isList() && !exprType.isMap()) compilerMessages.add(this, "Expression should return a list or a map", MessageType.ERROR);
			else if (beginVarDecl != null) {
				TypeList exprListType = (TypeList) exprType;
				Type baseType = exprListType.getBaseType();
				Type varType = beginVarDecl.getType();

				if ((baseType != null) && !baseType.canCast(varType)) compilerMessages.add(this, "Cannot cast " + baseType + " to " + varType, MessageType.ERROR);
			}
		}
	}
}
