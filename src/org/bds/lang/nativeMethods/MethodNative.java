package org.bds.lang.nativeMethods;

import java.util.ArrayList;
import java.util.Collections;

import org.bds.compile.CompilerMessages;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A native method declaration
 *
 * @author pcingola
 */
public abstract class MethodNative extends MethodDeclaration {

	public MethodNative(Type classType) {
		super(null, null);
		this.classType = classType;
		initMethod();
	}

	/**
	 * Add method to class scope
	 */
	protected void addNativeMethodToClassScope() {
		SymbolTable symTab = classType.getSymbolTable();
		symTab.add(functionName, getType());
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
		Value symThis = bdsThread.getScope().getValue(ClassDeclaration.THIS);
		Object objThis = symThis.get();

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
		Object res = runMethodNative(bdsThread, objThis);
		return returnType.newValue(res);
	}

	@Override
	public void typeChecking(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
