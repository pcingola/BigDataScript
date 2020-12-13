package org.bds.test.unit;

import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Test cases for language & compilation
 *
 * Note: These test cases just check language parsing and compilation (what is supposed to compile OK, and what is not).
 *
 * @author pcingola
 *
 */
public class TestCasesLang extends TestCasesBase {

	@Test
	public void test00() {
		Gpr.debug("Test");
		compileOk("test/test00.bds");
	}

	@Test
	public void test01() {
		Gpr.debug("Test");
		compileOk("test/test01.bds");
	}

	@Test
	public void test02() {
		Gpr.debug("Test");
		compileOk("test/test02.bds");
	}

	@Test
	public void test03() {
		Gpr.debug("Test");
		compileOk("test/test03.bds");
	}

	@Test
	public void test04() {
		Gpr.debug("Test");
		compileOk("test/test04.bds");
	}

	@Test
	public void test05() {
		Gpr.debug("Test");
		compileOk("test/test05.bds");
	}

	@Test
	public void test06() {
		Gpr.debug("Test");
		compileOk("test/test06.bds");
	}

	@Test
	public void test07() {
		Gpr.debug("Test");
		compileOk("test/test07.bds");
	}

	@Test
	public void test08() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test08.bds', line 11 ] :	Only variable reference can be used with ++ or -- operators\n";
		compileErrors("test/test08.bds", errs);
	}

	@Test
	public void test09() {
		Gpr.debug("Test");
		compileOk("test/test09.bds");
	}

	@Test
	public void test10() {
		Gpr.debug("Test");
		String err = "ERROR [ file 'test/test10.bds', line 2 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test10.bds", err);
	}

	@Test
	public void test11() {
		Gpr.debug("Test");
		String err = "ERROR [ file 'test/test11.bds', line 2 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test11.bds", err);
	}

	@Test
	public void test12() {
		Gpr.debug("Test");
		compileOk("test/test12.bds");
	}

	@Test
	public void test13() {
		Gpr.debug("Test");
		compileOk("test/test13.bds");
	}

	@Test
	public void test14() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test14.bds', line 3 ] :	Symbol 'i' cannot be resolved\n"//
				+ "ERROR [ file 'test/test14.bds', line 4 ] :	Symbol 'i' cannot be resolved\n";

		compileErrors("test/test14.bds", errs);
	}

	@Test
	public void test15() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test15.bds', line 4 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test15.bds", errs);
	}

	@Test
	public void test16() {
		Gpr.debug("Test");
		compileOk("test/test16.bds");
	}

	@Test
	public void test17() {
		Gpr.debug("Test");
		compileOk("test/test17.bds");
	}

	@Test
	public void test18() {
		Gpr.debug("Test");
		compileOk("test/test18.bds");
	}

	@Test
	public void test19() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test19.bds', line 4 ] :	Duplicate local name 'i'\n";
		compileErrors("test/test19.bds", errs);
	}

	@Test
	public void test20() {
		Gpr.debug("Test");
		compileOk("test/test20.bds");
	}

	@Test
	public void test21() {
		Gpr.debug("Test");
		compileOk("test/test21.bds");
	}

	@Test
	public void test22() {
		Gpr.debug("Test");
		compileOk("test/test22.bds");
	}

	@Test
	public void test23() {
		Gpr.debug("Test");
		compileOk("test/test23.bds");
	}

	@Test
	public void test24() {
		Gpr.debug("Test");
		compileOk("test/test24.bds");
	}

	@Test
	public void test25() {
		Gpr.debug("Test");
		compileOk("test/test25.bds");
	}

	@Test
	public void test26() {
		Gpr.debug("Test");
		compileOk("test/test26.bds");
	}

	@Test
	public void test27() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test27.bds', line 2 ] :	Cannot cast real to int\n";
		compileErrors("test/test27.bds", errs);
	}

	@Test
	public void test28() {
		Gpr.debug("Test");
		compileOk("test/test28.bds");
	}

	@Test
	public void test29() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test29.bds', line 3 ] :	For loop condition must be a bool expression\n";
		compileErrors("test/test29.bds", errs);
	}

	@Test
	public void test30() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test30.bds', line 4 ] :	Cannot cast real to int\n";
		compileErrors("test/test30.bds", errs);
	}

	@Test
	public void test31() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test31.bds', line 4 ] :	Function has no return statement\n";
		compileErrors("test/test31.bds", errs);
	}

	@Test
	public void test32() {
		Gpr.debug("Test");
		compileOk("test/test32.bds");
	}

	@Test
	public void test33() {
		Gpr.debug("Test");
		// As of bds version 3.0, this is no longer a compile error, it is just an "improper task"
		//
		//		String errs = "ERROR [ file 'test/test33.bds', line 7 ] :	Only sys statements are allowed in a task (line 11)\n";
		//		compileErrors("test/test33.bds", errs);
		//
		compileOk("test/test33.bds");
	}

	@Test
	public void test34() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test34.bds', line 5 ] :	Function f(int) cannot be resolved\n";
		compileErrors("test/test34.bds", errs);
	}

	@Test
	public void test35() {
		Gpr.debug("Test");
		compileOk("test/test35.bds");
	}

	@Test
	public void test36() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test36.bds', line 3 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test36.bds", errs);
	}

	@Test
	public void test37() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test37.bds', line 16 ] :	Symbol 'fruit' cannot be resolved\n";
		compileErrors("test/test37.bds", errs);
	}

	@Test
	public void test38() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test38.bds', line 6 ] :	Cannot cast string to int\n";
		compileErrors("test/test38.bds", errs);
	}

	@Test
	public void test39() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test39.bds', line 6 ] :	Cannot cast string to int\n";
		compileErrors("test/test39.bds", errs);
	}

	@Test
	public void test40() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test40.bds', line 6 ] :	Method int[].push(string) cannot be resolved\n";
		compileErrors("test/test40.bds", errs);
	}

	@Test
	public void test41() {
		Gpr.debug("Test");
		compileOk("test/test41.bds");
	}

	@Test
	public void test42() {
		Gpr.debug("Test");
		compileOk("test/test42.bds");
	}

	@Test
	public void test43() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test43.bds', line 8 ] :	Cannot declare variable 'res' type 'void'";
		compileErrors("test/test43.bds", errs);
	}

	@Test
	public void test44() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test44.bds', line 2 ] :	Cannot append int[] to string[]";
		compileErrors("test/test44.bds", errs);
	}

	@Test
	public void test45() {
		Gpr.debug("Test");
		compileOk("test/test45.bds");
	}

	@Test
	public void test46() {
		Gpr.debug("Test");
		compileOk("test/test46.bds");
	}

	@Test
	public void test47() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test47.bds', line 5 ] :	Duplicate local name 'gsea'";
		compileErrors("test/test47.bds", errs);
	}

	@Test
	public void test48() {
		Gpr.debug("Test");
		// String errs = "ERROR [ file 'test/test48.bds', line 5 ] :	extraneous input ':=' expecting {<EOF>, 'while', '{', 'void', 'for', 'error', 'debug', 'int', 'include', 'task', '(', 'kill', '\n', 'println', 'exit', '++', '~', 'wait', 'dep', '+', 'goal', 'continue', 'return', ';', 'if', 'warning', 'break', 'print', 'switch', 'parallel', 'par', '[', '--', 'bool', '!', 'string', 'checkpoint', 'breakpoint', '-', 'real', BOOL_LITERAL, INT_LITERAL, REAL_LITERAL, STRING_LITERAL, STRING_LITERAL_SINGLE, HELP_LITERAL, SYS_LITERAL, TASK_LITERAL, ID}";
		String errs = "ERROR [ file 'test/test48.bds', line 5 ] :	extraneous input ':=' expecting ";
		compileErrors("test/test48.bds", errs);
	}

	@Test
	public void test49() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test49.bds', line 4 ] :\tTask has empty statement";
		compileErrors("test/test49.bds", errs);
	}

	@Test
	public void test50() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test50.bds', line 6 ] :\tCannot assign to non-variable 'f(  )[0]'";
		compileErrors("test/test50.bds", errs);
	}

	@Test
	public void test51() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test51.bds', line 6 ] :	Cannot assign to non-variable 'f(  ){\"hi\"}'";
		compileErrors("test/test51.bds", errs);
	}

	@Test
	public void test52() {
		Gpr.debug("Test");
		compileOk("test/test52.bds");
	}

	@Test
	public void test53() {
		Gpr.debug("Test");
		compileOk("test/test53.bds");
	}

	@Test
	public void test54() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test54.bds', line 9 ] :	Switch expression and case expression types do not match (string vs int): case 7";
		compileErrors("test/test54.bds", errs);
	}

	@Test
	public void test55() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test55.bds', line 15 ] :	Symbol 'b' cannot be resolved";
		compileErrors("test/test55.bds", errs);
	}

	@Test
	public void test56() {
		Gpr.debug("Test");
		compileErrors("test/test56.bds", "Cannot find class 'A'");
	}

	@Test
	public void test57() {
		Gpr.debug("Test");
		compileErrors("test/test57.bds", "Cannot cast A to B");
	}

	@Test
	public void test58() {
		Gpr.debug("Test");
		compileErrors("test/test58.bds", "Symbol 'out' cannot be resolved");
	}

	@Test
	public void test59() {
		Gpr.debug("Test");
		compileErrors("test/test59.bds", "Expression should be string or string[], got '(A) -> string'");
	}

	@Test
	public void test60() {
		Gpr.debug("Test");
		compileOk("test/test60.bds");
	}

	@Test
	public void test61() {
		Gpr.debug("Test");
		compileOk("test/test61.bds");
	}

	@Test
	public void test62() {
		Gpr.debug("Test");
		compileOk("test/test62.bds");
	}

	@Test
	public void test63() {
		Gpr.debug("Test");
		compileErrors("test/test63.bds", "ERROR [ file 'test/test63.bds', line 5 ] :	Duplicate local name 'zzz'");
	}

	@Test
	public void test64() {
		Gpr.debug("Test");
		compileErrors("test/test64.bds", "Duplicate function 'zzz() -> void'");
	}

	@Test
	public void test65() {
		Gpr.debug("Test");
		compileErrors("test/test65.bds", "Duplicate method 'A.zzz(A) -> void'");
	}

	@Test
	public void test66() {
		Gpr.debug("Test");
		compileOk("test/test66.bds");
		compileOk("test/test66b.bds");
	}

	@Test
	public void test67() {
		Gpr.debug("Test");
		compileOk("test/test67.bds");
	}

	public void test67b() {
		Gpr.debug("Test");
		compileErrors("test/test67b.bds", "Cannot cast A to C");
	}

	public void test68() {
		Gpr.debug("Test");
		// Switch with empty statement crashes compile
		compileErrors("test/test68.bds", "Empty switch statment");
	}
}
