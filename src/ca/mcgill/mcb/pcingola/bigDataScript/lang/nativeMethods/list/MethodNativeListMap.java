package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeFunc;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMap extends MethodNativeList {

	public Type returnBaseType; // This is public because otherwise reflections in type checking won't be able to access it

	public MethodNativeListMap(Type baseType) {
		super(null);
		if (baseType != null) initMethod(baseType, baseType, "map");
	}

	public MethodNativeListMap(Type baseType, Type returnBaseType, String methodName) {
		super(null);
		initMethod(baseType, returnBaseType, methodName);
	}

	/**
	 * Find a function
	 * TODO: Move this to Scope?
	 */
	protected FunctionDeclaration findFunction(BigDataScriptThread bdsThread, String fname) {
		FunctionDeclaration function = (FunctionDeclaration) bdsThread.getObject("f");

		// Type checking
		// TODO: This is awful to say the least!
		//       Type checking should be done at compile time, not here
		//       (this is supposed to be a statically typed language)
		if (!function.getReturnType().canCast(returnBaseType)) bdsThread.fatalError(this, "Cannot cast " + function.getReturnType() + " to " + returnBaseType);

		// TODO: Check that function should only have one argument
		// TODO: Check List's elements should be 'castable' to function's argument

		return function;
	}

	@Override
	protected void initMethod(Type baseType) {
		throw new RuntimeException("This method should not be invoked!");
	}

	protected void initMethod(Type baseType, Type returnBaseType, String functionName) {
		this.functionName = functionName;
		classType = TypeList.get(baseType);
		this.returnBaseType = returnBaseType;
		returnType = TypeList.get(returnBaseType);

		TypeFunc typeFunc = TypeFunc.get(Parameters.get(baseType, ""), Type.ANY);
		String argNames[] = { "this", "f" };
		Type argTypes[] = { classType, typeFunc };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BigDataScriptThread bdsThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;

		// Get function
		FunctionDeclaration function = findFunction(bdsThread, "f");

		// Map
		ArrayList res = new ArrayList();
		Object values[] = new Object[1];
		for (Object o : list) {
			values[0] = o;
			Object r = function.apply(bdsThread, values); // Get result
			Object ret = returnBaseType.cast(r); // Cast to list's type
			res.add(ret); // Add to list
		}

		return res;
	}
}
