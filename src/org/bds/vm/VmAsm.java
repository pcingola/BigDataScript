package org.bds.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.util.Gpr;

/**
 * Assembly compiler and debugger for BdsVm
 *
 * @author pcingola
 */
public class VmAsm {

	static final Pattern labelPattern = Pattern.compile("\\s*(\\S+)\\s*:(.*)");

	boolean verbose, debug;
	int lineNum;
	String codeStr;
	String file;
	BdsVm bdsvm;
	List<Integer> code;
	Map<String, Type> typeByName;

	public VmAsm() {
		this(null);
	}

	public VmAsm(String file) {
		this.file = file;
	}

	/**
	 * Add instruction & param to code
	 */
	void addInstruction(OpCode opcode, String param) {
		code.add(opcode.ordinal());
		if (param != null) addParam(opcode, param);
	}

	/**
	 * Add param to code
	 * @param param
	 */
	void addParam(OpCode opcode, String param) {
		// We need to add 'param' to pool of constants and add a reference to it
		int idx = parseParam(opcode, param);
		code.add(idx);
	}

	public void addType(Type type) {
		typeByName.put(type.toString(), type);
		bdsvm.addType(type);
	}

	String code() {
		if (file != null) return Gpr.readFile(file);
		return codeStr;
	}

	/**
	 * BdsCompiler a file
	 *
	 * Note: This object cannot be re-used. Create a new VmAsm
	 *       each time you compile
	 */
	public BdsVm compile() {
		if (bdsvm != null) throw new RuntimeException("Code already compiled!");

		// Initialize
		bdsvm = new BdsVm();
		code = new ArrayList<>();
		bdsvm.setDebug(debug);
		bdsvm.setVerbose(verbose);
		initTypes();

		// Read file and parse each line
		lineNum = 1;
		for (String line : code().split("\n")) {
			// Remove comments and labels
			line = removeComments(line);

			// Parse label, if any.Keep the rest of the line
			line = label(line);
			if (line.isEmpty()) continue;

			// Decode instruction
			OpCode opcode = opcode(line);
			String param = null;
			if (opcode.hasParam()) param = param(line);
			if (verbose) {
				// Show instruction
				String q = opcode.isParamString() ? "'" : "";
				System.out.println("\t" + opcode.toString().toLowerCase() //
						+ (param != null ? " " + q + param + q : "") //
				);
			}
			// Add instruction
			addInstruction(opcode, param);
			lineNum++;
		}

		bdsvm.setCode(code);
		if (debug) System.err.println("# Assembly: Start\n" + bdsvm.toAsm() + "\n# Assembly: End\n");
		return bdsvm;
	}

	Type getType(String typeName) {
		return typeByName.get(typeName);
	}

	/**
	 * Initialize types
	 */
	void initTypes() {
		typeByName = new HashMap<>();

		Type types[] = { Types.BOOL, Types.INT, Types.REAL, Types.STRING };

		// Add all basic list types
		for (Type te : types)
			addType(TypeList.get(te));

		// Add all basic map types
		for (Type tk : types)
			for (Type tv : types)
				addType(TypeMap.get(tk, tv));
	}

	/**
	 * Get a label from an input line, null if there are no labels
	 */
	String label(String line) {
		Matcher m = labelPattern.matcher(line);
		if (!m.matches()) return line;

		String label = m.group(1);
		String rest = m.group(2);

		// Is 'label' a function signature?
		if (label.indexOf('(') > 0 && label.indexOf(')') > 0) {
			bdsvm.addFunction(label, pc());
		} else {
			bdsvm.addLabel(label, pc());
		}

		if (verbose) System.out.println(label + ":");
		return rest.trim();
	}

	/**
	 * Parse an opcode
	 */
	OpCode opcode(String line) {
		String s[] = line.split("\\s+", 2);
		String op = s[0].toUpperCase();
		try {
			return OpCode.valueOf(op);
		} catch (Exception e) {
			throw new RuntimeException("Unknown opcode '" + op + "', file '" + file + "', line " + lineNum);
		}
	}

	/**
	 * Parse a parameter
	 */
	String param(String line) {
		String s[] = line.split("\\s+", 2);
		return s.length > 1 ? s[1] : null;
	}

	/**
	 * Convert parameter string to appropriate constant type
	 */
	Object parseConstant(OpCode opcode, String param) {
		switch (opcode) {
		case CALL:
		case CALLNATIVE:
		case CALLM:
		case CALLMNATIVE:
		case JMP:
		case JMPT:
		case JMPF:
		case LOAD:
		case STORE:
		case PUSHS:
			int lastCharIdx = param.length() - 1;
			if ((param.charAt(0) == '\'' && param.charAt(lastCharIdx) == '\'') // Using single quotes
					|| (param.charAt(0) == '"' && param.charAt(lastCharIdx) == '"')) // Using double quotes
				return param.substring(1, param.length() - 1); // Remove quotes
			return param; // Unquoted string

		case NEW:
			return getType(param);

		case NODE:
			return Gpr.parseIntSafe(param);

		case PUSHB:
			return Gpr.parseBoolSafe(param);

		case PUSHI:
			return Gpr.parseLongSafe(param);

		case PUSHR:
			return Gpr.parseDoubleSafe(param);

		default:
			throw new RuntimeException("Unknown parameter type for opcode '" + opcode + "'");
		}
	}

	/**
	 * Convert parameter string to appropriate constant type and add it to constant pool
	 * @return Index in constant pool
	 */
	int parseParam(OpCode opcode, String param) {
		Object oparam = parseConstant(opcode, param);

		switch (opcode) {
		case NEW:
			return bdsvm.addType((Type) oparam);

		default:
			return bdsvm.addConstant(oparam);
		}
	}

	/**
	 * Current program counter
	 */
	int pc() {
		return code.size();
	}

	/**
	 * Remove comments
	 */
	String removeComments(String line) {
		int commentIdx = line.indexOf('#');
		return commentIdx >= 0 ? line.substring(0, commentIdx) : line;
	}

	public void setCode(String codeStr) {
		this.codeStr = codeStr;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Remove any labels
	 */
	String stripLabel(String line) {
		int labelIdx = line.indexOf(':');
		return labelIdx >= 0 ? line.substring(labelIdx + 1) : line;
	}

}
