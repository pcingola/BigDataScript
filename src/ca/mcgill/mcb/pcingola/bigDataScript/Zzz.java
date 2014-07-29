package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.GprString;

public class Zzz {

	public static void main(String[] args) throws Exception {

		String str = "\"After, \\\" ,checkpoint: $s\", \"Another,string\"";
		String split[] = GprString.splitCsv(str);

		for (String s : split)
			Gpr.debug("|" + s + "|");
	}
}
