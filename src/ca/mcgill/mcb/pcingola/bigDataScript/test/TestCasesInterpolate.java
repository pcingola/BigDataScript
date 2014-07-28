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

		// Check strings
		for (int i = 0; i < strings.length; i++) {
			System.out.print("\tIndex: " + i);
			System.out.print("\tstring.expected: " + strings[i] + "\tstring.actual: " + iv.getStrings().get(i));
			System.out.println("\tvar.expected: " + vars[i] + "\tvar.actual: " + iv.getVarRefs().get(i));
			Assert.assertEquals(strings[i], iv.getStrings().get(i));
			if (vars[i] != null) Assert.assertEquals(vars[i], iv.getVarRefs().get(i).toString());
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
		String strings[] = { "Hello ", "" };
		String vars[] = { "i", "j" };

		checkInterpolate("l[1] : $l[1]", strings, vars);
	}

	@Test
	public void test05() {
		String strings[] = { "Hello ", "" };
		String vars[] = { "i", "j" };

		checkInterpolate("m{'Helo'} : $m{'Helo'}", strings, vars);
	}

	@Test
	public void test06() {
		String strings[] = { "Hello ", "" };
		String vars[] = { "i", "j" };

		checkInterpolate("m{'Helo'} : $m{$l[$i]}", strings, vars);
	}

}
