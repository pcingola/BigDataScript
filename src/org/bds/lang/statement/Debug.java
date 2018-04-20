package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * An "debug" statement
 *
 * @author pcingola
 */
public class Debug extends Print {

	private static final long serialVersionUID = -5740742098036893666L;

	public Debug(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public String toAsm() {
		return toAsmNode() //
				+ (expr != null ? expr.toAsm() : "pushs ''\n") //
				+ "debug\n";
	}

}
