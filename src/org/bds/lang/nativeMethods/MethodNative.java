package org.bds.lang.nativeMethods;

import java.util.ArrayList;
import java.util.Collections;

import org.bds.compile.CompilerMessages;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;
import org.bds.serialize.BdsSerializer;

/**
 * A native method declaration
 *
 * @author pcingola
 */
public abstract class MethodNative extends MethodDeclaration {

	public MethodNative() {
		super(null, null);
		initMethod();
	}

	/**
	 * Add method to class scope
	 */
	protected void addNativeMethodToClassScope() {
		Scope classScope = Scope.getClassScope(getClassType());
		ScopeSymbol ssym = new ScopeSymbol(functionName, getType(), this);
		classScope.add(ssym);
	}

	/**
	 * Convert an array to a list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ArrayList array2list(Object objects[]) {
		if (objects == null) return new ArrayList();
		ArrayList list = new ArrayList(objects.length);
		Collections.addAll(list, objects);
		return list;
	}

	/**
	 * Convert an array to a list and sort the list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ArrayList array2listSorted(Object objects[]) {
		if (objects == null) return new ArrayList();
		ArrayList list = new ArrayList(objects.length);
		Collections.addAll(list, objects);
		Collections.sort(list);
		return list;
	}

	/**
	 * Initialize method parameters (if possible)
	 */
	protected abstract void initMethod();

	@Override
	public boolean isNative() {
		return true;
	}

	@Override
	public void runFunction(BdsThread bdsThread) {
		// Get object 'this'
		ScopeSymbol symThis = bdsThread.getScope().getSymbol(THIS_KEYWORD);
		Object objThis = symThis.getValue().get();

		// Run method
		try {
			Value result = runMethodNativeValue(bdsThread, objThis);
			bdsThread.setReturnValue(result); // Set result in scope
		} catch (Throwable t) {
			if (bdsThread.isVerbose()) t.printStackTrace();
			bdsThread.fatalError(this, t.getMessage());
		}
	}

	/**
	 * Run a method
	 */
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("Unimplemented method for class " + this.getClass().getSimpleName());
	}

	/**
	 * Run native method and wrap result in a 'Value'
	 */
	protected Value runMethodNativeValue(BdsThread bdsThread, Object objThis) {
		Object res = runMethodNativeValue(bdsThread, objThis);
		return returnType.newValue(res);
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
	public void typeChecking(Scope scope, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
