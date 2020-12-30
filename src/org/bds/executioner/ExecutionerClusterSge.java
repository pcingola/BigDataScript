package org.bds.executioner;

import java.util.List;
import java.util.regex.Pattern;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesCluster;
import org.bds.task.Task;
import org.bds.util.Timer;

/**
 * Execute tasks in an SGE cluster.
 *
 * @author pcingola
 */
public class ExecutionerClusterSge extends ExecutionerCluster {

	public static final String PID_REGEX_DEFAULT = "Your job (\\S+)";

	String sgePe = "", sgeMem = "", sgeTimeOut = "", sgeTimeOutSoft = "";
	boolean timeInSecs = false;

	public ExecutionerClusterSge(Config config) {
		super(config);

		// Postmortem needs "-j"
		String postMortemInfoCommand[] = { "qstat", "-f", "-j" };
		clusterPostMortemInfoCommand = postMortemInfoCommand;

		// When running qsub you get a line lie this:
		//
		//		$ echo ls | qsub
		// 		Your job 33 ("STDIN") has been submitted
		//
		// So, this is a pattern matcher to parse the PID
		pidRegexStr = config.getPidRegex(PID_REGEX_DEFAULT);
		pidRegex = Pattern.compile(pidRegexStr);
		debug("Using pidRegex '" + pidRegexStr + "'");

		// SGE parameters from config file
		sgePe = config.getString(Config.CLUSTER_SGE_PE, "");
		if (sgePe.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_PE + "'.");

		sgeMem = config.getString(Config.CLUSTER_SGE_MEM, "");
		if (sgeMem.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_MEM + "'.");

		sgeTimeOut = config.getString(Config.CLUSTER_SGE_TIMEOUT_HARD, "");
		if (sgeTimeOut.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_TIMEOUT_HARD + "'.");

		sgeTimeOutSoft = config.getString(Config.CLUSTER_SGE_TIMEOUT_SOFT, "");
		if (sgeTimeOut.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_TIMEOUT_SOFT + "'.");

		timeInSecs = config.getBool(Config.CLUSTER_SGE_TIME_IN_SECS, false);
		if (sgeMem.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_MEM + "'.");

	}

	/**
	 * Add resource options to command line parameters
	 */
	@Override
	protected void addResources(Task task, List<String> args) {
		// Add resources request
		TaskResourcesCluster res = (TaskResourcesCluster) task.getResources();

		// Cpu
		if (res.getCpus() > 0) {
			args.add("-pe");
			args.add(sgePe);
			args.add("" + res.getCpus());
		}

		// Memory
		if (res.getMem() > 0) {
			if (sgeMem.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_PE + "'.");
			args.add("-l");
			args.add(sgeMem + "=" + res.getMem());
		}

		// Timeout
		int clusterTimeout = calcTimeOut(res);
		if (clusterTimeout > 0) {
			// Hard timeout
			args.add("-l");
			args.add(sgeTimeOut + "=" + time(clusterTimeout));

			// Soft timeout
			args.add("-l");
			args.add(sgeTimeOutSoft + "=" + time(clusterTimeout));
		}

		// A particular queue was requested?
		String queue = res.getQueue();
		if (queue != null && !queue.isEmpty()) {
			args.add("-q");
			args.add(queue);
		}
	}

	/**
	 * Represent a time according for 'qsub' command line arguments
	 */
	protected String time(int secs) {
		if (timeInSecs) return secs + "";
		return Timer.toHHMMSS(secs * 1000L);
	}
}
