package org.bds.lang.nativeFunctions;

import java.util.ArrayList;
import java.util.Collections;

import org.bds.compile.CompilerMessages;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.GlobalScope;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;
import org.bds.serialize.BdsSerializer;

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
		Scope classScope = GlobalScope.get();
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
	public void runFunction(BdsThread bdsThread) {
		try {
			// Run function
			Value result = runFunctionNativeValue(bdsThread);
			bdsThread.setReturnValue(result); // Set result in scope
		} catch (Throwable t) {
			if (bdsThread.isVerbose()) t.printStackTrace();
			bdsThread.fatalError(this, t.getMessage());
		}
	}

	/**
	 * Run a native method
	 */
	protected abstract Object runFunctionNative(BdsThread bdsThread);

	/**
	 * Run a native method, wrap result in a 'Value'
	 */
	protected Value runFunctionNativeValue(BdsThread bdsThread) {
		Object ret = runFunctionNative(bdsThread);
		return Value.factory(ret);
	}

	@Override
	public void serializeParse(BdsSerializer serializer) {
		// Nothing to do: Native methods are not serialized
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
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
