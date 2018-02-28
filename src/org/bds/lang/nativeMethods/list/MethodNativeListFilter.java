package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunc;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

/**
 * Filter elements form a list by applying a function that returns a 'bool'
 *
 * @author pcingola
 */
public class MethodNativeListFilter extends MethodNativeList {

	public MethodNativeListFilter(Type baseType) {
		super(baseType);
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
		if (!function.getReturnType().canCast(Type.BOOL)) bdsThread.fatalError(this, "Cannot cast " + function.getReturnType() + " to " + Type.BOOL);

		// TODO: Check that function should only have one argument
		// TODO: Check List's elements should be 'castable' to function's argument

		return function;
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "filter";
		classType = TypeList.get(baseType);
		returnType = TypeList.get(baseType);;

		TypeFunc typeFunc = TypeFunc.get(Parameters.get(baseType, ""), Type.BOOL);
		String argNames[] = { "this", "f" };
		Type argTypes[] = { classType, typeFunc };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		ArrayList newList = new ArrayList();

		// Get function
		FunctionDeclaration function = findFunction(bdsThread, "f");

		for (Object val : list) {
			Object ret = function.apply(bdsThread, val);
			if ((Boolean) Type.BOOL.cast(ret)) newList.add(val);
		}

		return newList;
	}
}
