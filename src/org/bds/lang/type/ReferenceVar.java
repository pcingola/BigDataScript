package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.value.Value;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

/**
 * A variable reference
 *
 * @author pcingola
 */
public class ReferenceVar extends Reference {

	private static final long serialVersionUID = 8534323120718015390L;

	protected boolean classField;
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
		classField = false;
	}

	/**
	 * Find 'type' for 'name'
	 * Also mark this as a 'classField' if the it refers to 'this.name'
	 */
	protected Type findType(SymbolTable symtab) {
		Type t = symtab.getType(name);
		if (t != null) return t;

		// Is 'this' defined (is it a class?)
		TypeClass typeThis = (TypeClass) symtab.getType(ClassDeclaration.THIS);
		if (typeThis == null) return null;

		// Look up 'name' as a field in the class
		t = typeThis.getSymbolTable().getType(name);
		classField = (t != null);

		return t;
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
	public boolean isConstant(SymbolTable symtab) {
		return symtab.isConstant(getVariableName());
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	public boolean isVariableReference(SymbolTable symtab) {
		return true;
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
		returnType = findType(symtab);
		return returnType;
	}

	@Override
	public String toAsm() {
		if (classField) return "load this\nreffield " + name + "\n";
		return "load " + name + "\n";
	}

	@Override
	public String toAsmSet() {
		if (classField) return "load this\nsetfield " + name + "\n";
		return "store " + name + "\n"; // Leave value in the stack
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(symtab);

		if (returnType == null) {
			compilerMessages.add(this, "Symbol '" + name + "' cannot be resolved", MessageType.ERROR);
		}
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
