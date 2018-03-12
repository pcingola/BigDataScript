package org.bds.lang.nativeMethods;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * A native method to create objects (default constructor) for all classes
 *
 * @author pcingola
 */
public class MethodNativeDefaultConstructor extends MethodNative {

	public MethodNativeDefaultConstructor(Type classType) {
		super(classType);
	}

	@Override
	public void runFunction(BdsThread bdsThread) {
		// Run method
		try {
			Value result = classType.newValue();
			bdsThread.setReturnValue(result); // Set result in scope
		} catch (Throwable t) {
			if (bdsThread.isVerbose()) t.printStackTrace();
			bdsThread.fatalError(this, t.getMessage());
		}
	}

	@Override
	protected void initMethod() {
		TypeClass ct = (TypeClass) classType;
		functionName = ct.getClassName();
		returnType = classType;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

}
