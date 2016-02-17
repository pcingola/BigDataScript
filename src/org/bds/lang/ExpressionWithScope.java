package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.scope.Scope;

/**
 * Expression: An expression that requires a new scope
 *
 * @author pcingola
 */
public class ExpressionWithScope extends Expression {

	protected Scope scope; // Scope required for this statement. Note: This is not a scope used at run-time, this is just a used to hold the symbols that will be required when running
	boolean needsScope; // Do we really need a scope? If a scope is requested, but we don't add new symbols, then we don't really need it (e.g. while loop without any new variables)

	public ExpressionWithScope(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Scope getScope() {
		return scope;
	}

	@Override
	void initialize() {
		needsScope = true; // At priory, we think we need a scope. This may be changed at type-checking
	}

	@Override
	public boolean isNeedsScope() {
		return needsScope;
	}

	@Override
	public boolean isStopDebug() {
		return false;
	}

	@Override
	public void setNeedsScope(boolean needsScope) {
		this.needsScope = needsScope;
	}

	@Override
	public void setScope(Scope scope) {
		this.scope = scope;
	}
}
