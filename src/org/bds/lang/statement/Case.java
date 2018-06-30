package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.expression.ExpressionEq;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * Case statement (in switch condition)
 *
 */
public class Case extends StatementWithScope {

	private static final long serialVersionUID = -1263350436763086010L;

	Expression expression;
	Statement[] statements;
	ExpressionEq exprEq;

	public Case(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		statements = new Statement[0];
	}

	protected boolean isEndOfStatements(ParseTree tree, int idx) {
		if (idx >= tree.getChildCount()) return true;
		return isTerminal(tree, idx, "case") || isTerminal(tree, idx, "default");
	}

	protected String label() {
		return baseLabelName() + "case";
	}

	@Override
	protected void parse(ParseTree tree) {
		// Do nothing. The other parse method will be invoked by 'switch' parsing
	}

	/**
	 * Invoked by 'switch' parsing
	 * Return last index in tree that was parsed + 1
	 */
	protected int parse(ParseTree tree, int idx) {
		List<Statement> stats = new ArrayList<>();

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
			if (stat != null) stats.add(stat);
		}
		statements = stats.toArray(new Statement[0]);

		// Create an expression for comparing 'switch' expresion to 'case' expression
		exprEq = new ExpressionEq(this, null);
		exprEq.setLeft(((Switch) parent).getSwitchExpr());
		exprEq.setRight(expression);

		return idx;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		// Statement
		sb.append(label() + ":\n");
		for (Statement s : statements)
			sb.append(s.toAsm());

		return sb.toString();
	}

	/**
	 * Evaluate case expression and jump to case statements if it is equals to switch expression
	 */
	public String toAsmCondition(String varSwitchExpr) {
		StringBuilder sb = new StringBuilder();

		String labelCaseCond = baseLabelName() + "case_condition";
		sb.append(labelCaseCond + ":\n");

		// Switch expression return type
		Switch switchSt = (Switch) parent;
		Expression switchExpr = switchSt.getSwitchExpr();

		// Evaluate case expression
		sb.append("load " + varSwitchExpr + "\n");
		sb.append(expression.toAsm());

		// Is it equal to switch expression?
		sb.append("eq" + switchExpr.toAsmRetType() + "\n");
		sb.append("jmpt " + label() + "\n"); // Equal? Jump to label

		return sb.toString();
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
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (expression != null) {
			Type switchExprType = ((Switch) parent).getSwitchExpr().getReturnType();
			Type caseExprType = expression.returnType(symtab);

			if (switchExprType == null) {
				// This will be reported in error messages
			} else if (caseExprType == null) {
				compilerMessages.add(this, "Cannot 'case' resolve expression '" + expression + "'", MessageType.ERROR);
			} else if (switchExprType.isString() && caseExprType.isString()) {
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
	}

}
