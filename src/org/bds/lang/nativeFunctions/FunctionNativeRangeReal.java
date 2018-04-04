package org.bds.lang.nativeFunctions;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

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
		returnType = TypeList.get(Types.REAL);

		String argNames[] = { "min", "max", "step" };
		Type argTypes[] = { Types.REAL, Types.REAL, Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
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
