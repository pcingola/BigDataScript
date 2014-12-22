package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods;

import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.MethodDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

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
		ScopeSymbol ssym = new ScopeSymbol(functionName, getType());
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
	public void runFunction(BigDataScriptThread bdsThread) {
		// Get object 'this'
		ScopeSymbol symThis = bdsThread.getScope().getSymbol(THIS_KEYWORD);
		Object objThis = symThis.getValue();

		// Run method
		try {
			Object result = runMethodNative(bdsThread, objThis);
			bdsThread.setReturnValue(result); // Set result in scope
		} catch (Throwable t) {
			if (bdsThread.isVerbose()) t.printStackTrace();
			bdsThread.fatalError(this, t.getMessage());
		}
	}

	/**
	 * Run a method
	 */
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		throw new RuntimeException("Unimplemented method for class " + this.getClass().getSimpleName());
	}

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
