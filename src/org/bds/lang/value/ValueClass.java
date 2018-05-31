package org.bds.lang.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FieldDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;

/**
 * Define a value of an object (i.e. a class)
 * @author pcingola
 */
public class ValueClass extends ValueComposite {

	private static final long serialVersionUID = -1443386366370835828L;

	Map<String, Value> fields;

	public ValueClass(Type type) {
		super(type);
	}

	@Override
	public Value clone() {
		ValueClass vclone = new ValueClass(type);
		vclone.fields.putAll(fields);
		return vclone;
	}

	public Value getValue(String name) {
		if (isNull()) throw new RuntimeException("Null pointer: Cannot access field '" + getType() + "." + name + "'");
		return fields.get(name);
	}

	@Override
	public int hashCode() {
		return fields != null ? fields.hashCode() : 0;
	}

	/**
	 * Initialize fields (by default the fields are null)
	 */
	public void initializeFields() {
		fields = new HashMap<>();
		TypeClass tc = (TypeClass) type;

		// Fields for this class and all parent classes
		for (ClassDeclaration cd = tc.getClassDeclaration(); cd != null; cd = cd.getClassParent()) {
			FieldDeclaration fieldDecls[] = cd.getFieldDecl();
			for (FieldDeclaration fieldDecl : fieldDecls) { // Add all fields
				Type vt = fieldDecl.getType();
				for (VariableInit vi : fieldDecl.getVarInit()) {
					String fname = vi.getVarName();
					if (!fields.containsKey(fname)) { // Don't overwrite values 'shadowed' by a child class
						fields.put(fname, vt.newDefaultValue());
					}
				}
			}
		}
	}

	public boolean isNull() {
		return fields == null;
	}

	@Override
	public void parse(String str) {
		throw new RuntimeException("String parsing unimplemented for type '" + this + "'");
	}

	public void setValue(String name, Value v) {
		if (isNull()) throw new RuntimeException("Null pointer: Cannot set field '" + getType() + "." + name + "'");
		fields.put(name, v);
	}

	@Override
	public void setValue(Value v) {
		fields = ((ValueClass) v).fields;
	}

	@Override
	protected void toString(StringBuilder sb) {
		if (isNull()) {
			sb.append("null");
			return;
		}

		sb.append("{");
		if (fields != null) {
			List<String> fnames = new ArrayList<>(fields.size());
			fnames.addAll(fields.keySet());
			Collections.sort(fnames);
			for (int i = 0; i < fnames.size(); i++) {
				String fn = fnames.get(i);
				Value val = fields.get(fn);
				if (sb.length() < MAX_TO_STRING_LEN) {
					sb.append((i > 0 ? ", " : " ") + fn + ": ");
					val.toString(sb);
				} else {
					sb.append("...");
					return;
				}
			}
		}
		sb.append(" }");
	}

}
