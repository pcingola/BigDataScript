package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * Variable declaration
 *
 * @author pcingola
 */
public class FieldDeclaration extends VarDeclaration {

	public FieldDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add variable to symbol table
	 */
	@Override
	protected void addVar(SymbolTable symtab, CompilerMessages compilerMessages, String varName) {
		// Fields are added during class parsing. Nothing to do here
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree.getChild(0));
	}

	/**
	 * Run
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		for (VariableInit vi : varInit) {
			if (!bdsThread.isCheckpointRecover()) {
				bdsThread.getScope().add(vi.varName, type.newDefaultValue()); // Add variable to scope
			}

			bdsThread.run(vi);

			// Act based on run state
			switch (bdsThread.getRunState()) {
			case OK: // OK do nothing
			case CHECKPOINT_RECOVER:
				break;

			case BREAK: // Break form this block immediately
			case CONTINUE:
			case RETURN:
			case EXIT:
			case FATAL_ERROR:
				return;

			default:
				throw new RuntimeException("Unhandled RunState: " + bdsThread.getRunState());
			}
		}
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// This is checked during ClassDeclaration. Nothing to do here
	}

}
