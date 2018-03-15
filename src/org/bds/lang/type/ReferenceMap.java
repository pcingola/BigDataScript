package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.value.LiteralString;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

/**
 * A reference to a map variable. E.g. map{'hello'}
 *
 * @author pcingola
 */
public class ReferenceMap extends Reference {

	protected Expression exprMap;
	protected Expression expressionKey;

	public ReferenceMap(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Get symbol from scope
	 */
	@Override
	public ValueMap getValue(Scope scope) {
		if (exprMap instanceof ReferenceVar) return (ValueMap) ((ReferenceVar) exprMap).getValue(scope);
		return null;

	}

	@Override
	public String getVariableName() {
		if (exprMap instanceof ReferenceVar) return ((ReferenceVar) exprMap).getVariableName();
		return null;

	}

	@Override
	public boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		exprMap = (Expression) factory(tree, 0);
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
		exprMap = refVar;

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
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		expressionKey.returnType(symtab);

		// Retrieve map from scope
		Type mapType = exprMap.returnType(symtab);
		if (mapType != null && mapType.isMap()) {
			returnType = ((TypeMap) mapType).getValueType();
		}

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Evaluate expressions
		bdsThread.run(exprMap);
		bdsThread.run(expressionKey);

		if (bdsThread.isCheckpointRecover()) return;

		// Get results
		Value vkey = bdsThread.pop();
		ValueMap vmap = (ValueMap) bdsThread.pop();

		// Obtain map entry
		Value ret = vmap.getValue(vkey);
		if (ret == null) throw new RuntimeException("Map '" + getVariableName() + "' does not have key '" + vkey + "'.");

		bdsThread.push(ret);
	}

	@Override
	public void setValue(BdsThread bdsThread, Value value) {
		if (value == null) return;

		bdsThread.run(expressionKey);
		Value key = bdsThread.pop();
		if (bdsThread.isCheckpointRecover()) return;

		ValueMap vmap = getValue(bdsThread.getScope());
		if (vmap == null) bdsThread.fatalError(this, "Cannot find variable '" + this + "'");
		vmap.put(key, value);
	}

	@Override
	public String toString() {
		return exprMap + "{" + expressionKey + "}";
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(symtab);

		// Is it a map?
		if (!exprMap.isMap()) {
			compilerMessages.add(this, "Symbol '" + exprMap + "' is not a map", MessageType.ERROR);
			return;
		}

		// Check map key
		Type keyType = expressionKey.getReturnType();
		if (expressionKey.getReturnType() == null) return;

		TypeMap mapType = (TypeMap) exprMap.getReturnType();
		if (!keyType.canCastTo(mapType.getKeyType())) {
			compilerMessages.add(this, "Cannot cast key expression from '" + keyType + "' to '" + mapType.getKeyType() + "'", MessageType.ERROR);
		}
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
