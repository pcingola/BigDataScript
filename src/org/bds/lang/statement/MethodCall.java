package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueArgs;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * Method invocation
 *
 * @author pcingola
 */
public class MethodCall extends FunctionCall {

	// Object that calls the method: obj.method(args)
	// This object is also the used as the first argument in
	// the function call (so this information is redundant, but
	// kept here for convinience)
	protected Expression expresionObj;

	public MethodCall(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Find method (or function) matching the signature
	 */
	protected FunctionDeclaration findMethod(SymbolTable symtab, Type type) {
		if (type == null) return null;

		// Find function in class
		SymbolTable classSymTab = type.getSymbolTable();
		if (classSymTab == null) return null;

		TypeFunction tfunc = classSymTab.findFunction(functionName, args);

		// Not found? Try a 'regular' function
		if (tfunc == null) tfunc = symtab.findFunction(functionName, args);

		// Not found
		if (tfunc == null) return null;

		return tfunc.getFunctionDeclaration();
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
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		// Calculate return types for expr and args
		// Note that expresionObj is null in ExpressionNew (which is a MethodCall)
		Type exprType = (expresionObj != null ? expresionObj.returnType(symtab) : null);
		args.returnType(symtab);

		// Find method
		functionDeclaration = findMethod(symtab, exprType);
		if (functionDeclaration != null) returnType = functionDeclaration.getReturnType();

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Evaluate all expressions
		ValueArgs vargs = evalArgs(bdsThread);

		// Create scope
		if (!bdsThread.isCheckpointRecover()) functionDeclaration.createScopeAddArgs(bdsThread, vargs);

		// Run method body
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
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Could not find the function?
		if (functionDeclaration == null) compilerMessages.add(this, "Method " + signature() + " cannot be resolved", MessageType.ERROR);
	}

}
