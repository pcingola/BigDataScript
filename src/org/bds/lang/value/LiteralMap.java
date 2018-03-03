package org.bds.lang.value;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * Expression 'Literal' of a map
 *
 * @author pcingola
 */
public class LiteralMap extends Literal {

	Expression keys[];
	Expression values[];

	public LiteralMap(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	//	public Type baseType() {
	//		return ((TypeMap) returnType).getElementType();
	//	}

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
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		//---
		// Calculate elementType
		//---
		Type valueType = null;
		Type keyType = null;
		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

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

		// Get a map type
		returnType = TypeMap.get(keyType, valueType);

		return returnType;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		Map<String, Object> map = new HashMap<>(values.length);
		TypeMap mapType = (TypeMap) getReturnType();
		Type valueType = mapType.getValueType();

		for (int i = 0; i < keys.length; i++) {
			// Evaluate 'key' and 'map' expressions
			Expression keyExpr = keys[i];
			bdsThread.run(keyExpr);

			Expression valueExpr = values[i];
			bdsThread.run(valueExpr);

			// Assign to map
			if (!bdsThread.isCheckpointRecover()) {
				Value value = bdsThread.pop();
				String key = bdsThread.pop().toString();
				value = valueType.cast(value);
				map.put(key, value); // Add it to map
			}
		}

		// Create value map an push to stack
		ValueMap vmap = new ValueMap(mapType);
		vmap.set(map);
		bdsThread.push(vmap);
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		for (BdsNode csnode : values)
			if (!(csnode instanceof Expression)) compilerMessages.add(csnode, "Expecting expression instead of " + csnode.getClass().getSimpleName(), MessageType.ERROR);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.length; i++) {
			sb.append(keys[i] + " = " + values[i]);
			if (i < keys.length) sb.append(", ");
		}
		return sb.toString();

	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		Type valueType = ((TypeMap) returnType).getValueType();

		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCastTo(valueType)) {
				compilerMessages.add(this, "Map types are not consistent. Expecting " + valueType, MessageType.ERROR);
			}
		}

		// !!! TODO: Type check for keys
	}

	@Override
	protected Object parseValue(ParseTree tree) {
		// !!! TODO 
		throw new RuntimeException("!!! UNIMPLEMENTED");
	}

}
