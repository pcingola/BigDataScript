package org.bds.compile;

import java.util.HashSet;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.antlr.BigDataScriptParser;
import org.bds.lang.BdsNodeFactory;
import org.bds.lang.expression.Expression;

/**
 * BdsCompiler a Bds program.
 *
 * Runs lexer & parser, create AST, perform type-checking and create BdsNode tree
 *
 * @author pcingola
 */
public class BdsCompilerExpression extends BdsCompiler {

	protected String exprStr;
	protected Expression expr;

	public BdsCompilerExpression(String exprStr) {
		super(null);
		this.exprStr = exprStr;
	}

	/**
	 * Compile expression
	 */
	public Expression compileExpr() {
		// Convert to AST
		ParseTree tree = parseExpression();
		if (tree == null) return null;

		// Convert to BdsNodes
		return (Expression) BdsNodeFactory.get().factory(null, tree);
	}

	/**
	 * Lex, parse and create Abstract syntax tree (AST)
	 */
	ParseTree parseExpression() {
		ParseTree tree = null;

		try {
			tree = createAst(exprStr, debug, new HashSet<String>());
		} catch (Exception e) {
			System.err.println("Could not compile expression\n\t" + exprStr + "\n" + e.getMessage());
			return null;
		}

		// No tree produced? Fatal error
		if (tree == null) CompilerMessages.get().addError("Fatal error: Could not compile expression: " + exprStr);

		return tree;
	}

	@Override
	protected ParseTree parserNode(BigDataScriptParser parser) {
		return parser.expression();
	}

}
