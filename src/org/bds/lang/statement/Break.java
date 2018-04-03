package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.run.RunState;

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
	 * How many enclosing scopes do we have to "break' through?
	 */
	int countScopesBreakNode() {
		int countScopes = 0;
		for (BdsNode bn = this; bn != null; bn = bn.getParent()) {
			if (bn.isNeedsScope()) countScopes++;

			if (isBreak(bn)) return countScopes;
			else if (isFunction(bn)) return countScopes;
		}
		return countScopes;
	}

	/**
	 * Find enclosing loop or switch
	 */
	BdsNode findBreakNode() {
		for (BdsNode bn = this; bn != null; bn = bn.getParent()) {
			if (isBreak(bn)) return bn;
			else if (isFunction(bn)) return null; // Reached function or method definition?
		}
		return null;
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
		if (findBreakNode() == null) {
			compilerMessages.add(this, "'break' statement not inside 'for'/'while' loop or 'switch'", MessageType.ERROR);
		}
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		// Restore scopes
		int countScopes = countScopesBreakNode();
		for (int i = 0; i < countScopes; i++)
			sb.append("scopepop\n");

		// Jump for end of enclosing loop/switch
		BdsNode bdsNode = findBreakNode();
		String breakLabel = bdsNode.getClass().getSimpleName() + "_end_" + bdsNode.getId();
		sb.append("jmp " + breakLabel + "\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "break";
	}
}
