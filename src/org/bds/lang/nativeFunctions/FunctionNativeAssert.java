package org.bds.lang.nativeFunctions;

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

	//	@Override
	//	public void runFunction(BdsThread bdsThread) {
	//		try {
	//			// Run function
	//			Value result = runFunctionNativeValue(bdsThread);
	//			bdsThread.setReturnValue(result); // Set result in scope
	//		} catch (Throwable t) {
	//			// Exception caused by failed assertion
	//			if (bdsThread.isDebug()) t.printStackTrace();
	//			bdsThread.assertionFailed(this, t.getMessage());
	//		}
	//	}

}
