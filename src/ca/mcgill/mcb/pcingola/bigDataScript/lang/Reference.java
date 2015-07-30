package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * A reference to any type of variable
 */
public abstract class Reference extends Expression {

	public Reference(BigDataScriptNode parent, ParseTree tree) {
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
