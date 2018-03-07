package org.bds.lang.value;

import org.bds.lang.type.Type;

public abstract class ValueComposite extends Value {

	protected Type type;

	public ValueComposite(Type type) {
		super();
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

}
