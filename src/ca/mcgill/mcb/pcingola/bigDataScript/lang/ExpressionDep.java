package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Boolean OR
 *
 * @author pcingola
 */
public class ExpressionDep extends Expression {

	public static final String DEP_OPERATOR = "<-";
	public static final String DEP_SEPARATOR = ",";

	Expression left[];
	Expression right[];
	List<String> leftEval;
	List<String> rightEval;

	public ExpressionDep(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread bdsThread) {
		// All expressions are evaluated
		leftEval = eval(bdsThread, left);
		rightEval = eval(bdsThread, right);

		boolean debug = bdsThread.isDebug();

		// Left hand side
		// Calculate minimum modification time
		long minModifiedLeft = Long.MAX_VALUE;
		for (String fileName : leftEval) {
			File file = new File(fileName);

			// Any 'left' file does not exists? => We need to build this dependency
			if (!file.exists()) {
				if (debug) log("Left file '" + fileName + "' doesn't exist");
				return true;
			}

			if (file.isFile() && file.length() <= 0) {
				if (debug) log("Left file '" + fileName + "' is empty");
				return true; // File is empty? => We need to build this dependency.
			} else if (file.isDirectory()) {
				// Notice: If it is a directory, we must rebuild if it is empty
				File dirList[] = file.listFiles();
				if ((dirList == null) || dirList.length <= 0) {
					if (debug) log("Left file '" + fileName + "' is empty");
					return true;
				}
			}

			minModifiedLeft = Math.min(minModifiedLeft, file.lastModified());
		}

		// Right hand side
		// Calculate minimum modification time
		long maxModifiedRight = Long.MIN_VALUE;
		for (String fileName : rightEval) {
			File file = new File(fileName);

			if (file.exists()) {
				// Update max time
				maxModifiedRight = Math.max(maxModifiedRight, file.lastModified());
			} else {
				// Make sure that we schedule the task if the input file doesn't exits
				// The reason to do this, is that probably the input file was defined
				// by some other task that is pending execution.
				if (debug) log("Right file '" + fileName + "' doesn't exist");
				return true;
			}
		}

		// Have all 'left' files been modified before 'right' files?
		// I.e. Have all goals been created after the input files?
		if (minModifiedLeft < maxModifiedRight) {
			if (debug) log("Modification times, minModifiedLeft (" + minModifiedLeft + ") < maxModifiedRight (" + maxModifiedRight + ")");
			return true;
		}

		return false;
	}

	/**
	 * Evaluate all expressions in the array.
	 * @return A list of Strings with the results of all evaluations
	 */
	@SuppressWarnings("rawtypes")
	public List<String> eval(BigDataScriptThread bdsThread, Expression expr[]) {
		ArrayList<String> resList = new ArrayList<String>();

		for (Expression e : expr) {
			Object result = e.eval(bdsThread);

			if (result instanceof List) {
				// Flatten the list
				List l = (List) result;
				for (Object o : l)
					resList.add(o.toString());
			} else resList.add(result.toString());
		}

		return resList;
	}

	public List<String> getInputFiles() {
		return rightEval;
	}

	public List<String> getOutputFiles() {
		return leftEval;
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		// Find 'dependency' operator (i.e. '<-')
		int depIdx = indexOf(tree, "<-");

		// Create lists of expressions
		ArrayList<Expression> listl = new ArrayList<Expression>(); // Operator's left side
		ArrayList<Expression> listr = new ArrayList<Expression>(); // Operator's right side
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

		returnType = Type.BOOL;
		return returnType;
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
			else if (e.isList(Type.STRING)) ; //
			else compilerMessages.add(e, "Expression should be string or string[]", MessageType.ERROR);

		for (Expression e : right)
			if (e.isString()) ; // OK
			else if (e.isList(Type.STRING)) ; //
			else compilerMessages.add(e, "Expression should be string or string[]", MessageType.ERROR);

	}

}
