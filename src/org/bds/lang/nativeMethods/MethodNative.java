package org.bds.lang.nativeMethods;

import org.bds.compile.CompilerMessages;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A native method declaration
 *
 * @author pcingola
 */
public abstract class MethodNative extends MethodDeclaration {

	private static final long serialVersionUID = 7663900761574026674L;

	public MethodNative(Type classType) {
		super(null, null);
		this.classType = classType;
		initMethod();
		parameterNames = parameterNames();
	}

	/**
	 * Add method to class scope
	 */
	protected void addNativeMethodToClassScope() {
		SymbolTable symTab = classType.getSymbolTable();
		symTab.addFunction(this);
	}

	/**
	 * Convert an array to a list
	 */
	protected ValueList arrayString2valuelist(String strs[]) {
		TypeList typeList = TypeList.get(Types.STRING);
		ValueList vlist = new ValueList(typeList);

		if (strs == null) return vlist;

		for (String s : strs)
			vlist.add(new ValueString(s));

		return vlist;
	}

	/**
	 * Initialize method parameters (if possible)
	 */
	protected abstract void initMethod();

	@Override
	public boolean isNative() {
		return true;
	}

	/**
	 * Run a method
	 */
	public abstract Value runMethod(BdsThread bdsThread, Value vThis);

	@Override
	public void typeChecking(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
