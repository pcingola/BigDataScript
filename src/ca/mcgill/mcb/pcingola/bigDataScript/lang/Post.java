package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * Post increment / decrement operator
 * 
 * E.g. :  i++ or i--
 * 
 * @author pcingola
 */
public class Post extends Pre {

	public Post(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		VarReference name = (VarReference) expr;
		ScopeSymbol ssym = name.getScopeSymbol(csThread.getScope());
		long value = (Long) ssym.getValue();

		if (operation == PrePostOperation.INCREMENT) ssym.setValue(value + 1);
		else if (operation == PrePostOperation.DECREMENT) ssym.setValue(value - 1);
		else throw new RuntimeException("Unknown operator " + operation);

		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		BigDataScriptNode node = factory(tree, 0);
		if ((node instanceof VarReference)) expr = (Expression) node;

		operation = PrePostOperation.parse(tree.getChild(1).getText());
	}

}
