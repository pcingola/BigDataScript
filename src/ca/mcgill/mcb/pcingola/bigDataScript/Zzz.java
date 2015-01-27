package ca.mcgill.mcb.pcingola.bigDataScript;

import java.util.Set;

import ca.mcgill.mcb.pcingola.bigDataScript.executioner.CheckTasksRunning;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static boolean debug = true;
	public static boolean verbose = true;

	public static void main(String[] args) throws Exception {
		// Create 'CheckTasksRunning'
		Config config = new Config();
		config.setDebug(debug);
		config.setVerbose(verbose);

		Executioner ex = Executioners.getInstance(config).get(ExecutionerType.LOCAL);
		CheckTasksRunning ctr = new CheckTasksRunning(config, ex);

		// Parse 'qstat' lines
		String fileName = Gpr.HOME + "/qstat.txt";
		System.out.println("Reading file '" + fileName + "'");
		String file = Gpr.readFile(fileName);
		String lines[] = file.split("\n");
		Set<String> pids = ctr.parseCommandOutput(lines);

		// Check that all IDs are there
		for (int i = 1; i < 10; i++) {
			String pid = "" + i;
			System.out.println("PID: '" + pid + "'\t" + pids.contains(pid));
		}

		// Finished
		System.out.println("Killing executioners");
		ex.kill();
		System.out.println("Done");
	}
}
