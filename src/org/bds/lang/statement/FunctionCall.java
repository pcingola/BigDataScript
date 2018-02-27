package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.Config;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.Args;
import org.bds.lang.BdsNode;
import org.bds.lang.Type;
import org.bds.lang.VarDeclaration;
import org.bds.lang.expression.Expression;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

/**
 * Program unit (usually a file)
 *
 * @author pcingola
 */
public class FunctionCall extends Expression {

	protected String functionName;
	protected Args args;
	protected FunctionDeclaration functionDeclaration;

	public FunctionCall(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Apply function to pre-calculated parameters
	 */
	public void apply(BdsThread bdsThread, Object arguments[]) {
		bdsThread.push(functionDeclaration.apply(bdsThread, arguments));
	}

	/**
	 * Evaluate function's arguments
	 */
	public void evalFunctionArguments(BdsThread bdsThread) {
		VarDeclaration fparam[] = functionDeclaration.getParameters().getVarDecl();
		Expression arguments[] = args.getArguments();

		// Evaluate all expressions
		Object values[] = new Object[fparam.length];
		for (int i = 0; i < fparam.length; i++) {
			bdsThread.run(arguments[i]);

			Object value = bdsThread.pop();
			value = fparam[i].getType().cast(value);
			values[i] = value;
		}

		bdsThread.push(values);
	}

	public FunctionDeclaration getFunctionDeclaration() {
		return functionDeclaration;
	}

	public String getFunctionName() {
		return functionName;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	public boolean isStopDebug() {
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
			functionDeclaration = (FunctionDeclaration) ssfunc.getValue();
			returnType = functionDeclaration.getReturnType();
		}

		return returnType;
	}

	/**
	 * Run an expression: I.e. evaluate the expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
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
