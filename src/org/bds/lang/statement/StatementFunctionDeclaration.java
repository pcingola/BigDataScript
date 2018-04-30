package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Function declaration statement: It's just a statement that wraps a function declaration
 *
 * @author pcingola
 */
public class StatementFunctionDeclaration extends FunctionDeclaration {

	private static final long serialVersionUID = 3845968537453208768L;

	public StatementFunctionDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree.getChild(0));
	}

}
