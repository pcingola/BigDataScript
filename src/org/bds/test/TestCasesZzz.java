package org.bds.test;

import org.bds.Config;
import org.junit.Before;

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

	//	@Test
	//	public void test01_data_file_absolute() {
	//		String path = "/dir/sub/file.txt";
	//		Data dfile = Data.factory(path);
	//		Assert.assertFalse("Relative: " + dfile.isRelative(), dfile.isRelative());
	//		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
	//		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);
	//
	//		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
	//		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), path, dfile.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test01b_data_file_absolute() {
	//		String path = "/dir/sub/file.txt";
	//		Data dfile = Data.factory("file://" + path);
	//		Assert.assertFalse("Relative: " + dfile.isRelative(), dfile.isRelative());
	//		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
	//		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);
	//
	//		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
	//		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), path, dfile.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test02_data_file_relative() {
	//		String path = "dir/sub/file.txt";
	//		Data dfile = Data.factory(path);
	//
	//		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
	//		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
	//		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);
	//
	//		Data pwd = Data.factory("");
	//		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
	//		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test02b_data_file_relative() {
	//		String path = "dir/sub/file.txt";
	//		Data dfile = Data.factory("file://" + path);
	//
	//		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
	//		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
	//		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);
	//
	//		Data pwd = Data.factory("");
	//		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
	//		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test03_data_file_relative() {
	//		String path = "./dir/sub/file.txt";
	//		Data dfile = Data.factory(path);
	//		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
	//		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
	//		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);
	//
	//		Data pwd = Data.factory("");
	//		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
	//		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test03b_data_file_relative() {
	//		String path = "./dir/sub/file.txt";
	//		Data dfile = Data.factory("file://" + path);
	//		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
	//		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
	//		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);
	//
	//		Data pwd = Data.factory("");
	//		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
	//		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test04_file_join_segments() {
	//		Data dfile = Data.factory("dir/sub/file.txt");
	//		Data ddir = Data.factory("/home");
	//		Data djoin = ddir.join(dfile);
	//
	//		Assert.assertTrue(dfile.isRelative());
	//		Assert.assertFalse(ddir.isRelative());
	//		Assert.assertFalse(djoin.isRelative());
	//
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(ddir instanceof DataFile);
	//		Assert.assertTrue(djoin instanceof DataFile);
	//
	//		Assert.assertEquals("Path: " + djoin.getPath(), "/home/dir/sub/file.txt", djoin.getPath());
	//		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/dir/sub/file.txt", djoin.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test04b_file_join_segments() {
	//		Data dfile = Data.factory("file://dir/sub/file.txt");
	//		Data ddir = Data.factory("file:///home");
	//		Data djoin = ddir.join(dfile);
	//
	//		Assert.assertTrue(dfile.isRelative());
	//		Assert.assertFalse(ddir.isRelative());
	//		Assert.assertFalse(djoin.isRelative());
	//
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(ddir instanceof DataFile);
	//		Assert.assertTrue(djoin instanceof DataFile);
	//
	//		Assert.assertEquals("Path: " + djoin.getPath(), "/home/dir/sub/file.txt", djoin.getPath());
	//		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/dir/sub/file.txt", djoin.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test05_file_join_segments() {
	//		Data dfile = Data.factory("./dir/sub/file.txt");
	//		Data ddir = Data.factory("/home");
	//		Data djoin = ddir.join(dfile);
	//
	//		Assert.assertTrue(dfile.isRelative());
	//		Assert.assertFalse(ddir.isRelative());
	//		Assert.assertFalse(djoin.isRelative());
	//
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(ddir instanceof DataFile);
	//		Assert.assertTrue(djoin instanceof DataFile);
	//
	//		Assert.assertEquals("Path: " + djoin.getPath(), "/home/./dir/sub/file.txt", djoin.getPath());
	//		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/./dir/sub/file.txt", djoin.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test05b_file_join_segments() {
	//		Data dfile = Data.factory("file://./dir/sub/file.txt");
	//		Data ddir = Data.factory("file:///home");
	//		Data djoin = ddir.join(dfile);
	//
	//		Assert.assertTrue(dfile.isRelative());
	//		Assert.assertFalse(ddir.isRelative());
	//		Assert.assertFalse(djoin.isRelative());
	//
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(ddir instanceof DataFile);
	//		Assert.assertTrue(djoin instanceof DataFile);
	//
	//		Assert.assertEquals("Path: " + djoin.getPath(), "/home/./dir/sub/file.txt", djoin.getPath());
	//		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/./dir/sub/file.txt", djoin.getAbsolutePath());
	//	}
	//
	//	@Test
	//	public void test06_url() {
	//		String url = "http://www.ensembl.org";
	//		Data durl = Data.factory(url);
	//		Assert.assertFalse("Relative: " + durl.isRelative(), durl.isRelative());
	//		Assert.assertTrue("Exists: " + durl.exists(), durl.exists());
	//		Assert.assertFalse("Is dir: " + durl.isDirectory(), durl.isDirectory());
	//		Assert.assertTrue("Wrong data type: " + durl.getClass().getCanonicalName(), durl instanceof DataHttp);
	//
	//		Assert.assertEquals("Path: " + durl.getPath(), "", durl.getPath());
	//		Assert.assertEquals("Canonical: " + durl.getAbsolutePath(), "", durl.getAbsolutePath());
	//		Assert.assertEquals("URL: " + durl.toString(), url, durl.toString());
	//	}
	//
	//	@Test
	//	public void test07_url_join() {
	//		Data dfile = Data.factory("/dir/sub/file.txt");
	//		Data durl = Data.factory("http://www.ensembl.org");
	//		Data djoin = durl.join(dfile);
	//
	//		Assert.assertFalse(dfile.isRelative());
	//		Assert.assertFalse(durl.isRelative());
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(durl instanceof DataHttp);
	//		Assert.assertTrue(djoin instanceof DataHttp);
	//		DataHttp dhttp = (DataHttp) djoin;
	//		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(dhttp.toString(), "http://www.ensembl.org/dir/sub/file.txt");
	//	}
	//
	//	@Test
	//	public void test07b_url_join() {
	//		Data dfile = Data.factory("dir/sub/file.txt");
	//		Data durl = Data.factory("http://www.ensembl.org");
	//		Data djoin = durl.join(dfile);
	//
	//		Assert.assertTrue(dfile.isRelative());
	//		Assert.assertFalse(durl.isRelative());
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(durl instanceof DataHttp);
	//		Assert.assertTrue(djoin instanceof DataHttp);
	//		DataHttp dhttp = (DataHttp) djoin;
	//		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(dhttp.toString(), "http://www.ensembl.org/dir/sub/file.txt");
	//	}
	//
	//	@Test
	//	public void test08_s3_join() {
	//		Data dfile = Data.factory("/dir/sub/file.txt");
	//		Data ds3 = Data.factory("s3://my_bucket");
	//
	//		Assert.assertFalse(dfile.isRelative());
	//		Assert.assertFalse(ds3.isRelative());
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(ds3 instanceof DataS3);
	//		Data djoin = ds3.join(dfile);
	//
	//		Assert.assertTrue(djoin instanceof DataS3);
	//		DataS3 ds3join = (DataS3) djoin;
	//		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(ds3join.toString(), "s3://my_bucket/dir/sub/file.txt");
	//	}
	//
	//	@Test
	//	public void test08b_s3_join() {
	//		Data dfile = Data.factory("dir/sub/file.txt");
	//		Data ds3 = Data.factory("s3://my_bucket");
	//		Data djoin = ds3.join(dfile);
	//
	//		Assert.assertTrue(dfile.isRelative());
	//		Assert.assertFalse(ds3.isRelative());
	//		Assert.assertTrue(dfile instanceof DataFile);
	//		Assert.assertTrue(ds3 instanceof DataS3);
	//		Assert.assertTrue(djoin instanceof DataS3);
	//		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	//		Assert.assertEquals(djoin.toString(), "s3://my_bucket/dir/sub/file.txt");
	//	}

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	//	public void test00_parse_relative_02() {
	//		Data dfile = Data.factory("dir/sub/file.txt");
	//		Data durl = Data.factory("http://www.ensembl.org");
	//		Data djoin = durl.join(dfile);
	//
	//		assertTrue(!dfile.isRelative());
	//		assertTrue(durl.isRelative());
	//		assertTrue(dfile instanceof DataFile);
	//		assertTrue(durl instanceof DataHttp);
	//		assertTrue(djoin instanceof DataHttp);
	//		assertEquals(djoin.getUri().toString(), "http://www.ensembl.org/dir/sub/file.txt");
	//		assertEquals(djoin.getPath(), "/dir/sub/file.txt");
	//		assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
	//		assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	//	}
	//
	//	public void test00_parse_relative_03() {
	//		Data dfile = Data.factory("/dir/sub/file.txt");
	//		Data durl = Data.factory("s3://my_bucket");
	//		Data djoin = durl.join(dfile);
	//
	//		assertTrue(!dfile.isRelative());
	//		assertTrue(durl.isRelative());
	//		assertTrue(dfile instanceof DataFile);
	//		assertTrue(durl instanceof DataHttp);
	//		assertTrue(djoin instanceof DataHttp);
	//		assertEquals(djoin.getUri().toString(), "s3://my_bucket/dir/sub/file.txt");
	//		assertEquals(djoin.getPath(), "/dir/sub/file.txt");
	//		assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
	//		assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	//	}
	//
	//	@Test
	//	public void test00_parseFile() {
	//		Data dfile = Data.factory("/dir/sub/file.txt");
	//		assertTrue(!dfile.isRelative());
	//		assertEquals("/dir/sub/file.txt", dfile.getPath());
	//		assertEquals("/dir/sub/file.txt", dfile.getAbsolutePath());
	//		assertEquals("/dir/sub/file.txt", dfile.getCanonicalPath());
	//	}
	//
	//	@Test
	//	public void test00_parseFile_relative() {
	//		Data dfile = Data.factory("./dir/sub/file.txt");
	//		Gpr.debug("dfile: " + dfile);
	//		assertTrue(dfile.isRelative());
	//		assertEquals("./dir/sub/file.txt", dfile.getPath());
	//	}
	//
	//	@Test
	//	public void test00_parseFile_relative_02() throws Exception {
	//		String path = "tmp.txt";
	//		Data dpath = Data.factory(path);
	//		if (verbose) Gpr.debug("Path: " + dpath.getPath());
	//		Assert.assertTrue(dpath instanceof DataFile);
	//		assertTrue(dpath.isRelative());
	//
	//		// Absolute path
	//		File fpathAbs = new File(new File("").getAbsoluteFile(), path);
	//		Assert.assertEquals(fpathAbs.getAbsolutePath(), dpath.getAbsolutePath());
	//
	//		// Canonical path
	//		File fpathCan = new File(new File(".").getCanonicalFile(), path);
	//		Assert.assertEquals(fpathCan.getCanonicalPath(), dpath.getCanonicalPath());
	//
	//		Gpr.toFile(dpath.getAbsolutePath(), "test");
	//		Assert.assertTrue(dpath.isFile());
	//		Assert.assertFalse(dpath.isDirectory());
	//	}
	//
	//	@Test
	//	public void test00_parseFile_relative_03() throws Exception {
	//		String path = "./tmp.txt";
	//		Data dpath = Data.factory(path);
	//		if (verbose) Gpr.debug("Path: " + dpath.getPath());
	//		Assert.assertTrue(dpath instanceof DataFile);
	//		assertTrue(dpath.isRelative());
	//
	//		// Absolute path
	//		File fpathAbs = new File(new File("").getAbsoluteFile(), path);
	//		Assert.assertEquals(fpathAbs.getAbsolutePath(), dpath.getAbsolutePath());
	//
	//		// Canonical path
	//		File fpathCan = new File(new File(".").getCanonicalFile(), path);
	//		Assert.assertEquals(fpathCan.getCanonicalPath(), dpath.getCanonicalPath());
	//
	//		Gpr.toFile(dpath.getAbsolutePath(), "test");
	//		Assert.assertTrue(dpath.isFile());
	//		Assert.assertFalse(dpath.isDirectory());
	//	}
	//
	//	@Test
	//	public void test15_S3() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		String expectedOutput = "" //
	//				+ "baseName       : \n" //
	//				+ "baseName('txt'): \n" //
	//				+ "canRead        : true\n" //
	//				+ "canWrite       : true\n" //
	//				+ "dirName        : s3://pcingola.bds/test_dir\n" //
	//				+ "extName        : bds/test_dir/\n" //
	//				+ "exists         : true\n" //
	//				+ "isDir          : true\n" //
	//				+ "isFile         : false\n" //
	//				+ "path           : s3://pcingola.bds/test_dir/\n" //
	//				+ "pathName       : s3://pcingola.bds/test_dir\n" //
	//				+ "removeExt      : s3://pcingola\n" //
	//				+ "dirPath        : [s3://pcingola.bds/test_dir/z1.txt, s3://pcingola.bds/test_dir/z2.txt]\n" //
	//				+ "dir            : [z1.txt, z2.txt]\n" //
	//		;
	//
	//		runAndCheckStdout("test/remote_15.bds", expectedOutput);
	//	}
	//
	//	@Test
	//	public void test26_ftp_dir() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/remote_26.bds", "dHasReadme", "true");
	//	}
	//
	//	@Test
	//	public void test27_ftp_dir() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/remote_27.bds", "dd", "[Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	//	}
	//
	//	@Test
	//	public void test28_ftp_dirPath() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/remote_28.bds", "dd", "[ftp://ftp.ensembl.org/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	//	}
	//
	//	@Test
	//	public void test29_http_dir() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/remote_29.bds", "dd", "[http://ftp.ensemblorg.ebi.ac.uk/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	//	}
	//
	//	@Test
	//	public void test31_s3_dir() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/remote_31.bds", "dd", "[s3://...]");
	//	}
	//
	//	@Test
	//	public void test32_s3_dirPath() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/remote_32.bds", "dd", "[s3://...]");
	//	}

}
