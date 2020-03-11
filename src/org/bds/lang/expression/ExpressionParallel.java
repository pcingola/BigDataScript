package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.Args;
import org.bds.lang.statement.FunctionCall;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.StatementExpr;

/**
 * A 'par' expression
 *
 * @author pcingola
 */
public class ExpressionParallel extends ExpressionTask {

	private static final long serialVersionUID = 2497612763467835459L;

	public ExpressionParallel(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		asmPushDeps = false;
	}

	/**
	 * Extract a functionCall (if any)
	 */
	FunctionCall getFunctionCall() {
		if (statement instanceof FunctionCall) return (FunctionCall) statement;

		// May be it's a statementExpr that contains a function call
		if (statement instanceof StatementExpr) {
			Expression expr = ((StatementExpr) statement).getExpression();
			if (expr instanceof FunctionCall) return (FunctionCall) expr;
		}

		return null;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		idx++; // 'task' keyword

		// Do we have any task options?
		if (tree.getChild(idx).getText().equals("(")) {
			int lastIdx = indexOf(tree, ")");

			options = new ExpressionTaskOptions(this, null);
			options.parse(tree, ++idx, lastIdx);
			idx = lastIdx + 1; // Skip last ')'
		}

		statement = (Statement) factory(tree, idx++); // Parse statement
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		if (options != null) options.sanityCheck(compilerMessages);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsmNode()); // Task will use the node to get parameters
		sb.append("scopepush\n");

		// Define labels
		String labelEnd = baseLabelName() + "end";
		String labelFalse = baseLabelName() + "false";

		// Options
		sb.append(toAsmOptions(labelFalse));

		// Command (e.g. task and statements)
		sb.append(toAsmCmd(labelEnd));

		// Task expression not evaluated because one or more bool expressions was false
		sb.append(labelFalse + ":\n");
		sb.append("pushs ''\n"); // Task not executed, push an empty task id

		// End of task expression
		sb.append(labelEnd + ":\n");
		sb.append("scopepop\n");
		return sb.toString();
	}

	/**
	 * Fork and evaluate 'par' statements
	 */
	@Override
	protected String toAsmCmd(String labelEnd) {
		StringBuilder sb = new StringBuilder();

		FunctionCall functionCall = getFunctionCall();
		if (functionCall != null) sb.append(toAsmFunctionCall(functionCall, labelEnd));
		else sb.append(toAsmStatements(labelEnd));

		return sb.toString();
	}

	/**
	 * Fork and execute function call
	 * If the statement is a function call, we run it slightly differently.
	 * We first compute the function's arguments (in current thread), to
	 * avoid race conditions. Then we create a thread and call the function
	 */
	protected String toAsmFunctionCall(FunctionCall functionCall, String labelEnd) {
		StringBuilder sb = new StringBuilder();

		String labelParent = baseLabelName() + "parent";
		String labelChild = baseLabelName() + "child";
		String varThreadIdChild = baseVarName() + "threadId";

		Args args = functionCall.getArgs();
		if (args != null) sb.append(args.toAsm());

		// Evaluate statements in new thread
		// Fork returns non-empty string (i.e. bdsThreadId) for parent and
		// empty threadId for child.
		// Note that non-empty strings are 'true' when evaluated as bool, so
		// parent is 'true' and child is 'false'
		sb.append("pushi " + functionCall.getArgs().size() + "\n");
		sb.append("parallelpush\n"); // Fork and push 'n' parameters to the new thread's stack
		sb.append("var " + varThreadIdChild + "\n"); // Save thread ID
		sb.append("jmpt " + labelParent + "\n");
		sb.append(labelChild + ":\n");
		sb.append(functionCall.toAsmCall()); // Only call function, arguments have already been evaluated.
		sb.append("pop\n"); // Function's return value is discarded
		sb.append("halt\n"); // End thread when statements finish executing

		// Restore thread ID
		sb.append(labelParent + ":\n");
		sb.append("load " + varThreadIdChild + "\n");
		sb.append("jmp " + labelEnd + "\n");

		return sb.toString();
	}

	/**
	 * Fork and execute statements
	 */
	protected String toAsmStatements(String labelEnd) {
		StringBuilder sb = new StringBuilder();
		String labelParent = baseLabelName() + "parent";
		String labelChild = baseLabelName() + "child";

		// Evaluate statements in new thread
		// Fork returns non-empty string (i.e. bdsThreadId) for parent and
		// empty threadId for child.
		// Note that non-empty strings are 'true' when evaluated as bool, so
		// parent is 'true' and child is 'false'
		sb.append("parallel\n");
		sb.append("dup\n"); // Save thread ID
		sb.append("jmpt " + labelParent + "\n");
		sb.append(labelChild + ":\n");
		sb.append("pop\n"); // Remove thread ID on child process
		sb.append(statement.toAsm());
		sb.append("halt\n"); // End thread when statements finish executing
		sb.append(labelParent + ":\n");
		sb.append("jmp " + labelEnd + "\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		return "par" //
				+ (options != null ? options : "") //
				+ " " //
				+ toStringStatement() //
		;
	}
}
