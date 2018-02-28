package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.task.TaskDependency;

/**
 * Options for 'task' command
 *
 * @author pcingola
 */
public class ExpressionTaskOptions extends ExpressionList {

	boolean evalAll; // Force to evaluate all expressions

	public ExpressionTaskOptions(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate expressions and create a TaskDependency (only if clauses are satisfied
	 * Note: We only care about the value of bool expressions
	 * Note: If 'evalAll' is true, all expressions in the list are evaluated, even if the first one is false
	 * @return A 'TaskDependencies' object if the task has to be run, null otherwise
	 */
	public TaskDependency evalTaskDependency(BdsThread bdsThread) {
		boolean sat = true;
		TaskDependency taskDeps = new TaskDependency(this);

		for (Expression expr : expressions) {
			if (expr instanceof ExpressionDepOperator) {
				// This evaluation returns a 'TaskDependence' object
				ExpressionDepOperator exprDep = (ExpressionDepOperator) expr;
				TaskDependency taskDepsExpr = exprDep.evalTaskDependency(bdsThread);

				if (!bdsThread.isCheckpointRecover()) {
					taskDeps.add(taskDepsExpr);
					sat &= (taskDepsExpr != null); // Convert expression to boolean
				}
			} else {
				// All boolean expressions must be "true"
				bdsThread.run(expr);

				if (!bdsThread.isCheckpointRecover()) {
					Object value = bdsThread.pop();
					if (expr instanceof ExpressionAssignment) ; // Nothing to do
					else if (expr instanceof ExpressionVariableInitImplicit) ; // Nothing to do
					else sat &= (Boolean) Type.BOOL.cast(value); // Convert expression to boolean
				}
			}

			// Break expression evaluation if we already know it will not be executed
			if (!sat && !evalAll) return null;
		}

		return sat ? taskDeps : null;
	}

	/**
	 * Evaluate: Returns 'true' if all boolean expressions are 'true'.
	 *
	 * @return true if all clauses are satisfied and taskDependency was created.
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		TaskDependency taskDeps = evalTaskDependency(bdsThread);
		bdsThread.push(taskDeps != null);
	}

	public void setEvalAll(boolean evalAll) {
		this.evalAll = evalAll;
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
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		for (Expression e : expressions)
			if (!(e instanceof ExpressionAssignment) //
					&& !(e instanceof ExpressionVariableInitImplicit) //
			) {
				// Cannot convert to bool? => We cannot use the expression
				if (e.getReturnType() == null) compilerMessages.add(this, "Unknonw expression '" + e + "'", MessageType.ERROR);
				else if (!e.getReturnType().canCast(Type.BOOL)) compilerMessages.add(this, "Only assignment, implicit variable declarations or boolean expressions are allowed in task options", MessageType.ERROR);
			}
	}
}
