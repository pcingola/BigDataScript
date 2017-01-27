package org.bds.lang;

import java.util.ArrayList;
import java.util.List;

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
public class Switch extends Statement {

	Expression condition;
	List<Expression> caseExpression;
	List<Statement> caseStatements;
	Statement defaultStatement;

	public Switch(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "switch")) idx++; // 'switch'
		if (isTerminal(tree, idx, "(")) idx++; // '('
		if (!isTerminal(tree, idx, ")")) condition = (Expression) factory(tree, idx++);
		if (isTerminal(tree, idx, ")")) idx++; // ')'
		if (isTerminal(tree, idx, "{")) idx++; // '{'

		while(true) {
			idx = findIndex(tree, "case", idx);
			if (idx < 0) break; // No (other) case statements found
			Expression expr = (Expression) factory(tree, ++idx);
			if (isTerminal(tree, idx, ":")) idx++; // ':'
			
			List<Statement> stats = new ArrayList<>();
			while( true) {
				if (isTerminal(tree, idx, "case")|| isTerminal(tree, idx, "break")||isTerminal(tree, idx, "default")) 
					break;
				
				Statement stat = (Statement) factory(tree, idx + 1);
				stats.add(stat);
			}
			
			Gpr.debug(idx+"\t" + tree.getChild(idx));
		}
		
		// Do we have an 'default' statement?
		idx = findIndex(tree, "default", idx);
		if (idx > 0) defaultStatement = (Statement) factory(tree, idx + 1);
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
			//if (bdsThread.isCheckpointRecover()) bdsThread.run(statement);
			if (bdsThread.isCheckpointRecover()) bdsThread.run(defaultStatement);
			return;
		}

		if (runCondition(bdsThread)) {
			// bdsThread.run(statement);
		} else if (defaultStatement != null) {
			bdsThread.run(defaultStatement);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("if( ");
		if (condition != null) sb.append(condition);
		sb.append(" ) {\n");
		// sb.append(Gpr.prependEachLine("\t", statement.toString()));
		if (defaultStatement != null) {
			sb.append("\n} else {\n");
			sb.append(Gpr.prependEachLine("\t", defaultStatement.toString()));
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
