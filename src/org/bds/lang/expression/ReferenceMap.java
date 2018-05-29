package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.value.LiteralString;
import org.bds.lang.value.ValueMap;
import org.bds.lang.value.ValueString;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

/**
 * A reference to a map variable. E.g. map{'hello'}
 *
 * @author pcingola
 */
public class ReferenceMap extends Reference {

	private static final long serialVersionUID = 2655912724548019580L;

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
		if (exprMap instanceof Reference) return ((Reference) exprMap).getVariableName();
		return null;

	}

	@Override
	public boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	public boolean isVariableReference(SymbolTable symtab) {
		if (exprMap instanceof Reference) { return ((Reference) exprMap).isVariableReference(symtab); }
		return false;
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
		if ((idx1 <= 0) || (idx2 <= idx1)) throw new RuntimeException("Cannot parse map reference '" + str + "'");

		// Create VarReference
		String varName = str.substring(0, idx1);
		ReferenceVar refVar = new ReferenceVar(this, null);
		refVar.parse(varName);
		exprMap = refVar;

		// Create index expression
		String idxStr = str.substring(idx1 + 1, idx2);

		if (idxStr.startsWith("$")) {
			// We have to interpolate this string
			expressionKey = Expression.factory(this, idxStr.substring(1));
		} else {
			// String literal
			LiteralString exprIdx = new LiteralString(this, null);
			if (idxStr.startsWith("'")) idxStr = idxStr.substring(1);
			if (idxStr.endsWith("'")) idxStr = idxStr.substring(0, idxStr.length() - 1);
			exprIdx.setValue(new ValueString(idxStr));
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

	@Override
	public String toAsm() {
		return expressionKey.toAsm() //
				+ exprMap.toAsm() //
				+ "refmap\n";
	}

	@Override
	public String toAsmSet() {
		return expressionKey.toAsm() //
				+ exprMap.toAsm() //
				+ "setmap\n";
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
