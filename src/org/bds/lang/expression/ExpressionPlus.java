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

		if (left.canCastToInt() && right.canCastToInt()) returnType = Types.INT;
		else if (left.canCastToReal() && right.canCastToReal()) returnType = Types.REAL;
		else if (left.isList() && right.isList()) {
			if (left.getReturnType() == null || right.getReturnType() == null) return null;
			if (left.getReturnType().compareTo(right.getReturnType()) == 0) returnType = left.getReturnType(); // List plus List
		} else if (left.isList() && !right.isList()) {
			TypeList tlist = (TypeList) left.getReturnType();
			if (left.getReturnType() == null || right.getReturnType() == null) return null;
			if (right.getReturnType().compareTo(tlist.getElementType()) == 0) returnType = left.getReturnType(); // List plus Item
		} else if (!left.isList() && right.isList()) {
			TypeList tlist = (TypeList) right.getReturnType();
			if (left.getReturnType() == null || right.getReturnType() == null) return null;
			if (left.getReturnType().compareTo(tlist.getElementType()) == 0) returnType = right.getReturnType(); // Item plus List
		} else if (right.isList() && left.getReturnType().canCast(right.getReturnType())) returnType = right.getReturnType(); // Item plus List
		else if (left.isString() || right.isString()) returnType = Types.STRING;

		return returnType;
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
		if (left.isList() && right.isList()) {
			if (left.getReturnType().compareTo(right.getReturnType()) != 0) {
				compilerMessages.add(this, "Cannot append " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
			}
		} else if (left.isList() && !right.isList()) {
			TypeList tlist = (TypeList) left.getReturnType();
			if (right.getReturnType().compareTo(tlist.getElementType()) != 0) compilerMessages.add(this, "Cannot append " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
		} else if (right.isList() && !left.isList()) {
			TypeList tlist = (TypeList) right.getReturnType();
			if (left.getReturnType().compareTo(tlist.getElementType()) != 0) compilerMessages.add(this, "Cannot append " + left.getReturnType() + " to " + right.getReturnType(), MessageType.ERROR);
		} else if (left.isString() || right.isString()) {
			// Either side is a string? => String plus String
		} else {
			// Normal 'math'
			left.checkCanCastToNumeric(compilerMessages);
			right.checkCanCastToNumeric(compilerMessages);
		}
	}

}
