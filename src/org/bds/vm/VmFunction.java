package org.bds.vm;

/**
 * Represents a function definition in VmAsm
 *
 * Function's name / label is the signature (e.g. 'funcname(string, int, int, bool)')
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
		for (int i = 0; i < args.length; i++)
			args[i] = args[i].trim();
	}

	@Override
	public String toString() {
		return "function: " + label + ", pc: " + pc + ", argc: " + args.length + ", args: " + args;
	}

}
