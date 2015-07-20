package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Native function "range". Return a random list of int
 *
 * @author pcingola
 */
public class FunctionNativeRangeReal extends FunctionNative {

	public FunctionNativeRangeReal() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "range";
		returnType = TypeList.get(Type.REAL);

		String argNames[] = { "min", "max", "step" };
		Type argTypes[] = { Type.REAL, Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		double min = bdsThread.getReal("min");
		double max = bdsThread.getReal("max");
		double step = bdsThread.getReal("step");

		ArrayList<Double> list = new ArrayList<>();
		for (double d = min; d <= max; d += step)
			list.add(d);

		return list;
	}
}
