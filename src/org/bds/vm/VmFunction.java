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
	int pc;
	int argc;

	public VmFunction(String label, int pc) {
		this.label = label;
		this.pc = pc;
		params(label);
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
		String p[] = params.split(",");
		argc = p.length;
	}

	@Override
	public String toString() {
		return "function: " + label + ", pc: " + pc + ", argc: " + argc;
	}

}
