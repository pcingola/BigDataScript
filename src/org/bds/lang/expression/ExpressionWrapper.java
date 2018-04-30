package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * Something the is actually an expression
 *
 * @author pcingola
 */
public class ExpressionWrapper extends Expression {

	private static final long serialVersionUID = 4296005193087284250L;

	protected Expression expression;

	public ExpressionWrapper(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return expression.getReturnType() != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		expression = (Expression) factory(tree, 0);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		returnType = expression.returnType(symtab);
		return returnType;
	}

	@Override
	public String toAsm() {
		return super.toAsm() + expression.toAsm();
	}

	@Override
	public String toString() {
		return (expression != null ? expression.toString() : getClass().getSimpleName() + ":NULL");
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		expression.typeCheck(symtab, compilerMessages);
	}

}
