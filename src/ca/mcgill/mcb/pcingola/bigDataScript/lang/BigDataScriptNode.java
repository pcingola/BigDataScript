package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.TypeCheckedNodes;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Base AST node for BigDataScript language elements
 *
 * @author pcingola
 */
public abstract class BigDataScriptNode implements BigDataScriptSerialize {

	protected BigDataScriptNode parent;
	protected int id, lineNum, charPosInLine; // Source code info
	protected Type returnType;

	/**
	 * Constructor
	 * @param parent : Parent node
	 * @param tree   : Not null if you want the parsing to be performed now
	 */
	public BigDataScriptNode(BigDataScriptNode parent, ParseTree tree) {
		id = BigDataScriptNodeFactory.get().getNextNodeId(this);
		this.parent = parent;

		// Initialize some defaults
		initialize();
		doParse(tree);
	}

	/**
	 * Can returnType be casted to bool?
	 */
	protected boolean canCastBool() {
		return ((returnType != null) && returnType.canCast(Type.BOOL));
	}

	/**
	 * Can returnType be casted to int?
	 */
	protected boolean canCastInt() {
		return ((returnType != null) && returnType.canCast(Type.INT));
	}

	/**
	 * Can returnType be casted to real?
	 */
	protected boolean canCastReal() {
		return ((returnType != null) && returnType.canCast(Type.REAL));
	}

	/**
	 * Check that this expression can be casted to bool
	 * Add a compile error otherwise
	 */
	protected void checkCanCastBool(CompilerMessages compilerMessages) {
		if ((returnType != null) && !returnType.canCast(Type.BOOL)) compilerMessages.add(this, "Cannot cast " + returnType + " to bool", MessageType.ERROR);
	}

	/**
	 * Check that this expression can be casted to int
	 * Add a compile error otherwise
	 */
	protected void checkCanCastInt(CompilerMessages compilerMessages) {
		if ((returnType != null) && !returnType.canCast(Type.INT)) compilerMessages.add(this, "Cannot cast " + returnType + " to int", MessageType.ERROR);
	}

	/**
	 * Check that this expression can be casted to either int or real
	 * Add a compile error otherwise
	 */
	protected void checkCanCastIntOrReal(CompilerMessages compilerMessages) {
		if ((returnType != null) //
				&& (!returnType.canCast(Type.INT) //
						&& !returnType.canCast(Type.REAL)) //
				) compilerMessages.add(this, "Cannot cast " + returnType + " to int or real", MessageType.ERROR);
	}

	/**
	 * This should only be called from the outside if tree not passed in the constructor
	 */
	protected void doParse(ParseTree tree) {
		if (tree != null) {
			lineAndPos(tree); // Add line and char info

			// Parse ANTLR tree
			try {
				parse(tree);
			} catch (Exception e) {
				throw new RuntimeException("Error parsing file '" + getFileName() + "', line " + lineNum + ", char " + charPosInLine + ", node '" + this + "'", e);
			}

			// Sanity checks
			sanityCheck(CompilerMessages.get());
		} else {
			if (parent != null && parent.getLineNum() > 0) lineNum = parent.getLineNum();
		}
	}

	/**
	 * Evaluate an expression, return result
	 */
	public void eval(BigDataScriptThread bdsThread) {
		throw new RuntimeException("Unplemented method 'eval' for class " + getClass().getSimpleName());
	}

	//	/**
	//	 * Evaluate an expression as an 'bool'
	//	 */
	//	public boolean evalBool(BigDataScriptThread bdsThread) {
	//		eval(bdsThread);
	//		return (Boolean) Type.BOOL.cast(bdsThread.pop());
	//	}
	//
	//	/**
	//	 * Evaluate an expression as an 'int'
	//	 */
	//	public long evalInt(BigDataScriptThread bdsThread) {
	//		eval(bdsThread);
	//		return (Long) Type.INT.cast(bdsThread.pop());
	//	}
	//
	//	/**
	//	 * Evaluate an expression as an 'real'
	//	 */
	//	public double evalReal(BigDataScriptThread bdsThread) {
	//		eval(bdsThread);
	//		return (Double) Type.REAL.cast(bdsThread.pop());
	//	}
	//
	//	/**
	//	 * Evaluate an expression as an 'bool'
	//	 */
	//	public String evalString(BigDataScriptThread bdsThread) {
	//		eval(bdsThread);
	//		return (String) Type.STRING.cast(bdsThread.pop());
	//	}

