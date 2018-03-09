package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

/**
 * A variable reference
 *
 * @author pcingola
 */
public class ReferenceVar extends Reference {

	protected String name;

	/**
	 * Create a reference form a string
	 */
	public static Expression factory(BdsNode parent, String var) {
		if (var == null || var.isEmpty()) return null;

		int idxCurly = var.indexOf('{');
		int idxBrace = var.indexOf('[');

		Reference varRef = null;
		if (idxCurly < 0 && idxBrace < 0) varRef = new ReferenceVar(parent, null);
		else if (idxCurly < 0 && idxBrace > 0) varRef = new ReferenceList(parent, null);
		else if (idxCurly > 0 && idxBrace < 0) varRef = new ReferenceMap(parent, null);
		else if (idxBrace < idxCurly) varRef = new ReferenceList(parent, null);
		else varRef = new ReferenceMap(parent, null);

		// Parse string
		varRef.parse(var);
		return varRef;
	}

	public ReferenceVar(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Get symbol from scope
	 */
	@Override
	public ScopeSymbol getScopeSymbol(Scope scope) {
		return scope.getValue(name);
	}

	@Override
	public String getVariableName() {
		return name;
	}

	@Override
	public boolean isReturnTypesNotNull() {
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

		ScopeSymbol ss = scope.getValue(name);
		if (ss == null) return null; // Symbol not found

		returnType = ss.getType();
		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		ScopeSymbol ss = bdsThread.getScope().getValue(name);
		if (ss == null) bdsThread.fatalError(this, "Cannot find variable '" + name + "'");
		bdsThread.push(ss.getValue());
	}

	/**
	 * Set map to scope symbol
	 */
	@Override
	public void setValue(BdsThread bdsThread, Value value) {
		if (value == null) return;
		ScopeSymbol ssym = getScopeSymbol(bdsThread.getScope()); // Get scope symbol
		value = getReturnType().cast(value); // Cast to destination type
		ssym.setValue(value); // Assign
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		if (!scope.hasType(name)) compilerMessages.add(this, "Symbol '" + name + "' cannot be resolved", MessageType.ERROR);
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
