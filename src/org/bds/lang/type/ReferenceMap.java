package org.bds.lang.type;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.value.LiteralString;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

/**
 * A reference to a map variable. E.g. map{'hello'}
 *
 * @author pcingola
 */
public class ReferenceMap extends Reference {

	// protected ReferenceVar variable;
	protected Expression variable;
	protected Expression expressionKey;

	public ReferenceMap(BdsNode parent, ParseTree tree) {
		super(parent, tree);
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
		if (variable instanceof ReferenceVar) return ((ReferenceVar) variable).getScopeSymbol(scope);
		return null;

	}

	public Type getType(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return ss.getType();
	}

	@Override
	public String getVariableName() {
		if (variable instanceof ReferenceVar) return ((ReferenceVar) variable).getVariableName();
		return null;

	}

	@Override
	public boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		variable = (Expression) factory(tree, 0);
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
		ReferenceVar refVar = new ReferenceVar(this, null);
		refVar.parse(varName);
		variable = refVar;

		// Create index expression
		String idxStr = str.substring(idx1 + 1, idx2);

		if (idxStr.startsWith("$")) {
			// We have to interpolate this string
			expressionKey = ReferenceVar.factory(this, idxStr.substring(1));
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
	public void runStep(BdsThread bdsThread) {
		// Evaluate expressions
		bdsThread.run(variable);
		bdsThread.run(expressionKey);

		if (bdsThread.isCheckpointRecover()) return;

		// Get results
		String key = bdsThread.popString();
		Map map = (Map) bdsThread.pop();

		// Obtain map entry
		Object ret = map.get(key);
		if (ret == null) throw new RuntimeException("Map '" + getVariableName() + "' does not have key '" + key + "'.");

		bdsThread.push(ret);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValue(BdsThread bdsThread, Object value) {
		if (value == null) return;

		bdsThread.run(expressionKey);
		String key = bdsThread.popString();
		if (bdsThread.isCheckpointRecover()) return;

		HashMap map = getMap(bdsThread.getScope());
		if (map == null) bdsThread.fatalError(this, "Cannot assign to non-variable '" + this + "'");
		map.put(key, value);
	}

	@Override
	public String toString() {
		return variable + "{" + expressionKey + "}";
	}

	@Override
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
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
