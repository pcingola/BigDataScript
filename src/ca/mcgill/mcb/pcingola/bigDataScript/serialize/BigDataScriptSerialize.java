package ca.mcgill.mcb.pcingola.bigDataScript.serialize;

public interface BigDataScriptSerialize {

	/**
	 * Parse a line from a serialized file
	 * @param line
	 * @return
	 */
	public void serializeParse(BigDataScriptSerializer serializer);

	/**
	 * Create a string to serialize to a file
	 * @return
	 */
	public String serializeSave(BigDataScriptSerializer serializer);

}
