package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A Statement that requires a new Scope
 * 
 * @author pcingola
 */
public class StatementWithScope extends Statement {

	protected boolean needsScope; // Do we really need a scope? If a scope is requested, but we don't add new symbols, then we don't really need it (e.g. while loop without any new variables)

	public StatementWithScope(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
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
	public void setNeedsScope(boolean needsScope) {
		this.needsScope = needsScope;
	}

}