	/**
	 * Create a BigDataScriptNode
	 */
	final BigDataScriptNode factory(ParseTree tree, int childNum) {
		ParseTree child = childNum >= 0 ? tree.getChild(childNum) : tree;
		return BigDataScriptNodeFactory.get().factory(this, child);
	}

	/**
	 * Finds a terminal node in the tree (by name)
	 */
	protected int findIndex(ParseTree tree, String name, int start) {
		for (int i = start; i < tree.getChildCount(); i++)
			if (tree.getChild(i).toString().equals(name)) return i;

		return -1;
	}

	/**
	 * Find all nodes of a given type
	 * @param clazz : Class to find (all nodes if null)
	 * @param recurse : If true, perform recursive search
	 */
	@SuppressWarnings("rawtypes")
	public List<BigDataScriptNode> findNodes(Class clazz, boolean recurse) {
		HashSet<Object> visited = new HashSet<Object>();
		return findNodes(clazz, recurse, visited);
	}

	@SuppressWarnings("rawtypes")
	List<BigDataScriptNode> findNodes(Class clazz, boolean recurse, Set<Object> visited) {
		List<BigDataScriptNode> list = new ArrayList<BigDataScriptNode>();

		// Iterate over fields
		for (Field field : getAllClassFields()) {
			try {
				Object fieldObj = field.get(this);

				// Does the field have a value?
				if (fieldObj != null && !visited.contains(fieldObj)) {
					visited.add(fieldObj);

					// If it's an array, iterate on all objects
					if (fieldObj.getClass().isArray()) {
						for (Object fieldObjSingle : (Object[]) fieldObj)
							list.addAll(findNodes(clazz, fieldObjSingle, recurse, visited));
					} else {
						list.addAll(findNodes(clazz, fieldObj, recurse, visited));
					}

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
	List<BigDataScriptNode> findNodes(Class clazz, Object fieldObj, boolean recurse, Set<Object> visited) {
		List<BigDataScriptNode> list = new ArrayList<BigDataScriptNode>();

		// If it is a BigDataScriptNode then we can recurse into it
		if ((fieldObj != null) && (fieldObj instanceof BigDataScriptNode)) {
			// Found the requested type?
			if ((clazz == null) || (fieldObj.getClass() == clazz)) list.add((BigDataScriptNode) fieldObj);

			// We can recurse into this field
			if (recurse) {
				BigDataScriptNode csnode = ((BigDataScriptNode) fieldObj);
				list.addAll(csnode.findNodes(clazz, recurse, visited));
			}
		}

		return list;
	}

	/**
	 * Find a parent of type 'clazz'
	 */
	@SuppressWarnings("rawtypes")
	protected BigDataScriptNode findParent(Class clazz) {
		if (this.getClass() == clazz) return this;
		if (parent != null) return parent.findParent(clazz);
		return null;
	}

	/**
	 * Find any parent node 'clazz' before any node 'stopAtClass'
	 */
	@SuppressWarnings("rawtypes")
	protected BigDataScriptNode findParent(Class clazz, Class stopAtClass) {
		if (this.getClass() == clazz) return this;
		if (this.getClass() == stopAtClass) return null;
		if (parent != null) return parent.findParent(clazz);
		return null;
	}

	List<Field> getAllClassFields() {
		return getAllClassFields(false, true, true, true, true, false, false);
	}

	List<Field> getAllClassFields(boolean addParent) {
		return getAllClassFields(addParent, true, true, false, true, false, false);
	}

	/**
	 * Get all fields from this class
	 */
	@SuppressWarnings("rawtypes")
	List<Field> getAllClassFields(boolean addParent, boolean addNode, boolean addPrimitive, boolean addClass, boolean addArray, boolean addStatic, boolean addPrivate) {
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
	 */
	public File getFile() {
		return parent != null ? parent.getFile() : null;
	}

	public String getFileName() {
		File f = getFile();
		return f == null ? null : f.toString();
	}

	public int getId() {
		return id;
	}

	public int getLineNum() {
		return lineNum;
	}

	@Override
	public String getNodeId() {
		return getClass().getSimpleName() + ":" + id;
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
	 * Which type does this expression return?
	 */
	public Type getReturnType() {
		return returnType;
	}

	public Scope getScope() {
		return null;
	}

	/**
	 * Find a child terminal node having 'str' as text
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
	 * Is return type bool?
	 */
	protected boolean isBool() {
		return (returnType != null) && returnType.isBool();
	}

	/**
	 * Is this a fake node (created during serialization)
	 */
	public boolean isFake() {
		return id <= 0;
	}

	/**
	 * Is return type int?
	 */
	protected boolean isInt() {
		return (returnType != null) && returnType.isInt();
	}

	protected boolean isList() {
		return (returnType != null) && returnType.isList();
	}

	protected boolean isList(Type baseType) {
		if (returnType == null) return false;
		return returnType.isList(baseType);
	}

	protected boolean isMap() {
		return (returnType != null) && returnType.isMap();
	}

	protected boolean isMap(Type baseType) {
		return (returnType != null) && returnType.isMap(baseType);
	}

	/**
	 * Does this node require a new scope
	 */
	public boolean isNeedsScope() {
		return false;
	}

	/**
	 * Is return type real?
	 */
	protected boolean isReal() {
		return (returnType != null) && returnType.isReal();
	}

	/**
	 * Do all subordinate expressions have a non-null return type?
	 */
	protected boolean isReturnTypesNotNull() {
		return true;
	}

	/**
	 * Is return type string?
	 */
	protected boolean isString() {
		return (returnType != null) && returnType.isString();
	}

	/**
	 * Is child 'idx' a terminal node with value 'str'?
	 */
	protected boolean isTerminal(ParseTree tree, int idx, String str) {
		ParseTree node = tree.getChild(idx);
		return ((node instanceof TerminalNode) && node.getText().equals(str));
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
	 * Log a message to console
	 */
	public void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() //
				+ (getFileName() != null ? " (" + getFileName() + ":" + getLineNum() + ")" : "") //
				+ " : " + msg //
				);
	}

	/**
	 * Parse a tree
	 */
	protected abstract void parse(ParseTree tree);

	/**
	 * Pop a bool from stack
	 */
	public boolean popBool(BigDataScriptThread bdsThread) {
		return (Boolean) Type.BOOL.cast(bdsThread.pop());
	}

	/**
	 * Pop an int from stack
	 */
	public long popInt(BigDataScriptThread bdsThread) {
		return (Long) Type.INT.cast(bdsThread.pop());
	}

	/**
	 * Pop a real from stack
	 */
	public double popReal(BigDataScriptThread bdsThread) {
		return (Double) Type.REAL.cast(bdsThread.pop());
	}

	/**
	 * Pop a string from stack
	 */
	public String popString(BigDataScriptThread bdsThread) {
		return (String) Type.STRING.cast(bdsThread.pop());
	}

	/**
	 * Show a parseTree node
	 */
	void printNode(ParseTree tree) {
		System.out.println(tree.getClass().getSimpleName());
		for (int i = 0; i < tree.getChildCount(); i++) {
			ParseTree node = tree.getChild(i);
			System.out.println("\tchild[" + i + "]\ttype: " + node.getClass().getSimpleName() + "\ttext: '" + node.getText() + "'");
		}
	}

	/**
	 * Replace fake nodes by real nodes (serialization)
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
									// Find real node based on fake one
									BigDataScriptNode node = BigDataScriptNodeFactory.get().realNode(csnode);

									// Replace this array element
									Array.set(fieldObj, idx, node);
								}
							}
							idx++;
						}
					} else {
						if (fieldObj instanceof BigDataScriptNode) {
							BigDataScriptNode csnode = (BigDataScriptNode) fieldObj;

							// Is it a fake node? => Replace by real node
							if (csnode.isFake()) {
								// Find real node based on fake one
								BigDataScriptNode trueCsnode = BigDataScriptNodeFactory.get().realNode(csnode);

								// Set field to real node
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
	 * Calculate return type and assign it to 'returnType' variable.
	 */
	public Type returnType(Scope scope) {
		return Type.VOID;
	}

	/**
	 * Run this node
	 */
	public void run(BigDataScriptThread bdsThread) {
		boolean recover = bdsThread.isCheckpointRecover();

		if (!recover) runBegin(bdsThread); // Before node execution

		try {
			// Run?
			if (bdsThread.shouldRun(this)) {
				Gpr.debug("Run: " + this.getClass().getSimpleName() + "\t" + this + "\nStack: " + bdsThread.getScope().toStringStack());
				runStep(bdsThread);
			}
		} catch (Throwable t) {
			bdsThread.fatalError(this, t);
		}

		if (!recover) runEnd(bdsThread); // After node execution
	}

	/**
	 * Run before running the node
	 */
	protected void runBegin(BigDataScriptThread bdsThread) {
		// Need a new scope?
		if (isNeedsScope()) bdsThread.newScope(this);
		bdsThread.getPc().push(this);
	}

	/**
	 * Run after running the node
	 */
	protected void runEnd(BigDataScriptThread bdsThread) {
		bdsThread.getPc().pop(this);
		// Restore old scope?
		if (isNeedsScope()) bdsThread.oldScope();
	}

	protected void runStep(BigDataScriptThread bdsThread) {
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
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {

		// Use ID from file
		updateId((int) serializer.getNextFieldInt());
		lineNum = (int) serializer.getNextFieldInt();
		charPosInLine = (int) serializer.getNextFieldInt();

		// Set parent node
		int parentId = serializer.getNextFieldNodeId();
		if (parentId != 0) {
			// Parent node is not null
			parent = new ParentNode();
			parent.setFakeId(parentId);
		}

		returnType = serializer.getNextFieldType();

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
		out.append(getClass().getSimpleName() //
				+ "\t" + id //
				+ "\t" + lineNum //
				+ "\t" + charPosInLine //
				+ "\t" + serializer.serializeSaveValue(parent) //
				+ "\t" + serializer.serializeSaveValue(returnType) //
				+ "\t" //
				);
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
						if (fieldObj instanceof BigDataScriptNode) nodesToRecurse.add((BigDataScriptNode) fieldObj);
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
			if (!serializer.isSerialized(node)) out.append(serializer.serializeSave(node));

		return out.toString();
	}

	/**
	 * Set a fake ID number (this is a fake node created during serialization)
	 * NOTE!: We set it to a negative number. This is a fake node
	 */
	public void setFakeId(int id) {
		if (id < 0) return; // Is ID already 'fake' node? => Do nothing
		updateId(-id); // Update using a negative ID (fake ID)
	}

	public void setNeedsScope(boolean needsScope) {
		throw new RuntimeException("Cannot set 'needsScope' in this node:" + this.getClass().getSimpleName());
	}

	public void setScope(Scope scope) {
		throw new RuntimeException("Cannot set scope to node " + this.getClass().getSimpleName());
	}

	@Override
	public String toString() {
		return "Program: " + getFileName() + "\n";
	}

	/**
	 * Show the tree
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

	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		// Are return types non-null?
		// Note: null returnTypes happen if variables are missing.
		if (isReturnTypesNotNull()) typeCheckNotNull(scope, compilerMessages);
	}

	/**
	 * Perform several basic type checking tasks.
	 * Invoke 'typeCheck()' method on all sub-nodes
	 */
	public void typeChecking(Scope scope, CompilerMessages compilerMessages) {
		// Create a new scope?
		if (isNeedsScope()) {
			Scope newScope = new Scope(scope, this);
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
			if (scope.isEmpty()) setNeedsScope(false);
			else setScope(scope);

			// Get back to previous scope
			scope = scope.getParent();
		}
	}

	/**
	 * Type checking.
	 * This is invoked once we made sure all return types are non null (so we don't have to check for null every time)
	 */
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Nothing to do
	}

	/**
	 * Update ID field
	 */
	protected void updateId(int newId) {
		BigDataScriptNodeFactory.get().updateId(id, newId, this);
		id = newId;
	}

}
