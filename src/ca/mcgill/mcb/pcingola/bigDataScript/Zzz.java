package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.executioner.ExecutionerCluster;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		Config config = Config.get();
		ExecutionerCluster executionerCluster = new ExecutionerCluster(config);

		//		Cluster cluster = new Cluster();
		//		Host host = new Host(cluster, "pcingola@localhost");

		Gpr.debug("Zzz: End");
	}
}
