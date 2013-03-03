package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;

/**
 * A "Kill" statement.
 * 
 * Kill a taks (or a list of tasks) and wait untill all tasks finished (i.e. died)
 * 
 * @author pcingola
 */
public class Kill extends Statement {

	Expression taskId;

	public Kill(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'Kill'
		if (tree.getChildCount() > 1) taskId = (Expression) factory(tree, 1);
	}

	/**
	 * Run the program
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		// No arguments? Kill for all tasks
		Object val = taskId.eval(csThread);

		// Are we Killing for one task or a list of tasks?
		if (val instanceof List) {
			csThread.killTasks((List) val);
			csThread.waitTasks((List) val);
		} else {
			csThread.killTask(val.toString());
			csThread.waitTask(val.toString());
		}

		return RunState.OK;
	}

}
