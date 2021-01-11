package org.bds.test.unit;

import junit.framework.Assert;

import org.bds.task.TailFile;
import org.bds.test.TestCasesBase;
import org.junit.Test;

/**
 * Test cases for 'Tail'  file 
 * 
 * @author pcingola
 *
 */
public class TestCasesTail extends TestCasesBase {

	public static boolean debug = false;

	@Test
	public void test01() {
		String tail = TailFile.tail("test/tail_01.txt");
		Assert.assertEquals("", tail);
	}

	@Test
	public void test02() {
		String tail = TailFile.tail("test/tail_02.txt");
		Assert.assertEquals("Hi", tail);
	}

	@Test
	public void test03() {
		String tail = TailFile.tail("test/tail_03.txt");
		Assert.assertEquals("Hi\nbye\n", tail);
	}

	@Test
	public void test04() {
		String tail = TailFile.tail("test/tail_04.txt");
		Assert.assertEquals("line 1\nline 2\nline 3\nline 4\nline 5\nline 6\nline 7\nline 8\nline 9\nline 10\n", tail);
	}

	@Test
	public void test05() {
		// Lines larger than buffer size
		String tail = TailFile.tail("test/tail_05.txt");
		String lines[] = tail.split("\n");

		Assert.assertEquals(10, lines.length);
		for (int i = 0; i < lines.length; i++)
			Assert.assertEquals(81927, lines[i].length());
	}

	@Test
	public void test06() {
		String tail = TailFile.tail("test/tail_06.txt");
		Assert.assertEquals("line 11\nline 12\nline 13\nline 14\nline 15\nline 16\nline 17\nline 18\nline 19\nline 20\n", tail);
	}

}
