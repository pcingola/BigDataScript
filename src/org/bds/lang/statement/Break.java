package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
import org.bds.util.Gpr;

/**
 * A "break" statement
 *
 * @author pcingola
 */
public class Break extends Statement {

	private static final long serialVersionUID = -247651021018110921L;

	public Break(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Is it an enclosing loop you can 'break'?
	 */
	boolean isBreak(BdsNode bdsNode) {
		return bdsNode instanceof While //
				|| bdsNode instanceof ForLoop //
				|| bdsNode instanceof ForLoopList //
				|| bdsNode instanceof Switch //
		;
	}

	boolean isFunction(BdsNode bdsNode) {
		return bdsNode instanceof FunctionDeclaration //
				|| bdsNode instanceof MethodDeclaration //
				|| bdsNode instanceof StatementFunctionDeclaration //
		;
	}

	@Override
	protected void parse(ParseTree tree) {
		// Nothing to do
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.setRunState(RunState.BREAK);
	}

	/**
	 * Perform several basic sanity checks right after parsing the tree
	 */
	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		boolean found = false;
		for (BdsNode bn = this; bn != null; bn = bn.getParent()) {
			if (isBreak(bn)) {
				Gpr.debug("Found node:" + bn);
				found = true;
				break;
			} else if (isFunction(bn)) {
				// Reached function or method definition?
				break;
			}
		}

		if (!found) compilerMessages.add(this, "'break' statement not inside 'for'/'while' loop or 'switch'", MessageType.ERROR);
	}

	@Override
	public String toString() {
		return "break";
	}
}
