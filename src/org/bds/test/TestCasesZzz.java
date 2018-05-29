package org.bds.test;

import org.bds.Config;
import org.bds.compile.BdsCompilerExpression;
import org.bds.lang.expression.Expression;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

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
	public void test24() {
		Gpr.debug("Test");
		String exprStr = "1 + 20 * 2";
		BdsCompilerExpression be = new BdsCompilerExpression(exprStr);
		Expression expr = be.compileExpr();
		if (verbose) Gpr.debug("expr: " + expr);
		Assert.assertEquals(exprStr, expr.toString());
	}

	//	@Test
	//	public void test24() {
	//		Gpr.debug("Test");
	//		String strings[] = { "Hello " };
	//		String vars[] = { "a.x.z[56]" };
	//
	//		checkInterpolate("Hello $a.x.z[56]", strings, vars);
	//	}

	//	@Test
	//	public void test25() {
	//		Gpr.debug("Test");
	//		String strings[] = { "Hello " };
	//		String vars[] = { "a.x.z[56]{'hi'}" };
	//
	//		checkInterpolate("Hello $a.x.z[56]{'hi'}", strings, vars);
	//	}

	//	@Test
	//	public void test161() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		// Error interpolating object fields
	//		runAndCheck("test/run_161.bds", "out", "a.x = 42");
	//	}

	//	@Test
	//	public void test163() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		// Define and create inner classes
	//		runAndCheck("test/run_162.bds", "out", "B: A: Hi");
	//	}
	//
	//	@Test
	//	public void test164() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		// printErr error
	//		runAndCheck("test/run_164.bds", "", "");
	//	}

}
