package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Include statement: Get source from another file
 */
public class StatementInclude extends BlockWithFile {

	protected String parentFileName;

	/**
	 * Find the appropriate file
	 * @param includedFilename
	 * @param parent : Parent file (where the 'include' statement is)
	 * @return
	 */
	public static File includedFile(String includedFilename, File parent) {
		File includedFile = new File(includedFilename);
		if (includedFile.exists() && includedFile.canRead()) return includedFile;

		// Not an absolute file name? Try to find relative to parent file
		if (parent != null && parent.exists() && !includedFile.isAbsolute()) includedFile = new File(parent.getParent(), includedFilename);
		if (includedFile.exists() && includedFile.canRead()) return includedFile;

		// Try all include paths
		for (String incPath : Config.get().getIncludePath()) {
			File incDir = new File(incPath);
			includedFile = new File(incDir, includedFilename);
			if (includedFile.exists() && includedFile.canRead()) return includedFile; // Found the file?
		}

		return null;
	}

	/**
	 * Find the appropriate (canonical) include file name
	 * @param includedFilename
	 * @return
	 */
	public static String includedFileName(String includedFilename) {
		if (includedFilename.startsWith("\'") || includedFilename.startsWith("\"")) includedFilename = includedFilename.substring(1); // Remove leading quotes
		if (includedFilename.endsWith("\'") || includedFilename.endsWith("\"")) includedFilename = includedFilename.substring(0, includedFilename.length() - 1).trim(); // Remove trailing quotes
		if (!includedFilename.endsWith(".bds")) includedFilename += ".bds"; // Append '.bds' if needed (it's optional)

		return includedFilename;
	}

	private static boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.length() == 0 || !fileName.trim().equals(fileName)) return false;
		return true;
	}

	public StatementInclude(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public String getParentFileName() {
		return parentFileName;
	}

	@Override
	protected void parse(ParseTree tree) {
		setNeedsScope(false);

		// File name & parent file name: this is merely informational
		File parentFile = getFile();
		parentFileName = (parentFile != null ? parentFile.toString() : null);
		fileName = includedFileName(tree.getChild(0).getChild(1).getText());

		super.parse(tree.getChild(0)); // Block parses statement
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		if (!isValidFileName(fileName)) compilerMessages.add(this, "include: Invalid file name '" + fileName + "'", MessageType.ERROR);
		super.typeCheck(scope, compilerMessages);
	}

	@Override
	public void typeChecking(Scope scope, CompilerMessages compilerMessages) {
		super.typeChecking(scope, compilerMessages);
	}

}
