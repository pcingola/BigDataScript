package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Expression 'Literal' of a map
 *
 * @author pcingola
 */
public class LiteralMap extends Literal {

	private static final long serialVersionUID = 5866903221831301923L;

	Expression keys[];
	Expression values[];

	public LiteralMap(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type getReturnType() {
		return returnType;
	}

	@Override
	protected void parse(ParseTree tree) {
		int size = (tree.getChildCount() - 1) / 4;
		keys = new Expression[size];
		values = new Expression[size];

		for (int i = 1, j = 0; j < size; i += 2, j++) { // Skip first '[' and comma separator
			keys[j] = (Expression) factory(tree, i);
			i += 2;
			values[j] = (Expression) factory(tree, i);
		}
	}

	@Override
	protected ValueList parseValue(ParseTree tree) {
		throw new RuntimeException("This method should never be invoked!!!");
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		//---
		// Calculate elementType
		//---
		Type valueType = null;
		Type keyType = null;
		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(symtab);

			if (typeExpr != null) {
				if (valueType == null) {
					valueType = typeExpr;
				} else if (!typeExpr.canCastTo(valueType)) { // Can we cast ?
					if (valueType.canCastTo(typeExpr)) { // Can we cast the other way?
						valueType = typeExpr;
					} else {
						// We have a problem...we'll report it in typeCheck.
					}
				}
			}
		}

		// Default key is string
		if (keyType == null) keyType = Types.STRING;
		if (valueType == null) keyType = valueType = Types.VOID; // Empty map

		// Get a map type
		returnType = TypeMap.get(keyType, valueType);

		return returnType;
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		for (BdsNode csnode : values)
			if (!(csnode instanceof Expression)) compilerMessages.add(csnode, "Expecting expression instead of " + csnode.getClass().getSimpleName(), MessageType.ERROR);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		// Create a new map (temporary variable)
		String varMap = baseVarName() + "map";
		sb.append("new " + returnType + "\n");
		sb.append("varpop " + varMap + "\n");

		// Add all elements to map
		for (int i = 0; i < values.length; i++) {
			// Evaluate expression and assign to items: '$map[exprKey] = exprVal'
			Expression exprVal = values[i];
			Expression exprKey = keys[i];
			sb.append(exprVal.toAsm());
			sb.append(exprKey.toAsm());
			sb.append("load " + varMap + "\n");
			sb.append("setmappop\n");
		}

		// Leave map as last element in the stack
		sb.append("load " + varMap + "\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (int i = 0; i < keys.length; i++) {
			if (i > 0) sb.append(", ");
			sb.append(keys[i] + " => " + values[i]);
		}
		sb.append('}');
		return sb.toString();

	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Check value types
		Type valueType = ((TypeMap) returnType).getValueType();
		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(symtab);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCastTo(valueType)) {
				compilerMessages.add(this, "Map value types are not consistent. Expecting " + valueType, MessageType.ERROR);
			}
		}

		// Check key types
		Type keyType = ((TypeMap) returnType).getKeyType();
		for (BdsNode node : keys) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(symtab);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCastTo(keyType)) {
				compilerMessages.add(this, "Map key types are not consistent. Expecting " + keyType, MessageType.ERROR);
			}
		}
	}

}
