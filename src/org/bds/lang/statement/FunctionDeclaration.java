package org.bds.lang.statement;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueArgs;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * Function declaration
 *
 * @author pcingola
 */
public class FunctionDeclaration extends StatementWithScope {

	protected String functionName;
	protected TypeFunction funcType;
	protected Parameters parameters;
	protected Statement statement;
	protected String signature;

	public FunctionDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Apply function to arguments, return function's result
	 */
	public Value apply(BdsThread bdsThread, ValueArgs args) {
		// Create scope and add function arguments
		if (!bdsThread.isCheckpointRecover()) createScopeAddArgs(bdsThread, args);

		// Run function body
		runFunction(bdsThread);
		if (bdsThread.isFatalError()) throw new RuntimeException("Fatal error");

		// Get return value
		Value retVal = bdsThread.getReturnValue();

		// Restore old scope
		if (!bdsThread.isCheckpointRecover()) bdsThread.oldScope();

		// Return result
		return retVal;
	}

	/**
	 * Create a new scope and add arguments
	 */
	protected void createScopeAddArgs(BdsThread bdsThread, ValueArgs vargs) {
		// Create new scope
		bdsThread.newScope(this);

		// Add arguments to scope
		Scope scope = bdsThread.getScope();
		VarDeclaration fparam[] = getParameters().getVarDecl();
		for (int i = 0; i < fparam.length; i++) {
			String argName = fparam[i].getVarInit()[0].getVarName();
			scope.add(argName, vargs.getValue(i));
		}
	}

	public String getFunctionName() {
		return functionName;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public Statement getStatement() {
		return statement;
	}

	/**
	 * Get this function's type
	 */
	public TypeFunction getType() {
		if (funcType == null) funcType = new TypeFunction(this);
		return funcType;
	}

	public boolean isNative() {
		return false;
	}

	@Override
	protected void parse(ParseTree tree) {
		returnType = (Type) factory(tree, 0);
		functionName = tree.getChild(1).getText();

		// Parameters
		// child[2] = '('
		int maxParams = indexOf(tree, ")");
		parameters = new Parameters(this, null);
		parameters.parse(tree, 3, maxParams);
		// child[maxParams] = ')'

		statement = (Statement) factory(tree, maxParams + 1);
	}

	/**
	 * Run this function's statement
	 */
	protected void runFunction(BdsThread bdsThread) {
		bdsThread.run(statement);
		if (bdsThread.isCheckpointRecover()) return;

		// Not a standard 'return' statement? Make sure we are returning the right type.
		if (bdsThread.isReturn()) {
			bdsThread.setRunState(RunState.OK); // Restore 'OK' runState
		} else {
			Value retVal = bdsThread.getReturnValue();
			if (retVal == null || !retVal.getType().canCastTo(returnType)) {
				// No return value or not the right type?
				// Then force a default value for returnType
				// Note: This should be caught as a compile time error
				bdsThread.setReturnValue(returnType.newDefaultValue());
			}
		}
	}

	/**
	 * Run: Does nothing. A function declaration only declares
	 * a function, it doesn't do any real work.
	 * A FunctionCall actually makes the function 'run'. This
	 * is done by evaluating the call, which invokes
	 * 'FunctionDeclaration.runFunction()' method.
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Nothing to do (it's just a declaration)
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		if (!returnType.isVoid()) {
			List<BdsNode> returnStatements = findNodes(Return.class, true);
			if (returnStatements.isEmpty()) compilerMessages.add(this, "Function has no return statement", MessageType.ERROR);
		}
	}

	public String signature() {
		if (signature != null) return signature;
		signature = getType().signature();
		return signature;
	}

	public String signatureWithName() {
		return returnType + " " + functionName + "(" + parameters + ")";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(returnType + " " + functionName + "( " + parameters + " ) {\n");
		if (statement != null) sb.append(Gpr.prependEachLine("\t", statement.toString()));
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Function name collides with other names?
		if (symtab.getTypeLocal(functionName) != null) {
			compilerMessages.add(this, "Duplicate local name " + functionName, MessageType.ERROR);
		} else if ((functionName != null) && (getType() != null)) {
			// Add to parent symbol table, because the current
			// symbol table is for the function's body
			symtab.getParent().add(functionName, getType());
		}
	}
}
