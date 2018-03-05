package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueArgs;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

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
			Scope classScope = exprType.getScope();
			if (classScope != null) {
				ScopeSymbol ssfunc = classScope.findFunction(functionName, args);

				// Not found? Try a 'regular' function
				if (ssfunc == null) ssfunc = scope.findFunction(functionName, args);

				if (ssfunc != null) {
					functionDeclaration = (FunctionDeclaration) ssfunc.getValue().get();
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
		ValueArgs vargs = new ValueArgs(fparam.length);
		for (int i = 0; i < fparam.length; i++) {
			bdsThread.run(arguments[i]);
			Value value = bdsThread.pop();
			value = fparam[i].getType().cast(value);
			vargs.setValue(i, value);
		}

		if (!bdsThread.isCheckpointRecover()) {
			// Create new scope
			// TODO: Add class scope? (class variables & methods)
			bdsThread.newScope(this);

			// Add arguments to scope
			Scope scope = bdsThread.getScope();
			for (int i = 0; i < fparam.length; i++) {
				String argName = fparam[i].getVarInit()[0].getVarName();
				scope.add(new ScopeSymbol(argName, vargs.getValue(i)));
			}
		}

		// Run function body
		functionDeclaration.runFunction(bdsThread);

		if (!bdsThread.isCheckpointRecover()) {
			// Get return map
			Value retVal = bdsThread.getReturnValue();

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
