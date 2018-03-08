package org.bds.scope;

import org.bds.lang.type.Type;
import org.bds.util.Gpr;

/**
 * A symbol in the scope
 *
 * @author pcingola
 */
public class ScopeSymbolFunction extends ScopeSymbol {

	public ScopeSymbolFunction(String name, Type type, Object value) {
		super(name, type, value);
		Gpr.debug("!!! CREATING SymbolFunc: " + name);
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

}
