package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;

/**
 * A "Kill" statement.
 *
 * Kill a task/thread (or a list of tasks/threads) and
 * wait until all tasks/threads finished (i.e. died)
 *
 * @author pcingola
 */
public class Kill extends Statement {

	private static final long serialVersionUID = 7625787663609361813L;

	Expression taskId;

	public Kill(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'Kill'
		if (tree.getChildCount() > 1) taskId = (Expression) factory(tree, 1);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());
		sb.append(taskId.toAsm());
		sb.append("kill\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + (taskId != null ? " " + taskId : "") + "\n";
	}

}
