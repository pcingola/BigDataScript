package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

/**
 * An expression having an implicit type variable initialization ( varName := expression )
 *
 * @author pcingola
 */
public class ExpressionVariableInitImplicit extends Expression {

	VariableInitImplicit vInit;

	public ExpressionVariableInitImplicit(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return vInit.getExpression().getReturnType() != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		vInit = new VariableInitImplicit(this, tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = vInit.getExpression().returnType(scope);
		return returnType;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		// Evaluating the expression consists of initializing the variable and getting the result of that initialization

		// Add variable to scope
		Scope scope = null;
		if (!bdsThread.isCheckpointRecover()) {
			scope = bdsThread.getScope();
			scope.add(new ScopeSymbol(vInit.getVarName(), returnType));
		}

		// Evaluate assignment
		bdsThread.run(vInit);
		if (bdsThread.isCheckpointRecover()) return;

		// Return initialization's result
		ScopeSymbol ssym = scope.getSymbol(vInit.getVarName());
		bdsThread.push(ssym.getValue());
	}

	@Override
	public String toString() {
		return vInit != null ? vInit.toString() : "";
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		vInit.typeCheck(scope, compilerMessages);

		// Already declared?
		String varName = vInit.varName;
		if (scope.hasSymbolLocal(varName)) compilerMessages.add(this, "Duplicate local name " + varName, MessageType.ERROR);

		// Calculate implicit data type
		Type type = vInit.getExpression().returnType(scope);

		// Add variable to scope
		if ((varName != null) && (type != null)) scope.add(new ScopeSymbol(varName, type));
	}
}
