package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A reference to a field in 'this' object
 *
 * @author pcingola
 */
public class ReferenceThis extends ReferenceVar {

	public ReferenceThis(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

}
