package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A general purpose node
 * 
 * @author pcingola
 */
public class GenericNode extends BigDataScriptNode {

	File file;
	BigDataScriptNode nodes[];

	public GenericNode(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	protected void parse(ParseTree tree) {
		// Nothing to do
	}

	public void setFile(File file) {
		this.file = file;
	}

}
