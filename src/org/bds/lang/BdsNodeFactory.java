package org.bds.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.bds.BdsLog;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;

/**
 * A factory of nodes
 *
 * @author pcingola
 */
public class BdsNodeFactory implements BdsLog {

	private static BdsNodeFactory bdsNodeFactory = new BdsNodeFactory();

	public static final String packageNames[] = { //
			"org.bds.lang.expression" //
			, "org.bds.lang.statement" //
			, "org.bds.lang.type" //
			, "org.bds.lang.value" //
			, "org.bds.lang" //
	};

	int nodeNumber = 1;
	String packageName;
	Map<Integer, BdsNode> nodesById = new HashMap<>(); // Important note: Node 0 means 'null' (numbering is one-based)
	@SuppressWarnings("rawtypes")
	Map<String, Class> classByName = new HashMap<>(); // Class cache

	/**
	 * Get singleton
	 */
	public static BdsNodeFactory get() {
		return bdsNodeFactory;
	}

	/**
	 * Reset Factory instance
	 */
	public static void reset() {
		bdsNodeFactory = new BdsNodeFactory();
	}

	public synchronized BdsNode addNode(BdsNode bdsNode) {
		int nodeId = bdsNode.getId();
		if (nodeNumber <= nodeId) nodeNumber = nodeId + 1;
		return nodesById.put(nodeId, bdsNode);
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
	 * Find constructor and invoke it to create node
	 * @param clazz: Node class
	 * @param parent: Parameter for constructor
	 * @param tree: Parameter for constructor
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected BdsNode createBdsNode(Class clazz, BdsNode parent, ParseTree tree) {
		try {
			// Create class
			Constructor<BdsNode>[] classConstructors = clazz.getConstructors();
			if (classConstructors.length < 1) {
				// No public constructors found, try factory method
				return createBdsNodeFactory(clazz, parent, tree);
			}

			// Find appropriate constructor
			for (Constructor<BdsNode> classConstructor : classConstructors) {
				// Number of arguments in constructor?
				if (classConstructor.getParameterTypes().length == 0) {
					return (BdsNode) clazz.getDeclaredConstructor().newInstance();
				} else if (classConstructor.getParameterTypes().length == 2) {
					// Two parameter constructor
					Object[] params = { parent, tree };
					return classConstructor.newInstance(params);
				}
			}
			throw new RuntimeException("Unknown constructor method for class '" + clazz.getCanonicalName() + "'");
		} catch (Exception e) {
			throw new RuntimeException("Error creating object: Class '" + clazz.getCanonicalName() + "'", e);
		}
	}

	/**
	 * Find constructor and invoke it to create node
	 * @param clazz: Node class
	 * @param parent: Parameter for constructor
	 * @param tree: Parameter for constructor
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected BdsNode createBdsNodeFactory(Class clazz, BdsNode parent, ParseTree tree) {
		try {
			// Find factory method
			Class[] paramClasses = { BdsNode.class, ParseTree.class };
			Method factoryMethod = clazz.getMethod("factory", paramClasses);
			if (factoryMethod == null) throw new RuntimeException("Could not find any public constructors or factory method for class '" + clazz.getCanonicalName() + "'");

			// Invoke factory method
			Object[] params = { parent, tree };
			return (BdsNode) factoryMethod.invoke(null, params);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Could not find any public constructors or factory method for class '" + clazz.getCanonicalName() + "'", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Could not accedd factory method for class '" + clazz.getCanonicalName() + "'", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid arguments when invoking factory method for class '" + clazz.getCanonicalName() + "'", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Invalid invokation of factory method for class '" + clazz.getCanonicalName() + "'", e);
		}
	}

	/**
	 * Create BigDataScriptNodes
	 */
	public final BdsNode factory(BdsNode parent, ParseTree tree) {
		if (tree == null) return null;
		if (tree instanceof TerminalNode) {
			debug("Terminal node: " + tree.getClass().getCanonicalName() //
					+ "\n\t\tText    : '" + tree.getText() + "'" //
					+ "\n\t\tSymbol  : " + ((TerminalNode) tree).getSymbol() //
					+ "\n\t\tPayload : " + ((TerminalNode) tree).getPayload() //
			);
			return null;
		}

		// Skip container nodes (they don't add map)
		int childNum = -1;
		while ((childNum = isSkip(tree)) >= 0) {
			debug("Skipping container node: " + tree.getClass().getSimpleName());
			tree = tree.getChild(childNum);
		}
		debug("Factory : " + tree.getClass().getSimpleName());

		// Get class name
		String className = className(tree);

		// Create
		BdsNode node = factory(className, parent, tree);
		return node;
	}

	/**
	 * Create BigDataScriptNodes
	 */
	@SuppressWarnings({ "rawtypes" })
	public BdsNode factory(String className, BdsNode parent, ParseTree tree) {
		className = stripPackageName(className);

		// This node doesn't do anything, it should not be created (it is a sub-product of the grammar)
		if ((tree != null) && isIgnore(tree)) return null;

		// Types: Get instance nodes (singletons)
		if (className.equals("TypeAny")) return Types.ANY;
		if (className.equals("TypeBool")) return Types.BOOL;
		if (className.equals("TypeInt")) return Types.INT;
		if (className.equals("TypeNull")) return Types.NULL;
		if (className.equals("TypeReal")) return Types.REAL;
		if (className.equals("TypeString")) return Types.STRING;
		if (className.equals("TypeVoid")) return Types.VOID;

		// Create object
		Class clazz = findClass(className);

		// Is it a Type?
		if (clazz == Type.class) {
			if (tree == null) throw new RuntimeException("Tree data is null, cannot build node!"); // No tree data? return a new FAKE node
			throw new RuntimeException("TODO: REMOVE THIS CODE !!!!!????");
			//			// No need to create a new node
			//			String typeName = tree.getChild(0).getText();
			//			return Types.get(typeName);
		}

		// Create class
		return createBdsNode(clazz, parent, tree);
	}

	@SuppressWarnings("rawtypes")
	Class findClass(String className) {
		// Is it cached?
		if (classByName.containsKey(className)) return classByName.get(className);

		// Find full package name
		for (String packageName : packageNames) {
			try {
				String fqcn = packageName + "." + className;
				Class clazz = Class.forName(fqcn);
				classByName.put(className, clazz);
				return clazz;
			} catch (ClassNotFoundException e) {
				// Not found, keep looking
			}
		}

		throw new RuntimeException("Cannot find class '" + className + "'. This should never happen!");
	}

	/**
	 * Get current node ID (used for testing)
	 */
	public synchronized int getCurrentNodeId() {
		return nodeNumber;
	}

	/**
	 * Get an ID for a node and set 'nodesById'
	 */
	protected synchronized int getNextNodeId(BdsNode node) {
		int id = nodeNumber++;
		nodesById.put(id, node); // Update nodesById
		return id;
	}

	/**
	 * Get node by ID number
	 * @return Node or null if not found
	 */
	public synchronized BdsNode getNode(int nodeId) {
		return nodesById.get(nodeId);
	}

	/**
	 * Get all nodes
	 */
	public synchronized Collection<BdsNode> getNodes() {
		return nodesById.values();
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
		// if (className.equals("StatmentExpr")) return 0;
		if (className.equals("ExpressionParen")) return 1;
		return -1;
	}

	/**
	 * Get this class' package name
	 */
	public String packageName() {
		if (packageName != null) return packageName;

		packageName = BdsNode.class.getCanonicalName();
		int len = packageName.length();
		packageName = packageName.substring(0, len - BdsNode.class.getSimpleName().length());
		return packageName; // Add package name
	}

	/**
	 * Remove Java package name from class name
	 */
	String stripPackageName(String className) {
		if (className.indexOf('.') < 0) return className;
		String cn[] = className.split("\\.");
		return cn[cn.length - 1];
	}
}
