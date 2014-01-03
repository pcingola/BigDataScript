package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
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

	public ForLoopList(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'for'
		// child[1] = '('
		beginVarDecl = (VarDeclaration) factory(tree, 2);
		// child[3] = ':'
		expression = (Expression) factory(tree, 4);
		// child[5] = ')'
		statement = (Statement) factory(tree, 6);
	}

	public Type returnType(Scope scope) {
		return expression.returnType(scope);
	}

	/**
	 * Run 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		// Loop initialization
		beginVarDecl.run(csThread);
		String varName = beginVarDecl.getVarInit()[0].getVarName();
		ScopeSymbol varSym = csThread.getScope().getSymbol(varName);

		// Evaluate list
		Object res = expression.eval(csThread);

		// Find (or create) a collection we can iterate on
		Collection values = null;
		if (res instanceof List) values = (List) res;
		else if (res instanceof Map) {
			// Create a sorted list of values
			ArrayList list = new ArrayList();
			list.addAll(((Map) res).values());
			Collections.sort(list);
			values = list;
		} else {
			values = new LinkedList<Object>();
			values.add(res);
		}

		// Iterate on collection
		for (Object o : values) {
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
