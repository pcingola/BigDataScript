package org.bds.vm;

import org.bds.lang.statement.FunctionDeclaration;

/**
 * Represents a function definition in VmAsm
 *
 * Function's name / label is the signature (e.g. 'fname(string, int, int, bool)')
 *
 * If there are names in arguments, we use parse them (e.g. 'fname(string s, int i, int j, bool b)')
 *
 * @author pcingola
 */
public class VmFunction {

	String label;
	String args[];
	int pc;

	public VmFunction(String label, int pc) {
		this.label = label;
		this.pc = pc;
		params(label);
	}

	public String[] getArgs() {
		return args;
	}

	public String getLabel() {
		return label;
	}

	public int getPc() {
		return pc;
	}

	void params(String label) {
		String s[] = label.split("[\\(\\)]");
		if (s.length <= 1) return;
		String params = s[1];

		args = params.split(",");
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].trim();
			String[] tn = args[i].split("\\s+");
			if (tn.length > 1) args[i] = tn[1].trim(); // First item is 'type', second is argument name
		}
	}

	/**
	 * Update function declaration
	 */
	public void set(FunctionDeclaration fd) {
		args = fd.getParameterNames().toArray(new String[0]);
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("function: " + label);
		sb.append(", pc: " + pc);
		sb.append(", argc: " + args.length);
		sb.append(", args:");
		for (String arg : args)
			sb.append(" " + arg);
		return sb.toString();
	}

}
