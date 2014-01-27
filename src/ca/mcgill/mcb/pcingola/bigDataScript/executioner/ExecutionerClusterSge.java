package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.regex.Pattern;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

/**
 * Execute tasks in an SGE cluster.
 * 
 * @author pcingola
 */
public class ExecutionerClusterSge extends ExecutionerCluster {

	public ExecutionerClusterSge(Config config) {
		super(config);

		// When running qsub you get a line lie this:
		//
		//		$ echo ls | qsub
		// 		Your job 33 ("STDIN") has been submitted
		// 
		// So, this is a pattern matcher to parse the PID
		pidPattern = Pattern.compile("Your job (\\S+)");
	}

}
