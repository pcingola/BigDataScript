package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.compile.CompilerMessages;
import org.bigDataScript.compile.CompilerMessage.MessageType;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.scope.Scope;
import org.bigDataScript.scope.ScopeSymbol;

/**
 * Program unit (usually a file)
 *
 * @author pcingola
 */
public class MethodCall extends FunctionCall {

	// Object that calls the mathod: obj.method(args)
	// This object is also the used as the first argument in
	// the function call (so this information is redundant, but
	// kept here for convinience)
	protected Expression expresionObj;

	public MethodCall(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		expresionObj = (Expression) factory(tree, 0);
		// child[1] = '.'
		functionName = tree.getChild(2).getText();
		// child[3] = '('
		args = new Args(this, null);
		args.parse(tree, 4, tree.getChildCount() - 1);
		// child[tree.getChildCount()] = ')'

		// Create empty (non-null) args
		if (args == null) args = new Args(this, null);
		args = Args.getArgsThis(args, expresionObj);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		// Calculate return types for expr and args
		Type exprType = expresionObj.returnType(scope);
		args.returnType(scope);

		// Find method
		if (exprType != null) {
			// Find function in class
			Scope classScope = Scope.getClassScope(exprType);
			if (classScope != null) {
				ScopeSymbol ssfunc = classScope.findFunction(functionName, args);

				// Not found? Try a 'regular' function
				if (ssfunc == null) ssfunc = scope.findFunction(functionName, args);

				if (ssfunc != null) {
					functionDeclaration = (FunctionDeclaration) ssfunc.getValue();
					returnType = functionDeclaration.getReturnType();
				}
			}

		}

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		VarDeclaration fparam[] = functionDeclaration.getParameters().getVarDecl();
		Expression arguments[] = args.getArguments();

		// Evaluate all expressions
		Object values[] = new Object[fparam.length];
		for (int i = 0; i < fparam.length; i++) {
			bdsThread.run(arguments[i]);
			Object value = bdsThread.pop();
			value = fparam[i].type.cast(value);
			values[i] = value;
		}

		if (!bdsThread.isCheckpointRecover()) {
			// Create new scope
			// TODO: Add class scope? (class variables & methods)
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
		functionDeclaration.runFunction(bdsThread);

		if (!bdsThread.isCheckpointRecover()) {
			// Get return value
			Object retVal = bdsThread.getReturnValue();

			// Back to old scope
			bdsThread.oldScope();

			// Return result
			bdsThread.push(retVal);
		}
	}

	@Override
	protected String signature() {
		StringBuilder sig = new StringBuilder();

		Type classType = expresionObj.getReturnType();
		sig.append(classType != null ? classType : "null");
		sig.append(".");
		sig.append(functionName);
		sig.append("(");
		for (int i = 1; i < args.size(); i++) {
			sig.append(args.getArguments()[i].getReturnType());
			if (i < (args.size() - 1)) sig.append(",");
		}
		sig.append(")");
		return sig.toString();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Could not find the function?
		if (functionDeclaration == null) compilerMessages.add(this, "Method " + signature() + " cannot be resolved", MessageType.ERROR);
	}

}
