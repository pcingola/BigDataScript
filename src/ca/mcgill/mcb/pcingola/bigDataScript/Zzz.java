package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.InterpolateVars;

public class Zzz {

	public static void main(String[] args) throws Exception {
		String strs[] = { //
		//		"Hello" //
		//				, "Hello $" //
		//				, "Hello $\n" //
		//				, "Hello $i" //
		//				, "Hello $i$j$" //
		"l[1] : $l[1]" //
		//				, "m{'Helo'} : $m{'Helo'}" //
		//				, "m{'Helo'} : $m{$s}" //
		//				, "l[1] : '$l[1]'\n" //
		//				, "List with variable index: $l[$s]" //
		//				, "Map with variable index: $m{$s}" //
		//				, "Map with list and variable index: $m{$l[$s]}" //
		};

		for (String str : strs) {
			System.out.println("str:\t|" + str + "|");

			InterpolateVars iv = new InterpolateVars(null, null);
			iv.parse(str);
			System.out.println("String : " + str);
			System.out.println("Inter  : " + iv);
			System.out.println("\n\n");
		}
	}
}
