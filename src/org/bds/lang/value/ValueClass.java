package org.bds.lang.value;

import java.util.HashMap;
import java.util.Map;

import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.util.Gpr;

/**
 * Define a value of an object (i.e. a class)
 * @author pcingola
 */
public class ValueClass extends ValueComposite {

	Map<String, Value> fields; // TODO: Should I change to array for efficiency?

	public ValueClass(Type type) {
		super(type);
		init();
	}

	@Override
	public Value clone() {
		// !!! TODO Auto-generated method stub
		throw new RuntimeException("!!! UNIMPLEMENTED");
	}

	/**
	 * Get native object (raw data)
	 */
	@Override
	public Object get() {
		return fields;
	}

	@Override
	protected void init() {
		Gpr.debug("!!! INIT");
		fields = new HashMap<>();
		TypeClass tc = (TypeClass) type;
		for (VarDeclaration vd : tc.getVarDecl()) {
			Type vt = vd.getType();
			for (VariableInit vi : vd.getVarInit()) {
				Gpr.debug("!!! INIT: " + vi.getVarName() + "\t" + vt.newValue());
				fields.put(vi.getVarName(), vt.newValue());
			}
		}
	}

	@Override
	public void parse(String str) {
		throw new RuntimeException("String parsing unimplemented for type '" + this + "'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Object v) {
		fields = (Map<String, Value>) v;
	}

}
