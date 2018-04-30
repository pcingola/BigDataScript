package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.nativeMethods.list.MethodNativeListAdd;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.ValueFunction;
import org.bds.symbol.SymbolTable;

/**
 * A "wait" statement
 *
 * @author pcingola
 */
public class Wait extends Statement {

	private static final long serialVersionUID = -6803518993665623097L;

	Expression taskId;

	public Wait(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'wait'
		if (tree.getChildCount() > 1) taskId = (Expression) factory(tree, 1);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		String labelBase = baseLabelName();
		String labelOk = labelBase + "ok";
		String labelFail = labelBase + "fail";

		// No arguments? Wait for all tasks
		String errMsg = "Error in wait statement, file " + getFileName() + ", line " + getLineNum();
		if (taskId == null) {
			sb.append("waitall\n");
		} else if (taskId.isList()) {
			// Wait for a list of taskIds
			sb.append(taskId.toAsm());
			sb.append("wait\n");
		} else {
			// Wait for a single taskId: We need to pass a list of one element

			// Create an empty list, add single element to it
			TypeList listString = TypeList.get(Types.STRING);
			SymbolTable symtab = listString.getSymbolTable();
			ValueFunction methodAdd = symtab.findFunction(MethodNativeListAdd.class);

			sb.append("new " + listString + "\n");
			sb.append(taskId.toAsm());
			sb.append("callnative " + methodAdd + "\n");

			// Now we have a list of elements to wait
			sb.append("wait\n");
		}

		sb.append("jmpt " + labelOk + "\n");
		sb.append(labelFail + ":\n");
		sb.append("pushs '" + errMsg + "'\n");
		sb.append("error\n");
		sb.append(labelOk + ":\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + (taskId != null ? taskId : "") + "\n";
	}
}
