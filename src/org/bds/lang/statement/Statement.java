package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Statement
 *
 * @author pcingola
 */
public class Statement extends BdsNode {

	private static final long serialVersionUID = -5043760544380911589L;

	public Statement(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		throw new RuntimeException("This method should never be invoked!");
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + "\n";
	}

}
