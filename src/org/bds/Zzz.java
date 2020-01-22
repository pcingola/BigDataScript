package org.bds;

class A {
	;
}

class B extends A {
	;
}

class Z {
	;
}

public class Zzz {

	public static boolean debug = true;
	public static final String USER_DIR = "user.dir";

	public static boolean verbose = true;

	public static void main(String[] args) {
		System.out.println("Start");
		try {
			System.out.println("try: before");
			System.exit(0);
			System.out.println("try: after");
		} finally {
			System.out.println("finally");
		}
		System.out.println("End");
	}

}