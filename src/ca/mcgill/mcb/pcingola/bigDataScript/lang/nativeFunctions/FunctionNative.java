package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
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
		ScopeSymbol ssym = new ScopeSymbol(functionName, getType(), this);
		classScope.add(ssym);
	}

	/**
	 * Convert an array to a list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ArrayList array2list(Object objects[]) {
		ArrayList list = new ArrayList(objects.length);
		Collections.addAll(list, objects);
		return list;
	}

	/**
	 * Convert an array to a list and sort the list
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
	public boolean isNative() {
		return true;
	}

	@Override
	public void runFunction(BigDataScriptThread bdsThread) {
		try {
			// Run function
			Object result = runFunctionNative(bdsThread);
			bdsThread.setReturnValue(result); // Set result in scope
		} catch (Throwable t) {
			if (bdsThread.isVerbose()) t.printStackTrace();
			bdsThread.fatalError(this, t.getMessage());
		}

	}

	/**
	 * Run a method
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("native:" + this.getClass().getCanonicalName());
		return sb.toString();
	}

	@Override
	public void typeChecking(Scope scope, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
