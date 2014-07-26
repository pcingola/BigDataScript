package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

/**
 * A native function declaration 
 * 
 * @author pcingola
 */
public abstract class FunctionNative extends FunctionDeclaration {

	public FunctionNative() {
		super(null, null);
		initFunction();
	}

	/**
	 * Add method to global scope
	 */
	protected void addNativeFunctionToScope() {
		Scope classScope = Scope.getGlobalScope();
		ScopeSymbol ssym = new ScopeSymbol(functionName, getType());
		classScope.add(ssym);
	}

	/**
	 * Convert an array to a list
	 * @param strings
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ArrayList array2list(Object objects[]) {
		ArrayList list = new ArrayList(objects.length);
		Collections.addAll(list, objects);
		return list;
	}

	/**
	 * Convert an array to a list and sort the list
	 * @param strings
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ArrayList array2listSorted(Object objects[]) {
		ArrayList list = new ArrayList(objects.length);
		Collections.addAll(list, objects);
		Collections.sort(list);
		return list;
	}

	/**
	 * Initialize method parameters (if possible)
	 */
	protected abstract void initFunction();

	@Override
	public RunState runFunction(BigDataScriptThread csThread) {
		// Run method
		Object result = runFunctionNative(csThread);

		// Set result in scope
		csThread.setReturnValue(result);

		return RunState.OK;
	}

	/**
	 * Run a method
	 * @param csThread
	 * @param objThis
	 */
	protected abstract Object runFunctionNative(BigDataScriptThread csThread);

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		// Nothing to do: Native methods are not serialized
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		// Nothing to do: Native methods are not serialized
		return "";
	}

}
