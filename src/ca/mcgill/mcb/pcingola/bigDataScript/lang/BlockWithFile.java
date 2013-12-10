package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A block of statements associated to a file (program's source)
 * 
 * @author pcingola
 */
public class BlockWithFile extends Block {

	protected String fileName;
	protected String fileText;

	public BlockWithFile(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public File getFile() {
		if (fileName != null) return new File(fileName);
		else return super.getFile();
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	public String getFileText() {
		return fileText;
	}

	@Override
	public Statement[] getStatements() {
		return statements;
	}

	public void setFile(File file) {
		if (file == null) setFileName(null);
		else setFileName(file.toString());
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		if (fileName != null) fileText = Gpr.readFile(fileName);
	}

}
