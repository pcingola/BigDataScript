package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Primitive types
 *
 * @author pcingola
 */
public abstract class TypeComposite extends Type {

	protected TypeComposite(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public TypeComposite(PrimitiveType primitiveType) {
		super(primitiveType);
	}

	@Override
	public Object castNativeObject(Object o) {
		throw new RuntimeException("Cannot cast native object '" + o.getClass().getCanonicalName() + "' to type '" + this + "'");
	}

}
