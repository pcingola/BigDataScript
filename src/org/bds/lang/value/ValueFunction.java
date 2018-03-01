package org.bds.lang.value;

import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.type.Type;

/**
 * Define a value of type 'function'
 * @author pcingola
 */
public abstract class ValueFunction extends Value {

	FunctionDeclaration fdecl;

	private ValueFunction(Type type) {
		super(type);
		init();
	}

	@Override
	protected void init() {
		set(type.getDefaultValue());
	}

	@Override
	public void parse(String str) {
		throw new RuntimeException("String parsing unimplemented for type '" + this + "'");
	}

	@Override
	public void set(Object v) {
		// !!! TODO: Check that parameters and return type are OK
		fdecl = (FunctionDeclaration) v;
	};

}
