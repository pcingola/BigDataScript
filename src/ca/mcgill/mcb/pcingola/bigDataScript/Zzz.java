package ca.mcgill.mcb.pcingola.bigDataScript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		String str = "Your job 33 (\"STDIN\") has been submitted";

		Pattern pattern = Pattern.compile("Your job (\\S+)");
		Matcher matcher = pattern.matcher(str);

		if (matcher.find()) {
			Gpr.debug("MATCH: |" + matcher.group(1) + "|");
		}

		Gpr.debug("Zzz: End");
	}
}
