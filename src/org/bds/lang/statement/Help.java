package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A "help" statement
 * Help statements serve as annotations for automatic help
 *
 * @author pcingola
 */
public class Help extends Statement {

	private static final long serialVersionUID = 7858059563027890150L;

	public static final int HELP_KEYWORD_LEN = "help".length() + 1;

	String helpString;

	public Help(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public String getHelpString() {
		return helpString;
	}

	@Override
	protected void parse(ParseTree tree) {
		helpString = tree.getText();
		if (helpString.length() > HELP_KEYWORD_LEN) helpString = helpString.substring(HELP_KEYWORD_LEN);
		else helpString = "";
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + " " + helpString + "\n";
	}

}
