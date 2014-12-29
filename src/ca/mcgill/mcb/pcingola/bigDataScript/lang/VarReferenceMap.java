package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * A reference to a map variable. E.g. map{'hello'}
 *
 * @author pcingola
 */
public class VarReferenceMap extends Reference {

	protected VarReference variable;
	protected Expression expressionKey;

	public VarReferenceMap(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Return index evaluation
	 */
	public String evalKey(BigDataScriptThread bdsThread) {
		bdsThread.run(expressionKey);
		if (bdsThread.isCheckpointRecover()) return null;

		return popString(bdsThread);
	}

	@SuppressWarnings("rawtypes")
	public HashMap getMap(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return (HashMap) ss.getValue();
	}

	/**
	 * Get symbol from scope
	 */
	@Override
	public ScopeSymbol getScopeSymbol(Scope scope) {
		return variable.getScopeSymbol(scope);
	}

	public Type getType(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return ss.getType();
	}

	@Override
	public String getVariableName() {
		return variable.getVariableName();
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		variable = (VarReference) factory(tree, 0);
		// child[1] = '{'
		expressionKey = (Expression) factory(tree, 2);
		// child[3] = '}'
	}

	@Override
	public void parse(String str) {
		int idx1 = str.indexOf('{');
		int idx2 = str.lastIndexOf('}');
		if ((idx1 <= 0) || (idx2 <= idx1)) throw new RuntimeException("Cannot parse list reference '" + str + "'");

		// Create VarReference
		String varName = str.substring(0, idx1);
		variable = new VarReference(this, null);
		variable.parse(varName);

		// Create index expression
		String idxStr = str.substring(idx1 + 1, idx2);

		if (idxStr.startsWith("$")) {
			// We have to interpolate this string
			expressionKey = InterpolateVars.factory(this, idxStr.substring(1));
		} else {
			// String literal
			LiteralString exprIdx = new LiteralString(this, null);
			if (idxStr.startsWith("'")) idxStr = idxStr.substring(1);
			if (idxStr.endsWith("'")) idxStr = idxStr.substring(0, idxStr.length() - 1);
			exprIdx.setValue(idxStr);
			expressionKey = exprIdx;
		}
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		expressionKey.returnType(scope);
		Type nameType = variable.returnType(scope);

		if (nameType == null) return null;
		if (nameType.isMap()) returnType = ((TypeMap) nameType).getBaseType();

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		String key = evalKey(bdsThread);
		if (bdsThread.isCheckpointRecover()) return;

		HashMap map = getMap(bdsThread.getScope());
		Object ret = map.get(key);
		if (ret == null) throw new RuntimeException("Map '" + getVariableName() + "' does not have key '" + key + "'.");

		bdsThread.push(ret);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValue(BigDataScriptThread bdsThread, Object value) {
		if (value == null) return;

		String key = evalKey(bdsThread);
		if (bdsThread.isCheckpointRecover()) return;

		HashMap map = getMap(bdsThread.getScope());
		map.put(key, value);
	}

	@Override
	public String toString() {
		return variable + "{" + expressionKey + "}";
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		// Note: We do not perform type checking on 'key' since everything can be cast to a string
		if ((variable.getReturnType() != null) && !variable.getReturnType().isMap()) compilerMessages.add(this, "Symbol '" + variable + "' is not a map", MessageType.ERROR);
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
