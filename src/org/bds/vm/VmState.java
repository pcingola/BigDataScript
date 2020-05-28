package org.bds.vm;

import org.bds.scope.Scope;

/**
 * Save VM state during long running OpCode execution such as 'sys', 'wait', etc.
 *
 * IMPORTANT: This objects are meant to be reused!
 *
 * @author pcingola
 */
public class VmState extends CallFrame {

	private static final long serialVersionUID = -2276170953266575987L;

	public int fp, sp;

	public VmState() {
		set(-1, -1, -1, -1, null);
	}

	public boolean isValid() {
		return pc >= 0;
	}

	public void reset() {
		pc = -1;
	}

	public void set(int fp, int nodeId, int pc, int sp, Scope scope) {
		this.pc = pc;
		this.nodeId = nodeId;
		this.fp = fp;
		this.sp = sp;
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "{pc: " + pc //
				+ ", fp: " + fp //
				+ ", sp: " + sp //
				+ ", nodeId: " + nodeId//
				+ ", scope.name: " + scope.getScopeName() //
				+ "}" //
		;
	}
}
