package org.bds.lang.expression;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * Options for 'task' command
 *
 * @author pcingola
 */
public class ExpressionTaskOptions extends ExpressionList {

	private static final long serialVersionUID = 5543813044437054581L;
	private static final Expression EXPRESSION_ARRAY[] = new Expression[0];

	// boolean evalAll; // Force to evaluate all expressions

	public ExpressionTaskOptions(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public String toAsm() {
		throw new RuntimeException("This method should not be called directly!");
	}

	/**
	 * Evaluate expressions and create a TaskDependency
	 * Note: We only care about the value of bool expressions
	 */
	public String toAsm(String labelEnd) {
		StringBuilder sb = new StringBuilder();
		sb.append(toAsmVars());
		sb.append(toAsmBool(labelEnd));
		sb.append(toAsmDep());
		return sb.toString();
	}

	/**
	 * Add all boolean expression (and)
	 */
	String toAsmBool(String labelEnd) {
		StringBuilder sb = new StringBuilder();

		List<Expression> exprs = new LinkedList<>();
		for (Expression expr : expressions)
			if (expr instanceof ExpressionDepOperator //
					|| expr instanceof ExpressionAssignment //
					|| expr instanceof ExpressionVariableInitImplicit //
			) {
				// Skip these expressions
			} else {
				exprs.add(expr);
			}

		// No boolean expressions? Nothing to do
		if (exprs.isEmpty()) return "";

		// Perform a short-circuited and operation
		// We evaluate each expression, if it is false we
		// jump to the end of the command (labelEnd)
		for (Expression e : exprs) {
			sb.append(e.toAsm());
			sb.append("jmpf " + labelEnd + "\n");
		}

		return sb.toString();
	}

	/**
	 * Create dependency lists: outs <- ins
	 */
	String toAsmDep() {
		List<ExpressionDepOperator> deps = new LinkedList<>();
		for (Expression expr : expressions)
			if (expr instanceof ExpressionDepOperator) deps.add((ExpressionDepOperator) expr);

		// Most cases there zero or one dep operator
		if (deps.size() == 0) return "new string[]\n\"new string[]\n"; // No deps? Add empty lists
		if (deps.size() == 1) return deps.get(0).toAsm();

		//---
		// More than one dep operator? Collapse them into one
		//---
		List<Expression> left = new LinkedList<>();
		List<Expression> right = new LinkedList<>();
		for (ExpressionDepOperator d : deps) {
			for (Expression e : d.getLeft())
				left.add(e);

			for (Expression e : d.getRight())
				right.add(e);
		}

		// Create one big dependency using all left and right expressions
		ExpressionDepOperator dep = new ExpressionDepOperator(this, left.toArray(EXPRESSION_ARRAY), right.toArray(EXPRESSION_ARRAY));
		return dep.toAsm();
	}

	/**
	 * Initialize and assign variables
	 */
	String toAsmVars() {
		StringBuilder sb = new StringBuilder();
		// Dependencies, variable initializations and boolean expression
		for (Expression expr : expressions) {
			if (expr instanceof ExpressionAssignment) {
				sb.append(expr.toAsm());
				sb.append("pop\n");
			} else if (expr instanceof ExpressionVariableInitImplicit) {
				sb.append(expr.toAsm());
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("( ");

		if (expressions != null) {
			int i = 0;
			for (Expression exp : expressions) {
				if (i > 0) sb.append(", ");
				sb.append(exp);
				i++;
			}
		}

		sb.append(" )");

		return sb.toString();
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		for (Expression e : expressions)
			if (!(e instanceof ExpressionAssignment) //
					&& !(e instanceof ExpressionVariableInitImplicit) //
			) {
				// Cannot convert to bool? => We cannot use the expression
				if (e.getReturnType() == null) compilerMessages.add(this, "Unknonw expression '" + e + "'", MessageType.ERROR);
				else if (!e.getReturnType().canCastToBool()) compilerMessages.add(this, "Only assignment, implicit variable declarations or boolean expressions are allowed in task options", MessageType.ERROR);
			}
	}
}
