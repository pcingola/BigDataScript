package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.VariableInitImplicit;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * An expression having an implicit type variable initialization ( varName := expression )
 *
 * @author pcingola
 */
public class ExpressionVariableInitImplicit extends Expression {

	private static final long serialVersionUID = -2365717696772581323L;

	VariableInitImplicit vInit;

	public ExpressionVariableInitImplicit(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return vInit.getExpression().getReturnType() != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		vInit = new VariableInitImplicit(this, tree);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		returnType = vInit.getExpression().returnType(symtab);
		return returnType;
	}

	@Override
	public String toAsm() {
		return vInit != null ? vInit.toAsm() : "";
	}

	@Override
	public String toString() {
		return vInit != null ? vInit.toString() : "";
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		vInit.typeCheck(symtab, compilerMessages);

		// Already declared?
		String varName = vInit.getVarName();
		if (symtab.hasTypeLocal(varName)) compilerMessages.add(this, "Duplicate local name " + varName, MessageType.ERROR);

		// Calculate implicit data type
		Type type = vInit.getExpression().returnType(symtab);

		// Add variable to scope
		if ((varName != null) && (type != null)) symtab.addVariable(varName, type);
	}
}
