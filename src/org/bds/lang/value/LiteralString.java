package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;
import org.bds.util.GprString;

/**
 * Expression 'Literal'
 *
 * @author pcingola
 */
public class LiteralString extends Literal {

	private static final long serialVersionUID = -2844811652533999173L;

	InterpolateVars interpolateVars;

	public LiteralString(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type getReturnType() {
		return Types.STRING;
	}

	@Override
	protected void initialize() {
		super.initialize();
		value = new ValueString();
	}

	@Override
	protected void parse(ParseTree tree) {
		String valueStr = tree.getChild(0).getText();

		if (valueStr.charAt(0) == '\'' && valueStr.charAt(valueStr.length() - 1) == '\'') {
			// Remove quotes: No un-escaping, no interpolation
			valueStr = valueStr.substring(1, valueStr.length() - 1);
			valueStr = GprString.escape(valueStr);
			value = new ValueString(valueStr);
		} else {
			// Remove quotes and interpolate string
			valueStr = valueStr.substring(1, valueStr.length() - 1);
			valueStr = GprString.escapeMultiline(valueStr);
			setValueInterpolate(valueStr);
		}
	}

	@Override
	protected ValueString parseValue(ParseTree tree) {
		return new ValueString(tree.getChild(0).getText());
	}

	/**
	 * Sets literal map and finds interpolated variables
	 */
	public void setValueInterpolate(String valueStr) {
		value = new ValueString(valueStr);

		// Parse interpolated vars
		interpolateVars = new InterpolateVars(this, null);
		if (!interpolateVars.parse(valueStr)) {
			interpolateVars = null; // Nothing found? don't bother to keep the object
		}
	}

	@Override
	public String toAsm() {
		if (value == null) return "pushs ''\n";
		if (interpolateVars == null) return "pushs '" + value.asString() + "'\n";
		return interpolateVars.toAsm();
	}

	@Override
	public String toString() {
		return "\"" + value.toString() + "\"";
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (interpolateVars != null) interpolateVars.typeCheckNotNull(symtab, compilerMessages);
	}
}
