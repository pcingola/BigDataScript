package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.symbol.SymbolTable;

/**
 * Method declaration
 *
 * @author pcingola
 */
public class MethodDeclaration extends FunctionDeclaration {

	private static final long serialVersionUID = 350641659973344826L;

	protected Type classType;

	public MethodDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add 'this' parameter to method
	 */
	public void addThisArg(TypeClass typeThis) {
		if (isNative()) return; // Native methods do not need to add 'this' (it's already declared explicitly in the constructor)
		parameters.addThis(typeThis);
		signature = null;
	}

	public Type getClassType() {
		return classType;
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree.getChild(0));
	}

	@Override
	public String signature() {
		if (signature != null) return signature;

		signature = (classType != null ? classType : "null") //
				+ "." + functionName //
				+ getType().signature();

		return signature;
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// This is checked during ClassDeclaration. Nothing to do here
	}

}
