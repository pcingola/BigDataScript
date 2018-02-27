package org.bds.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bds.Bds;
import org.bds.Config;
import org.bds.lang.BdsNode;
import org.bds.lang.BdsNodeFactory;
import org.bds.lang.PrePostOperation;
import org.bds.lang.PrimitiveType;
import org.bds.lang.Type;
import org.bds.lang.TypeList;
import org.bds.lang.TypeMap;
import org.bds.lang.statement.BlockWithFile;
import org.bds.run.BdsThread;
import org.bds.run.FunctionCallThread;
import org.bds.run.ProgramCounter;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.GprString;

/**
 * Serialize elemnts to (and from) a file
 *
 * @author pcingola
 */
public class BdsSerializer {

	public static final String LIST_IDENTIFIER = "list:";
	public static final String MAP_IDENTIFIER = "map:";
	public static final String NODE_IDENTIFIER = "node:";
	public static final String TYPE_IDENTIFIER = "type:";

	public static boolean debug = false;

	String fileName;
	int lineNum;
	String line;
	int parsedField;
	String fields[];
	Config config;
	Set<BdsSerialize> serializedNodes;
	Map<String, BdsThread> threadsById;
	boolean extractSource;

	public BdsSerializer(String fileName, Config config) {
		this.fileName = fileName;
		this.config = config;
		extractSource = (config != null && config.isExtractSource());
		serializedNodes = new HashSet<BdsSerialize>();
		threadsById = new HashMap<String, BdsThread>();
	}

	public boolean add(BdsSerialize node) {
		return serializedNodes.add(node);
	}

