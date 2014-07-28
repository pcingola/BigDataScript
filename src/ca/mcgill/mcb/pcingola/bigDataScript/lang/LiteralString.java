package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.GprString;

/**
 * Expression 'Literal'
 *
 * @author pcingola
 */
public class LiteralString extends Literal {

	String value; // If it is a simple literal, we use this
	InterpolateVars interpolateVars;

	public LiteralString(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Object eval(BigDataScriptThread bdsThread) {
		if (interpolateVars == null) return value; // No variable interpolation? => Literal
		return interpolateVars.eval(bdsThread); // Variable interpolation
	}

	@Override
	protected void parse(ParseTree tree) {
		String valueStr = tree.getChild(0).getText();

		if (valueStr.charAt(0) == '\'' && valueStr.charAt(valueStr.length() - 1) == '\'') {
			// Remove quotes: No unescaping, no interpolation
			value = valueStr.substring(1, valueStr.length() - 1);
		} else {
			// Remove quotes and interpolate string
			valueStr = valueStr.substring(1, valueStr.length() - 1);
			setValueInterpolate(valueStr);
		}
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = Type.STRING;
		return returnType;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		super.serializeParse(serializer);
		setValueInterpolate(value); // Need to re-build this
	}

	/**
	 * Sets literal value and finds interpolated variables
	 */
	public void setValue(String valueStr) {
		value = valueStr;
	}

	/**
	 * Sets literal value and finds interpolated variables
	 */
	public void setValueInterpolate(String valueStr) {
		value = valueStr;

		// Parse interpolated vars
		interpolateVars = new InterpolateVars(this, null);
		if (!interpolateVars.parse(valueStr)) {
			interpolateVars = null; // Nothing found? don't bother to keep the object
			value = InterpolateVars.unEscape(valueStr); // Un-escape characters
		}
	}

	@Override
	public String toString() {
		return "\"" + GprString.escape(value) + "\"";
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (interpolateVars != null) interpolateVars.typeCheckNotNull(scope, compilerMessages);
	}
}
