package org.bds.lang.nativeMethods.string;

import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * A native method declaration for strings
 *
 * @author pcingola
 */
public abstract class MethodNativeString extends MethodNative {

	private static final long serialVersionUID = 6416138160794937233L;

	public MethodNativeString() {
		super(Types.STRING);
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vThis) {
		Object ret = runMethodNative(bdsThread, vThis.asString());
		return Value.factory(ret);
	}

	protected abstract Object runMethodNative(BdsThread bdsThread, Object objThis);

}
