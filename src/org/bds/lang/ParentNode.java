package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Fake node used during serialization
 * 
 * @author pcingola
 */
public class ParentNode extends BdsNode {

	public ParentNode() {
		super(null, null);
	}

	@Override
	protected void parse(ParseTree tree) {
		throw new RuntimeException("This method should never be invoked!");
	}

	@Override
	public void setFakeId(int id) {
		// Note: ParentNode does not invoke UpdateId
		this.id = -id;
	}

}
