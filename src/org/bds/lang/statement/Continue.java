package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * A "continue" statement
 *
 * @author pcingola
 */
public class Continue extends Break {

	private static final long serialVersionUID = -378455671089769598L;

	public Continue(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * How many enclosing scopes do we have to "break' through?
	 */
	@Override
	protected int countScopes() {
		int countScopes = 0;
		for (BdsNode bn = this; bn != null; bn = bn.getParent()) {
			if (bn.isNeedsScope()) countScopes++;

			if (isContinue(bn)) return countScopes - 1; // Pop all scopes, except the one in current loop
			else if (isFunction(bn)) return countScopes;
		}
		return countScopes;
	}

	/**
	 * Find enclosing loop
	 */
	protected BdsNode findContinueNode() {
		for (BdsNode bn = this; bn != null; bn = bn.getParent()) {
			if (isContinue(bn)) return bn;
			else if (isFunction(bn)) return null; // Reached function or method definition?
			// Note: Cannot be inside a 'try/catch/finally' statement
		}
		return null;
	}

	/**
	 * Find label to jump to (asm)
	 */
	@Override
	protected String findLabel() {
		BdsNode bdsNode = findBreakNode();
		return bdsNode.baseLabelName() + "continue";
	}

	/**
	 * Is it an enclosing loop you can 'continue'?
	 */
	boolean isContinue(BdsNode bdsNode) {
		return bdsNode instanceof While //
				|| bdsNode instanceof ForLoop //
				|| bdsNode instanceof ForLoopList //
		;
	}

	@Override
	protected void parse(ParseTree tree) {
		// Nothing to do
	}

	@Override
	public String toString() {
		return "continue";
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		if ((findParent(ForLoop.class, FunctionDeclaration.class) == null) //
				&& (findParent(ForLoopList.class, FunctionDeclaration.class) == null) //
				&& (findParent(While.class, FunctionDeclaration.class) == null) //
		) compilerMessages.add(this, "continue statement outside loop", MessageType.ERROR);
	}

}
