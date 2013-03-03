package ca.mcgill.mcb.pcingola.bigDataScript;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Zzz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "test/z.bds";

		File dir = new File("test");
		dir.listFiles();

		ArrayList<String> list = new ArrayList<String>();
		for (File f : dir.listFiles())
			try {
				list.add(f.getCanonicalPath());
			} catch (IOException e) {
				;
			}
		Collections.sort(list);
		System.out.println("END");
	}
}
