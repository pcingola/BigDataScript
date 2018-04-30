package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * An "error" statement (quit the program immediately)
 *
 * @author pcingola
 */
public class Error extends Print {

	private static final long serialVersionUID = 9181909100500109811L;

	public Error(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public String toAsm() {
		return toAsmNode() //
				+ expr.toAsm() //
				+ "error\n";
	}
}
