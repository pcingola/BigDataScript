package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

/**
 * While statement
 * 
 * @author pcingola
 */
public class While extends Statement {

	Expression condition;
	Statement statement;

	public While(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'while'
		// child[1] = '('		
		condition = (Expression) factory(tree, 2);
		// child[3] = ')'		
		statement = (Statement) factory(tree, 4);
	}

	/**
	 * Run the program
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		while (condition.evalBool(csThread)) {
			RunState rstate = statement.run(csThread);

			switch (rstate) {
			case OK: // OK continue
			case CHECKPOINT_RECOVER:
				break;

			case BREAK:
				return RunState.OK;

			case RETURN:
			case EXIT:
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
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		condition.returnType(scope);

		if ((condition != null) && !condition.isBool()) compilerMessages.add(this, "While loop condition must be a bool expression", MessageType.ERROR);
	}

}
