package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.executioner.ExecutionerCluster;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		Config config = new Config();
		ExecutionerCluster ec = new ExecutionerCluster(config);
		//		ec.monitorTaskCluster();

		Gpr.debug("Zzz: End");
	}
}
