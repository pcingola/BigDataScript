package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("PATH: " + System.getenv("PATH"));

		String ss[] = { "hi", "bye" };
		for ( String s : ss)
			System.out.println(s);
	}
}
