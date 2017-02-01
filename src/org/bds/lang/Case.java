package org.bds.lang;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.serialize.BdsSerializer;
import org.bds.util.Gpr;

/**
 * Case statement (in switch condition)
 *
 */
public class Case extends Statement {

	Expression expression;
	List<Statement> statements;
	ExpressionEq exprEq;

	public Case(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		statements = new ArrayList<>();
	}

	protected boolean isEndOfStatements(ParseTree tree, int idx) {
		if (idx >= tree.getChildCount()) return true;
		return isTerminal(tree, idx, "case") || isTerminal(tree, idx, "default");
	}

	@Override
	protected void parse(ParseTree tree) {
		// Do nothing. The other parse method will be invoked by 'switch' parsing
	}

	protected int parse(ParseTree tree, int idx) {
		statements = new ArrayList<>();

		idx = findIndex(tree, "case", idx);
		if (idx < 0) return idx; // No case statements found
		lineAndPos(tree.getChild(idx));

		// Add 'case' expression
		idx++;
		expression = (Expression) factory(tree, idx++);
		if (isTerminal(tree, idx, ":")) idx++; // ':'

		// Add all statement
		while (idx < tree.getChildCount()) {
			if (isEndOfStatements(tree, idx)) break;

			Statement stat = (Statement) factory(tree, idx++);
			if (stat != null) statements.add(stat);
		}

		// Create an expression for comparing 'switch' expresion to 'case' expression
		exprEq = new ExpressionEq(this, null);
		exprEq.setLeft(((Switch) parent).getSwitchExpr());
		exprEq.setRight(expression);

		return idx;
	}

	/**
	 * Evaluate case equality
	 */
	boolean runCondition(BdsThread bdsThread) {
		if (expression == null) return true; // No expression? Always true

		bdsThread.run(expression);
		if (bdsThread.isCheckpointRecover()) return true;

		Object caseRes = bdsThread.pop(); // Value form 'case expression'
		Object switchRes = bdsThread.peek(); // Switch expression value
		return exprEq.compare(bdsThread, switchRes, caseRes); //Compare them
	}

	/**
	 * Evaluate condition
	 */
	void runStatements(BdsThread bdsThread) {
		for (Statement s : statements)
			bdsThread.run(s);
	}

	/**
	 * Run the program
	 * Keep in mind that each 'case' is executed with two values 
	 * pushed into the stack by 'switch' execution:
	 * 		1) Previous case condition result (boolean: was the expression equal to 'swtich' expression?)
	 * 		2) Switch expression result
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		if (bdsThread.isCheckpointRecover()) {
			runCondition(bdsThread);
			if (bdsThread.isCheckpointRecover()) {
				runStatements(bdsThread);
				return;
			}
		}

		// Pop the previous 'case' condition value (fall-through?)
		boolean prevCaseCond = popBool(bdsThread);
		boolean caseCond = prevCaseCond;
		if (prevCaseCond) {
			// Previous case condition was true => Fall-through, we execute statements
			runStatements(bdsThread);
		} else {
			caseCond = runCondition(bdsThread);
			// Case condition true => execute statements
			if (caseCond) runStatements(bdsThread);
		}

		// Push 'case condition' to stack
		bdsThread.push(caseCond);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("case ");
		if (expression != null) sb.append(expression);
		sb.append(":\n");
		if (statements != null) {
			for (Statement s : statements)
				sb.append(Gpr.prependEachLine("\t", s.toString()));
		}
		sb.append("\n");

		return sb.toString();
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		if (expression != null) {
			Type switchExprType = ((Switch) parent).getSwitchExpr().getReturnType();
			Type caseExprType = expression.returnType(scope);

			if (switchExprType.isString() && caseExprType.isString()) {
				// OK, convert to string
			} else if (switchExprType.isNumeric() && caseExprType.isNumeric()) {
				// OK, convert to numeric
			} else {
				compilerMessages.add(this//
						,
						"Switch expression and case expression types do not match (" //
								+ switchExprType + " vs " + caseExprType //
								+ "): case " + expression,
						MessageType.ERROR);
			}
		}

		if (statements != null) {
			for (Statement s : statements)
				s.typeCheck(scope, compilerMessages);
		}
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
		String ret = super.serializeSave(serializer);
		Gpr.debug("Serialize: Case. Ret:" + ret);
		return ret;
	}

}
