package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

/**
 * Statistics collected after cmdRunner ends
 * @author pcingola
 *
 */
public interface CmdStats {

	/**
	 * Is this task finished?
	 * @param done
	 */
	public void setDone(boolean done);

	/**
	 * Exit value
	 * @param exitValue
	 */
	public void setExitValue(int exitValue);

}
