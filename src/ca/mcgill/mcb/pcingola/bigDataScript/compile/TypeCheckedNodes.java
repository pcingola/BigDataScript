package ca.mcgill.mcb.pcingola.bigDataScript.compile;

import java.util.HashSet;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;

/**
 * Have nodes been type-checked?
 * 
 * @author pcingola
 */
public class TypeCheckedNodes {

	private static TypeCheckedNodes typeCheckedNodes = new TypeCheckedNodes();

	public static TypeCheckedNodes get() {
		return typeCheckedNodes;
	}

	HashSet<BigDataScriptNode> done;

	private TypeCheckedNodes() {
		reset();
	}

	/**
	 * Add a node to the type checked set
	 * @param node
	 */
	public void add(BigDataScriptNode node) {
		done.add(node);
	}

	/**
	 * Has 'node' been type-checked?
	 */
	public boolean isTypeChecked(BigDataScriptNode node) {
		return done.contains(node);
	}

	/**
	 * Reset all data
	 */
	public void reset() {
		done = new HashSet<BigDataScriptNode>();
	}

}
