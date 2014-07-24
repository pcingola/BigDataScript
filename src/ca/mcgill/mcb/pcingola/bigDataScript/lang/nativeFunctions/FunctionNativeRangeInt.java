package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Native function "range". Return a random list of int
 *
 * @author pcingola
 */
public class FunctionNativeRangeInt extends FunctionNative {

	public FunctionNativeRangeInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "range";
		returnType = TypeList.get(Type.INT);

		String argNames[] = { "min", "max" };
		Type argTypes[] = { Type.INT, Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread bdsThread) {
		long min = bdsThread.getInt("min");
		long max = bdsThread.getInt("max");

		ArrayList<Long> list = new ArrayList<>();
		for (long i = min; i <= max; i++)
			list.add(i);

		return list;
	}
}
