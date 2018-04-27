package org.bds.vm;

import java.io.Serializable;

import org.bds.scope.Scope;

/**
 * Call frame object.
 *
 * IMPORTANT: This objects are meant to be re-used!
 *
 * Note: Stack is always clean when calling a function/method because
 *       we pop the parameters when adding them to the function's scope
 *
 * @author pcingola
 *
 */
public class CallFrame implements Serializable {

	private static final long serialVersionUID = 84261659748008514L;

	public int pc;
	public int nodeId;
	public Scope scope;

	public CallFrame() {
		set(-1, -1, null);
	}

	public int getNodeId() {
		return nodeId;
	}

	public int getPc() {
		return pc;
	}

	public void set(int pc, int nodeId, Scope scope) {
		this.pc = pc;
		this.nodeId = nodeId;
		this.scope = scope;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	@Override
	public String toString() {
		return "{pc: " + pc //
				+ ", nodeId: " + nodeId //
				+ ", scope " + scope.getScopeName() //
				+ "}" //
		;
	}
}
