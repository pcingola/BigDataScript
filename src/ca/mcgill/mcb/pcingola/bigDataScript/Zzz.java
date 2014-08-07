package ca.mcgill.mcb.pcingola.bigDataScript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		String pidLine = "18554104.gm-1r16-n04.guillimin.clumeq.ca";
		String regex = "(.+).guillimin.clumeq.ca";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(pidLine);
		while (m.find()) {
			Gpr.debug("Something found!");
			System.out.println("Found: '" + m.group(1) + "'");
		}
	}
}
