package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.InterpolateVars;

public class Zzz {

	public static void main(String[] args) throws Exception {

		// String str = "m{'Helo'} : $m{'Helo'}\n";
		// String str = "l[1] : $l[1]\n";
		String str = "Hello $i$j$";
		System.out.println("str:\t" + str);

		InterpolateVars iv = new InterpolateVars(null, null);
		iv.parse(str);
		System.out.println("String : " + str);
		System.out.println("Inter  : " + iv);
	}

}
