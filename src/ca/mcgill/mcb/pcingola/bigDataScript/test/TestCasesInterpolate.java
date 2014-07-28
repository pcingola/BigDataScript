package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.InterpolateVars;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesInterpolate extends TestCasesBase {

	void checkInterpolate(String str, String strings[], String vars[]) {
		InterpolateVars iv = new InterpolateVars(null, null);
		iv.parse(str);
		System.out.println("String: " + str);
		System.out.println("\tInterpolation result: |" + iv + "|");

		// Special case: No variables to interpolate
		if (strings.length == 1 && vars[0].isEmpty()) {
			Assert.assertTrue(iv.isEmpty());
			return;
		}

		// Check strings
		for (int i = 0; i < strings.length; i++) {
			System.out.print("\tIndex: " + i);
			System.out.print("\tstring.expected: " + strings[i] + "\tstring.actual: " + iv.getStrings().get(i));
			System.out.println("\tvar.expected: " + vars[i] + "\tvar.actual: " + iv.getVarRefs().get(i));
			Assert.assertEquals(strings[i], iv.getStrings().get(i));
			if (vars[i] != null && !vars[i].isEmpty()) Assert.assertEquals(vars[i], iv.getVarRefs().get(i).toString());
		}
	}

	@Test
	public void test00() {
		String strings[] = { "Hello $i" };
		String vars[] = { "" };

		checkInterpolate("Hello \\$i", strings, vars);
	}

	@Test
	public void test01() {
		String strings[] = { "Hello " };
		String vars[] = { "i" };

		checkInterpolate("Hello $i", strings, vars);
	}

	@Test
	public void test02() {
		String strings[] = { "Hello ", " " };
		String vars[] = { "i", "j" };

		checkInterpolate("Hello $i $j", strings, vars);
	}

	@Test
	public void test03() {
		String strings[] = { "Hello ", "" };
		String vars[] = { "i", "j" };

		checkInterpolate("Hello $i$j", strings, vars);
	}

	@Test
	public void test04() {
		String strings[] = { "l[1] : " };
		String vars[] = { "l[1]" };

		checkInterpolate("l[1] : $l[1]", strings, vars);
	}

	@Test
	public void test05() {
		String strings[] = { "m{'Helo'} : " };
		String vars[] = { "m{\"Helo\"}" };

		checkInterpolate("m{'Helo'} : $m{'Helo'}", strings, vars);
	}

	@Test
	public void test06() {
		String strings[] = { "m{'Helo'} : " };
		String vars[] = { "m{l[i]}" };

		checkInterpolate("m{'Helo'} : $m{$l[$i]}", strings, vars);
	}

	@Test
	public void test07() {
		String strings[] = { "Hello $" };
		String vars[] = { "" };

		checkInterpolate("Hello $", strings, vars);
	}

	@Test
	public void test08() {
		String strings[] = { "Hello $\n" };
		String vars[] = { "" };

		checkInterpolate("Hello $\n", strings, vars);
	}

	@Test
	public void test09() {
		String strings[] = { "m{'Helo'} : " };
		String vars[] = { "m{s}" };

		checkInterpolate("m{'Helo'} : $m{$s}", strings, vars);
	}

	@Test
	public void test10() {
		String strings[] = { "l[1] : '", "'\n" };
		String vars[] = { "l[1]", "" };

		checkInterpolate("l[1] : '$l[1]'\n", strings, vars);
	}

	@Test
	public void test11() {
		String strings[] = { "List with variable index: '", "'\n" };
		String vars[] = { "l[s]", "" };

		checkInterpolate("List with variable index: '$l[$s]'\n", strings, vars);
	}

	@Test
	public void test12() {
		String strings[] = { "List with variable index: {", "}\n" };
		String vars[] = { "l[s]", "" };

		checkInterpolate("List with variable index: {$l[$s]}\n", strings, vars);
	}

	@Test
	public void test13() {
		String strings[] = { "List with variable index: [", "]\n" };
		String vars[] = { "l[s]", "" };

		checkInterpolate("List with variable index: [$l[$s]]\n", strings, vars);
	}

	@Test
	public void test14() {
		String strings[] = { "Map with list and variable index: ", };
		String vars[] = { "m{l[s]}", "" };

		checkInterpolate("Map with list and variable index: $m{$l[$s]}", strings, vars);
	}

	@Test
	public void test15() {
		String strings[] = { "Map with list and variable index: {", "}\n" };
		String vars[] = { "m{l[s]}", "" };

		checkInterpolate("Map with list and variable index: {$m{$l[$s]}}\n", strings, vars);
	}

}
