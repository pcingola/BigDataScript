package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.GprString;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Tuple;

/**
 * Expression 'Literal'
 * 
 * @author pcingola
 */
public class LiteralString extends Literal {

	String value; // If it is a simple literal, we use this
	List<String> strings; // This is used in case of interpolated string literal
	List<String> variables; // This is used in case of interpolated string literal

	public LiteralString(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Object eval(BigDataScriptThread csThread) {
		// No variable interpolation? => Literal
		if (variables == null) return value;

		// Variable interpolation
		return csThread.getScope().interpolate(strings, variables);
	}

	public String getValue() {
		return value;
	}

	/**
	 * Interpolate variables
	 * @param value
	 */
	void interpolateVars(String value) {
		Tuple<List<String>, List<String>> interpolated = GprString.findVariables(value);
		if (!interpolated.second.isEmpty()) { // Anything found?
			strings = interpolated.first;
			variables = interpolated.second;
		}
	}

	@Override
	protected void parse(ParseTree tree) {
		String valueStr = tree.getChild(0).getText();

		if (valueStr.charAt(0) == '\'' && valueStr.charAt(valueStr.length() - 1) == '\'') {
			// Remove quotes: No unescaping, no interpolation
			value = valueStr.substring(1, valueStr.length() - 1);
		} else {
			// Remove quotes, unescape, interpolate
			valueStr = valueStr.substring(1, valueStr.length() - 1);
			valueStr = GprString.unescape(valueStr); // Parse escaped chars
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
		interpolateVars(value); // Need to re-build this
	}

	/**
	 * Sets literal value and finds interpolated variables
	 * @param valueStr
	 */
	public void setValue(String valueStr) {
		value = valueStr;
	}

	/**
	 * Sets literal value and finds interpolated variables
	 * @param valueStr
	 */
	public void setValueInterpolate(String valueStr) {
		value = valueStr;
		interpolateVars(value); // Find interpolated vars		
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in th scope
		if (variables != null) //
			for (String varName : variables)
				if (!varName.isEmpty() && !scope.hasSymbol(varName, false)) //
					compilerMessages.add(this, "Symbol '" + varName + "' cannot be resolved", MessageType.ERROR);
	}
}
