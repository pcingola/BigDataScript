package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Expression: An expression that requires a new scope
 * 
 * @author pcingola
 */
public class ExpressionWithScope extends Expression {

	boolean needsScope; // Do we really need a scope? If a scope is requested, but we don't add new symbols, then we don't really need it (e.g. while loop without any new variables)

	public ExpressionWithScope(BigDataScriptNode parent, ParseTree tree) {
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
