package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;

/**
 * Function / method parameters declaration
 *
 * @author pcingola
 */
public class Parameters extends BdsNode implements Comparable<Parameters> {

	private static final long serialVersionUID = -5814584605870004567L;

	public static final Parameters EMPTY = new Parameters(null, null);

	VarDeclaration varDecl[];

	/**
	 * Create a list of 'num' parameters
	 */
	public static Parameters get(Type types[], String names[]) {
		Parameters params = new Parameters(null, null);
		params.varDecl = new VarDeclaration[types.length];

		for (int i = 0; i < types.length; i++) {
			VarDeclaration vd = VarDeclaration.get(types[i], names[i]);
			params.varDecl[i] = vd;
		}

		return params;
	}

	/**
	 * Single parameter
	 */
	public static Parameters get(Type type, String name) {
		Parameters params = new Parameters(null, null);
		params.varDecl = new VarDeclaration[1];

		VarDeclaration vd = VarDeclaration.get(type, name);
		params.varDecl[0] = vd;

		return params;
	}

	public Parameters(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		if (parent == null && tree == null) varDecl = new VarDeclaration[0];
	}

	/**
	 * Prepend 'this' to parameters
	 */
	public void addThis(TypeClass typeThis) {
		// Copy to new array (shifted by 1)
		VarDeclaration newVarDecl[] = new VarDeclaration[varDecl.length + 1];
		for (int i = 0; i < varDecl.length; i++)
			newVarDecl[i + 1] = varDecl[i];

		// Add 'this'
		VarDeclaration varThis = VarDeclaration.get(typeThis, ClassDeclaration.VAR_THIS);
		newVarDecl[0] = varThis;

		varDecl = newVarDecl;
	}

	@Override
	public int compareTo(Parameters o) {
		throw new RuntimeException("!!! Unimplemented!");
	}

	/**
	 * Compare arguments for method call
	 */
	public boolean equalsMethod(Parameters p) {
		int parsSize = size();

		// Same number of parameters?
		if (parsSize != p.size()) return false;

		// Only 'this' parameter, it's a match
		if (parsSize == 1) return true;

		// Compare arguments, except for first argument ('this')
		for (int i = 1; i < parsSize; i++) {
			Type pi = getType(i);
			Type ppi = p.getType(i);
			if (!pi.equals(ppi)) return false;
		}

		return true;
	}

	/**
	 * Get parameter's 'i' type
	 */
	public Type getType(int i) {
		return varDecl[i].getType();
	}

	public VarDeclaration[] getVarDecl() {
		return varDecl;
	}

	/**
	 * Get parameter's 'i' name
	 */
	public String getVarName(int i) {
		if (varDecl == null) return null;
		VariableInit[] vis = varDecl[i].getVarInit();
		return vis[0].getVarName();
	}

	@Override
	protected void parse(ParseTree tree) {
		parse(tree, 0, tree.getChildCount());
	}

	public void parse(ParseTree tree, int offset, int max) {
		int num = (max - offset + 1) / 2; // Comma separated list of expressions
		varDecl = new VarDeclaration[num];

		for (int i = offset, j = 0; i < max; j++, i += 2) {
			varDecl[j] = (VarDeclaration) factory(tree, i);
		}
	}

	public int size() {
		return varDecl.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < varDecl.length; i++) {
			sb.append(varDecl[i]);
			if (i < varDecl.length - 1) sb.append(",");
		}
		return sb.toString();
	}

}
