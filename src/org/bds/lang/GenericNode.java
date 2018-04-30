package org.bds.lang;

import java.io.File;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A general purpose node
 *
 * @author pcingola
 */
public class GenericNode extends BdsNode {

	private static final long serialVersionUID = 5426115409963411438L;

	File file;
	BdsNode nodes[];

	public GenericNode(BdsNode parent, ParseTree tree) {
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
