package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

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
	public Value getValue(Scope scope) {
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
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		Type t = symtab.getType(name);
		if (t == null) return null; // Symbol not found

		returnType = t;
		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		Value val = bdsThread.getScope().getValue(name);
		if (val == null) bdsThread.fatalError(this, "Cannot find variable '" + name + "'");
		bdsThread.push(val);
	}

	/**
	 * Set map to scope symbol
	 */
	@Override
	public void setValue(BdsThread bdsThread, Value value) {
		if (value == null) return;
		Value val = getValue(bdsThread.getScope()); // Get scope symbol
		value = getReturnType().cast(value); // Cast to destination type
		val.setValue(value); // Assign
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(symtab);

		if (!symtab.hasType(name)) {
			compilerMessages.add(this, "Symbol '" + name + "' cannot be resolved", MessageType.ERROR);
		}
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
