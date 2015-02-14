package ca.mcgill.mcb.pcingola.bigDataScript.test;

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
		compileOk("test/test00.bds");
	}

	@Test
	public void test01() {
		compileOk("test/test01.bds");
	}

	@Test
	public void test02() {
		compileOk("test/test02.bds");
	}

	@Test
	public void test03() {
		compileOk("test/test03.bds");
	}

	@Test
	public void test04() {
		compileOk("test/test04.bds");
	}

	@Test
	public void test05() {
		compileOk("test/test05.bds");
	}

	@Test
	public void test06() {
		compileOk("test/test06.bds");
	}

	@Test
	public void test07() {
		compileOk("test/test07.bds");
	}

	@Test
	public void test08() {
		String errs = "ERROR [ file 'test/test08.bds', line 11 ] :	Only variable reference can be used with ++ or -- operators\n";
		compileErrors("test/test08.bds", errs);
	}

	@Test
	public void test09() {
		compileOk("test/test09.bds");
	}

	@Test
	public void test10() {
		String err = "ERROR [ file 'test/test10.bds', line 2 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test10.bds", err);
	}

	@Test
	public void test11() {
		String err = "ERROR [ file 'test/test11.bds', line 2 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test11.bds", err);
	}

	@Test
	public void test12() {
		compileOk("test/test12.bds");
	}

	@Test
	public void test13() {
		compileOk("test/test13.bds");
	}

	@Test
	public void test14() {
		String errs = "ERROR [ file 'test/test14.bds', line 3 ] :	Symbol 'i' cannot be resolved\n"//
				+ "ERROR [ file 'test/test14.bds', line 4 ] :	Symbol 'i' cannot be resolved\n";

		compileErrors("test/test14.bds", errs);
	}

	@Test
	public void test15() {
		String errs = "ERROR [ file 'test/test15.bds', line 4 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test15.bds", errs);
	}

	@Test
	public void test16() {
		compileOk("test/test16.bds");
	}

	@Test
	public void test17() {
		compileOk("test/test17.bds");
	}

	@Test
	public void test18() {
		compileOk("test/test18.bds");
	}

	@Test
	public void test19() {
		String errs = "ERROR [ file 'test/test19.bds', line 4 ] :	Duplicate local name 'i'\n";
		compileErrors("test/test19.bds", errs);
	}

	@Test
	public void test20() {
		compileOk("test/test20.bds");
	}

	@Test
	public void test21() {
		compileOk("test/test21.bds");
	}

	@Test
	public void test22() {
		compileOk("test/test22.bds");
	}

	@Test
	public void test23() {
		compileOk("test/test23.bds");
	}

	@Test
	public void test24() {
		compileOk("test/test24.bds");
	}

	@Test
	public void test25() {
		compileOk("test/test25.bds");
	}

	@Test
	public void test26() {
		compileOk("test/test26.bds");
	}

	@Test
	public void test27() {
		String errs = "ERROR [ file 'test/test27.bds', line 2 ] :	Cannot cast real to int\n";
		compileErrors("test/test27.bds", errs);
	}

	@Test
	public void test28() {
		compileOk("test/test28.bds");
	}

	@Test
	public void test29() {
		String errs = "ERROR [ file 'test/test29.bds', line 3 ] :	For loop condition must be a bool expression\n";
		compileErrors("test/test29.bds", errs);
	}

	@Test
	public void test30() {
		String errs = "ERROR [ file 'test/test30.bds', line 4 ] :	Cannot cast real to int\n";
		compileErrors("test/test30.bds", errs);
	}

	@Test
	public void test31() {
		String errs = "ERROR [ file 'test/test31.bds', line 4 ] :	Function has no return statement\n";
		compileErrors("test/test31.bds", errs);
	}

	@Test
	public void test32() {
		// WARNING: Since now expressions are casted to BOOL, we should not get an error using this task(...)
		compileOk("test/test32.bds");

		//		String errs = "ERROR [ file 'test/test32.bds', line 7 ] :	Only assignment or boolean expressions are allowed in task options\n";
		//		compileErrors("test/test32.bds", errs);
	}

	@Test
	public void test33() {
		String errs = "ERROR [ file 'test/test33.bds', line 7 ] :	Only sys statements are allowed in a task (line 11)\n";
		compileErrors("test/test33.bds", errs);
	}

	@Test
	public void test34() {
		String errs = "ERROR [ file 'test/test34.bds', line 5 ] :	Function f(int) cannot be resolved\n";
		compileErrors("test/test34.bds", errs);
	}

	@Test
	public void test35() {
		compileOk("test/test35.bds");
	}

	@Test
	public void test36() {
		String errs = "ERROR [ file 'test/test36.bds', line 3 ] :	Symbol 'j' cannot be resolved\n";
		compileErrors("test/test36.bds", errs);
	}

	@Test
	public void test37() {
		String errs = "ERROR [ file 'test/test37.bds', line 16 ] :	Symbol 'fruit' cannot be resolved\n";
		compileErrors("test/test37.bds", errs);
	}

	@Test
	public void test38() {
		String errs = "ERROR [ file 'test/test38.bds', line 6 ] :	Cannot cast string to int\n";
		compileErrors("test/test38.bds", errs);
	}

	@Test
	public void test39() {
		String errs = "ERROR [ file 'test/test39.bds', line 6 ] :	Cannot cast string to int\n";
		compileErrors("test/test39.bds", errs);
	}

	@Test
	public void test40() {
		String errs = "ERROR [ file 'test/test40.bds', line 6 ] :	Method int[].push(string) cannot be resolved\n";
		compileErrors("test/test40.bds", errs);
	}

	@Test
	public void test41() {
		compileOk("test/test41.bds");
	}

	@Test
	public void test42() {
		compileOk("test/test42.bds");
	}

	@Test
	public void test43() {
		String errs = "ERROR [ file 'test/test43.bds', line 8 ] :	Cannot declare variable 'res' type 'void'";
		compileErrors("test/test43.bds", errs);
	}

	@Test
	public void test44() {
		String errs = "ERROR [ file 'test/test44.bds', line 2 ] :	Cannot append int[] to string[]";
		compileErrors("test/test44.bds", errs);
	}

	@Test
	public void test45() {
		String errs = "ERROR [ file 'test/test45.bds', line 2 ] :	Cannot append int to string[]";
		compileErrors("test/test45.bds", errs);
	}

	@Test
	public void test46() {
		compileOk("test/test46.bds");
	}

	@Test
	public void test47() {
		String errs = "ERROR [ file 'test/test47.bds', line 3 ] :	Duplicate local name 'gsea' (function 'gsea' declared in test/test47.bds, line 5)";
		compileErrors("test/test47.bds", errs);
	}

	@Test
	public void test48() {
		String errs = "ERROR [ file 'test/test48.bds', line 5 ] :	extraneous input ':=' expecting {<EOF>, 'while', '{', 'void', 'for', 'error', 'debug', 'int', 'include', 'task', '(', 'kill', '\n', 'println', 'exit', '++', '~', 'wait', 'dep', '+', 'goal', 'continue', 'return', ';', 'if', 'warning', 'break', 'print', 'parallel', 'par', '[', '--', 'bool', '!', 'string', 'checkpoint', 'breakpoint', '-', 'real', BOOL_LITERAL, INT_LITERAL, REAL_LITERAL, STRING_LITERAL, STRING_LITERAL_SINGLE, SYS_LITERAL, TASK_LITERAL, ID}";
		compileErrors("test/test48.bds", errs);
	}

}
