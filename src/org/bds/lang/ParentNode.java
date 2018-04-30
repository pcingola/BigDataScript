package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Fake node used during serialization
 * 
 * @author pcingola
 */
public class ParentNode extends BdsNode {

	private static final long serialVersionUID = 6198450603450990592L;


	public ParentNode() {
		super(null, null);
	}

	@Override
	protected void parse(ParseTree tree) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
