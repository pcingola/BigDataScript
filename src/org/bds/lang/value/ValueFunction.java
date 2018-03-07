package org.bds.lang.value;

import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.type.Type;

/**
 * Define a value of type 'function'
 * @author pcingola
 */
public class ValueFunction extends ValueComposite {

	FunctionDeclaration fdecl;

	public ValueFunction(Type type) {
		super(type);
	}

	@Override
	public Value clone() {
		return this; // Function types are not cloned
	}

	@Override
	public Object get() {
		return fdecl;
	}

	@Override
	public void parse(String str) {
		throw new RuntimeException("String parsing unimplemented for type '" + this + "'");
	}

	@Override
	public void set(Object v) {
		fdecl = (FunctionDeclaration) v;
	};

}
