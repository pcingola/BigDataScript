package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

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

	public FunctionDeclaration(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Apply function to arguments, return function's result
	 */
	public Object apply(BigDataScriptThread bdsThread, Object values[]) {
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

		// Run function body
		RunState rstate = runFunction(bdsThread);
		if (rstate == RunState.FATAL_ERROR) throw new RuntimeException("Fatal error");

		// Get return value
		Object retVal = bdsThread.getReturnValue();

		// Back to old scope
		bdsThread.oldScope();

		// Return result
		return retVal;
	}

	/**
	 * Apply function to arguments, return function's result
	 */
	public Object apply(BigDataScriptThread bdsThread, Object value) {
		VarDeclaration fparam[] = getParameters().getVarDecl();

		// Create new scope
		bdsThread.newScope(this);

		// Add arguments to scope
		Scope scope = bdsThread.getScope();

		// Only one argument
		Type argType = fparam[0].type;
		String argName = fparam[0].getVarInit()[0].varName;
		scope.add(new ScopeSymbol(argName, argType, value));

		// Run function body
		RunState rstate = runFunction(bdsThread);
		if (rstate == RunState.FATAL_ERROR) throw new RuntimeException("Fatal error");

		// Get return value
		Object retVal = bdsThread.getReturnValue();

		// Back to old scope
		bdsThread.oldScope();

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
	protected RunState runFunction(BigDataScriptThread bdsThread) {
		RunState runState = statement.run(bdsThread);

		// Not a standard 'return' statement? Make sure we are returning the right type.
		if ((runState != RunState.RETURN) && !returnType.canCastObject(bdsThread.getReturnValue())) {
			// Not the right type? Force a default value of the right type
			bdsThread.setReturnValue(returnType.defaultValue());
		}

		return runState;
	}

	/**
	 * Run: Does nothing. A function declaration only declares
	 * a function, it doesn't do any real work.
	 * A FunctionCall actually makes the function 'run'. This
	 * is done by evaluating the call, which invokes
	 * 'FunctionDeclaration.runFunction()' method.
	 */
	@Override
	protected RunState runStep(BigDataScriptThread bdsThread) {
		// Nothing to do (it's just a declaration)
		return RunState.OK;
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		if (!returnType.isVoid()) {
			List<BigDataScriptNode> returnStatements = findNodes(Return.class, true);
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
		sb.append(returnType + " " + functionName + "( " + parameters + " )\t{\n");
		sb.append(statement);
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Function name collides with variable name?
		if (scope.getSymbolLocal(functionName) != null) compilerMessages.add(this, "Duplicate local name " + functionName, MessageType.ERROR);

	}

}
