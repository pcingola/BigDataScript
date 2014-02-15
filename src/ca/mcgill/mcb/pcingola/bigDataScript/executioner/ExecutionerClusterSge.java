package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.List;
import java.util.regex.Pattern;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in an SGE cluster.
 * 
 * @author pcingola
 */
public class ExecutionerClusterSge extends ExecutionerCluster {

	protected final String CONFIG_SGE_PE = "sge.pe";
	protected final String CONFIG_SGE_MEM = "sge.mem";
	protected final String CONFIG_SGE_TIMEOUT = "sge.timeout";

	String sgePe = "", sgeMem = "", sgeTimeOut = "";

	public ExecutionerClusterSge(Config config) {
		super(config);

		// When running qsub you get a line lie this:
		//
		//		$ echo ls | qsub
		// 		Your job 33 ("STDIN") has been submitted
		// 
		// So, this is a pattern matcher to parse the PID
		pidPattern = Pattern.compile("Your job (\\S+)");

		// SGE parameters from config file
		sgePe = config.getString(CONFIG_SGE_PE, "");
		if (sgePe.isEmpty()) throw new RuntimeException("Missing config file entry '" + CONFIG_SGE_PE + "'.");

		sgeMem = config.getString(CONFIG_SGE_MEM, "");
		if (sgeMem.isEmpty()) throw new RuntimeException("Missing config file entry '" + CONFIG_SGE_MEM + "'.");

		sgeTimeOut = config.getString(CONFIG_SGE_TIMEOUT, "");
		if (sgeTimeOut.isEmpty()) throw new RuntimeException("Missing config file entry '" + CONFIG_SGE_TIMEOUT + "'.");
	}

	/**
	 * Add resource options to command line parameters
	 * 
	 * @param task
	 * @param args
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
			if (sgeMem.isEmpty()) throw new RuntimeException("Missing config file entry '" + CONFIG_SGE_PE + "'.");
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
