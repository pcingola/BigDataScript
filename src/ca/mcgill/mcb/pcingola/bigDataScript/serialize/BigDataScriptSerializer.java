package ca.mcgill.mcb.pcingola.bigDataScript.serialize;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringEscapeUtils;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNodeFactory;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.PrePostOperation;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.PrimitiveType;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ProgramUnit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeMap;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.ProgramCounter;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Serialize elemnts to (and from) a file
 * 
 * @author pcingola
 */
public class BigDataScriptSerializer {

	public static final String LIST_IDENTIFIER = "list:";
	public static final String MAP_IDENTIFIER = "map:";
	public static final String NODE_IDENTIFIER = "node:";
	public static final String TYPE_IDENTIFIER = "type:";

	String fileName;
	int lineNum;
	String line;
	int parsedField;
	String fields[];
	Config config;
	HashSet<BigDataScriptNode> serializedNodes;

	public BigDataScriptSerializer(String fileName, Config config) {
		this.fileName = fileName;
		this.config = config;
		serializedNodes = new HashSet<BigDataScriptNode>();
	}

	public boolean add(BigDataScriptNode node) {
		return serializedNodes.add(node);
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
	 * @param type
	 * @return
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
	 * 
	 * @return
	 */
	public int getNextFieldNodeId() {
		return parseNodeId(getNextField());
	}

	public double getNextFieldReal() {
		return Gpr.parseDoubleSafe(getNextField());
	}

	public String getNextFieldString() {
		return StringEscapeUtils.unescapeJava(getNextField());
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
	 * @return
	 */
	public boolean isNextFieldNode() {
		String nextVal = "";
		if (fields.length > parsedField) nextVal = fields[parsedField];
		return nextVal.startsWith(NODE_IDENTIFIER);
	}

	public boolean isSerialized(BigDataScriptNode node) {
		return serializedNodes.contains(node);
	}

	/**
	 * Load from a file
	 */
	public List<BigDataScriptThread> load() {
		// Read the whole file
		String file = Gpr.read(Gpr.reader(fileName, true));
		if ((file == null) || file.isEmpty()) throw new RuntimeException("Cannot read file '" + fileName + "'");

		// Split file into lines
		String lines[] = file.split("\n");

		// Parse everything else
		Scope.resetGlobalScope();

		List<BigDataScriptThread> list = parseLines(lines, null);

		return list;
	}

	/** 
	 * Parse a value
	 * 
	 * @param fieldClass : Class of field to parse
	 * @param componentType : Component class (in case of an array)
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object parse(Class fieldClass, Class componentType) {
		if (fieldClass.isArray()) {
			// Create a list
			ArrayList list = new ArrayList();

			// Add all nodes to array
			String arrayVal = getNextField();
			for (String nodeNum : arrayVal.split(",")) {
				BigDataScriptNode csnode = BigDataScriptNodeFactory.get().factory(componentType.getCanonicalName(), null, null);
				csnode.setFakeId(parseNodeId(nodeNum));
				list.add(csnode);
			}

			// Create array
			Object[] array = (Object[]) Array.newInstance(componentType, 0);
			return list.toArray(array);
		} else if (fieldClass == PrePostOperation.class) return PrePostOperation.valueOf(getNextField());
		else if (fieldClass == PrimitiveType.class) return PrimitiveType.valueOf(getNextField());
		else if (fieldClass.getCanonicalName().startsWith(Type.class.getCanonicalName())) {
			return getNextFieldType();
		} else if (fieldClass.getCanonicalName().startsWith(BigDataScriptNodeFactory.get().packageName())) {
			BigDataScriptNode csnode = BigDataScriptNodeFactory.get().factory(fieldClass.getCanonicalName(), null, null);
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
	List<BigDataScriptThread> parseLines(String lines[], String classNameFilter) {
		// Set fake IDs on
		BigDataScriptNodeFactory.get().setCreateFakeIds(true);

		// Initialize
		ArrayList<BigDataScriptThread> bigDataScriptThreads = new ArrayList<BigDataScriptThread>();
		Scope currScope = null;
		ArrayList<Scope> scopes = new ArrayList<Scope>();
		BigDataScriptThread currCsThread = null;
		//		List<Task> tasks = new LinkedList<Task>();

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
				BigDataScriptSerialize bigDataScriptSerialize = null;

				//---
				// Class selector
				//---
				if (clazz.equals(BigDataScript.class.getSimpleName())) {
					// Check version number
					double version = Gpr.parseDoubleSafe(fields[1]);
					double versionThis = Gpr.parseDoubleSafe(BigDataScript.VERSION_MAJOR);
					if (versionThis < version) throw new RuntimeException("Version numbers do not match.\n\tThis version: " + versionThis + "\n\tFile's version: " + version);
					bigDataScriptSerialize = null; // Nothing to parse
				} else if (clazz.equals(BigDataScriptThread.class.getSimpleName())) {
					// Parse BigDataScriptThread
					BigDataScriptThread bigDataScriptThread = new BigDataScriptThread(null, config);
					bigDataScriptThreads.add(bigDataScriptThread);
					currCsThread = bigDataScriptThread;
					currCsThread.setScope(null);
					currScope = null;

					bigDataScriptSerialize = bigDataScriptThread;
				} else if (clazz.equals(ProgramCounter.class.getSimpleName())) {
					// Parse ProgramCounter
					bigDataScriptSerialize = new ProgramCounter();
				} else if (clazz.equals(Scope.class.getSimpleName())) {
					// Parse Scope
					Scope scope = new Scope();
					if (currScope != null) currScope.setParent(scope);
					currScope = scope;
					scopes.add(scope);
					bigDataScriptSerialize = currScope;
				} else if (clazz.equals(ScopeSymbol.class.getSimpleName())) {
					// Parse ScopeSymbol
					bigDataScriptSerialize = new ScopeSymbol();
				} else if (clazz.equals(Task.class.getSimpleName())) {
					// Parse Task
					bigDataScriptSerialize = new Task();
				} else {
					// Everything else has been parsed, this must be a BigDataScriptNode
					String className = BigDataScriptNodeFactory.get().packageName() + clazz;
					bigDataScriptSerialize = BigDataScriptNodeFactory.get().factory(className, null, null);
				}

				//---
				// Parsing
				//---
				if (bigDataScriptSerialize != null) {
					// De-serialize
					bigDataScriptSerialize.serializeParse(this);

					// Post processing 
					if (bigDataScriptSerialize instanceof ScopeSymbol) {
						// Add symbol to current scope
						currScope.add((ScopeSymbol) bigDataScriptSerialize);
					} else if (bigDataScriptSerialize instanceof ProgramCounter) {
						// Set PC
						currCsThread.setPc((ProgramCounter) bigDataScriptSerialize);
					} else if (bigDataScriptSerialize instanceof Scope) {
						// csThread's scope not set?
						if (currCsThread.getScope() == null) {
							Scope scope = (Scope) bigDataScriptSerialize;
							currCsThread.setScope(scope);
						}
					} else if (bigDataScriptSerialize instanceof Task) {
						Task task = (Task) bigDataScriptSerialize;
						currCsThread.addUnserialized(task);
					} else if (bigDataScriptSerialize instanceof BigDataScriptNode) {
						// UnSerialize 
						BigDataScriptNode csnode = (BigDataScriptNode) bigDataScriptSerialize;

						serializedNodes.add(csnode);

						// Set ProgramUnit
						if (csnode instanceof ProgramUnit) currCsThread.setProgram((ProgramUnit) csnode);
					}
				}
			}
		}

		// Set fake IDs off
		BigDataScriptNodeFactory.get().setCreateFakeIds(false);

		//---
		// Replace fake nodes by real nodes
		//---
		for (BigDataScriptNode csnode : serializedNodes)
			if (csnode != null) csnode.replaceFake();

		for (Scope scope : scopes)
			scope.replaceFake();

		return bigDataScriptThreads;
	}

	public int parseNodeId(String fielsVal) {
		if (fielsVal.equals("null")) return 0; // null node
		String str[] = fielsVal.split(":");
		return Gpr.parseIntSafe(str[1]);
	}

	/**
	 * Save data to file
	 * @param shellFileName
	 */
	public void save(BigDataScriptThread csThread) {
		try {
			// Open compressed output file
			PrintStream outFile = new PrintStream(new GZIPOutputStream(new FileOutputStream(fileName)));

			// Save version
			outFile.print(BigDataScript.class.getSimpleName() + "\t" + BigDataScript.VERSION_SHORT + "\n");

			// Serialize thread
			outFile.print(csThread.serializeSave(this));

			outFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Serialize a value
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String serializeSaveValue(Object value) {
		if (value == null) return "null";

		if (value instanceof Type) return TYPE_IDENTIFIER + ((Type) value).toStringSerializer();

		if (value instanceof BigDataScriptNode) return NODE_IDENTIFIER + ((BigDataScriptNode) value).getId();

		if (value instanceof String) {
			String escapedStr = StringEscapeUtils.escapeJava(value.toString());
			return escapedStr;
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
		return StringEscapeUtils.escapeJava(str);
	}
}
