package ca.mcgill.mcb.pcingola.bigDataScript;

public class Hgonzal {
	
	
	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
		BigDataScript bds = new BigDataScript(new String[]{"test/tmp/testinclude.bds"});
		boolean ok = bds.compile();
		System.out.println(ok);
	}
}
