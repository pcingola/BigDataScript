package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.ReferenceThis;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueArgs;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

/**
 * Operator 'new' calls a constructor method
 *
 * @author pcingola
 */
public class ExpressionNew extends MethodCall {

	// Operator 'new' calls a constructor method
	public ExpressionNew(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		expresionObj = (ReferenceThis) factory(tree, 0);
		functionName = tree.getChild(1).getText();
		// child[2] = '('
		args = new Args(this, null);
		args.parse(tree, 3, tree.getChildCount() - 1);
		// child[tree.getChildCount()] = ')'

		// Create empty (non-null) args
		if (args == null) args = new Args(this, null);
		args = Args.getArgsThis(args, expresionObj);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		// Calculate return types for expr and args
		Type exprType = expresionObj.returnType(symtab);
		args.returnType(symtab);

		// Find method
		if (exprType != null) {
			// Find function in class
			SymbolTable classSymTab = exprType.getSymbolTable();
			if (classSymTab != null) {
				TypeFunction tfunc = classSymTab.findFunction(functionName, args);

				// Not found? Try a 'regular' function
				if (tfunc == null) tfunc = symtab.findFunction(functionName, args);

				if (tfunc != null) {
					functionDeclaration = tfunc.getFunctionDeclaration();
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
				scope.add(argName, vargs.getValue(i));
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
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Could not find the function?
		if (functionDeclaration == null) compilerMessages.add(this, "Constructor " + signature() + " cannot be resolved", MessageType.ERROR);
	}

}
