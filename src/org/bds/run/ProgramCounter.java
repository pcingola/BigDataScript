package org.bds.run;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Stack;

import org.bds.lang.BdsNode;
import org.bds.util.Gpr;

/**
 * Program counter: A 'stack' of nodes that we are currently executing
 *
 * @author pcingola
 */
public class ProgramCounter implements Serializable, Iterable<Integer> {

	private static final long serialVersionUID = -1907288389943318693L;

	private static int programCounterNum = 0;

	int id;
	int checkPointRecoverNodeIdx; // Checkpoint recovery node index
	int initialSize;
	Stack<Integer> nodeIds;

	protected static int nextId() {
		return ++programCounterNum;
	}

	public ProgramCounter() {
		nodeIds = new Stack<>();
		id = nextId();
		initialSize = 0;
	}

	public ProgramCounter(ProgramCounter pc) {
		nodeIds = new Stack<>();
		nodeIds.addAll(pc.nodeIds);
		id = nextId();
		initialSize = pc.size();
	}

	/**
	 * Found a node (CHECKPOINT_RECOVER run state)
	 */
	public void checkpointRecoverFound() {
		checkPointRecoverNodeIdx++;
	}

	/**
	 * Is there a next node to find? (CHECKPOINT_RECOVER run state0
	 */
	public boolean checkpointRecoverHasNextNode() {
		return size() > checkPointRecoverNodeIdx;
	}

	/**
	 * Get next node to find (CHECKPOINT_RECOVER run state)
	 */
	public int checkpointRecoverNextNode() {
		return nodeId(checkPointRecoverNodeIdx);
	}

	public boolean checkpointRecoverReset(BdsNode statement) {
		for (checkPointRecoverNodeIdx = 0; checkPointRecoverNodeIdx < size(); checkPointRecoverNodeIdx++) {
			if (nodeId(checkPointRecoverNodeIdx) == statement.getId()) return true;
		}

		return false;
	}

	boolean isEmpty() {
		return size() <= initialSize;
	}

	@Override
	public Iterator<Integer> iterator() {
		return nodeIds.iterator();
	}

	/**
	 * Get node ID for index 'idx'
	 */
	public int nodeId(int idx) {
		return nodeIds.get(idx);
	}

	/**
	 * Add to program counter
	 */
	public void pop(BdsNode bdsNode) {
		int nodeId = nodeIds.pop();

		// Sanity check
		if (nodeId != bdsNode.getId()) {
			Gpr.debug("Node ID does not match!" //
					+ "\n\tPC         : " + this //
					+ "\n\tNode Id    : " + nodeId //
					+ "\n\tbdsNode Id : " + bdsNode.getId() //
					+ "\t" + bdsNode.getClass().getCanonicalName() //
					+ "\t" + bdsNode.getFileName() + ":" + bdsNode.getLineNum() + ", " + bdsNode.getCharPosInLine() //
			);
		}
	}

	/**
	 * Add to program counter
	 */
	public void push(BdsNode bdsNode) {
		nodeIds.push(bdsNode.getId());
	}

	public int size() {
		return nodeIds.size();
	}

	@Override
	public String toString() {
		StringBuilder pc = new StringBuilder();
		for (int nn : nodeIds)
			pc.append((pc.length() > 0 ? " -> " : "") + nn);

		return "PC: size " + size() + " / " + initialSize //
				+ (isEmpty() ? " [Empty] " : "") //
				+ (checkPointRecoverNodeIdx > 0 ? ", checkPointRecoverNodeIdx: " + checkPointRecoverNodeIdx : "") //
				+ ", nodes: " + pc //
		;
	}
}
