package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.LiteralListEmpty;
import org.bds.lang.type.LiteralMapEmpty;
import org.bds.lang.type.Reference;
import org.bds.scope.Scope;

/**
 * Expression
 *
 * @author pcingola
 */
public class ExpressionAssignmentPlus extends ExpressionAssignmentBinary {

	public ExpressionAssignmentPlus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionPlus(this, null);
	}

	@Override
	protected String op() {
		return "+=";
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Trying to assign to a constant?
		if (((Reference) left).isConstant(scope)) compilerMessages.add(this, "Cannot assign to constant '" + left + "'", MessageType.ERROR);

		// Can we cast 'right type' into 'left type'?
		if (left.isList() && right.isList() && right instanceof LiteralListEmpty) {
			// OK, empty list can be assigned to any list
		} else if (left.isList() && left.isList(right.getReturnType())) {
			// OK, append
		} else if (left.isMap() && right.isMap() && right instanceof LiteralMapEmpty) {
			// OK, empty map can be assigned to any map
		} else if (!right.getReturnType().canCast(left.getReturnType())) {
			compilerMessages.add(this, "Cannot cast " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
		}
	}
}
