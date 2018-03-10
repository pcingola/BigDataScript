package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.VariableInitImplicit;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

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
	public boolean isReturnTypesNotNull() {
		return vInit.getExpression().getReturnType() != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		vInit = new VariableInitImplicit(this, tree);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		returnType = vInit.getExpression().returnType(symtab);
		return returnType;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		// Evaluating the expression consists of initializing the variable and getting the result of that initialization

		// Add variable to scope
		Scope scope = null;
		if (!bdsThread.isCheckpointRecover()) {
			scope = bdsThread.getScope();
			scope.add(vInit.getVarName(), returnType);
		}

		// Evaluate assignment
		bdsThread.run(vInit);
		if (bdsThread.isCheckpointRecover()) return;

		// Return initialization's result
		Value val = scope.getValue(vInit.getVarName());
		bdsThread.push(val);
	}

	@Override
	public String toString() {
		return vInit != null ? vInit.toString() : "";
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		vInit.typeCheck(symtab, compilerMessages);

		// Already declared?
		String varName = vInit.getVarName();
		if (symtab.hasTypeLocal(varName)) compilerMessages.add(this, "Duplicate local name " + varName, MessageType.ERROR);

		// Calculate implicit data type
		Type type = vInit.getExpression().returnType(symtab);

		// Add variable to scope
		if ((varName != null) && (type != null)) symtab.add(varName, type);
	}
}
