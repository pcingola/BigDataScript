package org.bds.lang.expression;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.task.TaskDependency;

/**
 * Dependency operator '<-'
 *
 * @author pcingola
 */
public class ExpressionDepOperator extends Expression {

	public static final String DEP_OPERATOR = "<-";
	public static final String DEP_SEPARATOR = ",";

	Expression left[];
	Expression right[];

	public ExpressionDepOperator(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate expressions and create a task dependency
	 */
	@SuppressWarnings("unchecked")
	public TaskDependency evalTaskDependency(BdsThread bdsThread) {
		// All expressions are evaluated
		runStep(bdsThread, left);
		runStep(bdsThread, right);
		if (bdsThread.isCheckpointRecover()) return null;

		ValueList rightEval = (ValueList) bdsThread.pop();
		ValueList leftEval = (ValueList) bdsThread.pop();

		// Create task dependency and add all results
		TaskDependency taskDependency = new TaskDependency(this);
		taskDependency.addOutput(leftEval.get());
		taskDependency.addInput(rightEval.get());

		return taskDependency;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		// Find 'dependency' operator (i.e. '<-')
		int depIdx = indexOf(tree, "<-");

		// Create lists of expressions
		ArrayList<Expression> listl = new ArrayList<>(); // Operator's left side
		ArrayList<Expression> listr = new ArrayList<>(); // Operator's right side
		for (int i = 0; i < tree.getChildCount(); i++) {
			if (isTerminal(tree, i, DEP_OPERATOR) || isTerminal(tree, i, DEP_SEPARATOR)) {
				// Do not add this node
			} else if (i < depIdx) listl.add((Expression) factory(tree, i)); // Add to left
			else if (i > depIdx) listr.add((Expression) factory(tree, i)); // Add to right
		}

		// Create arrays
		left = listl.toArray(new Expression[0]);
		right = listr.toArray(new Expression[0]);
	}

	@Override
	public Type returnType(Scope scope) {
		// Make sure we calculate return type fo all expressions
		for (Expression e : left)
			e.returnType(scope);

		for (Expression e : right)
			e.returnType(scope);

		returnType = Types.BOOL;
		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		TaskDependency taskDependency = evalTaskDependency(bdsThread);
		if (bdsThread.isCheckpointRecover()) return;

		taskDependency.setDebug(bdsThread.isDebug());
		boolean dep = taskDependency.depOperator();
		bdsThread.push(dep);
	}

	/**
	 * Evaluate all expressions in the array.
	 * @return A list of Strings with the results of all evaluations
	 */
	@SuppressWarnings("rawtypes")
	public void runStep(BdsThread bdsThread, Expression exprs[]) {
		ValueList resList = new ValueList(TypeList.get(Types.STRING));

		for (Expression e : exprs) {
			bdsThread.run(e);
			Value result = bdsThread.pop();

			if (result.getType().isList()) {
				// Add all elements as strings
				for (Object o : (List) result.get()) {
					resList.addNative(o.toString());
				}
			} else {
				resList.addNative(result.get().toString());
			}
		}

		bdsThread.push(resList);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (left.length == 1) {
			sb.append(left[0]);
		} else {
			sb.append("[ ");
			for (int i = 0; i < left.length; i++) {
				sb.append(left[i]);
				if (i < left.length) sb.append(",");
			}
			sb.append(" ]");
		}

		sb.append(" <- ");

		if (right.length == 1) {
			sb.append(right[0]);
		} else {
			sb.append("[ ");
			for (int i = 0; i < right.length; i++) {
				sb.append(right[i]);
				if (i < right.length) sb.append(",");
			}
			sb.append(" ]");
		}

		return sb.toString();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Check that expression lists are either strings or lists of strings
		for (Expression e : left)
			if (e.isString()) ; // OK
			else if (e.isList(Types.STRING)) ; //
			else compilerMessages.add(e, "Expression should be string or string[]", MessageType.ERROR);

		for (Expression e : right)
			if (e.isString()) ; // OK
			else if (e.isList(Types.STRING)) ; //
			else compilerMessages.add(e, "Expression should be string or string[]", MessageType.ERROR);
	}

}
