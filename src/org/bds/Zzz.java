package org.bds;

public class Zzz {

	public static boolean debug = true;
	public static final String USER_DIR = "user.dir";

	public static boolean verbose = true;

	public static void main(String[] args) throws Exception {
		System.out.println("Start");
		Zzz zzz = new Zzz();
		System.out.println("f(): " + zzz.f());
		System.out.println("End");
	}

	int f() {
		try {
			System.out.println("try");
			if (Math.random() < 2.0) throw new Exception("except");
			return 1;
		} catch (Exception e) {
			System.out.println("catch");
			return 2;
		} finally {
			System.out.println("finally");
			return 3;
		}

	}
}
