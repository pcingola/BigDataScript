package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 * 
 * @author pcingola
 */
public class ForLoop extends StatementWithScope {

	// Note:	It is important that 'begin' node is type-checked before the others in order to 
	//			add variables to the scope before ForCondition, ForEnd or Statement uses them.
	//			So the field name should be alphabetically sorted before the other (that's why 
	//			I call it 'begin' and not 'init').
	//			Yes, it's a horrible hack.
	ForInit begin;
	ForCondition condition;
	ForEnd end;
	Statement statement;

	public ForLoop(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		idx++; // 'for'

		idx++; // '('

		begin = (ForInit) factory(tree, idx++);
		idx++; // ';'

		condition = (ForCondition) factory(tree, idx++);
		idx++; // ';'

		end = (ForEnd) factory(tree, idx++);
		idx++; // ')'

		statement = (Statement) factory(tree, idx++);
	}

	/**
	 * Run 
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		// Loop initialization
		begin.run(csThread);

		while (condition.evalBool(csThread)) { // Loop condition
			RunState rstate = statement.run(csThread); // Loop statement

			switch (rstate) {
			case OK:
			case CHECKPOINT_RECOVER:
				break;

			case BREAK: // Break from loop, OK done
				return RunState.OK;

			case RETURN: // Return
			case EXIT: // Exit program
				return rstate;

			case CONTINUE: // Nothing to do, just continue with the next iteration
				break;

			default:
				throw new RuntimeException("Unhandled RunState: " + rstate);
			}

			end.run(csThread); // End of loop
		}

		return RunState.OK;
	}

}
