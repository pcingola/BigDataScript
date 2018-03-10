package org.bds.lang.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A 'goal' expression
 *
 * @author pcingola
 */
public class ExpressionGoal extends ExpressionUnary {

	public ExpressionGoal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "goal";
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		returnType = TypeList.get(Types.STRING);
		return returnType;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(expr);
		if (bdsThread.isCheckpointRecover()) return;

		// Get expression map
		Value value = bdsThread.pop();

		// Goal returns a list of taskIds to be run
		List<String> taskIds = null;
		if (expr.isList()) {
			// Is is a list? Run goal for each element
			taskIds = new ArrayList<>();

			// Process each goal
			Collection goals = (Collection) value.get();
			for (Object goal : goals)
				taskIds.addAll(bdsThread.goal(goal.toString()));
		} else {
			// Single valued
			taskIds = bdsThread.goal(value.get().toString());
		}

		// Add results to stack
		bdsThread.push(taskIds);
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		super.typeCheckNotNull(symtab, compilerMessages);

		if (!expr.getReturnType().isString()) compilerMessages.add(this, "Expression does not return 'string' (" + expr.getReturnType() + ")", MessageType.ERROR);
	}
}
