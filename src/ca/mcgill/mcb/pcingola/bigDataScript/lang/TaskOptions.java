package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

/**
 * Options for 'task' command
 * 
 * @author pcingola
 */
public class TaskOptions extends ExpressionList {

	public TaskOptions(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate: Returns 'true' if all boolean expressions are 'true'.
	 * 
	 * Note: We only care about the value of bool expressions
	 * Note: All expressions in the list are evaluated, even if the first one is false
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		boolean sat = true;

		for (Expression expr : expressions) {
			Object value = expr.eval(csThread);

			// All boolean expressions must be "true"
			if (expr.getReturnType().isBool()) sat &= (Boolean) value;
		}

		return sat;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		for (Expression e : expressions)
			if (!(e instanceof ExpressionAssignment)) {
				if (!e.getReturnType().isBool()) compilerMessages.add(this, "Only assignment or boolean expressions are allowed in task options", MessageType.ERROR);
			}
	}

}
