package org.bds.lang.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
	}

	@Override
	public Value clone() {
		throw new RuntimeException("!!! UNIMPLEMENTED");
	}

	/**
	 * Get native object (raw data)
	 */
	@Override
	public Object get() {
		return fields;
	}

	/**
	 * Initialize fields (by default the fields are null)
	 */
	public void initializeFields() {
		Gpr.debug("!!! INIT");
		fields = new HashMap<>();
		TypeClass tc = (TypeClass) type;
		VarDeclaration vdecl[] = tc.getClassDeclaration().getVarDecl();
		for (VarDeclaration vd : vdecl) {
			Type vt = vd.getType();
			for (VariableInit vi : vd.getVarInit()) {
				Gpr.debug("!!! INIT: " + vi.getVarName() + "\t" + vt.newDefaultValue());
				fields.put(vi.getVarName(), vt.newDefaultValue());
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<String> fnames = new ArrayList<>(fields.size());
		fnames.addAll(fields.keySet());
		Collections.sort(fnames);
		sb.append("{");
		for (int i = 0; i < fnames.size(); i++) {
			String fn = fnames.get(i);
			sb.append((i > 0 ? ", " : " ") + fn + ": " + fields.get(fn));
		}
		sb.append(" }");
		return sb.toString();
	}

}
