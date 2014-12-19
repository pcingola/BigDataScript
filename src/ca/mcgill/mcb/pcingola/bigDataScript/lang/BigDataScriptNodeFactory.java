package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A factory of nodes
 *
 * @author pcingola
 */
public class BigDataScriptNodeFactory {

	public static boolean debug = false;
	private static BigDataScriptNodeFactory bigDataScriptNodeFactory = new BigDataScriptNodeFactory();

	boolean createFakeIds = false;
	int nodeNumber = 1, fakeNodeNumber = Integer.MIN_VALUE;
	String packageName;
	HashMap<Integer, BigDataScriptNode> nodesById = new HashMap<Integer, BigDataScriptNode>(); // Important note: Node 0 means 'null' (numbering is one-based)

	/**
	 * Get singleton
	 */
	public static BigDataScriptNodeFactory get() {
		return bigDataScriptNodeFactory;
	}

	/**
	 * Reset Factory instance
	 */
	public static void reset() {
		bigDataScriptNodeFactory = new BigDataScriptNodeFactory();
	}

	/**
	 * Transform to a class name
	 */
	public String className(ParseTree tree) {
		String className = tree.getClass().getSimpleName();
		String end = "Context";
		if (className.endsWith(end)) className = className.substring(0, className.length() - end.length());
		if (className.equals("TypeArray")) return TypeList.class.getSimpleName();
		return className;
	}

	/**
	 * Create BigDataScriptNodes
	 */
	public final BigDataScriptNode factory(BigDataScriptNode parent, ParseTree tree) {
		if (tree == null) return null;
		if (tree instanceof TerminalNode) {
			if (debug) Gpr.debug("Terminal node: " + tree.getClass().getCanonicalName() + "\n\t\tText: '" + tree.getText() + "'" + "\n\t\tSymbol: " + ((TerminalNode) tree).getSymbol() + "\n\t\tPayload: " + ((TerminalNode) tree).getPayload());
			return null;
		}

		// Skip container nodes (they don't add value)
		int childNum = -1;
		while ((childNum = isSkip(tree)) >= 0) {
			if (debug) Gpr.debug("Skipping container node: " + tree.getClass().getSimpleName());
			tree = tree.getChild(childNum);
		}
		if (debug) Gpr.debug("Factory : " + tree.getClass().getSimpleName());

		// Get class name
		String className = className(tree);

		// Create
		BigDataScriptNode node = factory(className, parent, tree);
		return node;
	}

	/**
	 * Create BigDataScriptNodes
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BigDataScriptNode factory(String className, BigDataScriptNode parent, ParseTree tree) {
		if (className.startsWith(packageName())) className = className.substring(packageName().length());

		// This node doesn't do anything, it should not be created (it is a sub-product of the grammar)
		if ((tree != null) && isIgnore(tree)) return null;

		// Types: Get instance nodes (singletons)
		if (className.equals("TypePrimitiveBool")) return Type.BOOL;
		if (className.equals("TypePrimitiveInt")) return Type.INT;
		if (className.equals("TypePrimitiveReal")) return Type.REAL;
		if (className.equals("TypePrimitiveString")) return Type.STRING;
		if (className.equals("TypePrimitiveVoid")) return Type.VOID;

		// Create object
		try {
			className = packageName() + className;
			Class clazz = Class.forName(className);

			// Is it a Type?
			if (clazz == Type.class) {
				if (tree == null) return new Type(); // No tree data? return a new FAKE node

				// No need to create a new node
				String typeName = tree.getChild(0).getText().toUpperCase();
				return Type.get(typeName);
			}

			// Create class
			Constructor<BigDataScriptNode>[] classConstructors = clazz.getConstructors();
			Constructor<BigDataScriptNode> classConstructor = classConstructors[0];

			BigDataScriptNode csnode;

			// Number of arguments in constructor?
			if (classConstructor.getParameterTypes().length == 0) {
				csnode = (BigDataScriptNode) clazz.newInstance();
			} else if (classConstructor.getParameterTypes().length == 2) {
				// Two parameter constructor
				Object[] params = new Object[2];
				params[0] = parent;
				params[1] = tree;
				csnode = classConstructor.newInstance(params);
			} else throw new RuntimeException("Unknown constructor method for class '" + className + "'");

			// Done, return new object
			return csnode;
		} catch (Exception e) {
			throw new RuntimeException("Error creating object: Class '" + className + "'", e);
		}
	}

	/**
	 * Get current node ID (used for testing)
	 * @return
	 */
	public synchronized int getCurrentNodeId() {
		return nodeNumber;
	}

	/**
	 * Get an ID for a node and set 'nodesById'
	 */
	protected synchronized int getNextNodeId(BigDataScriptNode node) {
		int id = (!createFakeIds ? nodeNumber++ : fakeNodeNumber++);
		nodesById.put(id, node); // Update nodesById
		return id;
	}

	/**
	 * Get node by ID number
	 * @return Node or null if not found
	 */
	public synchronized BigDataScriptNode getNode(int nodeId) {
		return nodesById.get(nodeId);
	}

	/**
	 * Get all nodes
	 */
	public synchronized Collection<BigDataScriptNode> getNodes() {
		return nodesById.values();
	}

	public boolean isCreateFakeIds() {
		return createFakeIds;
	}

	public boolean isIgnore(ParseTree tree) {
		String className = className(tree);
		if (className.equals("Eol")) return true;
		if (className.equals("StatmentEol")) return true;
		return false;
	}

	/**
	 * Is this node just a container that can be skipped?
	 * @return Node number to use in the child or negative if 'tree' node should not be skipped
	 */
	int isSkip(ParseTree tree) {
		String className = className(tree);
		if (className.equals("StatementVarDeclaration")) return 0;
		if (className.equals("StatmentExpr")) return 0;
		if (className.equals("ExpressionParen")) return 1;
		return -1;
	}

	/**
	 * Get this class' package name
	 */
	public String packageName() {
		if (packageName != null) return packageName;

		packageName = BigDataScriptNode.class.getCanonicalName();
		int len = packageName.length();
		packageName = packageName.substring(0, len - BigDataScriptNode.class.getSimpleName().length());
		return packageName; // Add package name
	}

	/**
	 * Get the 'real node' corresponding to this 'fake node' (this is used during serialization)
	 */
	public BigDataScriptNode realNode(BigDataScriptNode fakeNode) {
		if (fakeNode == null) return null; // Nothing to do
		if (!fakeNode.isFake()) return fakeNode; // Real node? don't replace

		// Type nodes are not replaced, just ID is updated
		if (fakeNode instanceof Type) {
			int newId = getNextNodeId(fakeNode);
			fakeNode.updateId(newId);
			return fakeNode;
		}

		// Is it a fake node? => Replace by real node
		// Find real node based on fake ID
		int nodeId = -fakeNode.getId(); // Fake IDs are the negative values of real IDs
		BigDataScriptNode realNode = BigDataScriptNodeFactory.get().getNode(nodeId);

		// Check that node was replaced
		if ((nodeId > 0) && (realNode == null)) throw new RuntimeException("Cannot replace fake node '" + nodeId + "'");

		return realNode;
	}

	public void setCreateFakeIds(boolean createFakeIds) {
		this.createFakeIds = createFakeIds;
	}

	public void updateId(int oldId, int newId, BigDataScriptNode node) {
		if (debug) Gpr.debug("Update node ID: " + oldId + " -> " + newId + "\t" + node.getClass().getSimpleName());
		nodesById.remove(oldId);
		if (newId != 0) nodesById.put(newId, node);
		if (nodeNumber <= newId) nodeNumber = newId + 1;
	}
}
