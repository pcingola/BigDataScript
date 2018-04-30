package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * An "print" statement
 *
 * @author pcingola
 */
public class Println extends Print {

	private static final long serialVersionUID = 2059553580460692477L;

	public Println(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public String toAsm() {
		return toAsmNode() //
				+ (expr != null ? expr.toAsm() : "push ''\n") //
				+ "println\n";
	}
}
