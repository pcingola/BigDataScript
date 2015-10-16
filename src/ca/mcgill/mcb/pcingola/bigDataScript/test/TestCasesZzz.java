package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.data.Data;
import ca.mcgill.mcb.pcingola.bigDataScript.data.DataFile;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test01_parse_URLs_file() {
		Gpr.debug("Test");

		String currPath;
		try {
			currPath = (new File(".")).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (verbose) Gpr.debug("CurrPath: " + currPath);

		Data d = Data.factory("tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals(currPath + "/tmp.txt", d.getAbsolutePath());
		Gpr.toFile(d.getAbsolutePath(), "test");
		Assert.assertTrue(d.isFile());
		Assert.assertFalse(d.isDirectory());

		d = Data.factory("./tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tabsolutePath: " + d.getAbsolutePath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals(currPath + "/./tmp.txt", d.getAbsolutePath());
		Assert.assertEquals(currPath + "/tmp.txt", d.getCanonicalPath());

		d = Data.factory("/tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals("/tmp.txt", d.getAbsolutePath());

		d = Data.factory("file:///tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals("/tmp.txt", d.getAbsolutePath());
	}

}
