package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeFunc;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMap extends MethodNativeList {

	public MethodNativeListMap(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "map";
		classType = TypeList.get(baseType);
		returnType = TypeList.get(baseType);

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
		FunctionDeclaration function = (FunctionDeclaration) bdsThread.getObject("f");
		Gpr.debug("Function: " + function);

		// Map
		ArrayList res = new ArrayList();
		Object values[] = new Object[1];
		for (Object o : list) {
			values[0] = o;
			Object r = function.apply(bdsThread, values);
			res.add(r);
		}

		return res;
	}
}
