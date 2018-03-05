package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
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

	/**
	 * Evaluate a 'plus' expression involving at least one list
	 */
	ValueList listPlus(BdsThread bdsThread) {
		Value rval = bdsThread.pop();
		Value lval = bdsThread.pop();

		Type lt = left.getReturnType();
		Type rt = right.getReturnType();

		if (lt.isList() && rt.isList()) {
			// List + List
			ValueList llist = (ValueList) lval;
			ValueList rlist = (ValueList) rval;
			ValueList vlist = new ValueList(lt, llist.size() + rlist.size());
			vlist.addAll(llist);
			vlist.addAll(rlist);
			return vlist;
		} else if (lt.isList() && !rt.isList()) {
			// List + element
			ValueList llist = (ValueList) lval;
			Type let = ((TypeList) lt).getElementType();
			ValueList vlist = new ValueList(lt, llist.size() + 1);
			vlist.addAll(llist);
			vlist.add(let.cast(rval));
			return vlist;
		} else if (!lt.isList() && rt.isList()) {
			// element + List
			ValueList rlist = (ValueList) rval;
			Type ret = ((TypeList) rt).getElementType();
			ValueList vlist = new ValueList(rt, rlist.size() + 1);
			vlist.add(ret.cast(lval));
			vlist.addAll(rlist);
			return vlist;
		}

		return null;
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

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;

		if (isInt()) {
			long r = bdsThread.popInt();
			long l = bdsThread.popInt();
			bdsThread.push(l + r);
		} else if (isReal()) {
			double r = bdsThread.popReal();
			double l = bdsThread.popReal();
			bdsThread.push(l + r);
		} else if (isString()) {
			String rval = bdsThread.popString();
			String lval = bdsThread.popString();
			bdsThread.push(lval.toString() + rval.toString());
		} else if (isList()) {
			bdsThread.push(listPlus(bdsThread));
		} else {
			throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
		}
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (left.isList() || right.isList()) {
			Type rt = returnTypeListPlusList(left.getReturnType(), right.getReturnType());
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
