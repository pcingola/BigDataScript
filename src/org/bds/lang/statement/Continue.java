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
public class Continue extends Statement {

	public Continue(BdsNode parent, ParseTree tree) {
		super(parent, tree);
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
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		if ((findParent(ForLoop.class, FunctionDeclaration.class) == null) //
				&& (findParent(ForLoopList.class, FunctionDeclaration.class) == null) //
				&& (findParent(While.class, FunctionDeclaration.class) == null) //
		) compilerMessages.add(this, "continue statement outside loop", MessageType.ERROR);
	}
}
