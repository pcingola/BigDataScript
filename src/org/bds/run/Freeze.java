package org.bds.run;

/**
 * Global freeze state
 *
 * @author pcingola
 */
public class Freeze {

	public static boolean freeze;

	public static boolean isFreeze() {
		return freeze;
	}

	public static void setFreeze(boolean freeze) {
		Freeze.freeze = freeze;
	}

}
