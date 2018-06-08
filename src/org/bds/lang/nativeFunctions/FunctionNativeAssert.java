package org.bds.lang.nativeFunctions;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.run.BdsThread;

/**
 * A native 'assert' function
 * The only difference is how we handle error conditions (we show file & line info)
 *
 * @author pcingola
 */
public abstract class FunctionNativeAssert extends FunctionNative {

	private static final long serialVersionUID = 6415943745404236449L;

	public FunctionNativeAssert() {
		super();
	}

	@Override
	public Value runFunction(BdsThread bdsThread) {
		try {
			// Run function
			runFunctionNative(bdsThread);
			return new ValueBool(true);
		} catch (Throwable t) {
			// Exception caused by failed assertion
			if (bdsThread.isDebug() | t.getMessage() == null) t.printStackTrace();
			bdsThread.assertionFailed(this, t.getMessage());
			return new ValueBool(false);
		}
	}

}
