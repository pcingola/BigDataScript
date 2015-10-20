package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Function declaration
 *
 * @author pcingola
 */
public class FunctionDeclaration extends StatementWithScope {

	protected String functionName;
	protected Type funcType;
	protected Parameters parameters;
	protected Statement statement;
	protected String signature;

	public FunctionDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Apply function to arguments, return function's result
	 */
	public Object apply(BdsThread bdsThread, Object values[]) {

		// Create scope and add function arguments
		if (!bdsThread.isCheckpointRecover()) {
			VarDeclaration fparam[] = getParameters().getVarDecl();

			// Create new scope
			bdsThread.newScope(this);

			// Add arguments to scope
			Scope scope = bdsThread.getScope();
			for (int i = 0; i < fparam.length; i++) {
				Type argType = fparam[i].type;
				String argName = fparam[i].getVarInit()[0].varName;
				scope.add(new ScopeSymbol(argName, argType, values[i]));
			}
		}

		// Run function body
		runFunction(bdsThread);

		// Get return value
		Object retVal = bdsThread.getReturnValue();

		// Restore old scope
		if (!bdsThread.isCheckpointRecover()) bdsThread.oldScope();

		// Return result
		return retVal;
	}

	/**
	 * Apply function to arguments, return function's result
	 */
	public Object apply(BdsThread bdsThread, Object value) {
		// Create scope and add function arguments
		if (!bdsThread.isCheckpointRecover()) {
			VarDeclaration fparam[] = getParameters().getVarDecl();

			// Create new scope
			bdsThread.newScope(this);

			// Add arguments to scope
			Scope scope = bdsThread.getScope();

			// Only one argument
			Type argType = fparam[0].type;
			String argName = fparam[0].getVarInit()[0].varName;
			scope.add(new ScopeSymbol(argName, argType, value));
		}

		// Run function body
		runFunction(bdsThread);
		if (bdsThread.isFatalError()) throw new RuntimeException("Fatal error");

		// Get return value
		Object retVal = bdsThread.getReturnValue();

		// Back to old scope
		if (!bdsThread.isCheckpointRecover()) bdsThread.oldScope();

		// Return result
		return retVal;
	}

	public String getFunctionName() {
		return functionName;
	}

	public Parameters getParameters() {
		return parameters;
	}

	@Override
	public Type getReturnType() {
		return returnType;
	}

	public Statement getStatement() {
		return statement;
	}

	/**
	 * Get this function's type
	 */
	public Type getType() {
		if (funcType == null) funcType = TypeFunc.get(this);
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
		} else if (!returnType.canCastObject(bdsThread.getReturnValue())) {
			// Not the right type? Force a default value of the right type
			bdsThread.setReturnValue(returnType.defaultValue());
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
	protected void sanityCheck(CompilerMessages compilerMessages) {
		if (!returnType.isVoid()) {
			List<BdsNode> returnStatements = findNodes(Return.class, true);
			if (returnStatements.isEmpty()) compilerMessages.add(this, "Function has no return statement", MessageType.ERROR);
		}
	}

	public String signature() {
		if (signature != null) return signature;
		signature = TypeFunc.signature(parameters, returnType);
		return signature;
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
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Function name collides with variable name?
		if (scope.getSymbolLocal(functionName) != null) compilerMessages.add(this, "Duplicate local name " + functionName, MessageType.ERROR);
	}

}
