package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Expression 'Literal'
 *
 * @author pcingola
 */
public class LiteralList extends Literal {

	private static final long serialVersionUID = -7535741001316384850L;

	Expression values[];

	public LiteralList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Type getElementType() {
		return ((TypeList) returnType).getElementType();
	}

	@Override
	public Type getReturnType() {
		return returnType;
	}

	@Override
	protected void parse(ParseTree tree) {
		int listSize = (tree.getChildCount() - 1) / 2;
		values = new Expression[listSize];
		for (int i = 1, j = 0; j < listSize; i += 2, j++) { // Skip first '[' and comma separators
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
		Type baseType = null;
		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(symtab);

			if (typeExpr != null) {
				if (baseType == null) {
					baseType = typeExpr;
				} else if (!typeExpr.canCastTo(baseType)) { // Can we cast ?
					if (baseType.canCastTo(typeExpr)) { // Can we cast the other way?
						baseType = typeExpr;
					} else {
						// We have a problem...we'll report it in typeCheck.
					}
				}
			}
		}

		// Default base type if nothing found
		if (baseType == null) baseType = Types.VOID;

		// Create a list of 'elementType'
		returnType = TypeList.get(baseType);

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

		// Create a new list (temporary variable)
		String varList = baseVarName() + "list";
		sb.append("new " + returnType + "\n");
		sb.append("varpop " + varList + "\n");

		// Add all elements to list
		for (int i = 0; i < values.length; i++) {
			// Evaluate expression and assign to list item: '$list[i] = expr'
			Expression expr = values[i];
			sb.append(expr.toAsm());
			sb.append("pushi " + i + "\n");
			sb.append("load " + varList + "\n");
			sb.append("setlistpop\n");
		}

		// Leave list as last element in the stack
		sb.append("load " + varList + "\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");

		for (int i = 0; i < values.length; i++) {
			if (i > 0) sb.append(" , ");
			sb.append(values[i]);
		}

		sb.append(" ]");

		return sb.toString();
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		Type baseType = ((TypeList) returnType).getElementType();

		for (BdsNode node : values) {
			Expression expr = (Expression) node;
			Type typeExpr = expr.returnType(symtab);

			// Can we cast ?
			if ((typeExpr != null) && !typeExpr.canCastTo(baseType)) {
				compilerMessages.add(this, "List types are not consistent. Expecting " + baseType, MessageType.ERROR);
			}
		}
	}

}
