package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

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

	public ForLoopList(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Variable declaration (Loop initialization)
	 * @param csThread
	 * @return
	 */
	protected ScopeSymbol initBeginDecl(BigDataScriptThread csThread) {
		beginVarDecl.run(csThread);
		String varName = beginVarDecl.getVarInit()[0].getVarName();
		ScopeSymbol varSym = csThread.getScope().getSymbol(varName);
		return varSym;
	}

	/**
	 * Iterable counter (current position in iterator)
	 * @param csThread
	 * @return
	 */
	protected ScopeSymbol initIterableCounter(BigDataScriptThread csThread) {
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
	 * @param csThread
	 * @param varSym
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ArrayList initIterableValues(BigDataScriptThread csThread, ScopeSymbol varSym) {
		// Are we recovering state from a checkpoint file?
		if (csThread.isCheckpointRecover()) {
			ScopeSymbol ssIterableList = csThread.getScope().getSymbol(iterableListName);
			return (ArrayList) ssIterableList.getValue();
		}

		// Evaluate list
		Object res = expression.eval(csThread);

		//---
		// Find (or create) a collection we can iterate on
		//---
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
		csThread.getScope().add(ssIterableList);

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

	public Type returnType(Scope scope) {
		return expression.returnType(scope);
	}

	/**
	 * Run 
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		ScopeSymbol varSym = initBeginDecl(csThread);
		ArrayList iterableValues = initIterableValues(csThread, varSym);
		ScopeSymbol iterableCount = initIterableCounter(csThread);

		// First element to iterate.
		// Note: This could be set by a checkpoint recovery, so we have to read it from the scope
		long interStart = (Long) iterableCount.getValue(); // 

		// Iterate on collection
		for (int iter = (int) interStart; iter < iterableValues.size(); iter++) {
			iterableCount.setValue(iter); // Update scope symbol (so that checkpoints can save state)

			// Get the element we are iterating on
			Object o = iterableValues.get(iter);
			varSym.setValue(varSym.getType().cast(o));

			RunState rstate = statement.run(csThread); // Loop statement

			switch (rstate) {
			case OK:
			case CHECKPOINT_RECOVER:
				break;

			case BREAK: // Break from loop, OK done
				return RunState.OK;

			case FATAL_ERROR:
			case RETURN: // Return
			case EXIT: // Exit program
				return rstate;

			case CONTINUE: // Nothing to do, just continue with the next iteration
				break;

			default:
				throw new RuntimeException("Unhandled RunState: " + rstate);
			}
		}

		return RunState.OK;
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
