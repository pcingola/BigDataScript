package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * Assign one variable to another one
 *
 * Example:
 *     a = b
 *
 * @author pcingola
 */
public class ExpressionAssignment extends ExpressionBinary {

	private static final long serialVersionUID = -186692673709030759L;

	public ExpressionAssignment(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "=";
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);
		returnType = left.getReturnType();

		return returnType;
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		// Is 'left' a variable?
		if (left == null) compilerMessages.add(this, "Cannot parse left expresison.", MessageType.ERROR);
		else if (!(left instanceof Reference)) compilerMessages.add(this, "Assignment to non variable ('" + left + "')", MessageType.ERROR);
	}

	@Override
	public String toAsm() {
		return right.toAsm() //
				+ ((Reference) left).toAsmSet() //
		;
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Trying to assign to a constant?
		Reference rleft = ((Reference) left);
		if (rleft.isConstant(symtab)) compilerMessages.add(this, "Cannot assign to constant '" + left + "'", MessageType.ERROR);
		if (!rleft.isVariableReference(symtab)) compilerMessages.add(this, "Cannot assign to non-variable '" + left + "'", MessageType.ERROR);

		// Can we cast 'right type' into 'left type'?
		if (!right.canCastTo(left)) {
			compilerMessages.add(this, "Cannot cast " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
		}
	}

}
