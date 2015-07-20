package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForInit extends Statement {

	VarDeclaration varDeclaration;
	Expression expressions[];

	public ForInit(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		String nodeTxt = tree.getChild(0).getClass().getSimpleName();

		if (nodeTxt.startsWith("VarDeclaration")) varDeclaration = (VarDeclaration) factory(tree, 0);
		else {
			// Expressions
			int num = tree.getChildCount();
			expressions = new Expression[num];
			for (int i = 0; i < num; i++)
				expressions[i] = (Expression) factory(tree, i);
		}
	}

	/**
	 * Run
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		if (varDeclaration != null) bdsThread.run(varDeclaration);
		else {
			for (Expression expr : expressions) {
				bdsThread.run(expr);
				bdsThread.pop(); // Remove from stack, nobody is reading the results
			}
		}
	}

	@Override
	public String toString() {

		if (varDeclaration != null) return varDeclaration.toString();

		// Use expressions
		StringBuilder sb = new StringBuilder();
		if (expressions != null) {
			for (Expression exp : expressions) {
				if (sb.length() > 0) sb.append(" , ");
				sb.append(exp.toString());
			}
		}

		return sb.toString();
	}
}
