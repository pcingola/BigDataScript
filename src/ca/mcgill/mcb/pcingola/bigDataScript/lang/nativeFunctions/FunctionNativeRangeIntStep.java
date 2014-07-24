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
public class FunctionNativeRangeIntStep extends FunctionNative {

	public FunctionNativeRangeIntStep() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "range";
		returnType = TypeList.get(Type.INT);

		String argNames[] = { "min", "max", "step" };
		Type argTypes[] = { Type.INT, Type.INT, Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread bdsThread) {
		long min = bdsThread.getInt("min");
		long max = bdsThread.getInt("max");
		long step = bdsThread.getInt("step");

		ArrayList<Long> list = new ArrayList<>();
		for (long i = min; i <= max; i += step)
			list.add(i);

		return list;
	}
}
