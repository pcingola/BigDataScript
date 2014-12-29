package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.util.Iterator;
import java.util.Stack;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Program counter: A 'stack' of nodes that we are currently executing
 *
 * @author pcingola
 */
public class ProgramCounter implements BigDataScriptSerialize, Iterable<Integer> {

	private static int programCounterNum = 0;

	int id;
	int checkPointRecoverNodeIdx; // Checkpoint recovery node index
	Stack<Integer> nodeIds;

	protected static int nextId() {
		return ++programCounterNum;
	}

	public ProgramCounter() {
		nodeIds = new Stack<Integer>();
		id = nextId();
	}

	public ProgramCounter(ProgramCounter pc) {
		nodeIds = new Stack<Integer>();
		nodeIds.addAll(pc.nodeIds);
		id = nextId();
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

	public boolean checkpointRecoverReset(BigDataScriptNode statement) {
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
		return nodeIds.isEmpty();
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
	public void pop(BigDataScriptNode bdsNode) {
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
	public void push(BigDataScriptNode csnode) {
		nodeIds.push(csnode.getId());
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		for (int i = 1; i < serializer.getFields().length; i++)
			nodeIds.push((int) serializer.getNextFieldInt());
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();
		out.append(getClass().getSimpleName() + "\t");

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
		StringBuilder out = new StringBuilder();
		for (int nn : nodeIds)
			out.append((out.length() > 0 ? " -> " : "") + nn);
		return out.toString();
	}
}
