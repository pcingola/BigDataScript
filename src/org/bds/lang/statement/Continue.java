package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
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
	 * Find label to jump to (asm)
	 */
	@Override
	protected String findLabel() {
		BdsNode bdsNode = findBreakNode();
		return bdsNode.getClass().getSimpleName() + "_continue_" + bdsNode.getId();
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
		if (!bdsThread.isCheckpointRecover()) bdsThread.setRunState(RunState.CONTINUE);
	}

	@Override
	protected String scopePop() {
		return ""; // Continue does not need to pop scopes
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		if ((findParent(ForLoop.class, FunctionDeclaration.class) == null) //
				&& (findParent(ForLoopList.class, FunctionDeclaration.class) == null) //
				&& (findParent(While.class, FunctionDeclaration.class) == null) //
		) compilerMessages.add(this, "continue statement outside loop", MessageType.ERROR);
	}

}
