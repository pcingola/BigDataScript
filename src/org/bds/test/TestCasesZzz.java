package org.bds.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataFile;
import org.bds.data.DataHttp;
import org.bds.util.Gpr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

	public void test00_parse_relative_01() {
		Data dfile = Data.factory("/dir/sub/file.txt");
		Data durl = Data.factory("http://www.ensembl.org");
		Data djoin = durl.join(dfile);

		assertTrue(!dfile.isRelative());
		assertTrue(durl.isRelative());
		assertTrue(dfile instanceof DataFile);
		assertTrue(durl instanceof DataHttp);
		assertTrue(djoin instanceof DataHttp);
		assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	}

	public void test00_parse_relative_02() {
		Data dfile = Data.factory("dir/sub/file.txt");
		Data durl = Data.factory("http://www.ensembl.org");
		Data djoin = durl.join(dfile);

		assertTrue(!dfile.isRelative());
		assertTrue(durl.isRelative());
		assertTrue(dfile instanceof DataFile);
		assertTrue(durl instanceof DataHttp);
		assertTrue(djoin instanceof DataHttp);
		assertEquals(djoin.getUri().toString(), "http://www.ensembl.org/dir/sub/file.txt");
		assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	}

	public void test00_parse_relative_03() {
		Data dfile = Data.factory("/dir/sub/file.txt");
		Data durl = Data.factory("s3://my_bucket");
		Data djoin = durl.join(dfile);

		assertTrue(!dfile.isRelative());
		assertTrue(durl.isRelative());
		assertTrue(dfile instanceof DataFile);
		assertTrue(durl instanceof DataHttp);
		assertTrue(djoin instanceof DataHttp);
		assertEquals(djoin.getUri().toString(), "s3://my_bucket/dir/sub/file.txt");
		assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
	}

	@Test
	public void test00_parseFile() {
		Data dfile = Data.factory("/dir/sub/file.txt");
		assertTrue(!dfile.isRelative());
		assertEquals("/dir/sub/file.txt", dfile.getPath());
		assertEquals("/dir/sub/file.txt", dfile.getAbsolutePath());
		assertEquals("/dir/sub/file.txt", dfile.getCanonicalPath());
	}

	@Test
	public void test00_parseFile_relative() {
		Data dfile = Data.factory("./dir/sub/file.txt");
		Gpr.debug("dfile: " + dfile);
		assertTrue(dfile.isRelative());
		assertEquals("./dir/sub/file.txt", dfile.getPath());
	}

	@Test
	public void test00_parseFile_relative_02() throws Exception {
		String path = "tmp.txt";
		Data dpath = Data.factory(path);
		if (verbose) Gpr.debug("Path: " + dpath.getPath());
		Assert.assertTrue(dpath instanceof DataFile);
		assertTrue(dpath.isRelative());

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

	@Test
	public void test00_parseFile_relative_03() throws Exception {
		String path = "./tmp.txt";
		Data dpath = Data.factory(path);
		if (verbose) Gpr.debug("Path: " + dpath.getPath());
		Assert.assertTrue(dpath instanceof DataFile);
		assertTrue(dpath.isRelative());

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
