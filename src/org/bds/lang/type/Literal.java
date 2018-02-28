package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.scope.Scope;

/**
 * A boolean literal
 * 
 * @author pcingola
 */
public class Literal extends Expression {

	public Literal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
