package org.bds.test.unit;

import java.io.File;
import java.io.IOException;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataFile;
import org.bds.data.DataHttp;
import org.bds.data.DataS3;
import org.bds.run.BdsRun;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for "remote" Data files
 *
 * @author pcingola
 *
 */
public class TestCasesRemote extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		BdsRun.reset();
		//		Config.reset();
		Config.get().load();
	}

	String getCurrPath() {
		try {
			return (new File(".")).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Test parsing a local file (DataFile)
	 */
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

	/**
	 * Test local file: DataFile using absolute path
	 */
	@Test
	public void test101_data_file_absolute() {
		String path = "/dir/sub/file.txt";
		Data dfile = Data.factory(path);
		Assert.assertFalse("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), path, dfile.getAbsolutePath());
	}

	/**
	 * Test local file: DataFile using "file://" format
	 */
	@Test
	public void test101b_data_file_absolute() {
		String path = "/dir/sub/file.txt";
		Data dfile = Data.factory("file://" + path);
		Assert.assertFalse("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), path, dfile.getAbsolutePath());
	}

	/**
	 * Test local file: DataFile using relative file
	 */
	@Test
	public void test102_data_file_relative() {
		String path = "dir/sub/file.txt";
		Data dfile = Data.factory(path);

		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	/**
	 * Test local file: DataFile using relative file with "file://" format
	 */
	@Test
	public void test102b_data_file_relative() {
		String path = "dir/sub/file.txt";
		Data dfile = Data.factory("file://" + path);

		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	/**
	 * Test local file: DataFile using relative file with "./" format
	 */
	@Test
	public void test103_data_file_relative() {
		String path = "./dir/sub/file.txt";
		Data dfile = Data.factory(path);
		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	/**
	 * Test local file: DataFile using relative file with "file://./" format
	 */
	@Test
	public void test103b_data_file_relative() {
		String path = "./dir/sub/file.txt";
		Data dfile = Data.factory("file://" + path);
		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	/**
	 * Test `join` for local file segments
	 */
	@Test
	public void test104_file_join_segments() {
		Data dfile = Data.factory("dir/sub/file.txt");
		Data ddir = Data.factory("/home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/dir/sub/file.txt", djoin.getAbsolutePath());
	}

	/**
	 * Test `join` for local file segments, using "file://" formats
	 */
	@Test
	public void test104b_file_join_segments() {
		Data dfile = Data.factory("file://dir/sub/file.txt");
		Data ddir = Data.factory("file:///home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/dir/sub/file.txt", djoin.getAbsolutePath());
	}

	/**
	 * Test `join` for local file segments using "./" formats
	 */
	@Test
	public void test105_file_join_segments() {
		Data dfile = Data.factory("./dir/sub/file.txt");
		Data ddir = Data.factory("/home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/./dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/./dir/sub/file.txt", djoin.getAbsolutePath());
	}

	/**
	 * Test `join` for local file segments using "file://./" formats
	 */
	@Test
	public void test105b_file_join_segments() {
		Data dfile = Data.factory("file://./dir/sub/file.txt");
		Data ddir = Data.factory("file:///home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/./dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/./dir/sub/file.txt", djoin.getAbsolutePath());
	}

	/**
	 * Test Data.factory and joining for remote HTTP file
	 * using absolute path
	 */
	@Test
	public void test107_url_join() {
		Data dfile = Data.factory("/dir/sub/file.txt");
		Data durl = Data.factory("http://www.ensembl.org");
		Data djoin = durl.join(dfile);

		Assert.assertFalse(dfile.isRelative());
		Assert.assertFalse(durl.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(durl instanceof DataHttp);
		Assert.assertTrue(djoin instanceof DataHttp);
		DataHttp dhttp = (DataHttp) djoin;
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(dhttp.toString(), "http://www.ensembl.org/dir/sub/file.txt");
	}

	/**
	 * Test Data.factory and joining for remote HTTP file
	 * using relative path
	 */
	@Test
	public void test107b_url_join() {
		Data dfile = Data.factory("dir/sub/file.txt");
		Data durl = Data.factory("http://www.ensembl.org");
		Data djoin = durl.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(durl.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(durl instanceof DataHttp);
		Assert.assertTrue(djoin instanceof DataHttp);
		DataHttp dhttp = (DataHttp) djoin;
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(dhttp.toString(), "http://www.ensembl.org/dir/sub/file.txt");
	}

	/**
	 * Test Data.factory and joining for remote S3 file
	 * using absolute path
	 */
	@Test
	public void test108_s3_join() {
		Data dfile = Data.factory("/dir/sub/file.txt");
		Data ds3 = Data.factory("s3://my_bucket");

		Assert.assertFalse(dfile.isRelative());
		Assert.assertFalse(ds3.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ds3 instanceof DataS3);
		Data djoin = ds3.join(dfile);

		Assert.assertTrue(djoin instanceof DataS3);
		DataS3 ds3join = (DataS3) djoin;
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(ds3join.toString(), "s3://my_bucket/dir/sub/file.txt");
	}

	/**
	 * Test Data.factory and joining for remote S3 file
	 * using relative path
	 */
	@Test
	public void test108b_s3_join() {
		Data dfile = Data.factory("dir/sub/file.txt");
		Data ds3 = Data.factory("s3://my_bucket");
		Data djoin = ds3.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ds3.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ds3 instanceof DataS3);
		Assert.assertTrue(djoin instanceof DataS3);
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.toString(), "s3://my_bucket/dir/sub/file.txt");
	}

	/**
	 * Test Data.factory using file without any path
	 */
	@Test
	public void test109_parseFile_relative() throws Exception {
		String path = "tmp.txt";
		Data dpath = Data.factory(path);
		if (verbose) Gpr.debug("Path: " + dpath.getPath());
		Assert.assertTrue(dpath instanceof DataFile);
		Assert.assertTrue(dpath.isRelative());

		// Absolute path
		File fpathAbs = new File(new File("").getAbsoluteFile(), path);
		Assert.assertEquals(fpathAbs.getAbsolutePath(), dpath.getAbsolutePath());

		// Canonical path
		File fpathCan = new File(new File(".").getCanonicalFile(), path);
		Assert.assertEquals(fpathCan.getCanonicalPath(), dpath.getCanonicalPath());

		Gpr.toFile(dpath.getAbsolutePath(), "test");
		Assert.assertTrue(dpath.isFile());
		Assert.assertFalse(dpath.isDirectory());
	}

	/**
	 * Test Data.factory using file with "./" path
	 */
	@Test
	public void test109b_parseFile_relative_03() throws Exception {
		String path = "./tmp.txt";
		Data dpath = Data.factory(path);
		if (verbose) Gpr.debug("Path: " + dpath.getPath());
		Assert.assertTrue(dpath instanceof DataFile);
		Assert.assertTrue(dpath.isRelative());

		// Absolute path
		File fpathAbs = new File(new File("").getAbsoluteFile(), path);
		Assert.assertEquals(fpathAbs.getAbsolutePath(), dpath.getAbsolutePath());

		// Canonical path
		File fpathCan = new File(new File(".").getCanonicalFile(), path);
		Assert.assertEquals(fpathCan.getCanonicalPath(), dpath.getCanonicalPath());

		Gpr.toFile(dpath.getAbsolutePath(), "test");
		Assert.assertTrue(dpath.isFile());
		Assert.assertFalse(dpath.isDirectory());
	}

}
