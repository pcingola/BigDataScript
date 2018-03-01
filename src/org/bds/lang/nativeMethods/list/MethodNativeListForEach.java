package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Filter elements form a list by applying a function that returns a 'bool'
 *
 * @author pcingola
 */
public class MethodNativeListForEach extends MethodNativeList {

	public MethodNativeListForEach(TypeList listType) {
		super(listType);
	}

	/**
	 * Find a function
	 * TODO: Move this to Scope?
	 */
	protected FunctionDeclaration findFunction(BdsThread bdsThread, String fname) {
		FunctionDeclaration function = (FunctionDeclaration) bdsThread.getObject("f");

		// Type checking
		// TODO: This is awful to say the least!
		//       Type checking should be done at compile time, not here
		//       (this is supposed to be a statically typed language)
		//		if (!function.getReturnType().canCast(Type.BOOL)) bdsThread.fatalError(this, "Cannot cast " + function.getReturnType() + " to " + Type.BOOL);

		// TODO: Check that function should only have one argument
		// TODO: Check List's elements should be 'castable' to function's argument

		return function;
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "foreach";
		classType = TypeList.get(baseType);
		returnType = TypeList.get(baseType);;

		TypeFunction typeFunc = TypeFunction.get(Parameters.get(baseType, ""), Types.BOOL);
		String argNames[] = { "this", "f" };
		Type argTypes[] = { classType, typeFunc };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;

		// Get function
		FunctionDeclaration function = findFunction(bdsThread, "f");

		// !!! TODO: IMPLEMENT!
		//		for (Object val : list)
		//			function.apply(bdsThread, val);

		return list;
	}
}
