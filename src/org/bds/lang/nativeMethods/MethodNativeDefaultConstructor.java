package org.bds.lang.nativeMethods;

import org.bds.lang.Parameters;
import org.bds.lang.statement.VarDeclaration;
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
			// Create new value
			Value value = classType.newValue();

			// TODO: We should store new value in 'this' variable
			bdsThread.getScope().add("this", value);

			// Initialize fields using expressions			
			TypeClass tc = (TypeClass) classType;
			VarDeclaration vdecl[] = tc.getClassDeclaration().getVarDecl();
			for (VarDeclaration vd : vdecl)
				bdsThread.run(vd);

			bdsThread.setReturnValue(value); // Set result in scope
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