	/**
	 * Read the object from Base64 string.
	 */
	public Object base64Decode(String s) {
		try {
			byte[] data = Base64Coder.decode(s);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Write the object to a Base64 string.
	 */
	public String base64encode(Object o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			return new String(Base64Coder.encode(baos.toByteArray()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public BdsThread getBdsThread(String bdsThreadId) {
		return threadsById.get(bdsThreadId);
	}

	public String getCurrField() {
		if (fields.length < parsedField) return "";
		return fields[parsedField];
	}

	public String[] getFields() {
		return fields;
	}

	public String getNextField() {
		if (fields.length <= parsedField) return "";
		return fields[parsedField++];
	}

	/**
	 * Get next field as a given 'type'
	 */
	public Object getNextField(Type type) {
		switch (type.getPrimitiveType()) {
		case VOID:
			return null;

		case BOOL:
			return getNextFieldBool();

		case INT:
			return getNextFieldInt();

		case REAL:
			return getNextFieldReal();

		case STRING:
			return getNextFieldString();

		case LIST:
			return getNextFieldList((TypeList) type);

		case MAP:
			return getNextFieldMap((TypeMap) type);

		default:
			throw new RuntimeException("Cannot parse type '" + type + "'");
		}
	}

	public boolean getNextFieldBool() {
		return Gpr.parseBoolSafe(getNextField());
	}

	public long getNextFieldInt() {
		return Gpr.parseLongSafe(getNextField());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getNextFieldList(TypeList type) {
		ArrayList list = new ArrayList();

		// Sanity check: Is it a list?
		String nextField = getNextField();
		if (nextField.equals("null")) return null;
		if (!nextField.startsWith(LIST_IDENTIFIER)) throw new RuntimeException("Serialization error: '" + LIST_IDENTIFIER + "' expected instead of '" + nextField + "'");

		// Parse list size
		String sizeStr = nextField.substring(LIST_IDENTIFIER.length());
		int size = Gpr.parseIntSafe(sizeStr);

		for (int i = 0; i < size; i++) {
			Object value = getNextField(type.getBaseType());
			list.add(value);
		}

		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getNextFieldMap(TypeMap type) {
		HashMap map = new HashMap();

		// Sanity check: Is it a list?
		String nextField = getNextField();
		if (!nextField.startsWith(MAP_IDENTIFIER)) throw new RuntimeException("Serialization error: '" + MAP_IDENTIFIER + "' expected instead of '" + nextField + "'");

		// Parse list size
		String sizeStr = nextField.substring(MAP_IDENTIFIER.length());
		int size = Gpr.parseIntSafe(sizeStr);

		for (int i = 0; i < size; i++) {
			Object key = getNextFieldString();
			Object value = getNextField(type.getBaseType());
			map.put(key, value);
		}

		return map;
	}

	/**
	 * Get nodeId from next field.
	 * Format : "node:ID_NUM"
	 * E.g.   : "node:42"
	 */
	public int getNextFieldNodeId() {
		return parseNodeId(getNextField());
	}

	public double getNextFieldReal() {
		return Gpr.parseDoubleSafe(getNextField());
	}

	public String getNextFieldString() {
		String str = getNextField();
		return parseString(str);
	}

	public String[] getNextFieldStringArray() {
		String str = getNextField();
		return parseStringArray(str);
	}

	public Type getNextFieldType() {
		String typeStr = getNextField();

		if (typeStr.equals("null")) return null;

		if (!typeStr.startsWith(TYPE_IDENTIFIER)) throw new RuntimeException("Serialized Type expected, found '" + typeStr + "'");
		typeStr = typeStr.substring(TYPE_IDENTIFIER.length());

		String fields[] = typeStr.split(":");

		// Base type?
		if (fields.length == 1) return Type.get(fields[0]);

		// List
		if (fields[0].equals(PrimitiveType.LIST.toString())) {
			Type baseType = Type.get(fields[1]);
			return TypeList.get(baseType);
		} else if (fields[0].equals(PrimitiveType.MAP.toString())) {
			Type baseType = Type.get(fields[1]);
			return TypeMap.get(baseType);
		}

		// Error
		throw new RuntimeException("Cannot parse type '" + typeStr + "'");
	}

	/**
	 * Is the nexf field a node?
	 */
	public boolean isNextFieldNode() {
		String nextVal = "";
		if (fields.length > parsedField) nextVal = fields[parsedField];
		return nextVal.startsWith(NODE_IDENTIFIER);
	}

	public boolean isSerialized(BdsSerialize node) {
		return serializedNodes.contains(node);
	}

	/**
	 * Load from a file
	 */
	public List<BdsThread> load() {
		// Read the whole file
		String file = Gpr.read(Gpr.reader(fileName, true));
		if ((file == null) || file.isEmpty()) throw new RuntimeException("Cannot read file '" + fileName + "'");

		// Split file into lines
		String lines[] = file.split("\n");

		// Parse everything else
		Scope.resetGlobalScope();

		List<BdsThread> bdsThreads = parseLines(lines, null);
		return bdsThreads;
	}

	/**
	 * Parse a value
	 * @param fieldClass : Class of field to parse
	 * @param componentType : Component class (in case of an array)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object parse(Class fieldClass, Class componentType) {
		if (fieldClass.isArray()) {

			if (componentType == String.class) return getNextFieldStringArray();

			// Create a list
			ArrayList list = new ArrayList();

			// Add all nodes to array
			String arrayVal = getNextField();
			if (arrayVal != null && !arrayVal.isEmpty()) {
				for (String nodeNum : arrayVal.split(",")) {
					BdsNode csnode = BdsNodeFactory.get().factory(componentType.getCanonicalName(), null, null);
					csnode.setFakeId(parseNodeId(nodeNum));
					list.add(csnode);
				}
			}

			// Create array
			Object[] array = (Object[]) Array.newInstance(componentType, 0);
			return list.toArray(array);
		} else if (fieldClass == PrePostOperation.class) return PrePostOperation.valueOf(getNextField());
		else if (fieldClass == PrimitiveType.class) return PrimitiveType.valueOf(getNextField());
		else if (fieldClass.getCanonicalName().startsWith(Type.class.getCanonicalName())) {
			return getNextFieldType();
		} else if (fieldClass.getCanonicalName().startsWith(BdsNodeFactory.get().packageName())) {
			BdsNode csnode = BdsNodeFactory.get().factory(fieldClass.getCanonicalName(), null, null);
			csnode.setFakeId(getNextFieldNodeId());
			return csnode;
		} else if (fieldClass == Scope.class) return null; // Nothing to do (this only happens in ProgramUnit class
		else if (fieldClass == String.class) return getNextFieldString();
		else if ((fieldClass == Boolean.class) || (fieldClass == boolean.class)) return getNextFieldBool();
		else if ((fieldClass == Integer.class) || (fieldClass == int.class)) return (int) getNextFieldInt();
		else if ((fieldClass == Long.class) || (fieldClass == long.class)) return getNextFieldInt();
		else if ((fieldClass == Double.class) || (fieldClass == double.class)) return getNextFieldReal();

		throw new RuntimeException("Cannot load class " + fieldClass.getCanonicalName());
	}

	/**
	 * Parse lines
	 *
	 * @param lines
	 * @param classNameFilter : If not null, only parse lines matching this className
	 */
	List<BdsThread> parseLines(String lines[], String classNameFilter) {
		// Set fake IDs on
		BdsNodeFactory.get().setCreateFakeIds(true);

		// Initialize
		ArrayList<BdsThread> bdsThreads = new ArrayList<BdsThread>();
		BdsThread currBdsThread = null;
		Scope currScope = null;
		ArrayList<Scope> scopes = new ArrayList<Scope>();
		Map<String, BdsSerialize> nodesById = new HashMap<String, BdsSerialize>();

		// Parse lines
		for (int i = 0; i < lines.length; i++) {
			// Update line info
			lineNum = i + 1;
			line = lines[i];
			fields = line.split("\t");

			// Fields parsed
			String clazz = fields[0];
			parsedField = 1;

			if ((classNameFilter == null) || (classNameFilter.equals(clazz))) {
				// Object to un-serialize
				BdsSerialize bdsSerialize = null;

				//---
				// Create class (before parsing it)
				//---
				if (clazz.equals(Bds.class.getSimpleName())) {
					// Check version number
					double version = Gpr.parseDoubleSafe(fields[1]);
					double versionThis = Gpr.parseDoubleSafe(Bds.VERSION_MAJOR);
					if (versionThis < version) throw new RuntimeException("Version numbers do not match.\n\tThis version: " + versionThis + "\n\tFile's version: " + version);
					bdsSerialize = null; // Nothing to parse
				} else if (clazz.equals(BdsThread.class.getSimpleName())) {
					// Parse BigDataScriptThread
					BdsThread bdsThread = new BdsThread(null, config);
					currBdsThread = bdsThread;
					currBdsThread.setScope(null);
					currScope = null;

					bdsSerialize = bdsThread;
				} else if (clazz.equals(FunctionCallThread.class.getSimpleName())) {
					// Parse BigDataScriptThread
					FunctionCallThread fcallThread = new FunctionCallThread(config);
					currBdsThread = fcallThread;
					currBdsThread.setScope(null);
					currScope = null;

					bdsSerialize = fcallThread;
				} else if (clazz.equals(ProgramCounter.class.getSimpleName())) {
					// Parse ProgramCounter
					bdsSerialize = new ProgramCounter();
				} else if (clazz.equals(Scope.class.getSimpleName())) {
					// Parse Scope
					Scope scope = new Scope();
					if (currScope != null) currScope.setParent(scope);
					currScope = scope;
					scopes.add(scope);
					bdsSerialize = currScope;
				} else if (clazz.equals(ScopeSymbol.class.getSimpleName())) {
					// Parse ScopeSymbol
					bdsSerialize = new ScopeSymbol();
				} else if (clazz.equals(Task.class.getSimpleName())) {
					// Parse Task
					bdsSerialize = new Task();
				} else {
					// Everything else has been parsed, this must be a BigDataScriptNode
					String className = BdsNodeFactory.get().packageName() + clazz;
					bdsSerialize = BdsNodeFactory.get().factory(className, null, null);
				}

				//---
				// Parsing and additional tasks after parsing
				//---
				if (bdsSerialize != null) {
					// De-serialize
					bdsSerialize.serializeParse(this);
					nodesById.put(bdsSerialize.getNodeId(), bdsSerialize);

					// Extract source code files?
					if (extractSource && bdsSerialize instanceof BlockWithFile) {
						((BlockWithFile) bdsSerialize).save(true);
					}

					// Post processing
					if (bdsSerialize instanceof BdsThread) {
						// Restore a thread
						BdsThread bdsThread = (BdsThread) bdsSerialize;
						bdsThreads.add(bdsThread);
						threadsById.put(bdsThread.getBdsThreadId(), bdsThread);
					} else if (bdsSerialize instanceof ScopeSymbol) {
						// Add symbol to current scope
						currScope.add((ScopeSymbol) bdsSerialize);
					} else if (bdsSerialize instanceof ProgramCounter) {
						// Set PC
						currBdsThread.setPc((ProgramCounter) bdsSerialize);
					} else if (bdsSerialize instanceof Task) {
						Task task = (Task) bdsSerialize;
						currBdsThread.addUnserialized(task);
					} else if (bdsSerialize instanceof BdsNode) {
						// UnSerialize
						BdsNode csnode = (BdsNode) bdsSerialize;
						serializedNodes.add(csnode);
					}
				}
			}
		}

		// Set fake IDs off
		BdsNodeFactory.get().setCreateFakeIds(false);

		//---
		// Replace fake nodes by real nodes
		//---
		for (BdsSerialize bdsNode : serializedNodes)
			if (bdsNode != null && bdsNode instanceof BdsNode) ((BdsNode) bdsNode).replaceFake();

		for (Scope scope : scopes)
			scope.replaceFake();

		// Set parent scopes
		for (Scope scope : scopes) {
			String parentScopeId = scope.getParentNodeId();
			if (parentScopeId != null && !parentScopeId.isEmpty()) {
				Scope parentScope = (Scope) nodesById.get(parentScopeId);
				if (parentScope == null) throw new RuntimeException("Cannot find scope node '" + parentScope + "'");
				scope.setParent(parentScope);
			} else {
				// Root (a.k.a. Global) scope
				scope.setParent(null);
				Scope.setGlobalScope(scope);
			}
		}

		//---
		// Set scope and statement for each bdsThread
		//---
		for (BdsThread bth : bdsThreads) {
			// Set statement
			bth.setStatement(nodesById);
			bth.checkpointRecoverReset(); // Checkpoint starts recovering node from 'statement' (instead of 'programUnit')

			String scopeId = bth.getScopeNodeId();
			Scope scope = (Scope) nodesById.get(scopeId);
			if (scope == null) throw new RuntimeException("Cannot find scope node '" + scopeId + "'");
			bth.setScope(scope);
		}

		return bdsThreads;
	}

	public int parseNodeId(String fielsVal) {
		if (fielsVal.equals("null")) return 0; // null node
		String str[] = fielsVal.split(":");
		return Gpr.parseIntSafe(str[1]);
	}

	public String parseString(String str) {
		if (str.equals("null")) return null;
		str = StringEscapeUtils.unescapeJava(str); // Un-escape
		str = str.substring(1, str.length() - 1); // Remove quotes
		return str;
	}

	public String[] parseStringArray(String strArray) {
		String splitted[] = GprString.splitCsv(strArray);

		for (int i = 0; i < splitted.length; i++) {
			if (splitted[i].equals("null")) splitted[i] = null;
			else splitted[i] = StringEscapeUtils.unescapeJava(splitted[i]); // Un-escape
		}

		return splitted;
	}

	/**
	 * Save data to file
	 */
	public void save(BdsThread bdsThread) {
		try {
			// Open compressed output file
			PrintStream outFile = new PrintStream(new GZIPOutputStream(new FileOutputStream(fileName)));

			// Save version
			outFile.print(Bds.class.getSimpleName() + "\t" + Bds.VERSION_SHORT + "\n");

			// Save main thread
			outFile.print(this.serializeSave(bdsThread));
			outFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Serialize a node
	 */
	public String serializeSave(BdsSerialize bdsSer) {
		if (add(bdsSer)) return bdsSer.serializeSave(this);
		return "";
	}

	/**
	 * Serialize a value
	 */
	@SuppressWarnings("rawtypes")
	public String serializeSaveValue(Object value) {
		if (value == null) return "null";

		if (value instanceof Type) return TYPE_IDENTIFIER + ((Type) value).toStringSerializer();

		if (value instanceof BdsNode) return NODE_IDENTIFIER + ((BdsNode) value).getId();

		if (value instanceof String) {
			String escapedStr = StringEscapeUtils.escapeJava(value.toString());
			return "\"" + escapedStr + "\"";
		}

		if (value instanceof List) {
			List list = (List) value;
			StringBuilder sb = new StringBuilder(LIST_IDENTIFIER + list.size());
			for (Object o : list)
				sb.append("\t" + serializeSaveValue(o));

			return sb.toString();
		}

		if (value instanceof Map) {
			Map map = (Map) value;
			StringBuilder sb = new StringBuilder(MAP_IDENTIFIER + map.size());
			for (Object o : map.keySet()) {
				sb.append("\t" + serializeSaveValue(o));
				sb.append("\t" + serializeSaveValue(map.get(o)));
			}

			return sb.toString();
		}

		// All other values: use default
		return value.toString();
	}

	public String serializeSaveValue(String str) {
		if (str == null) return "null";
		String escapedStr = StringEscapeUtils.escapeJava(str);
		return "\"" + escapedStr + "\"";
	}
}
