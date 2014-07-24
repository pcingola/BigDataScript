package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Options for 'task' command
 *
 * @author pcingola
 */
public class TaskOptions extends ExpressionList {

	List<String> outputFiles, inputFiles;

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
	public Object eval(BigDataScriptThread bdsThread) {
		boolean sat = true;

		outputFiles = new ArrayList<String>();
		inputFiles = new ArrayList<String>();

		for (Expression expr : expressions) {
			Object value = expr.eval(bdsThread);

			// All boolean expressions must be "true"
			if (expr instanceof ExpressionAssignment) ; // Nothing to do
			else if (expr instanceof ExpressionVariableInitImplicit) ; // Nothing to do
			else if (expr instanceof ExpressionDepOperator) {
				// Calculate input & output files
				ExpressionDepOperator dep = ((ExpressionDepOperator) expr);
				outputFiles.addAll(dep.getOutputFiles());
				inputFiles.addAll(dep.getInputFiles());

				sat &= (Boolean) Type.BOOL.cast(value); // Convert expression to boolean
			} else sat &= (Boolean) Type.BOOL.cast(value); // Convert expression to boolean

			// Break expression evaluation if we already know it will not be executed
			if (!sat) return false;
		}

		return sat;
	}

	List<String> getInputFiles() {
		return inputFiles;
	}

	List<String> getOutputFiles() {
		return outputFiles;

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

		if ((outputFiles != null && !outputFiles.isEmpty()) || (inputFiles != null && !inputFiles.isEmpty())) {

			if (outputFiles != null && !outputFiles.isEmpty()) {
				boolean comma = false;
				for (String f : outputFiles) {
					sb.append((comma ? ", " : "") + "'" + f + "'");
					comma = true;
				}
			}

			sb.append(" <- ");

			if (inputFiles != null && !inputFiles.isEmpty()) {
				boolean comma = false;
				for (String f : inputFiles) {
					sb.append((comma ? ", " : "") + "'" + f + "'");
					comma = true;
				}
			}
		}
		sb.append(" )");

		return sb.toString();
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		for (Expression e : expressions)
			if (!(e instanceof ExpressionAssignment) && !(e instanceof ExpressionVariableInitImplicit)) {
				// Cannot convert to bool? => We cannot use the expression
				if (!e.getReturnType().canCast(Type.BOOL)) compilerMessages.add(this, "Only assignment, implicit variable declarations or boolean expressions are allowed in task options", MessageType.ERROR);
			}
	}
}
