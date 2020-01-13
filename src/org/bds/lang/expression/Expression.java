package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.BdsCompilerExpression;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.Statement;

/**
 * Expression: A statement that returns a value
 *
 * @author pcingola
 */
public class Expression extends Statement {

	private static final long serialVersionUID = -2172180316283535708L;

	/**
	 * Create an expression from a string (compile to BdsNode)
	 */
	public static Expression factory(BdsNode parent, String exprStr) {
		if (exprStr == null || exprStr.trim().isEmpty()) return null;

		// Sometimes expressions from interpolated variables have several '$' signs
		// E.g. Convert '$list[$i].x{$key}' to 'list[i].x{key}'
		exprStr = exprStr.replace('$', ' ');

		// Compile expression
		BdsCompilerExpression be = new BdsCompilerExpression(exprStr);
		Expression expr = be.compileExpr();
		if (expr == null) return null;
		if (parent != null) {
			expr.setParent(parent);
			expr.setLineNum(parent.getLineNum());
		}
		return expr;
	}

	public Expression(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public String toAsm() {
		return "";
	}

}
