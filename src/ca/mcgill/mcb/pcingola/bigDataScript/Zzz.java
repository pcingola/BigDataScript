package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.exec.Tail;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Begin");
		String tailDir = Gpr.HOME + "/tail";

		int maxFiles = 1000;

		Tail tail = new Tail();
		tail.start();

		Thread.sleep(1000);

		for (int i = 0; i < maxFiles; i++) {
			String file = tailDir + "/" + i + ".txt";

			if (file.endsWith(".txt")) {
				Gpr.debug("Adding file : " + file);
				tail.add(file, null, false);
			}
		}

		while (true)
			Thread.sleep(1000);
	}
}
