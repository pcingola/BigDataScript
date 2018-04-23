package org.bds.test;

import org.bds.lang.type.Types;
import org.bds.util.Gpr;
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
		Gpr.debug("Test");
		verbose = true;
		debug = true;
		Types.reset();
		runVmAndCheck("test/vm01.asm", "z", "8");
	}
}
