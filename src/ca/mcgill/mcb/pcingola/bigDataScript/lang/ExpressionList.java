package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A list of expression
 * 
 * An expression list returns the type of the last expression
 * 
 * @author pcingola
 */
public class ExpressionList extends Expression {

	Expression expressions[];

	public ExpressionList(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Object eval(BigDataScriptThread csThread) {
		Object value = null;
		for (Expression expr : expressions)
			value = expr.eval(csThread);
		return value;
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		for (Expression expr : expressions)
			if (expr.getReturnType() == null) return false;
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		parse(tree, 0, tree.getChildCount());
	}

	/**
	 * Parse child nodes between child[offset] and child[max] 
	 * Note:  child[max] is NOT parsed
	 * 
	 * @param tree
	 * @param offset
	 * @param max
	 */
	protected void parse(ParseTree tree, int offset, int max) {
		int num = (max - offset + 1) / 2; // Comma separated list of expressions
		expressions = new Expression[num];

		// Note, we add 2 to skip the next ',' separator
		for (int i = offset, j = 0; i < max; j++, i += 2) {
			if (lineNum <= 0) lineAndPos(tree.getChild(i)); // Set line number if not already done
			expressions[j] = (Expression) factory(tree, i);
		}
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		Type type = null;
		for (Expression expr : expressions)
			type = expr.returnType(scope);

		returnType = type;
		return returnType;
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		for (Expression expr : expressions)
			expr.typeCheck(scope, compilerMessages);
	}

}
