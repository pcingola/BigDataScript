package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForInit extends Statement {

	private static final long serialVersionUID = -5184554138462446111L;

	VarDeclaration varDeclaration;
	Expression expressions[];

	public ForInit(BdsNode parent, ParseTree tree) {
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

	@Override
	public String toAsm() {
		if (varDeclaration != null) return varDeclaration.toAsm();

		// Use expressions
		StringBuilder sb = new StringBuilder();
		if (expressions != null) {
			for (Expression exp : expressions) {
				sb.append(exp.toAsm() + "\npop\n");
			}
		}

		return sb.toString();
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
