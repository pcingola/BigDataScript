package ca.mcgill.mcb.pcingola.bigDataScript.serialize;

public interface BdsSerialize {

	public String getNodeId();

	/**
	 * Parse a line from a serialized file
	 */
	public void serializeParse(BdsSerializer serializer);

	/**
	 * Create a string to serialize to a file
	 */
	public String serializeSave(BdsSerializer serializer);

}
