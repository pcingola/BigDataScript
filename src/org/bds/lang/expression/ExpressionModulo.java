package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A module operation
 *
 * @author pcingola
 */
public class ExpressionModulo extends ExpressionMath {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ExpressionModulo(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "%";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;

		long den = bdsThread.popInt();
		long num = bdsThread.popInt();
		bdsThread.push(num % den);
	}

	@Override
	public String toAsm() {
		String eb = super.toAsm();
		String op = "modi";
		return eb + op + "\n";
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		left.checkCanCastToInt(compilerMessages);
		right.checkCanCastToInt(compilerMessages);
	}

}
