package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.expression.Expression;
import org.bds.scope.Scope;

/**
 * Expression: Literal empty map
 * 
 * @author pcingola
 */
public class LiteralMapEmpty extends LiteralMap {

	public LiteralMapEmpty(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		keys = new Expression[0];
		values = new Expression[0];
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		// Create a list of 'baseType'
		returnType = TypeMap.get(Type.VOID);

		return returnType;
	}

}
