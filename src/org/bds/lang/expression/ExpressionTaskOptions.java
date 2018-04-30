package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Options for 'task' command
 *
 * @author pcingola
 */
public class ExpressionTaskOptions extends ExpressionList {

	private static final long serialVersionUID = 5543813044437054581L;

	public ExpressionTaskOptions(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public String toAsm() {
		throw new RuntimeException("This method should not be called directly!");
	}

	/**
	 * Evaluate expressions and create a TaskDependency
	 * Note: We evaluate expression as boolean
	 * @param labelEnd: Label for short-circuit and on boolean expressions
	 * @param pushDeps: If true, push list of input/output dependencies to the stack
	 */
	public String toAsm(String labelEnd, boolean pushDeps) {
		StringBuilder sb = new StringBuilder();

		// Create variables for all input and output dependencies
		String varOutputs = baseVarName() + "outputs";
		String varInputs = baseVarName() + "inputs";
		TypeList listString = TypeList.get(Types.STRING);

		if (pushDeps) {
			sb.append("new " + listString + "\n");
			sb.append("varpop " + varOutputs + "\n");
			sb.append("new " + listString + "\n");
			sb.append("varpop " + varInputs + "\n");
		}

		// Evaluate all expressions
		for (Expression expr : expressions) {
			if (expr instanceof ExpressionAssignment) {
				// Variable assignment: Perform assignment and remove result from stack
				sb.append(expr.toAsm());
				sb.append("pop\n");
			} else if (expr instanceof ExpressionDepOperator) {
				// Implicit variable declaration
				sb.append(toAsmDep(labelEnd, (ExpressionDepOperator) expr, varInputs, varOutputs, pushDeps));
			} else if (expr instanceof ExpressionVariableInitImplicit) {
				sb.append(expr.toAsm());
			} else {
				// Boolean expression:
				//   Perform a short-circuited 'AND' expression of all
				//   boolean expressions.
				//   We evaluate each expression, if it is false we
				//   jump to the end of the command (labelEnd)
				sb.append(expr.toAsm());
				sb.append("jmpf " + labelEnd + "\n");
			}
		}

		// Leave lists with all inputs and outputs in the stack (all dependencies)
		if (pushDeps) {
			sb.append("load " + varOutputs + "\n");
			sb.append("load " + varInputs + "\n");
		}

		return sb.toString();
	}

	String toAsmDep(String labelEnd, ExpressionDepOperator dep, String varInputs, String varOutputs, boolean pushDeps) {
		StringBuilder sb = new StringBuilder();
		sb.append(dep.toAsm(varInputs, varOutputs, pushDeps));
		sb.append("jmpf " + labelEnd + "\n");
		return sb.toString();
	}

	String toAsmList() {
		StringBuilder sb = new StringBuilder();
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
