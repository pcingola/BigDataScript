package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * Program unit (usually a file)
 *
 * @author pcingola
 */
public class FunctionCall extends Expression {

	protected String functionName;
	protected Args args;
	protected FunctionDeclaration functionDeclaration;

	public FunctionCall(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Apply function to pre-calculated parameters
	 */
	public void apply(BigDataScriptThread bdsThread, Object arguments[]) {
		bdsThread.push(functionDeclaration.apply(bdsThread, arguments));
	}

	/**
	 * Evaluate function's arguments
	 */
	public void evalFunctionArguments(BigDataScriptThread bdsThread) {
		VarDeclaration fparam[] = functionDeclaration.getParameters().getVarDecl();
		Expression arguments[] = args.getArguments();

		// Evaluate all expressions
		Object values[] = new Object[fparam.length];
		for (int i = 0; i < fparam.length; i++) {
			bdsThread.run(arguments[i]);
			Object value = bdsThread.pop();

			if (!bdsThread.isCheckpointRecover()) {
				value = fparam[i].type.cast(value);
				values[i] = value;
			}
		}

		bdsThread.push(values);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		functionName = tree.getChild(0).getText();
		// child[1] is '('

		args = new Args(this, null);
		args.parse(tree, 2, tree.getChildCount() - 1);

		if (args == null) args = new Args(this, null);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		args.returnType(scope);

		ScopeSymbol ssfunc = scope.findFunction(functionName, args);
		if (ssfunc != null) {
			functionDeclaration = ((TypeFunc) ssfunc.getType()).getFunctionDeclaration();
			returnType = functionDeclaration.getReturnType();
		}

		return returnType;
	}

	/**
	 * Run an expression: I.e. evaluate the expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		try {
			// Evaluate function arguments
			evalFunctionArguments(bdsThread);
			Object arguments[] = (Object[]) bdsThread.pop();

			// Apply function to parameters
			bdsThread.push(functionDeclaration.apply(bdsThread, arguments));
		} catch (Throwable t) {
			if (Config.get().isDebug()) t.printStackTrace();
			bdsThread.fatalError(this, t);
		}
	}

	protected String signature() {
		StringBuilder sig = new StringBuilder();
		sig.append(functionName);
		sig.append("(");
		for (int i = 0; i < args.size(); i++) {
			sig.append(args.getArguments()[i].getReturnType());
			if (i < (args.size() - 1)) sig.append(",");
		}
		sig.append(")");
		return sig.toString();
	}

	@Override
	public String toString() {
		return functionName + "( " + args + " )";
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Could not find the function?
		if (functionDeclaration == null) compilerMessages.add(this, "Function " + signature() + " cannot be resolved", MessageType.ERROR);
	}
}
