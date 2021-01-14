package org.bds.lang.nativeFunctions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Loads all classes used for native functions library
 *
 * @author pcingola
 *
 */
public class NativeLibraryFunctions {

	@SuppressWarnings("rawtypes")
	public static Class classes[] = { //
			FunctionNativeAssertBool.class //
			, FunctionNativeAssertBoolNoMsg.class //
			, FunctionNativeAssertInt.class //
			, FunctionNativeAssertIntNoMsg.class //
			, FunctionNativeAssertString.class //
			, FunctionNativeAssertStringNoMsg.class //
			, FunctionNative_clone_bool.class //
			, FunctionNative_clone_int.class //
			, FunctionNative_clone_real.class //
			, FunctionNative_clone_string.class //
			, FunctionNativeConfig.class //
			, FunctionNativeConfigOri.class //
			, FunctionNativeGetModulePath.class //
			, FunctionNativeGetVar.class //
			, FunctionNativeGetVarDefault.class //
			, FunctionNativeHasVar.class //
			, FunctionNativeLog.class //
			, FunctionNativeLogd.class //
			, FunctionNativeMinInt.class //
			, FunctionNativeMaxInt.class //
			, FunctionNativeMinReal.class //
			, FunctionNativeMaxReal.class //
			, FunctionNativePrint.class //
			, FunctionNativePrintErr.class //
			, FunctionNativePrintHelp.class //
			, FunctionNativeRand.class //
			, FunctionNativeRandInt.class //
			, FunctionNativeRandIntRange.class //
			, FunctionNativeRandSeed.class //
			, FunctionNativeRangeInt.class //
			, FunctionNativeRangeIntStep.class //
			, FunctionNativeRangeReal.class //
			, FunctionNativeSleep.class //
			, FunctionNativeSleepReal.class //
			, FunctionNativeTasksDone.class //
			, FunctionNativeTasksRunning.class //
			, FunctionNativeTasksToRun.class //
			, FunctionNativeTime.class //
			, FunctionNativeToIntFromBool.class //
			, FunctionNativeToIntFromReal.class //
			, FunctionNativeType.class //
			//
			// Math functions
			, org.bds.lang.nativeFunctions.math.FunctionNative_abs_int.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_getExponent_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_round_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_abs_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_acos_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_asin_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_atan_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_atan2_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_cbrt_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_ceil_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_copySign_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_cos_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_cosh_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_exp_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_expm1_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_floor_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_hypot_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_IEEEremainder_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_log_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_log10_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_log1p_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_max_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_min_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_nextAfter_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_nextUp_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_pow_real_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_rint_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_scalb_real_int.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_signum_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_sin_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_sinh_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_sqrt_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_tan_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_tanh_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_toDegrees_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_toRadians_real.class //
			, org.bds.lang.nativeFunctions.math.FunctionNative_ulp_real.class //
	};

	ArrayList<FunctionNative> functions;

	@SuppressWarnings("rawtypes")
	public NativeLibraryFunctions() {
		try {
			functions = new ArrayList<>();
			for (Class<FunctionNative> c : classes) {
				Constructor<FunctionNative> constructor = c.getDeclaredConstructor();
				functions.add(constructor.newInstance());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error creating native library", e);
		}
	}

	public int size() {
		return functions.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + ":\n");
		for (FunctionNative m : functions)
			sb.append("\t" + m.getClass().getSimpleName() + "\n");
		return sb.toString();
	}

}
