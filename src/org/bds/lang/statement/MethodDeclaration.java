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
	@SuppressWarnings("unused")
	public String signature() {
		if (signature != null) return signature;

		StringBuilder sigsb = new StringBuilder();
		sigsb.append(classType != null ? classType : "null");
		sigsb.append(".");
		sigsb.append(functionName);
		sigsb.append("(");
		int count = 0;
		for (VarDeclaration vdecl : parameters.getVarDecl()) {
			if (count > 0) {
				Type type = vdecl.getType();
				for (VariableInit vi : vdecl.getVarInit()) {
					sigsb.append(type + ",");
				}
				sigsb.deleteCharAt(sigsb.length() - 1);
			}

			count++;
		}
		sigsb.append(")");
		signature = sigsb.toString();

		return signature;
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// This is checked during ClassDeclaration. Nothing to do here
	}

}
