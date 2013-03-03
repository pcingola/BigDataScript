package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

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

	public ExpressionDep(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		// All expressions are evaluated
		List<String> leftEval = eval(csThread, left);
		List<String> rightEval = eval(csThread, right);

		// Left hand side
		// Calculate minimum modification time
		long minModifiedLeft = Long.MAX_VALUE;
		for (String fileName : leftEval) {
			File file = new File(fileName);

			// Any 'left' file does not exists? => We need to build this dependency
			if (!file.exists()) return true;

			minModifiedLeft = Math.min(minModifiedLeft, file.lastModified());
		}

		// Right hand side
		// Calculate minimum modification time
		long maxModifiedRight = Long.MIN_VALUE;
		for (String fileName : rightEval) {
			File file = new File(fileName);

			// Note: If file does not exists, we don't care
			if (file.exists()) maxModifiedRight = Math.max(maxModifiedRight, file.lastModified());
		}

		// Have all 'left' files been modified before 'right' files?
		// I.e. Have all goals been created after the input files?
		return minModifiedLeft < maxModifiedRight;
	}

	/**
	 * Evaluate all expressions in the array. 
	 * @param csThread
	 * @param expr
	 * @return A list of Strings with the results of all evaluations
	 */
	@SuppressWarnings("rawtypes")
	public List<String> eval(BigDataScriptThread csThread, Expression expr[]) {
		ArrayList<String> resList = new ArrayList<String>();

		for (Expression e : expr) {
			Object result = e.eval(csThread);

			if (result instanceof List) {
				// Flatten the list
				List l = (List) result;
				for (Object o : l)
					resList.add(o.toString());
			} else resList.add(result.toString());
		}

		return resList;
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
