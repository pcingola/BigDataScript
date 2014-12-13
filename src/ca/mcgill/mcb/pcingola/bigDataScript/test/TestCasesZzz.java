package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test01() {
		String stdout = runAndReturnStdout("test/z.bds");

		Set<String> linesPar = new HashSet<String>();
		for (String line : stdout.split("\n")) {
			if (line.startsWith("Par:")) {
				if (linesPar.contains(line)) throw new RuntimeException("Line repeated (this should never happen): '" + line + "'");
				linesPar.add(line);
			}
		}
	}

	//	@Test
	//	public void test02() {
	//		runAndCheckpoint("test/z_splitLines.bds", null, "l", "15");
	//	}

}
