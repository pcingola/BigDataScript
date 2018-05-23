package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A 'task' statement (to execute a command line in a node)
 *
 * @author pcingola
 */
public class ExpressionTaskLiteral extends ExpressionTask {

	private static final long serialVersionUID = 1353838436863213568L;

	public static final String TASK_STR = "task";

	public ExpressionTaskLiteral(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// Get task literal
		String valueStr = tree.getText();
		if (valueStr.startsWith(TASK_STR)) valueStr = tree.getText().substring(TASK_STR.length());
		valueStr = valueStr.trim();

		ExpressionSys sys = new ExpressionSys(this, null);
		sys.setCommands(valueStr);
		statement = sys;

		parsePrelude();
	}

}
