package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.exec.LocalExecutioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Gpr.debug("Begin");

		LocalExecutioner localSysExecutioner = new LocalExecutioner();
		Task task = new Task("ID_ZZZ", Gpr.HOME + "/zzz.sh", "#!/bin/sh\n\nls -al");
		localSysExecutioner.run();
		localSysExecutioner.add(task);

		Gpr.debug("End");
	}
}
