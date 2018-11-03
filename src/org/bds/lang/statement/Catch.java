package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.TypeClass;

/**
 * try / catch / finally statements
 *
 * @author pcingola
 */
public class Catch extends StatementWithScope {

	/**
	 *
	 */
	private static final long serialVersionUID = -2978341443887136421L;
	String[] varNames;
	TypeClass[] typeClassExceptions;
	Statement statement;

	public Catch(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
	}

	public int parse(ParseTree tree, int idx) {
		List<TypeClass> typeClassList = new ArrayList<>();
		List<String> varNameList = new ArrayList<>();

		if (isTerminal(tree, idx, "catch")) idx++;
		if (isTerminal(tree, idx, "(")) idx++;
		while (!isTerminal(tree, idx, ")")) {
			// Exception type class
			TypeClass tc = (TypeClass) factory(tree, idx++);
			typeClassList.add(tc);

			// Variable name
			String varName = tree.getChild(idx++).getText();
			varNameList.add(varName);
		}

		// Convert lists to arrays
		varNames = varNameList.toArray(new String[0]);
		typeClassExceptions = typeClassList.toArray(new TypeClass[0]);

		if (isTerminal(tree, idx, ")")) idx++;
		statement = (Statement) factory(tree, idx++);
		return idx;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("catch (");
		if (typeClassExceptions != null) {
			for (int i = 0; i < typeClassExceptions.length; i++)
				sb.append(typeClassExceptions[i] + " " + varNames[i]);
		}
		sb.append(") {\n");
		if (statement != null) sb.append(statement);
		sb.append("} ");
		return sb.toString();
	}

}
