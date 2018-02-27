package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.scope.Scope;

/**
 * A Statement that requires a new Scope
 *
 * @author pcingola
 */
public class StatementWithScope extends Statement {

	protected Scope scope; // Scope required for this statement. Note: This is not a scope used at run-time, this is just a used to hold the symbols that will be required when running
	protected boolean needsScope; // Do we really need a scope? If a scope is requested, but we don't add new symbols, then we don't really need it (e.g. while loop without any new variables)

	public StatementWithScope(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Scope getScope() {
		return scope;
	}

	@Override
	protected void initialize() {
		needsScope = true; // A priory, we think we need a scope. This may be changed at type-checking
	}

	@Override
	public boolean isNeedsScope() {
		return needsScope;
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
