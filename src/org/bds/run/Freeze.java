package org.bds.run;

import org.bds.executioner.Executioners;

/**
 * Global freeze state
 *
 * @author pcingola
 */
public class Freeze {

	public static boolean freeze;

	public static void freeze() {
		setFreeze(true);
	}

	public static boolean isFreeze() {
		return freeze;
	}

	private static void setFreeze(boolean freeze) {
		// All executioners
		Executioners.getInstance().setFreeze(freeze);

		// All bdsThreads
		BdsThread bdsThread = BdsThreads.getInstance().get();
		BdsThread bdsThreadRoot = bdsThread.getRoot();
		bdsThreadRoot.setFreeze(freeze);
		Freeze.freeze = freeze;
	}

	public static void unfreeze() {
		setFreeze(false);
	}

}
