package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;

/**
 * A block of statements
 * 
 * @author pcingola
 */
public class Block extends StatementWithScope {

	String fileName;
	Statement statements[];

	public Block(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public File getFile() {
		if (fileName != null) return new File(fileName);
		else return super.getFile();
	}

	public Statement[] getStatements() {
		return statements;
	}

	@Override
	protected void parse(ParseTree tree) {
		ArrayList<Statement> stats = new ArrayList<Statement>();
		for (int i = 0; i < tree.getChildCount(); i++) {
			BigDataScriptNode node = factory(tree, i);
			if (node != null) stats.add((Statement) node);
		}

		// Create an array
		statements = stats.toArray(new Statement[0]);
	}

	/**
	 * Run the program
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		for (Statement st : statements) {
			if (st != null) {
				RunState rstate = st.run(csThread);

				// Act based on run state
				switch (rstate) {
				case OK: // OK do nothing
				case CHECKPOINT_RECOVER:
					break;

				case BREAK: // Break form this block immediately
				case CONTINUE:
				case RETURN:
				case EXIT:
					return rstate;

				default:
					throw new RuntimeException("Unhandled RunState: " + rstate);
				}
			}
		}

		return RunState.OK;
	}

	public void setFile(File file) {
		if (file == null) fileName = null;
		else this.fileName = file.toString();
	}

}
