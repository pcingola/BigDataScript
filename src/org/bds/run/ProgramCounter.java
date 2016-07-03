package org.bds.run;

import java.util.Iterator;
import java.util.Stack;

import org.bds.lang.BdsNode;
import org.bds.serialize.BdsSerialize;
import org.bds.serialize.BdsSerializer;
import org.bds.util.Gpr;

/**
 * Program counter: A 'stack' of nodes that we are currently executing
 *
 * @author pcingola
 */
public class ProgramCounter implements BdsSerialize, Iterable<Integer> {

	private static int programCounterNum = 0;

	int id;
	int checkPointRecoverNodeIdx; // Checkpoint recovery node index
	int initialSize;
	Stack<Integer> nodeIds;

	protected static int nextId() {
		return ++programCounterNum;
	}

	public ProgramCounter() {
		nodeIds = new Stack<Integer>();
		id = nextId();
		initialSize = 0;
	}

	public ProgramCounter(ProgramCounter pc) {
		nodeIds = new Stack<Integer>();
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

	@Override
	public String getNodeId() {
		return getClass().getSimpleName() + ":" + id;
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
			);
		}
	}

	/**
	 * Add to program counter
	 */
	public void push(BdsNode bdsNode) {
		nodeIds.push(bdsNode.getId());
	}

	@Override
	public void serializeParse(BdsSerializer serializer) {
		initialSize = (int) serializer.getNextFieldInt();

		for (int i = 1; i < serializer.getFields().length - 1; i++)
			nodeIds.push((int) serializer.getNextFieldInt());
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
		StringBuilder out = new StringBuilder();
		out.append(getClass().getSimpleName() + "\t");
		out.append(initialSize + "\t");

		for (int nn : nodeIds)
			out.append(nn + "\t");

		out.deleteCharAt(out.length() - 1); // Remove last tab
		out.append("\n");

		return out.toString();
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
