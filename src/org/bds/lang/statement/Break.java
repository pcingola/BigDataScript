package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;

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
	protected int countScopes() {
		int countScopes = 0;
		for (BdsNode bn = this; bn != null; bn = bn.getParent()) {
			if (bn.isNeedsScope()) countScopes++;

			if (isBreak(bn)) return countScopes - 1; // Pop all scopes, except the one in current loop
			else if (isFunction(bn)) return countScopes;
		}
		return countScopes;
	}

	/**
	 * Find enclosing loop or switch
	 */
	protected BdsNode findBreakNode() {
		for (BdsNode bn = this; bn != null; bn = bn.getParent()) {
			if (isBreak(bn)) return bn;
			else if (isFunction(bn)) return null; // Reached function or method definition?
			// Note: Cannot be inside a 'try/catch/finally' statement
		}
		return null;
	}

	/**
	 * Find label to jump to (asm)
	 */
	protected String findLabel() {
		BdsNode bdsNode = findBreakNode();
		return bdsNode.baseLabelName() + "end";
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
		sb.append(toAsmScopePop());

		// Jump for end of enclosing loop/switch
		String jmpLabel = findLabel();
		sb.append("jmp " + jmpLabel + "\n");
		return sb.toString();
	}

	protected String toAsmScopePop() {
		StringBuilder sb = new StringBuilder();
		int countScopes = countScopes();
		for (int i = 0; i < countScopes; i++)
			sb.append("scopepop\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "break";
	}
}
