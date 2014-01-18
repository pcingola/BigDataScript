package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunnerSsh;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		Cluster cluster = new Cluster();
		Host host = new Host(cluster, "eq8302@ehs.grid.wayne.edu");

		// String programFile = Gpr.HOME + "/z.sh";
		String commandArgs[] = { "echo Hello; hostname ; ls -al zxzxzx ; echo Done" };

		CmdRunnerSsh cssh = new CmdRunnerSsh("CMDID", commandArgs);
		cssh.setHost(host);
		cssh.start();
		cssh.join();

		Gpr.debug("Zzz: End");
	}
}
