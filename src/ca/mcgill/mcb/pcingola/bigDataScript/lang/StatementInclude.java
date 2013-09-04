package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

/**
 * Include statement: Get source from another file
 */
public class StatementInclude extends Block {

	String includedFilename;
	File includedFile;

	private static boolean isValidFileName(String fileName2) {
		if (fileName2 == null || fileName2.length() == 0 || !fileName2.trim().equals(fileName2)) return false;
		return true;
	}

	public StatementInclude(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		setNeedsScope(false);
		// ----------file name: this is merely informational
		includedFilename = tree.getChild(0).getChild(1).getText();
		includedFilename = includedFilename.substring(1, includedFilename.length() - 1).trim();
		File includedFile = new File(includedFilename);
		File parentFile = getFile();
		if (parentFile != null && !includedFile.isAbsolute()) includedFile = new File(parentFile.getParent(), includedFilename);
		// -------------
		setFile(includedFile);
		super.parse(tree.getChild(0)); // the Block parses the stament
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		if (!isValidFileName(includedFilename)) compilerMessages.add(this, "For loop condition must be a bool expression", MessageType.ERROR);
		super.typeCheck(scope, compilerMessages);
	}

	@Override
	public void typeChecking(Scope scope, CompilerMessages compilerMessages) {
		super.typeChecking(scope, compilerMessages);
	}

}
