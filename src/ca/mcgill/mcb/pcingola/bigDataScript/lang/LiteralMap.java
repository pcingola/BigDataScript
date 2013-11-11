package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Expression 'Literal' of a map
 * 
 * @author pcingola
 */
public class LiteralMap extends Literal {

	Expression keys[];
	Expression values[];

	public LiteralMap(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Type baseType() {
		return ((TypeMap) returnType).baseType;
	}

	@Override
	public Object eval(BigDataScriptThread csThread) {
		HashMap<String, Object> map = new HashMap<String, Object>(values.length);
		Type baseType = baseType();

		for (int i = 0; i < keys.length; i++) {
			// Evaluate 'key' expression
			Expression keyExpr = keys[i];
			String key = keyExpr.eval(csThread).toString();

			// Evaluate 'value' expression
			Expression valueExpr = values[i];
			Object value = valueExpr.eval(csThread);
			value = baseType.cast(value);

			map.put(key, value); // Add it to map
		}

		return map;
	}

	@Override
	protected void parse(ParseTree tree) {
		int size = tree.getChildCount() / 4;
		keys = new Expression[size];
		values = new Expression[size];

		for (int i = 1, j = 0; i < tree.getChildCount(); i += 2, j++) { // Skip first '[' and comma separator
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
		Type baseType = Type.BOOL;
		for (BigDataScriptNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCast(baseType)) {
				// Can we cast the other way?
				if (baseType.canCast(typeExpr)) baseType = typeExpr;
				else {
					// We have a problem...we'll report it in typeCheck.
				}
			}
		}

		// Create a list of 'baseType'
		returnType = TypeMap.get(baseType);

		return returnType;
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		for (BigDataScriptNode csnode : values)
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
		Type baseType = ((TypeMap) returnType).baseType;

		for (BigDataScriptNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(scope);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCast(baseType)) compilerMessages.add(this, "Map types are not consistent. Expecting " + baseType, MessageType.ERROR);
		}
	}

}
