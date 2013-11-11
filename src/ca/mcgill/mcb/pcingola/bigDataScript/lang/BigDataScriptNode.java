package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.TypeCheckedNodes;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Base AST node for BigDataScript language elements
 * 
 * @author pcingola
 */
public abstract class BigDataScriptNode implements BigDataScriptSerialize {

	public static boolean debug = false;

	protected BigDataScriptNode parent;
	protected int id, lineNum, charPosInLine; // Source code info

	/**
	 * Constructor
	 * @param parent
	 * @param tree Not null if you want the parsing to be performed now
	 */
	public BigDataScriptNode(BigDataScriptNode parent, ParseTree tree) {
		id = BigDataScriptNodeFactory.get().getNextNodeId(this);
		this.parent = parent;

		if (debug) Gpr.debug("NEW BigDataScriptNode:\t" + id + "\t" + getClass().getSimpleName());

		// Initialize some defaults
		initialize();
		doParse(tree);
	}

	/**
	 * This should only be called from the outside if tree not passed in the constructor 
	 * @param tree
	 */
	protected void doParse(ParseTree tree) {
		if (tree != null) {
			// TODO: Add line and char info
			lineAndPos(tree);

			// Parse ANTLR tree
			try {
				parse(tree);
			} catch (Exception e) {
				throw new RuntimeException("Error parsing file '" + getFileName() + "', line " + lineNum + ", char " + charPosInLine + ", node '" + this + "'", e);
			}

			// Sanity checks
			sanityCheck(CompilerMessages.get());
		}

	}

	/**
	 * Create a BigDataScriptNode
	 * @param tree
	 * @param childNum
	 * @return
	 */
	final BigDataScriptNode factory(ParseTree tree, int childNum) {
		ParseTree child = tree.getChild(childNum);
		return BigDataScriptNodeFactory.get().factory(this, child);
	}

	/**
	 * Find all nodes of a given type
	 * @param clazz : Class to find (all nodes if null)
	 * @param recurse : If true, perform recursive search
	 */
	@SuppressWarnings("rawtypes")
	public List<BigDataScriptNode> findNodes(Class clazz, boolean recurse) {
		ArrayList<BigDataScriptNode> list = new ArrayList<BigDataScriptNode>();

		// Iterate over fields
		for (Field field : getAllClassFields()) {
			try {
				Object fieldObj = field.get(this);

				// Does the field have a value?
				if (fieldObj != null) {

					// If it's an array, iterate on all objects
					if (fieldObj.getClass().isArray()) {
						for (Object fieldObjSingle : (Object[]) fieldObj)
							list.addAll(findNodes(clazz, fieldObjSingle, recurse));
					} else list.addAll(findNodes(clazz, fieldObj, recurse));

				}
			} catch (Exception e) {
				throw new RuntimeException("Error getting field '" + field.getName() + "' from class '" + this.getClass().getCanonicalName() + "'", e);
			}
		}

		return list;
	}

	/**
	 * Find all nodes of a given type
	 * @param clazz : If null, all nodes are added
	 * @param fieldObj
	 */
	@SuppressWarnings("rawtypes")
	List<BigDataScriptNode> findNodes(Class clazz, Object fieldObj, boolean recurse) {
		ArrayList<BigDataScriptNode> list = new ArrayList<BigDataScriptNode>();

		// If it is a BigDataScriptNode then we can recurse into it
		if ((fieldObj != null) && (fieldObj instanceof BigDataScriptNode)) {
			// Found the requested type?
			if ((clazz == null) || (fieldObj.getClass() == clazz)) list.add((BigDataScriptNode) fieldObj);

			// We can recurse into this field
			if (recurse) {
				BigDataScriptNode csnode = ((BigDataScriptNode) fieldObj);
				list.addAll(csnode.findNodes(clazz, recurse));
			}
		}

		return list;
	}

