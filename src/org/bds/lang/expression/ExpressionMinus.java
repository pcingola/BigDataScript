package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A subtraction
 *
 * @author pcingola
 */
public class ExpressionMinus extends ExpressionMath {

	private static final long serialVersionUID = 4071972960119042793L;

	public ExpressionMinus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "-";
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		if (right == null) {
			// Unary minus operator
			bdsThread.run(left);
			if (bdsThread.isCheckpointRecover()) return;

			// This should be an unary expression!
			if (isInt()) {
				bdsThread.push(-bdsThread.popInt());
				return;
			}

			if (isReal()) {
				bdsThread.push(-bdsThread.popReal());
				return;
			}
		} else {
			// Binary minus operator: Subtraction
			bdsThread.run(left);
			bdsThread.run(right);
			if (bdsThread.isCheckpointRecover()) return;

			if (isInt()) {
				long r = bdsThread.popInt();
				long l = bdsThread.popInt();
				bdsThread.push(l - r);
				return;
			}

			if (isReal()) {
				double r = bdsThread.popReal();
				double l = bdsThread.popReal();
				bdsThread.push(l - r);
				return;
			}

		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());
		sb.append("sub" + toAsmRetType() + "\n");
		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (left != null) left.checkCanCastToNumeric(compilerMessages);
		if (right != null) right.checkCanCastToNumeric(compilerMessages);
	}

}
