package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

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

	public Type baseType() {
		return ((TypeMap) returnType).getBaseType();
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
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		//---
		// Calculate baseType
		//---
		Type baseType = null;
		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

			if (typeExpr != null) {
				if (baseType == null) {
					baseType = typeExpr;
				} else if (!typeExpr.canCast(baseType)) { // Can we cast ?
					if (baseType.canCast(typeExpr)) { // Can we cast the other way?
						baseType = typeExpr;
					} else {
						// We have a problem...we'll report it in typeCheck.
					}
				}
			}
		}

		// Create a list of 'baseType'
		returnType = TypeMap.get(baseType);

		return returnType;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		HashMap<String, Object> map = new HashMap<String, Object>(values.length);
		Type baseType = baseType();

		for (int i = 0; i < keys.length; i++) {
			// Evaluate 'key' and 'value' expressions
			Expression keyExpr = keys[i];
			bdsThread.run(keyExpr);

			Expression valueExpr = values[i];
			bdsThread.run(valueExpr);

			// Assign to map
			if (!bdsThread.isCheckpointRecover()) {
				Object value = bdsThread.pop();
				String key = bdsThread.pop().toString();
				value = baseType.cast(value);

				map.put(key, value); // Add it to map
			}
		}

		bdsThread.push(map);
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
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
		Type baseType = ((TypeMap) returnType).getBaseType();

		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCast(baseType)) compilerMessages.add(this, "Map types are not consistent. Expecting " + baseType, MessageType.ERROR);
		}
	}

}
