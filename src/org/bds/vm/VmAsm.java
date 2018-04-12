package org.bds.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.lang.BdsNode;
import org.bds.lang.ProgramUnit;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.StatementFunctionDeclaration;
import org.bds.lang.type.InterpolateVars;
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

	boolean verbose, debug;
	int lineNum;
	String codeStr;
	String file;
	BdsVm bdsvm;
	ProgramUnit programUnit;
	List<Integer> code;
	Map<String, Type> typeByName;

	public VmAsm(ProgramUnit programUnit) {
		this.programUnit = programUnit;
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
		init();

		// Read file and parse each line
		lineNum = 1;
		for (String line : code().split("\n")) {
			// Remove comments and labels
			if (isCommentLine(line)) continue;

			// Parse label, if any.Keep the rest of the line
			line = label(line);
			if (line.isEmpty()) continue;

			// Decode instruction
			OpCode opcode = opcode(line);
			String param = null;
			if (opcode.hasParam()) param = param(line);
			if (verbose) {
				// Show instruction
				System.out.println("\t" + opcode.toString().toLowerCase() //
						+ (param != null ? " " + param : "") //
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
		if (!typeByName.containsKey(typeName)) throw new RuntimeException("Cannot find type '" + typeName + "'");
		return typeByName.get(typeName);
	}

	/**
	 * Initialzie symbols, classes, functions, types, etc.
	 */
	void init() {
		initTypes();
		initFunctions();
		initClasses();
	}

	/**
	 * Add all defined classes
	 */
	void initClasses() {
		if (programUnit == null) return;

		// Add all classes
		List<BdsNode> cdecls = programUnit.findNodes(ClassDeclaration.class, true, true);
		for (BdsNode n : cdecls) {
			ClassDeclaration cd = (ClassDeclaration) n;
			bdsvm.addType(cd.getType());
		}
	}

	/**
	 * Add all defined functions
	 */
	void initFunctions() {
		if (programUnit == null) return;

		// Add all functions
		List<BdsNode> fdecls = programUnit.findNodes(StatementFunctionDeclaration.class, true, true);
		for (BdsNode n : fdecls) {
			FunctionDeclaration fd = (FunctionDeclaration) n;
			bdsvm.addFunction(fd);
		}
	}

	/**
	 * Initialize types
	 */
	void initTypes() {
		typeByName = new HashMap<>();

		Type types[] = { Types.BOOL, Types.INT, Types.REAL, Types.STRING };

		// Add primitive types
		for (Type t : types)
			addType(t);

		// Add all basic list types
		for (Type te : types)
			addType(TypeList.get(te));

		// Add all basic map types
		for (Type tk : types)
			for (Type tv : types)
				addType(TypeMap.get(tk, tv));

		// Add all classes
		Types.getAll().stream() //
				.filter(t -> t.isClass()) //
				.forEach(t -> addType(t)) //
		;
	}

	/**
	 * Get a label from an input line, null if there are no labels
	 */
	String label(String line) {
		int idx = line.lastIndexOf(':');
		int idxQuote = line.lastIndexOf('\'');
		if (idx < 0 || idx < idxQuote) return line.trim();

		String label = line.substring(0, idx).trim();
		String rest = line.substring(idx + 1).trim();

		// Is 'label' a function signature?
		if (label.indexOf('(') > 0 && label.indexOf(')') > 0) {
			bdsvm.addFunctionPc(label, pc());
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
		case PUSHS:
		case REFFIELD:
		case SETFIELD:
		case STORE:
		case VAR:
			return parseParamString(param);

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
	 * Parse literal string
	 */
	String parseParamString(String param) {
		int lastCharIdx = param.length() - 1;
		if ((param.charAt(0) == '\'' && param.charAt(lastCharIdx) == '\'') // Using single quotes
				|| (param.charAt(0) == '"' && param.charAt(lastCharIdx) == '"')) // Using double quotes
		{
			String escapedStr = param.substring(1, param.length() - 1); // Remove quotes
			return InterpolateVars.unEscape(escapedStr);
		}
		return param; // Unquoted string
	}

	/**
	 * Current program counter
	 */
	int pc() {
		return code.size();
	}

	/**
	 * Remove comment lines
	 */
	boolean isCommentLine(String line) {
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (Character.isWhitespace(c)) continue;
			if (c == '#') return true; // First non-whitespace is '#'
			return false;
		}
		return true; // Only whitespaces
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
