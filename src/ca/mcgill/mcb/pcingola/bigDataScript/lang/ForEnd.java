package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 * 
 * @author pcingola
 */
public class ForEnd extends ExpressionList {

	public ForEnd(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

}
