package ca.mcgill.mcb.pcingola.bigDataScript.serialize;

public interface BigDataScriptSerialize {

	/**
	 * Parse a line from a serialized file
	 */
	public void serializeParse(BigDataScriptSerializer serializer);

	/**
	 * Create a string to serialize to a file
	 */
	public String serializeSave(BigDataScriptSerializer serializer);

}
