package org.bds.lang.nativeClasses.exception;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * Constructor for native class Exception
 * @author pcingola
 */
public class MethodConstructor extends MethodNative {

	private static final long serialVersionUID = -8937479079704278314L;

	public MethodConstructor(Type classType) {
		super(classType);
	}

	@Override
	protected void initMethod() {
		TypeClass ct = (TypeClass) classType;
		functionName = ct.getClassName();
		returnType = classType;

		String argNames[] = { "this", "message" };
		Type argTypes[] = { classType, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vThis) {
		return vThis; // Default constructor doesn't do anything, just return the newly created object
	}

}
