package ca.mcgill.mcb.pcingola.bigDataScript;

import java.util.Random;

import ca.mcgill.mcb.pcingola.bigDataScript.exec.LocalExecutioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.LocalQueueExecutioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Gpr.debug("Begin");

		int test = 2;

		if (test == 1) {
			LocalExecutioner localSysExecutioner = new LocalExecutioner();
			Task task = new Task("ID_ZZZ", Gpr.HOME + "/zzz.sh", "#!/bin/sh\n\nls -al");
			localSysExecutioner.start();
			localSysExecutioner.add(task);
		} else if (test == 2) {
			LocalQueueExecutioner executioner = new LocalQueueExecutioner();
			executioner.start();

			Random rand = new Random(20130319);

			for (int i = 0; i < 10; i++) {
				int randTime = rand.nextInt(5) + 1;

				String script = "";
				script += "#!/bin/sh\n\n";
				script += "echo Hello\n";
				script += "sleep " + randTime + "\n";
				script += "echo Bye 1>&2\n";

				Task task = new Task("ID_ZZZ_" + i, Gpr.HOME + "/zzz_" + i + ".sh", script);
				executioner.add(task);
			}
		}
		Gpr.debug("End");
	}
}