	/**
	 * Find a parent of type 'clazz'
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected BigDataScriptNode findParent(Class clazz) {
		if (this.getClass() == clazz) return this;
		if (parent != null) return parent.findParent(clazz);
		return null;
	}

	/**
	 * Find any parent node 'clazz' before any node 'stopAtClass'
	 * @param clazz
	 * @param stopAtClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected BigDataScriptNode findParent(Class clazz, Class stopAtClass) {
		if (this.getClass() == clazz) return this;
		if (this.getClass() == stopAtClass) return null;
		if (parent != null) return parent.findParent(clazz);
		return null;
	}

	ArrayList<Field> getAllClassFields() {
		return getAllClassFields(false, true, true, true, true, false, false);
	}

	ArrayList<Field> getAllClassFields(boolean addParent) {
		return getAllClassFields(addParent, true, true, false, true, false, false);
	}

	/**
	 * Get all fields from this class
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	ArrayList<Field> getAllClassFields(boolean addParent, boolean addNode, boolean addPrimitive, boolean addClass, boolean addArray, boolean addStatic, boolean addPrivate) {
		// Top class (if we are looking for 'parent' field, we need to include BigDataScriptNode, otherwise we don't
		Class topClass = (addParent ? Object.class : BigDataScriptNode.class);

		// Get all fields for each parent class
		ArrayList<Field> fields = new ArrayList<Field>();
		for (Class clazz = this.getClass(); clazz != topClass; clazz = clazz.getSuperclass()) {
			for (Field f : clazz.getDeclaredFields()) {
				// Add field?
				if (Modifier.isPrivate(f.getModifiers())) {
					if (addPrivate) fields.add(f);
				} else if (Modifier.isStatic(f.getModifiers())) {
					if (addStatic) fields.add(f);
				} else if (f.getName().equals("parent")) {
					if (addParent) fields.add(f);
				} else if (f.getType().getCanonicalName().startsWith(BigDataScriptNodeFactory.get().packageName())) {
					if (addNode) fields.add(f);
				} else if (f.getType().isPrimitive() || (f.getType() == String.class)) {
					if (addPrimitive) fields.add(f);
				} else if (f.getType().isArray()) {
					if (addArray) fields.add(f);
				} else if (!f.getType().isPrimitive()) {
					if (addClass) fields.add(f);
				}
			}
		}

		// Sort by name
		Collections.sort(fields, new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return fields;
	}

	public BigDataScriptThread getBigDataScriptThread() {
		if (parent != null) return parent.getBigDataScriptThread();
		return null;
	}

	public int getCharPosInLine() {
		return charPosInLine;
	}

	/**
	 * Find file (this information is stored in 'ProgramUnit', or in 'Block' node)
	 * @return
	 */
	public File getFile() {
		return parent != null ? parent.getFile() : null;
	}

	public final String getFileName() {
		File f = getFile();
		return f == null ? null : f.toString();
	}

	public int getId() {
		return id;
	}

	public int getLineNum() {
		return lineNum;
	}

	public BigDataScriptNode getParent() {
		return parent;
	}

	/**
	 * Recurse unti top node is found.
	 * Top node is always a 'ProgramUnit' node.
	 * 
	 * @return
	 */
	public ProgramUnit getProgramUnit() {
		BigDataScriptNode n = this;
		while (n.getParent() != null)
			n = n.getParent();
		return (ProgramUnit) n;
	}

	/**
	 * Find a child terminal node having 'str' as text
	 * @param tree
	 * @param str
	 * @return
	 */
	protected int indexOf(ParseTree tree, String str) {
		for (int i = 0; i < tree.getChildCount(); i++)
			if (isTerminal(tree, i, str)) return i;

		return -1;
	}

	/**
	 * Initialize some defaults (before parsing)
	 */
	void initialize() {
	}

	/**
	 * Is this a fake node (created during serialization)
	 * @return
	 */
	public boolean isFake() {
		return id <= 0;
	}

	/**
	 * Does this node require a new scope
	 * @return
	 */
	public boolean isNeedsScope() {
		return false;
	}

	/**
	 * Is child 'idx' a terminal node with value 'str'?
	 * @param tree
	 * @param idx
	 * @param str
	 * @return
	 */
	protected boolean isTerminal(ParseTree tree, int idx, String str) {
		ParseTree node = tree.getChild(idx);
		if ((node instanceof TerminalNode) && node.getText().equals(str)) return true;
		return false;
	}

