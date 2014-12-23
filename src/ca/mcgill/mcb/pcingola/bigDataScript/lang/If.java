package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * If statement
 *
 * @author pcingola
 */
public class If extends Statement {

	Expression condition;
	Statement statement;
	Statement elseStatement;

	public If(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate condition
	 */
	boolean evalCondition(BigDataScriptThread bdsThread) {
		if (condition == null) return true;
		condition.run(bdsThread);
		return popBool(bdsThread);
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
	 * Run the program
	 */
	@Override
	protected void runStep(BigDataScriptThread bdsThread) {
		if (evalCondition(bdsThread)) {
			statement.run(bdsThread);
		} else if (elseStatement != null) {
			elseStatement.run(bdsThread);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("if( ");
		if (condition != null) sb.append(condition);
		sb.append(" ) {\n");
		sb.append(statement);
		if (elseStatement != null) {
			sb.append("\n} else {\n");
			sb.append(elseStatement);
		}
		sb.append("\n}\n");

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
