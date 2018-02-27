package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.expression.Expression;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

/**
 * A reference to any type of variable
 */
public abstract class Reference extends Expression {

	public Reference(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public abstract ScopeSymbol getScopeSymbol(Scope scope);

	public abstract String getVariableName();

	public boolean isConstant(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return (ss != null) && ss.isConstant();
	}

	public boolean isVariable(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return (ss != null);
	}

	/**
	 * Parse a string (e.g. an interpolated string)
	 */
	public abstract void parse(String str);

	public abstract void setValue(BdsThread bdsThread, Object value);

}
