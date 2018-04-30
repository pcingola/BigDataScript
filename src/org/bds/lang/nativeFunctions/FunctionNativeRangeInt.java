package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Native function "range". Return a random list of int
 *
 * @author pcingola
 */
public class FunctionNativeRangeInt extends FunctionNative {

	private static final long serialVersionUID = 592085429755974826L;

	public FunctionNativeRangeInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "range";
		returnType = TypeList.get(Types.INT);

		String argNames[] = { "min", "max" };
		Type argTypes[] = { Types.INT, Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	public Value runFunction(BdsThread bdsThread) {
		long min = bdsThread.getInt("min");
		long max = bdsThread.getInt("max");

		ValueList list = new ValueList(returnType);
		for (long i = min; i <= max; i++)
			list.add(new ValueInt(i));

		return list;
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		throw new RuntimeException("This method should not be used!");
	}
}
