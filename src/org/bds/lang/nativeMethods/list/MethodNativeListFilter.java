package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.ValueFunction;
import org.bds.run.BdsThread;

/**
 * Filter elements form a list by applying a function that returns a 'bool'
 *
 * @author pcingola
 */
public class MethodNativeListFilter extends MethodNativeList {

	public MethodNativeListFilter(TypeList listType) {
		super(listType);
	}

	/**
	 * Find a function
	 * TODO: Move this to Scope?
	 */
	protected FunctionDeclaration findFunction(BdsThread bdsThread, String fname) {
		ValueFunction vf = (ValueFunction) bdsThread.getValue("f");
		FunctionDeclaration function = (FunctionDeclaration) vf.get();

		// Type checking
		// TODO: This is awful to say the least!
		//       Type checking should be done at compile time, not here
		//       (this is supposed to be a statically typed language)
		if (!function.getReturnType().canCastTo(Types.BOOL)) bdsThread.fatalError(this, "Cannot cast " + function.getReturnType() + " to " + Types.BOOL);

		// TODO: Check that function should only have one argument
		// TODO: Check List's elements should be 'castable' to function's argument

		return function;
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "filter";
		classType = TypeList.get(baseType);
		returnType = TypeList.get(baseType);;

		// !!! TODO: This is broken
		//		TypeFunction typeFunc = TypeFunction.get(Parameters.get(baseType, ""), Types.BOOL);
		//		String argNames[] = { "this", "f" };
		//		Type argTypes[] = { classType, typeFunc };
		//		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		ArrayList newList = new ArrayList();

		// Get function
		FunctionDeclaration function = findFunction(bdsThread, "f");
		// !!! TODO: IMPLEMENT
		//		for (Object val : list) {
		//			Value ret = function.apply(bdsThread, val);
		//			if (ret.asBool()) newList.add(val);
		//		}

		return newList;
	}
}
