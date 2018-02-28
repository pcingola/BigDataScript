package org.bds.lang.expression;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.LiteralListEmpty;
import org.bds.lang.type.Reference;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * Expression assignment for a list of variables
 *
 * 		a , b , c = [ 1, 2, 3 ]
 * 		field, var = line.split('\t')
 *
 * @author pcingola
 */
public class ExpressionAssignmentList extends ExpressionAssignment {

	Expression lefts[];

	public ExpressionAssignmentList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isReturnTypesNotNull() {
		if (right == null || right.getReturnType() != null) return false;

		for (Expression l : lefts)
			if (l == null || l.getReturnType() != null) return false;

		return true;
	}

	@Override
	protected String op() {
		return "=";
	}

	@Override
	protected void parse(ParseTree tree) {
		int listSize = (tree.getChildCount() - 2) / 2;
		lefts = new Expression[listSize];

		int idx = 1;
		for (int j = 0; j < listSize; idx += 2, j++) { // Skip comma separators
			lefts[j] = (Expression) factory(tree, idx);
		}

		idx++;
		right = (Expression) factory(tree, idx);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		for (Expression l : lefts)
			l.returnType(scope);
		right.returnType(scope);

		returnType = lefts[0].returnType(scope); // Get return type for first expreesion
		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void runStep(BdsThread bdsThread) {

		// Get value
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;

		List list = (List) bdsThread.peek();
		for (int i = 0; i < lefts.length; i++) {
			// Get variable
			Reference vr = (Reference) lefts[i];

			// Get value
			Object value;
			if (i < list.size()) value = list.get(i);
			else value = vr.getReturnType().defaultValue(); // List too short? Assign variable's default value

			// Assign value to variable
			vr.setValue(bdsThread, value);
		}
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		for (Expression l : lefts)
			if (l == null) compilerMessages.add(this, "Cannot parse left expresison.", MessageType.ERROR);
			else if (!(l instanceof Reference)) compilerMessages.add(this, "Assignment to non variable (" + l + ")", MessageType.ERROR);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("(");
		for (int i = 0; i < lefts.length; i++) {
			if (i > 0) sb.append(", ");
			sb.append(lefts[i]);
		}

		sb.append(") = " + right);

		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Trying to assign to a constant?
		for (Expression l : lefts) {
			if (l == null) compilerMessages.add(this, "Cannot parse left expresison.", MessageType.ERROR);
			else if (!(l instanceof Reference)) compilerMessages.add(this, "Left hand side expression is not a variable reference '" + l + "'", MessageType.ERROR);
			else if (!l.getReturnType().isPrimitiveType()) compilerMessages.add(this, "Variable '" + l + "' is non-primitive type " + l.getReturnType(), MessageType.ERROR);
			else if (((Reference) l).isConstant(scope)) compilerMessages.add(this, "Cannot assign to constant '" + l + "'", MessageType.ERROR);
		}

		// Can we cast 'right type' into 'left type'?
		if (right.isList()) {
			if (right instanceof LiteralListEmpty) {
				// OK, empty list can be assigned to any variable
			} else {
				TypeList typeList = (TypeList) right.getReturnType();
				Type type = typeList.getBaseType();

				// Check that all left hand sides match
				for (Expression l : lefts)
					if (!type.canCast(l.getReturnType())) compilerMessages.add(this, "Cannot cast " + type + " to " + l.getReturnType(), MessageType.ERROR);
			}
		} else compilerMessages.add(this, "Right hand side expreesion is not a list.", MessageType.ERROR);
	}
}
