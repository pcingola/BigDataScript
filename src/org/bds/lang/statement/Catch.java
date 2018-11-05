package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.TypeClass;

/**
 * try / catch / finally statements
 *
 * @author pcingola
 */
public class Catch extends StatementWithScope {

	private static final long serialVersionUID = -2978341443887136421L;

	String varName;
	TypeClass typeClassException;
	Statement statement;

	public Catch(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	String getLabel() {
		return baseLabelName() + "catch";
	}

	@Override
	protected void parse(ParseTree tree) {
	}

	public int parse(ParseTree tree, int idx) {
		if (isTerminal(tree, idx, "catch")) idx++;
		if (isTerminal(tree, idx, "(")) idx++;
		typeClassException = (TypeClass) factory(tree, idx++);
		varName = tree.getChild(idx++).getText();
		if (isTerminal(tree, idx, ")")) idx++;
		statement = (Statement) factory(tree, idx++);
		return idx;
	}

	public String toAsm(String finallyLabel) {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());
		sb.append(getLabel() + ":\n");
		// Reset Exception handler
		// Note: If another exception is thrown within the 'catch' block, this
		// exception handler should not handle it (it should be handled
		// by a surrounding try/catch)
		sb.append("reh\n");
		if (statement != null) sb.append(statement.toAsm());
		sb.append("jmp '" + finallyLabel + "'\n");
		return sb.toString();
	}

	/**
	 * Add to Exception handler
	 */
	public String toAsmAddToExceptionHandler() {
		return "pushs '" + varName + "'\n" //
				+ "pushs '" + typeClassException.getClassName() + "'\n" //
				+ "aeh '" + getLabel() + "'\n" //
		;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("catch (");
		if (typeClassException != null && varName != null) sb.append(typeClassException + " " + varName);
		sb.append(") {\n");
		if (statement != null) sb.append(statement);
		sb.append("} ");
		return sb.toString();
	}

}
