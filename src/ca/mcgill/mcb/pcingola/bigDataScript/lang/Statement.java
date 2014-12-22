package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Statement
 *
 * @author pcingola
 */
public class Statement extends BigDataScriptNode {

	public Statement(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		throw new RuntimeException("This method should never be invoked!");
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + "\n";
	}

}
