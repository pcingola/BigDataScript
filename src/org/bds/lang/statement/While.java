package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
import org.bds.scope.Scope;
import org.bds.util.Gpr;

/**
 * While statement
 *
 * @author pcingola
 */
public class While extends Statement {

	Expression condition;
	Statement statement;

	public While(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "while")) idx++; // 'while'
		if (isTerminal(tree, idx, "(")) idx++; // '('
		if (!isTerminal(tree, idx, ")")) condition = (Expression) factory(tree, idx++); // Is this a 'while:condition'? (could be empty)
		if (isTerminal(tree, idx, ")")) idx++; // ')'
		statement = (Statement) factory(tree, idx);
	}

	/**
	 * Evaluate condition
	 */
	boolean runCondition(BdsThread bdsThread, boolean first) {
		if (condition == null) return true;

		bdsThread.run(condition);

		// If we are recovering from a checkpoint, we have to get
		// into the loop's statements to find the node where the
		// program created the checkpoint
		if (bdsThread.isCheckpointRecover()) return first;

		// Return map form 'condition'
		return bdsThread.popBool();
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {

		boolean first = true;
		while (runCondition(bdsThread, first)) { // Loop condition
			first = false;

			bdsThread.run(statement);

			switch (bdsThread.getRunState()) {
			case OK: // OK continue
			case CHECKPOINT_RECOVER:
				break;

			case BREAK: // Break from loop
				bdsThread.setRunState(RunState.OK);
				return;

			case CONTINUE: // Continue: Nothing to do, just continue with the next iteration
				bdsThread.setRunState(RunState.OK);
				break;

			case FATAL_ERROR:
			case RETURN:
			case EXIT:
				return;

			default:
				throw new RuntimeException("Unhandled RunState: " + bdsThread.getRunState());
			}
		}
	}

	@Override
	public String toString() {
		return "while(  " + condition + " ) {\n" //
				+ Gpr.prependEachLine("\t", statement.toString()) //
				+ "\n}" //
		;
	}

	@Override
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		if (condition != null) condition.returnType(scope);
		if ((condition != null) && !condition.isBool()) compilerMessages.add(this, "While loop condition must be a bool expression", MessageType.ERROR);
	}

}
