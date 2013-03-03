package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

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
	@SuppressWarnings("rawtypes")
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		// Loop initialization
		beginVarDecl.run(csThread);
		String varName = beginVarDecl.getVarInit()[0].getVarName();
		ScopeSymbol varSym = csThread.getScope().getSymbol(varName);

		// Evaluate list
		ArrayList list = (ArrayList) expression.eval(csThread);

		for (Object o : list) {
			varSym.setValue(varSym.getType().cast(o));

			RunState rstate = statement.run(csThread); // Loop statement

			switch (rstate) {
			case OK:
			case CHECKPOINT_RECOVER:
				break;

			case BREAK: // Break from loop, OK done
				return RunState.OK;

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
			if (!exprType.isList()) compilerMessages.add(this, "Expression should return a list", MessageType.ERROR);
			else if (beginVarDecl != null) {
				TypeList exprListType = (TypeList) exprType;
				Type baseType = exprListType.getBaseType();
				Type varType = beginVarDecl.getType();

				if ((baseType != null) && !baseType.canCast(varType)) compilerMessages.add(this, "Cannot cast " + baseType + " to " + varType, MessageType.ERROR);
			}
		}
	}
}
