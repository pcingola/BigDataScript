package org.bigDataScript;

import java.io.File;
import java.util.Enumeration;

import org.bigDataScript.util.Gpr;

public class Zzz {

	public static boolean debug = true;
	public static boolean verbose = true;

	public static final String USER_DIR = "user.dir";

	public static void main(String[] args) throws Exception {
		System.out.println("Start");

		for (Enumeration<Object> keys = System.getProperties().keys(); keys.hasMoreElements();) {
			System.out.println(keys.nextElement());
		}

		System.out.println((new File(".")).getAbsolutePath());
		System.setProperty(USER_DIR, Gpr.HOME);
		System.out.println((new File(".")).getAbsolutePath());

		//		int a = 1;
		//		int b = 2;
		//		System.out.println((a == 1 || b == 2) + "\n"); // OK
		//
		//		//		System.out.println(a == 1 || b == 2 + "\n"); // Incompatible operand types int and String
		//
		//		//		System.out.println((a == 1) || (b == 2) + "\n"); // Operator '||' undefined for types boolean, String
		//		//		System.out.println("Done");
		//
		//		boolean aa = true;
		//		boolean bb = false;
		//		System.out.println((aa == true) || (bb == false) + "\n");
	}
}
