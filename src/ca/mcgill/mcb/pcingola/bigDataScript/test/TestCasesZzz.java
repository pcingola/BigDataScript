package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test29() {
		runAndCheck("test/run_29.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test30() {
		runAndCheck("test/run_30.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test31() {
		Timer timer = new Timer();
		timer.start();
		runAndCheck("test/run_31.bds", "events", "[runnning, kill, done]");
		Assert.assertTrue(timer.elapsed() < 1 * 1000); // We should finish in much less than 1 secs (the program waits 60secs)
	}

	@Test
	public void test32() {
		runAndCheck("test/run_32.bds", "out", "Hi\n");
	}

	@Test
	public void test33() {
		runAndCheck("test/run_33.bds", "err", "Hi\n");
	}

	/**
	 *  Test cases to create
	 */

	//	@Test
	//	public void test01_parse_URLs() {
	//		// Parse URLs: file, http, https, s3, ...
	//
	//		// Parse: data.getPath()
	//
	//		// Parse: data.getCanonicalPath()
	//	}
	//
	//	@Test
	//	public void test01_parse_URLs_path_canonicalPath() {
	//		// Parse: dir
	//
	//		// Parse: dirPath
	//	}
	//
	//	@Test
	//	public void test02_download_URLs() {
	//	}
	//
	//	@Test
	//	public void test03_task_URL() {
	//	
	//	// Program 1:
	//	task cat $in > $out
	//	
	//	// Program 2:
	//	task (....) sys cat $in > $out
	//	
	//	// Program 3:
	//	task ( out <- in ) {
	//		sys cat $in > $out
	//		sys cat $in > $out
	//	}
	//
	//	// Program 4:
	//	task cat "$in" > $out
	//	
	//	// Program 5:
	//	task cat '$in' > $out
	//	
	//	// Program 6:
	//	task cat "$in\"this is not changed" > $out
	//	
	//	// Program 7:
	//	task ( out <- [in, in2] ) {
	//		sys cat $in > $out
	//	}
	//
	//	// Program 8:
	//  inList := [$in, $in2]
	//	task ( out <- inList ) {
	//		sys cat $in > $out
	//	}
	//
	//	// Program 9:
	//	task ( 'file.txt' <- 'http://www.google.com/index.html' ) {
	//		sys cat http://www.google.com/index.html > file.txt
	//	}
	//
	//	}
	//
	//	@Test
	//	public void test04_task_URL_download() {
	//	}
	//
	//	@Test
	//	public void test05_task_URL_upload() {
	//	}

}