	/**
	 * Try to get line number and character position
	 * 
	 * Note: For some weird reason this info is only available at Token level.
	 * 
	 * @param tree
	 */
	boolean lineAndPos(ParseTree tree) {
		// Is this a token?
		if (tree.getPayload() instanceof Token) {
			lineAndPos((Token) tree.getPayload());
			return true;
		}

		// Any child a token?
		for (int i = 0; i < tree.getChildCount(); i++) {
			ParseTree node = tree.getChild(i);
			if (node.getPayload() instanceof Token) {
				lineAndPos((Token) node.getPayload());
				return true;
			}
		}

		// Last resort: Recurse
		for (int i = 0; i < tree.getChildCount(); i++)
			if (lineAndPos(tree.getChild(i))) return true;

		// Cannot set 
		return false;
	}

	void lineAndPos(Token token) {
		lineNum = token.getLine();
		charPosInLine = token.getCharPositionInLine();
	}

	/**
	 * Parse a tree
	 */
	protected abstract void parse(ParseTree tree);

	/**
	 * Show a parseTree node
	 * @param tree
	 */
	void printNode(ParseTree tree) {
		System.out.println(tree.getClass().getSimpleName());
		for (int i = 0; i < tree.getChildCount(); i++) {
			ParseTree node = tree.getChild(i);
			System.out.println("\tchild[" + i + "]\ttype: " + node.getClass().getSimpleName() + "\ttext: '" + node.getText() + "'");
		}
	}

