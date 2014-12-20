package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * A variable reference
 *
 * @author pcingola
 */
public class VarReference extends Reference {

	protected String name;

	public VarReference(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread bdsThread) {
		ScopeSymbol ss = bdsThread.getScope().getSymbol(name);
		return ss.getValue();
	}

	/**
	 * Get symbol from scope
	 */
	@Override
	public ScopeSymbol getScopeSymbol(Scope scope) {
		return scope.getSymbol(name);
	}

	@Override
	public String getVariableName() {
		return name;
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		name = tree.getChild(0).getText();
	}

	@Override
	public void parse(String str) {
		name = str;
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		ScopeSymbol ss = scope.getSymbol(name);
		if (ss == null) return null; // Symbol not found

		returnType = ss.getType();
		return returnType;
	}

	/**
	 * Set value to scope symbol
	 */
	@Override
	public void setValue(BigDataScriptThread bdsThread, Object value) {
		ScopeSymbol ssym = getScopeSymbol(bdsThread.getScope()); // Get scope symbol
		value = getReturnType().cast(value); // Cast to destination type
		ssym.setValue(value); // Assign
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		if (!scope.hasSymbol(name)) //
			compilerMessages.add(this, "Symbol '" + name + "' cannot be resolved", MessageType.ERROR);
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
