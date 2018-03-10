package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A multiplication
 *
 * @author pcingola
 */
public class ExpressionTimes extends ExpressionMath {

	public ExpressionTimes(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "*";
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);

		if (isReturnTypesNotNull()) {
			if (left.isString() || right.isString()) returnType = Types.STRING;
			else if (left.canCastToInt() && right.canCastToInt()) returnType = Types.INT;
			else if (left.canCastToReal() && right.canCastToReal()) returnType = Types.REAL;
		}
		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Evaluate expressions
		bdsThread.run(left);
		bdsThread.run(right);

		if (bdsThread.isCheckpointRecover()) return;

		// Combine results
		if (isInt()) {
			long r = bdsThread.popInt();
			long l = bdsThread.popInt();
			bdsThread.push(l * r);
		} else if (isReal()) {
			double r = bdsThread.popReal();
			double l = bdsThread.popReal();
			bdsThread.push(l * r);
		} else if (isString()) {
			long num = 0;
			String str = "";

			if (right.canCastToInt()) {
				num = bdsThread.popInt();
				str = bdsThread.popString();
			} else if (left.canCastToInt()) {
				str = bdsThread.popString();
				num = bdsThread.popInt();
			}
			// Multiply (append the same string num times
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < num; i++)
				sb.append(str);

			bdsThread.push(sb.toString());
		} else {
			throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
		}
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (left.isString() && right.canCastToInt()) {
			// string * int: OK
		} else if (left.canCastToInt() && right.isString()) {
			// int * string : OK
		} else {
			// Normal 'math'
			left.checkCanCastToNumeric(compilerMessages);
			right.checkCanCastToNumeric(compilerMessages);
		}
	}

}
