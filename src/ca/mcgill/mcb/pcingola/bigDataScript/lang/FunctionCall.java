package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

/**
 * Program unit (usually a file)
 * 
 * @author pcingola
 */
public class FunctionCall extends Expression {

	String functionName;
	Args args;
	FunctionDeclaration functionDeclaration;

	public FunctionCall(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {

		VarDeclaration fparam[] = functionDeclaration.getParameters().getVarDecl();
		Expression arguments[] = args.getArguments();

		// Evaluate all expressions
		Object values[] = new Object[fparam.length];
		for (int i = 0; i < fparam.length; i++) {
			Object value = arguments[i].eval(csThread);
			value = fparam[i].type.cast(value);
			values[i] = value;
		}

		// Create new scope
		csThread.newScope();

		// Add arguments to scope
		Scope scope = csThread.getScope();
		for (int i = 0; i < fparam.length; i++) {
			Type argType = fparam[i].type;
			String argName = fparam[i].getVarInit()[0].varName;
			scope.add(new ScopeSymbol(argName, argType, values[i]));
		}

		// Run function body
		functionDeclaration.runFunction(csThread);

		// Get return value
		Object retVal = csThread.getReturnValue();

		// Back to old scope
		csThread.oldScope();

		// Return result
		return retVal;
	}

	/**
	 * Find a function that matches this call
	 * @param scope
	 * @return
	 */
	ScopeSymbol findFunction(Scope scope, String functionName, Args args) {
		// Retrieve all functions with the same name
		List<ScopeSymbol> ssfuncs = scope.getFunctions(functionName);

		// For each function...
		ScopeSymbol bestSsfunc = null;
		int bestScore = Integer.MAX_VALUE;
		for (ScopeSymbol ssfunc : ssfuncs) {
			boolean ok = false;
			int score = 0;
			TypeFunc sstype = (TypeFunc) ssfunc.getType();

			// Find the ones with the same number of parameters
			int argc = args.size();
			if (argc == sstype.getParameters().size()) {
				ok = true;

				// Find the ones with matching exact parameters
				for (int i = 0; i < args.size(); i++) {
					Type argType = args.getArguments()[i].getReturnType();
					Type funcType = sstype.getParameters().getType(i);

					// Same argument?
					if (!argType.equals(funcType)) {
						// Can we cast?
						if (argType.canCast(funcType)) score++; // Add a point if we can cast
						else ok = false;
					}
				}
			}

			// Found anything?
			if (ok) {
				// Perfect match? Don't look any further
				if (score == 0) return ssfunc;

				// Get the one with less argument casts
				if (score < bestScore) {
					bestScore = score;
					bestSsfunc = ssfunc;
				}
			}
		}

		return bestSsfunc;
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

		ScopeSymbol ssfunc = findFunction(scope, functionName, args);
		if (ssfunc != null) {
			functionDeclaration = ((TypeFunc) ssfunc.getType()).getFunctionDeclaration();
			returnType = functionDeclaration.getReturnType();
		}

		return returnType;
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
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {

		// Could not find the function?
		if (functionDeclaration == null) compilerMessages.add(this, "Function " + signature() + " cannot be resolved", MessageType.ERROR);
	}
}
