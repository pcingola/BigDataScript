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
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
import org.bds.scope.ScopeSymbol;
import org.bds.symbol.SymbolTable;
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
	protected Value initBeginDecl(BdsThread bdsThread) {
		bdsThread.run(beginVarDecl);
		String varName = beginVarDecl.getVarInit()[0].getVarName();
		Value valSym = bdsThread.getScope().getValue(varName);
		return valSym;
	}

	/**
	 * Iterable counter (current position in iterator)
	 */
	protected Value initIterableCounter(BdsThread bdsThread) {
		// Are we recovering state from a checkpoint file?
		if (bdsThread.isCheckpointRecover()) {
			Value ssIterableCount = bdsThread.getScope().getValue(iterableCountName);
			return ssIterableCount;
		}

		// Create counter
		iterableCountName = ScopeSymbol.INTERNAL_SYMBOL_START + "iterableCount." + getFileName() + "." + getLineNum() + "." + getCharPosInLine();
		Type iterableCountType = Types.INT;
		Value ssIterableCount = iterableCountType.newValue(0L);
		bdsThread.getScope().add(iterableCountName, ssIterableCount);
		return ssIterableCount;
	}

	/**
	 * Iterable values (list of elements to iterate)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ValueList initIterableValues(BdsThread bdsThread, Value varSym) {
		// Evaluate list
		bdsThread.run(expression);

		// Are we recovering state from a checkpoint file?
		if (bdsThread.isCheckpointRecover()) {
			ValueList ssIterableList = (ValueList) bdsThread.getScope().getValue(iterableListName);
			return ssIterableList;
		}

		//---
		// Find (or create) a collection we can iterate on
		//---
		Value res = bdsThread.pop();
		Type resType = res.getType();
		List iterableValues = new ArrayList();
		if (resType.isList()) iterableValues.addAll((List) res.get());
		else if (resType.isMap()) {
			// Create a sorted list of values
			iterableValues.addAll(((Map) res.get()).values());
			Collections.sort(iterableValues);
		} else {
			// Single object
			iterableValues.add(res.get());
		}

		//---
		// Iterable list
		//---
		iterableListName = ScopeSymbol.INTERNAL_SYMBOL_START + "iterableList." + getFileName() + "." + getLineNum() + "." + getCharPosInLine();
		Type iterableListType = TypeList.get(varSym.getType());
		ValueList ssIterableList = new ValueList(iterableListType);
		ssIterableList.set(iterableValues);
		bdsThread.getScope().add(iterableListName, ssIterableList);
		return ssIterableList;
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
	public Type returnType(SymbolTable symtab) {
		returnType = expression.returnType(symtab);
		return returnType;
	}

	/**
	 * Run
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		Value varSym = initBeginDecl(bdsThread);
		ValueList iterableValues = initIterableValues(bdsThread, varSym);
		Value iterableCount = initIterableCounter(bdsThread);

		// First element to iterate.
		// Note: This could be set by a checkpoint recovery, so we have to read it from the scope
		ValueInt counter = (ValueInt) iterableCount;
		long interStart = counter.asInt(); //

		// Iterate on collection
		for (int idx = (int) interStart; idx < iterableValues.size(); idx++) {
			counter.set(idx); // Update scope symbol (so that checkpoints can save state)

			// Get the element we are iterating on
			Value v = iterableValues.getValue(idx);
			varSym.setValue(varSym.getType().cast(v));

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
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		Type exprType = getReturnType();

		if (statement == null) compilerMessages.add(this, "Empty for statement", MessageType.ERROR);

		if (exprType != null) {
			if (!exprType.isList() && !exprType.isMap()) {
				compilerMessages.add(this, "Expression should return a list or a map", MessageType.ERROR);
			} else if (beginVarDecl != null) {
				Type baseType;

				if (exprType.isList()) baseType = ((TypeList) exprType).getElementType();
				else if (exprType.isMap()) baseType = ((TypeMap) exprType).getValueType();
				else {
					compilerMessages.add(this, "Expression should return a list or a map", MessageType.ERROR);
					return;
				}

				Type varType = beginVarDecl.getType();
				if ((baseType != null) && !baseType.canCastTo(varType)) {
					compilerMessages.add(this, "Cannot cast " + baseType + " to " + varType, MessageType.ERROR);
				}
			}
		}
	}
}
