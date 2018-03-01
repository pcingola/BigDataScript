package org.bds.lang.nativeMethods.string;

import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Types;

/**
 * A native method declaration for strings
 *
 * @author pcingola
 */
public abstract class MethodNativeString extends MethodNative {

	public MethodNativeString() {
		super(Types.STRING);
	}

}
