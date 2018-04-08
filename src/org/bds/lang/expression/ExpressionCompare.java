package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * A comparison expression
 *
 * @author pcingola
 */
public abstract class ExpressionCompare extends ExpressionBinary {

	private static final long serialVersionUID = 2704019952314908802L;

	public ExpressionCompare(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);
		returnType = Types.BOOL;

		return returnType;

	}

	@Override
	public String toAsm() {
		String eb = super.toAsm();
		return eb + toAsmOp() + toAsmType() + "\n";
	}

	protected abstract String toAsmOp();

	protected String toAsmType() {
		if (left.isReal() || right.isReal()) return "r";
		else if (left.isInt() || right.isInt()) return "i";
		else if (left.isBool() || right.isBool()) return "b";
		else if (left.isString() || right.isString()) return "s";
		throw new RuntimeException("Unknown comparisson type: " + left.getReturnType() + ", " + right.getReturnType());
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Either side is a string? => String plus String
		if (left.isString() || right.isString()) {
			// OK, convert to string
		} else if (left.isNumeric() && right.isNumeric()) {
			// OK, convert to numeric
		} else {
			compilerMessages.add(this, "Uncomparabale types", MessageType.ERROR);
		}
	}

}
