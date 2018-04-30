package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.nativeMethods.list.MethodNativeListAdd;
import org.bds.lang.nativeMethods.list.MethodNativeListAddList;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.ValueFunction;
import org.bds.symbol.SymbolTable;

/**
 * A sum of two expressions
 *
 * @author pcingola
 */
public class ExpressionPlus extends ExpressionMath {

	private static final long serialVersionUID = 3368537782650014362L;

	public ExpressionPlus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "+";
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);

		if (left.canCastToInt() && right.canCastToInt()) {
			// Int + Int
			returnType = Types.INT;
		} else if (left.canCastToReal() && right.canCastToReal()) {
			// Real + Real
			returnType = Types.REAL;
		} else if (left.isList() || right.isList()) {
			// Either side is a list
			returnType = returnTypeList();
		} else if (left.isString() || right.isString()) {
			// String plus something (convert to string)
			returnType = Types.STRING;
		}

		return returnType;
	}

	/**
	 * Calculate the element type of the resulting list '+' operation
	 * @param let: Left type (can be either list or element)
	 * @param ret: Right type (can be either list or element)
	 * @return
	 */
	public Type returnTypeList() {
		Type lt = left.getReturnType();
		Type rt = right.getReturnType();

		if (lt == null || rt == null) return null;
		if (!lt.isList() && !rt.isList()) return null;

		if (lt.isList() && rt.isList()) {
			// List + List: They should have the same element type
			if (lt.equals(rt)) return lt;
		} else if (lt.isList() && !rt.isList()) {
			// List + element: If the element can be casted, we are fine
			Type let = ((TypeList) lt).getElementType();
			if (rt.canCastTo(let)) return lt;
		} else if (!lt.isList() && rt.isList()) {
			// element + List : If the element can be casted, we are fine
			Type ret = ((TypeList) rt).getElementType();
			if (lt.canCastTo(ret)) return rt;
		}
		return null;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		if (returnType.isList()) {
			sb.append(toAsmNode());
			sb.append(toAsmList());
		} else {
			sb.append(super.toAsm());
			sb.append("add" + toAsmRetType() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Evaluate a 'plus' expression involving at least one list
	 * Note: We use native method calls
	 */
	String toAsmList() {
		StringBuilder sb = new StringBuilder();
		Type lt = left.getReturnType();
		Type rt = right.getReturnType();

		ValueFunction methodAdd = returnType.getSymbolTable().findFunction(MethodNativeListAdd.class);
		ValueFunction methodAddList = returnType.getSymbolTable().findFunction(MethodNativeListAddList.class);

		if (lt.isList() && rt.isList()) {
			// List + List
			sb.append("new " + returnType + "\n");
			sb.append(left.toAsm());
			sb.append("callnative " + methodAddList + "\n");
			sb.append(right.toAsm());
			sb.append("callnative " + methodAddList + "\n");
		} else if (lt.isList() && !rt.isList()) {
			// List + element
			sb.append("new " + returnType + "\n");
			sb.append(left.toAsm());
			sb.append("callnative " + methodAddList + "\n");
			sb.append(right.toAsm());
			sb.append("callnative " + methodAdd + "\n");
		} else if (!lt.isList() && rt.isList()) {
			// element + List
			sb.append("new " + returnType + "\n");
			sb.append(left.toAsm());
			sb.append("callnative " + methodAdd + "\n");
			sb.append(right.toAsm());
			sb.append("callnative " + methodAddList + "\n");
		}

		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (left.isList() || right.isList()) {
			Type rt = returnTypeList();
			if (rt == null) {
				compilerMessages.add(this, "Cannot append " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
			}
		} else if (left.isString() || right.isString()) {
			// Either side is a string? => String plus String
		} else {
			// Normal 'math'
			left.checkCanCastToNumeric(compilerMessages);
			right.checkCanCastToNumeric(compilerMessages);
		}
	}

}