	/**
	 * Replace fake nodes by real nodes
	 */
	public void replaceFake() {

		// Iterate over fields
		for (Field field : getAllClassFields(true)) {
			try {
				Object fieldObj = field.get(this);

				// Does the field have a value?
				if (fieldObj != null) {
					// If it's an array, iterate on all objects
					if (fieldObj.getClass().isArray()) {
						int idx = 0;
						for (Object fieldObjSingle : (Object[]) fieldObj) {
							if ((fieldObjSingle != null) && (fieldObjSingle instanceof BigDataScriptNode)) {
								BigDataScriptNode csnode = (BigDataScriptNode) fieldObjSingle;

								// Is it a fake node? => Replace by real node
								if (csnode.isFake()) {
									// Find real node based on fake ID
									int nodeId = -csnode.getId(); // Fake IDs are the negative values of real IDs
									BigDataScriptNode trueCsnode = BigDataScriptNodeFactory.get().getNode(nodeId);
									if ((nodeId > 0) && (trueCsnode == null)) throw new RuntimeException("Cannot replace fake node :" + nodeId);

									// Replace this array element 
									if (debug) Gpr.debug(getClass().getSimpleName() + "." + field.getName() + "[" + idx + "] = \tNode id:" + nodeId + "\t" + (trueCsnode != null ? trueCsnode.getClass().getSimpleName() : "null"));
									Array.set(fieldObj, idx, trueCsnode);
								}
							}
							idx++;
						}
					} else {
						if (fieldObj instanceof BigDataScriptNode) {
							BigDataScriptNode csnode = (BigDataScriptNode) fieldObj;

							// Is it a fake node? => Replace by real node
							if (csnode.isFake()) {
								// Find real node based on fake ID
								int nodeId = -csnode.getId(); // Fake IDs are the negative values of real IDs
								BigDataScriptNode trueCsnode = BigDataScriptNodeFactory.get().getNode(nodeId);
								if ((nodeId > 0) && (trueCsnode == null)) //
									throw new RuntimeException("Cannot replace fake node :" + nodeId);

								// Set field to real node
								if (debug) Gpr.debug(getClass().getSimpleName() + "." + field.getName() + " = \tNode id:" + nodeId + "\t" + (trueCsnode != null ? trueCsnode.getClass().getSimpleName() : "null"));
								field.set(this, trueCsnode);
							}
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Error replacing fake field '" + field.getName() + "' from class '" + this.getClass().getCanonicalName() + "'", e);
			}
		}

	}

	/**
	 * Run this node
	 * @param csThread
	 * @return
	 */
	public RunState run(BigDataScriptThread csThread) {
		boolean recover = csThread.isCheckpointRecover();

		if (!recover) runBegin(csThread); // Before node execution

		RunState rstate = null;
		try {
			if (csThread.shouldRun(this)) rstate = runStep(csThread); // Run 
			else rstate = RunState.CHECKPOINT_RECOVER;
		} catch (Throwable t) {
			System.err.println("Fatal error: " + getFileName() + "[" + getLineNum() + ":" + getCharPosInLine() + "]");

			// Re-throw the exception if possible
			if (t instanceof RuntimeException) throw ((RuntimeException) t);
			else throw new RuntimeException(t);
		}

		if (!recover) runEnd(csThread); // After node execution

		return rstate;
	}

	/**
	 * Run before running the node
	 * @param csThread
	 */
	protected void runBegin(BigDataScriptThread csThread) {
		// Need a new scope?
		if (isNeedsScope()) csThread.newScope();
		csThread.getPc().push(this);
	}

	/**
	 * Run after running the node
	 * @param csThread
	 */
	protected void runEnd(BigDataScriptThread csThread) {
		csThread.getPc().pop(this);
		// Restore old scope?
		if (isNeedsScope()) csThread.oldScope();
	}

	protected RunState runStep(BigDataScriptThread csThread) {
		throw new RuntimeException("Unimplemented method for class " + getClass().getSimpleName() + ", id = " + id);
	}

	/**
	 * Perform several basic sanity checks right after parsing the tree
	 */
	protected void sanityCheck(CompilerMessages compilerMessages) {
		// Default : Do nothing
	}

	/**
	 * Parse a line from a serialized file
	 * @param line
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		// Use ID from file
		updateId((int) serializer.getNextFieldInt());

		// Set parent node
		int parentId = serializer.getNextFieldNodeId();
		if (parentId != 0) {
			// Parent node is not null
			parent = new ParentNode();
			parent.setFakeId(parentId);
		}

		// Iterate over fields
		for (Field field : getAllClassFields(false)) {
			try {
				Class fieldClass = field.getType();
				Object value = serializer.parse(fieldClass, fieldClass.getComponentType());

				field.set(this, value);
			} catch (Exception e) {
				throw new RuntimeException("Error loading field '" + field.getName() + "' from class '" + this.getClass().getCanonicalName() + "'", e);
			}
		}
	}

	/**
	 * Create a string to serialize to a file
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();

		// Not an array: Single field. Show 
		out.append(getClass().getSimpleName() + "\t" + id + "\t" + serializer.serializeSaveValue(parent) + "\t");
		ArrayList<BigDataScriptNode> nodesToRecurse = new ArrayList<BigDataScriptNode>();

		// Iterate over fields
		for (Field field : getAllClassFields(false)) {
			try {
				Object fieldObj = field.get(this);
				Class fieldClass = field.getDeclaringClass();

				// Does the field have a value?
				if (fieldObj != null) {
					// If it's an array, iterate on all objects
					if (fieldObj.getClass().isArray()) {
						for (Object fieldObjSingle : (Object[]) fieldObj) {
							out.append(serializer.serializeSaveValue(fieldObjSingle) + ",");

							// Can we recurse into this field?
							if ((fieldObjSingle != null) && (fieldObjSingle instanceof BigDataScriptNode)) nodesToRecurse.add((BigDataScriptNode) fieldObjSingle);
						}

						out.deleteCharAt(out.length() - 1); // Remove last comma
						out.append("\t");
					} else {
						// Serialize field value

						if (fieldObj instanceof Scope) {
							// Do not serialize scope here
						} else out.append(serializer.serializeSaveValue(fieldObj) + "\t");

						// Can we recurse into this field?
						if ((fieldObj != null) && (fieldObj instanceof BigDataScriptNode)) nodesToRecurse.add((BigDataScriptNode) fieldObj);
					}
				} else {
					// Value of this field is null
					if (fieldClass.getCanonicalName().startsWith(BigDataScriptNodeFactory.get().packageName())) out.append("null\t");
					else out.append(serializer.serializeSaveValue(fieldObj) + "\t");
				}
			} catch (Exception e) {
				throw new RuntimeException("Error getting field '" + field.getName() + "' from class '" + this.getClass().getCanonicalName() + "'", e);
			}
		}

		out.deleteCharAt(out.length() - 1); // Remove last tab
		out.append("\n");

		serializer.add(this);

		// Recurse
		for (BigDataScriptNode node : nodesToRecurse)
			if (!serializer.isSerialized(node)) out.append(node.serializeSave(serializer));

		return out.toString();
	}

	/**
	 * Set a fake ID number (this is a fake node created during serialization)
	 * NOTE!: We set it to a negative number. This is a fake node
	 * @param id
	 */
	public void setFakeId(int id) {
		if (id < 0) return; // Is ID already 'fake' node? => Do nothing
		updateId(-id); // Update using a negative ID (fake ID)
	}

	public void setNeedsScope(boolean needsScope) {
		throw new RuntimeException("Cannot set 'needsScope' in this node:" + this.getClass().getSimpleName());
	}

	@Override
	public String toString() {
		return "Program: " + getFileName() + "\n";
	}

	/**
	 * Show the tree
	 * @param tabs
	 */
	public String toStringTree(String tabs, String fieldName) {
		StringBuilder out = new StringBuilder();

		// Not an array: Single field. Show 
		out.append(tabs + this.getClass().getSimpleName() + " " + fieldName + "\t[" + id + " | " + (parent != null ? parent.getId() : "") + "]\n");

		// Iterate over fields
		for (Field field : getAllClassFields()) {
			try {
				Object fieldObj = field.get(this);

				// Does the field have a value?
				if (fieldObj != null) {

					// If it's an array, iterate on all objects
					if (fieldObj.getClass().isArray()) {
						int idx = 0;
						for (Object fieldObjSingle : (Object[]) fieldObj) {
							// We can recurse into this field
							if ((fieldObjSingle != null) && (fieldObjSingle instanceof BigDataScriptNode)) {
								BigDataScriptNode csnode = (BigDataScriptNode) fieldObjSingle;
								out.append(csnode.toStringTree(tabs + "\t", field.getName() + "[" + idx + "]") + "\n");
							}
							idx++;
						}
					} else {
						// We can recurse into this field
						if ((fieldObj != null) && (fieldObj instanceof BigDataScriptNode)) {
							BigDataScriptNode csnode = (BigDataScriptNode) fieldObj;
							out.append(csnode.toStringTree(tabs + "\t", field.getName()) + "\n");
						} else out.append(tabs + field.getType().getSimpleName() + " " + field.getName() + " : " + fieldObj + "\n");
					}
				} else {
					// Value of this field is null
					out.append(tabs + field.getType().getSimpleName() + " " + field.getName() + " = null\n");
				}
			} catch (Exception e) {
				throw new RuntimeException("Error getting field '" + field.getName() + "' from class '" + this.getClass().getCanonicalName() + "'", e);
			}
		}

		while ((out.length() > 2) && (out.charAt(out.length() - 1) == '\n') && (out.charAt(out.length() - 2) == '\n'))
			out.deleteCharAt(out.length() - 1);

		return out.toString();
	}

	/**
	 * Perform a typecheck
	 * @param scope
	 * @param compilerMessages
	 */
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Default: Nothing to do
	}

	/**
	 * Perform several basic type checking tasks.
	 * Invoke 'typeCheck()' method on all sub-nodes
	 */
	public void typeChecking(Scope scope, CompilerMessages compilerMessages) {
		// Create a new scope?
		if (isNeedsScope()) {
			Scope newScope = new Scope(scope);
			scope = newScope;
		}

		// Once the scope is right, we can perform the real type-check
		typeCheck(scope, compilerMessages);

		// Get all sub-nodes (first level, do not recurse)
		List<BigDataScriptNode> nodes = findNodes(null, false);

		// Add this node as 'type-checked' to avoid infinite recursion
		TypeCheckedNodes.get().add(this);

		// Check all sub-nodes
		for (BigDataScriptNode node : nodes) {
			// Not already type-checked? Go ahead
			if (!TypeCheckedNodes.get().isTypeChecked(node)) {
				node.typeChecking(scope, compilerMessages);
			}
		}

		// Scope processing
		if (isNeedsScope()) {
			// Do we really need a scope? If the scope is empty, we don't really need it
			setNeedsScope(!scope.isEmpty());

			// Get back to previous scope
			scope = scope.getParent();
		}
	}

	/**
	 * Update ID field
	 * @param newId
	 */
	protected void updateId(int newId) {
		BigDataScriptNodeFactory.get().updateId(id, newId, this);
		id = newId;
	}
}
