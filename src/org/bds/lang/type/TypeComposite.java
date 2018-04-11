package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.value.Value;

/**
 * Primitive types
 *
 * @author pcingola
 */
public abstract class TypeComposite extends Type {

	private static final long serialVersionUID = 6716080829178399904L;

	protected TypeComposite(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public TypeComposite(PrimitiveType primitiveType) {
		super(primitiveType);
	}

	@Override
	public Value newValue(Object v) {
		throw new RuntimeException("Unimplemented. This method should never be invoked!");
	}

}
