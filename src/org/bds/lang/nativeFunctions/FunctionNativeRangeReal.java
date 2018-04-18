package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueReal;
import org.bds.run.BdsThread;

/**
 * Native function "range". Return a random list of int
 *
 * @author pcingola
 */
public class FunctionNativeRangeReal extends FunctionNative {

	private static final long serialVersionUID = 5104518981465556948L;

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
	public Value runFunction(BdsThread bdsThread) {
		double min = bdsThread.getReal("min");
		double max = bdsThread.getReal("max");
		double step = bdsThread.getReal("step");

		ValueList list = new ValueList(returnType);
		for (double d = min; d <= max; d += step)
			list.add(new ValueReal(d));

		return list;
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		throw new RuntimeException("This method should not be used!");
	}

}
