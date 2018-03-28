package org.bds.vm;

import java.util.ArrayList;
import java.util.List;

import org.bds.util.Gpr;

/**
 * Assembly compiler and debugger for BdsVm
 *
 * @author pcingola
 */
public class VmAsm {

	boolean verbose, debug;
	String file;
	BdsVm bdsvm;
	List<Integer> code;

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
		if (opcode.hasPc()) {
			code.add(Gpr.parseIntSafe(param.toString()));
		} else if (opcode.hasConstant()) {
			// We need to add 'param' to pool of constants and add a reference to it
			Object obj = parseConstant(opcode, param);
			int idx = bdsvm.addConstant(obj);
			code.add(idx);
		} else throw new RuntimeException("Cannot add parameter for opcode '" + opcode + "'. This should never happen!");
	}

	/**
	 * Compile a file
	 */
	public BdsVm compile() {
		// Initialize
		bdsvm = new BdsVm();
		code = new ArrayList<>();

		// Read file and parse each line
		String str = Gpr.readFile(file);
		for (String line : str.split("\n")) {
			// Remove comments and labels
			line = removeComments(line);

			// Parse label, if any
			String label = label(line);
			if (label != null) System.out.println(label + ":");

			// Keep the rest of the line (no labels)
			line = stripLabel(line).trim();
			if (line.isEmpty()) continue;

			// Decode instruction
			OpCode opcode = opcode(line);
			String param = null;
			if (opcode.numParam() > 0) param = param(line);
			System.out.println("\t" + opcode + (param != null ? " " + param : ""));

			// Add instruction
			addInstruction(opcode, param);
		}

		bdsvm.setCode(code);
		return bdsvm;
	}

	/**
	 * Get a label from an input line, null if there are no labels
	 */
	String label(String line) {
		int labelIdx = line.indexOf(':');
		return labelIdx >= 0 ? line.substring(0, labelIdx).trim() : null;
	}

	/**
	 * Parse an opcode
	 */
	OpCode opcode(String line) {
		String s[] = line.split("\\s+");
		String op = s[0].toUpperCase();
		return OpCode.valueOf(op);
	}

	/**
	 * Parse a parameter
	 */
	String param(String line) {
		String s[] = line.split("\\s+");
		return s.length > 1 ? s[1] : null;
	}

	/**
	 * Convert parameter string to appropriate constant type
	 */
	Object parseConstant(OpCode opcode, String param) {
		switch (opcode) {
		case LOAD:
		case PUSHS:
		case STORE:
			return param; // Type is String

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
	 * Remove comments
	 */
	String removeComments(String line) {
		int commentIdx = line.indexOf('#');
		return commentIdx >= 0 ? line.substring(0, commentIdx) : line;
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
