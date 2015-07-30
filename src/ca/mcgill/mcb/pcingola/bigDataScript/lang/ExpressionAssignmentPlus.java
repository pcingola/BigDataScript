package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Expression
 *
 * @author pcingola
 */
public class ExpressionAssignmentPlus extends ExpressionAssignmentBinary {

	public ExpressionAssignmentPlus(BigDataScriptNode parent, ParseTree tree) {
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
