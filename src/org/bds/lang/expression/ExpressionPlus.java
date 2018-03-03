package org.bds.lang.expression;

import java.util.ArrayList;
import java.util.Collection;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * A sum of two expressions
 *
 * @author pcingola
 */
public class ExpressionPlus extends ExpressionMath {

	public ExpressionPlus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "+";
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);

		if (left.canCastToInt() && right.canCastToInt()) {
			// Int + Int
			returnType = Types.INT;
		} else if (left.canCastToReal() && right.canCastToReal()) {
			// Real + Real
			returnType = Types.REAL;
		} else if (left.isList() || right.isList()) {
			// Either side is a list
			returnType = returnTypeListPlusList(left.getReturnType(), right.getReturnType());
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
	public Type returnTypeListPlusList(Type lt, Type rt) {
		if (lt == null || rt == null) return null;
		if (!lt.isList() && !rt.isList()) return null;

		// Get element type
		Type let = lt, ret = rt;
		if (lt.isList()) let = ((TypeList) lt).getElementType();
		if (rt.isList()) ret = ((TypeList) rt).getElementType();

		// Which one we can cast?
		if (ret.canCastTo(let)) return TypeList.get(let);
		if (let.canCastTo(ret)) return TypeList.get(ret);
		return null;
	}

	/**
	 * Evaluate an expression
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;

		if (isInt()) {
			long r = bdsThread.popInt();
			long l = bdsThread.popInt();
			bdsThread.push(l + r);
			return;
		}

		if (isReal()) {
			double r = bdsThread.popReal();
			double l = bdsThread.popReal();
			bdsThread.push(l + r);
			return;
		}

		if (isString()) {
			Object rval = bdsThread.pop();
			Object lval = bdsThread.pop();
			bdsThread.push(lval.toString() + rval.toString());
			return;
		}

		if (isList()) {
			Object rval = bdsThread.pop();
			Object lval = bdsThread.pop();

			ArrayList list = new ArrayList();
			if (left.isList()) list.addAll((Collection) lval);
			else list.add(lval);

			if (right.isList()) list.addAll((Collection) rval);
			else list.add(rval);

			bdsThread.push(list);
			return;
		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (left.isList() || right.isList()) {
			Type rt = returnTypeListPlusList(left.getReturnType(), right.getReturnType());
			if (rt == null) {
				compilerMessages.add(this, "Cannot append " + left.getReturnType() + " and " + right.getReturnType(), MessageType.ERROR);
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
