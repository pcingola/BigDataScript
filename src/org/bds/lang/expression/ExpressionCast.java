package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Cast one type to another
 * E.g.
 *
 * 	a = (A) b
 *
 * @author pcingola
 */
public class ExpressionCast extends ExpressionUnary {

	private static final long serialVersionUID = 1043280143559100089L;
	protected String castTo;
	protected boolean dynamicChecking = false;

	public ExpressionCast(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "cast";
	}

	@Override
	protected void parse(ParseTree tree) {
		castTo = tree.getChild(1).getText();
		expr = (Expression) factory(tree, 3);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		returnType = Types.get(castTo);
		expr.returnType(symtab);
		return returnType;
	}

	@Override
	public String toAsm() {
		return expr.toAsm() + "CAST_TOC " + returnType + "\n";
	}

	@Override
	public String toString() {
		return "(" + castTo + ") " + expr.toString();
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType = returnType(symtab);

		// Are return types non-null?
		// Note: null returnTypes happen if variables are missing.
		if (isReturnTypesNotNull()) typeCheckNotNull(symtab, compilerMessages);
		else compilerMessages.add(this, "Unknown type '" + castTo + "'", MessageType.ERROR);
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Can cast to? (e.g. sub-class)
		if (expr.canCastTo(returnType)) return;

		// Both classes? Check for down-casting
		if (expr.getReturnType().isClass() && returnType.isClass() //
				&& returnType.canCastTo(expr.getReturnType())) {
			// Could be a 'down-casting'.
			// E.g.:
			//    class A {;}
			//    class B extends A {;}
			//    A a = new B()
			//    B b = (B) a       <- Variable 'a' is type A, but contains an object type 'B', so this is valid
			// In this case, we need to dynamically check if the object can be casted
			dynamicChecking = true;
			return;
		}
		compilerMessages.add(this, "Cannot cast from type '" + returnType + "' to type '" + castTo + "'", MessageType.ERROR);
	}

}
