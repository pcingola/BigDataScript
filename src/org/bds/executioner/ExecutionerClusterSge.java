package org.bds.executioner;

import java.util.List;
import java.util.regex.Pattern;

import org.bds.Config;
import org.bds.cluster.host.HostResources;
import org.bds.task.Task;
import org.bds.util.Timer;

/**
 * Execute tasks in an SGE cluster.
 *
 * @author pcingola
 */
public class ExecutionerClusterSge extends ExecutionerCluster {

	public static final String PID_REGEX_DEFAULT = "Your job (\\S+)";

	String sgePe = "", sgeMem = "", sgeTimeOut = "";

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
		if (debug) log("Using pidRegex '" + pidRegexStr + "'");

		// SGE parameters from config file
		sgePe = config.getString(Config.CLUSTER_SGE_PE, "");
		if (sgePe.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_PE + "'.");

		sgeMem = config.getString(Config.CLUSTER_SGE_MEM, "");
		if (sgeMem.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_MEM + "'.");

		sgeTimeOut = config.getString(Config.CLUSTER_SGE_TIMEOUT, "");
		if (sgeTimeOut.isEmpty()) throw new RuntimeException("Missing config file entry '" + Config.CLUSTER_SGE_TIMEOUT + "'.");
	}

	/**
	 * Add resource options to command line parameters
	 */
	@Override
	protected void addResources(Task task, List<String> args) {
		// Add resources request
		HostResources res = task.getResources();

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
		if (res.getMem() > 0) {
			args.add("-l");
			args.add(sgeTimeOut + "=" + Timer.toHHMMSS(clusterTimeout * 1000));
		}
	}
}
