package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.symbol.GlobalSymbolTable;
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
		if (classType == null) classType = typeThis;

		// Recalculate cached values
		signature = null;
		parameterNames = parameterNames();
	}

	public Type getClassType() {
		return classType;
	}

	/**
	 * Has this function already been declared?
	 * @return True if a function with the same signature already exists
	 */
	@Override
	boolean isDuplicateFunction(SymbolTable symtab) {
		SymbolTable symtabClass = classType.getSymbolTable();
		return symtabClass.hasOtherFunction(this) || symtab.hasOtherFunction(this) || GlobalSymbolTable.get().hasOtherFunction(this);
	}

	@Override
	public boolean isMethod() {
		return true;
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
	public String toAsmNode() {
		// Methods to not write node OpCode, because they don't get counted
		// in coverage. At the beginning of the class, there is a
		//     jump label_ClassDeclaration_NNN_end
		// this means that method definition code is never reached (because
		// it's not necesary for the VM to go in there). But this will
		// result of every method definition being shown as 'not covered'
		// when doing coverage analysis. To avoid this, we just don't write
		// the NODE opcode on methods
		return "";
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// This is checked during ClassDeclaration....not much to do here
		if (isDuplicateFunction(symtab.getParent())) compilerMessages.add(this, "Duplicate method '" + signature() + "'", MessageType.ERROR);
	}

}
