package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunnerSsh;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		Cluster cluster = new Cluster();
		Host host = new Host(cluster, "pablocingolani@localhost");

		String programFile = Gpr.HOME + "/z.sh";
		String commandArgs[] = { "ls", "-alh" };

		CmdRunnerSsh cssh = new CmdRunnerSsh("CMDID", programFile, commandArgs);
		cssh.setHost(host);
		cssh.start();
		//		cssh.wait();

		Gpr.debug("Zzz: End");
	}
}
