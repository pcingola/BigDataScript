package ca.mcgill.mcb.pcingola.bigDataScript;


public class Hjg {
	
	public static void main(String[] args) throws Exception {
		test1();
	}

	private static void test1() {
		BigDataScript bds = new BigDataScript(new String[]{"tmp/t.bds"});
		bds.run();
		System.out.println("done:" + bds.getCompilerMessages() + "==");
	}
}
