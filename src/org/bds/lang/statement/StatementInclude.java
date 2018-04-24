package org.bds.lang.statement;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.Config;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * Include statement: Get source from another file
 */
public class StatementInclude extends BlockWithFile {

	private static final long serialVersionUID = 7172299775285428224L;


	protected String parentFileName;

	/**
	 * Find the appropriate file
	 * @param includedFilename
	 * @param parent : Parent file (where the 'include' statement is)
	 */
	public static File includeFile(String includedFilename, File parent) {
		File includedFile = new File(includedFilename);
		if (includedFile.exists() && includedFile.canRead()) return includedFile;

		// Not an absolute file name? Try to find relative to parent file
		if (!includedFile.isAbsolute()) {
			if (parent != null && parent.exists()) includedFile = new File(parent.getParent(), includedFilename);
			if (includedFile.exists() && includedFile.canRead()) return includedFile;

			// Try parent's canonical path (e.g. when file is a symLink)
			File parentCanon = null;
			try {
				parentCanon = parent.getCanonicalFile();
				if (parentCanon != null && parentCanon.exists()) includedFile = new File(parentCanon.getParent(), includedFilename);
				if (includedFile.exists() && includedFile.canRead()) return includedFile;
			} catch (IOException e) {
			}

			// Try all include paths
			for (String incPath : Config.get().getIncludePath()) {
				File incDir = new File(incPath);
				includedFile = new File(incDir, includedFilename);
				if (includedFile.exists() && includedFile.canRead()) return includedFile; // Found the file?
			}
		}

		return null;
	}

	/**
	 * Find the appropriate include file name
	 */
	public static String includeFileName(String includedFilename) {
		if (includedFilename.startsWith("\'") || includedFilename.startsWith("\"")) includedFilename = includedFilename.substring(1); // Remove leading quotes
		if (includedFilename.endsWith("\'") || includedFilename.endsWith("\"")) includedFilename = includedFilename.substring(0, includedFilename.length() - 1).trim(); // Remove trailing quotes
		if (!includedFilename.endsWith(".bds")) includedFilename += ".bds"; // Append '.bds' if needed (it's optional)
		return includedFilename;
	}

	private static boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.length() == 0 || !fileName.trim().equals(fileName)) return false;
		return true;
	}

	public StatementInclude(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public String getParentFileName() {
		return parentFileName;
	}

	@Override
	protected void parse(ParseTree tree) {
		setNeedsScope(false);

		// File name & parent file name: this is merely informational
		File parentFile = getParent().getFile();
		parentFileName = (parentFile != null ? parentFile.toString() : null);
		fileName = includeFileName(tree.getChild(0).getChild(1).getText());

		// Resolve file and read program text
		File includedFile = StatementInclude.includeFile(fileName, parentFile);
		setFile(includedFile);

		super.parse(tree.getChild(0)); // Block parses statement
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (!isValidFileName(fileName)) compilerMessages.add(this, "include: Invalid file name '" + fileName + "'", MessageType.ERROR);
	}

}
