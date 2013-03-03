package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A general pourpose node
 * 
 * @author pcingola
 */
public class GenericNode extends BigDataScriptNode {

	BigDataScriptNode nodes[];

	public GenericNode(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	//	public BigDataScriptNode[] getNodes() {
	//		return nodes;
	//	}

	@Override
	protected void parse(ParseTree tree) {
		throw new RuntimeException("Deprecated node type " + getClass().getSimpleName());

		//		int num = tree.getChildCount();
		//		nodes = new BigDataScriptNode[num];
		//		for (int i = 0; i < num; i++)
		//			nodes[i] = factory(tree, i);
	}

}
