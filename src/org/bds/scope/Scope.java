package org.bds.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;

/**
 * Scope: Variables, functions and classes
 *
 * @author pcingola
 */
public class Scope implements Iterable<String> {

	Scope parent;
	String parentNodeId;
	Map<String, Value> values;
	BdsNode node;

	public Scope() {
		parent = GlobalScope.get();
		node = null;
		values = new HashMap<>();
	}

	/**
	 * Constructor
	 * @param parent : If null => use global Scope
	 */
	public Scope(Scope parent, BdsNode node) {
		this.parent = parent;
		this.node = node;
		values = new HashMap<>();
	}

	/**
	 * Add a value to scope using a primitive object
	 */
	public synchronized void add(String name, Object val) {
		Value value = Value.factory(val);
		values.put(name, value);
	}

	/**
	 * Add a new variable of type 'type'
	 */
	public synchronized void add(String name, Type type) {
		values.put(name, type.newDefaultValue());
	}

	/**
	 * Add a value to scope
	 */
	public synchronized void add(String name, Value value) {
		values.put(name, value);
	}

	/**
	 * Add all values from 'scope'
	 */
	public synchronized void addAll(Scope scope) {
		for (String name : scope)
			add(name, scope.getValue(name));
	}

	public Collection<String> getNames() {
		return values.keySet();
	}

	public BdsNode getNode() {
		return node;
	}

	public Scope getParent() {
		return parent;
	}

	public String getScopeName() {
		if (node == null) return "Global";
		return (node.getFileName() != null ? node.getFileName() + ":" + node.getLineNum() + ":" : "") + node.getClass().getSimpleName();
	}

	/**
	 * Get value on this scope (or any parent scope)
	 */
	public Value getValue(String name) {
		// Find value on this or any parent scope
		for (Scope scope = this; scope != null; scope = scope.parent) {
			// Try to find a value
			Value v = scope.getValueLocal(name);
			if (v != null) return v;
		}

		// Nothing found
		return null;
	}

	/**
	 * Get value on this scope (only search this scope)
	 */
	public synchronized Value getValueLocal(String value) {
		return values.get(value);
	}

	public Collection<Value> getValues() {
		return values.values();
	}

	public boolean hasValue(String value) {
		return getValue(value) != null;
	}

	/**
	 * Is value available on this scope or any parent scope?
	 */
	public boolean hasValueLocal(String value) {
		return getValueLocal(value) != null;
	}

	/**
	 * Is this scope empty?
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Iterator<String> iterator() {
		return values.keySet().iterator();
	}

	public void setParent(Scope parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean showFunc) {
		// Show parents
		StringBuilder sb = new StringBuilder();
		if (parent != null) {
			String parentStr = parent.toString(showFunc);
			if (!parentStr.isEmpty()) sb.append(parentStr);
		}

		// Show scope values
		StringBuilder sbThis = new StringBuilder();
		List<String> names = new ArrayList<>();
		names.addAll(values.keySet());
		Collections.sort(names);
		for (String n : names)
			sbThis.append(n + ": " + getValueLocal(n) + "\n");

		// Show header
		if (sbThis.length() > 0) sb.append("\n---------- Scope " + getScopeName() + " ----------\n" + sbThis.toString());

		return sb.toString();
	}

	public String toStringScopeNames() {
		StringBuilder sb = new StringBuilder();
		sb.append("Scopes:\n");

		int i = 0;
		for (Scope scope = this; scope != null; scope = scope.getParent())
			sb.append("\t" + (i++) + ": " + scope.getScopeName() + "\n");

		return sb.toString();
	}

}
