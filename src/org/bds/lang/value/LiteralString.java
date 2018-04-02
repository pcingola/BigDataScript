package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.InterpolateVars;
import org.bds.run.BdsThread;
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
	protected void initialize() {
		super.initialize();
		value = new ValueString();
	}

	@Override
	protected void parse(ParseTree tree) {
		String valueStr = parseValue(tree);

		if (valueStr.charAt(0) == '\'' && valueStr.charAt(valueStr.length() - 1) == '\'') {
			// Remove quotes: No un-escaping, no interpolation
			value.set(valueStr.substring(1, valueStr.length() - 1));
		} else {
			// Remove quotes and interpolate string
			valueStr = valueStr.substring(1, valueStr.length() - 1);
			setValueInterpolate(valueStr);
		}
	}

	@Override
	protected String parseValue(ParseTree tree) {
		return tree.getChild(0).getText();
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		if (interpolateVars == null) bdsThread.push(value); // No variable interpolation? => Literal
		else bdsThread.run(interpolateVars); // Variable interpolation
	}

	/**
	 * Sets literal map and finds interpolated variables
	 */
	public void setValueInterpolate(String valueStr) {
		value.set(valueStr);

		// Parse interpolated vars
		interpolateVars = new InterpolateVars(this, null);
		if (!interpolateVars.parse(valueStr)) {
			interpolateVars = null; // Nothing found? don't bother to keep the object
			value.set(InterpolateVars.unEscape(valueStr)); // Un-escape characters
		}
	}

	@Override
	public String toAsm() {
		if (value == null) return "pushs ''\n";
		if (interpolateVars != null) return "interp '" + value.asString() + "'\n";
		return "pushs '" + value.asString() + "'\n";
	}

	@Override
	public String toString() {
		return "\"" + GprString.escape(value.get().toString()) + "\"";
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (interpolateVars != null) interpolateVars.typeCheckNotNull(symtab, compilerMessages);
	}
}
