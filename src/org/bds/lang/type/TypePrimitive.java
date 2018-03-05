package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Primitive types
 *
 * @author pcingola
 */
public abstract class TypePrimitive extends Type {

	protected TypePrimitive(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public TypePrimitive(PrimitiveType primitiveType) {
		super(primitiveType);
	}

}
