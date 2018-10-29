package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.symbol.SymbolTable;

/**
 * A reference to a field in 'this' object
 *
 * @author pcingola
 */
public class ReferenceThis extends ReferenceVar {

	private static final long serialVersionUID = -6552060494780352017L;

	public ReferenceThis(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		name = ClassDeclaration.VAR_THIS;
	}

	public ReferenceThis(BdsNode parent, TypeClass typeClass) {
		super(parent, null);
		name = ClassDeclaration.VAR_THIS;
		returnType = typeClass;
	}

	@Override
	protected void parse(ParseTree tree) {
		// Nothing to do
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		returnType = symtab.resolve(ClassDeclaration.VAR_THIS);
		return returnType;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(symtab);
	}

}
