package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;

/**
 * A "help" statement
 * Help statements serve as annotations for automatic help
 *
 * @author pcingola
 */
public class Help extends Statement {

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

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Nothing to do
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + " " + helpString + "\n";
	}

}
