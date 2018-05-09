package org.bds.test;

import java.util.HashSet;
import java.util.Set;

import org.bds.Config;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		Config.reset();
		Config.get().load();
	}

	@Test
	public void test113_parallel_function_calls() {
		Gpr.debug("Test");
		String stdout = runAndReturnStdout("test/run_113.bds");

		Set<String> linesPar = new HashSet<>();
		for (String line : stdout.split("\n")) {
			if (line.startsWith("Par:")) {
				if (linesPar.contains(line)) throw new RuntimeException("Line repeated (this should never happen): '" + line + "'");
				linesPar.add(line);
			}
		}
	}

}
