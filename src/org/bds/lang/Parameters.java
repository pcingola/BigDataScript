package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Function / method parameters declaration
 *
 * @author pcingola
 */
public class Parameters extends BdsNode implements Comparable<Parameters> {

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
	}

	@Override
	public int compareTo(Parameters o) {
		throw new RuntimeException("Unimplemented!");
		// return 0;
	}

	/**
	 * Get parameter 'i' type
	 */
	public Type getType(int i) {
		return varDecl[i].type;
	}

	public VarDeclaration[] getVarDecl() {
		return varDecl;
	}

	@Override
	protected void parse(ParseTree tree) {
		parse(tree, 0, tree.getChildCount());
	}

	protected void parse(ParseTree tree, int offset, int max) {
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
