package org.bds;

public class Zzz {

	public static boolean debug = true;
	public static boolean verbose = true;

	public static final String USER_DIR = "user.dir";

	public static void main(String[] args) throws Exception {
		System.out.println("Start");

		int i = 3;
		switch (i) {
		default:
			System.out.println("DEFAULT");
		case 1:
			System.out.println("CASE 1");
			break;
		case 2:
			System.out.println("CASE 2");
			break;
		}

		System.out.println("End");
	}
}
