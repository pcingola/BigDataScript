package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.htmlTemplate.RTemplate;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		// RTemplate rTemplate = new RTemplate(BigDataScript.class, "resources/SummaryTemplate.html", Gpr.HOME + "/z.html");
		RTemplate rTemplate = new RTemplate(BigDataScript.class, "SummaryTemplate.html", Gpr.HOME + "/z.html");

		rTemplate.add("title", "Zzz");
		rTemplate.add("title", "World");

		rTemplate.add("subtitle", "SubZzz");
		rTemplate.add("subtitle", "SubWorld");

		rTemplate.createOuptut();

		Gpr.debug("Zzz: End");
	}
}
