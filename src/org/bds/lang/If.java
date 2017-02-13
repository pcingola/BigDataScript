package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.util.Gpr;

/**
 * If statement
 *
 * @author pcingola
 */
public class If extends Statement {

	Expression condition;
	Statement statement;
	Statement elseStatement;

	public If(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "if")) idx++; // 'if'
		if (isTerminal(tree, idx, "(")) idx++; // '('
		if (!isTerminal(tree, idx, ")")) condition = (Expression) factory(tree, idx++);
		if (isTerminal(tree, idx, ")")) idx++; // ')'
		statement = (Statement) factory(tree, idx++);

		// Do we have an 'else' statement?
		idx = findIndex(tree, "else", idx);
		if (idx > 0) elseStatement = (Statement) factory(tree, idx + 1);
	}

	/**
	 * Evaluate condition
	 */
	boolean runCondition(BdsThread bdsThread) {
		if (condition == null) return true;

		bdsThread.run(condition);

		if (bdsThread.isCheckpointRecover()) return true;

		// Return value form 'condition'
		return popBool(bdsThread);
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {

		if (bdsThread.isCheckpointRecover()) {
			runCondition(bdsThread);
			if (bdsThread.isCheckpointRecover()) bdsThread.run(statement);
			if (bdsThread.isCheckpointRecover()) bdsThread.run(elseStatement);
			return;
		}

		if (runCondition(bdsThread)) {
			bdsThread.run(statement);
		} else if (elseStatement != null) {
			bdsThread.run(elseStatement);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("if( ");
		if (condition != null) sb.append(condition);
		sb.append(" ) {\n");
		if (statement != null) sb.append(Gpr.prependEachLine("\t", statement.toString()));
		if (elseStatement != null) {
			sb.append("\n} else {\n");
			sb.append(Gpr.prependEachLine("\t", elseStatement.toString()));
		}
		sb.append("\n}");

		return sb.toString();
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		Type retType = condition.returnType(scope);
		if ((condition != null) //
				&& !condition.isBool() //
				&& (retType != null) //
				&& !retType.canCast(Type.BOOL)//
		) compilerMessages.add(this, "Condition in 'if' statement must be a bool expression", MessageType.ERROR);
	}
}
