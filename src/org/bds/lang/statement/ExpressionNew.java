package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.Config;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.ReferenceThis;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueArgs;
import org.bds.lang.value.ValueClass;
import org.bds.run.BdsThread;
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
		argsStart = 1;
	}

	//	@Override
	//	public ValueArgs evalArgs(BdsThread bdsThread) {
	//		ValueArgs vargs = super.evalArgs(bdsThread);
	//
	//		// Create and add empty 'this' object
	//		Value newValue = expresionObj.getReturnType().newValue();
	//		vargs.setValue(0, newValue);
	//
	//		return vargs;
	//	}

	@Override
	protected Value evalThis(BdsThread bdsThread) {
		return expresionObj.getReturnType().newValue();
	}

	/**
	 * Run field initialization
	 */
	public ValueClass initializeFields(BdsThread bdsThread) {
		ValueClass vthis = (ValueClass) bdsThread.getScope().getValue(ClassDeclaration.THIS);
		TypeClass tthis = (TypeClass) vthis.getType();

		FieldDeclaration fieldDecls[] = tthis.getClassDeclaration().getFieldDecl();
		for (FieldDeclaration fieldDecl : fieldDecls)
			bdsThread.run(fieldDecl);

		return vthis;
	}

	@Override
	protected void parse(ParseTree tree) {
		expresionObj = null; // Note that object 'this' does not exists yet
		functionName = tree.getChild(1).getText(); // Same as class name

		// Parse arguments
		args = new Args(this, null);
		args.parse(tree, 3, tree.getChildCount() - 1);

		// Create empty args
		if (args == null) args = new Args(this, null);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		// Calculate return types for expr and args
		// Note that expresionObj is null in ExpressionNew (which is a MethodCall)
		TypeClass thisType = (TypeClass) symtab.getType(functionName); // Constructors have same name as class
		if (thisType == null) return null;
		returnType = thisType;

		// Prepend 'this' argument to method signature
		expresionObj = new ReferenceThis(this, thisType);
		args = Args.getArgsThis(args, expresionObj);

		// Calculate return type for args
		args.returnType(symtab);

		// Find method
		functionDeclaration = findMethod(symtab, thisType);

		return returnType;
	}

	/**
	 * Run an expression: I.e. evaluate the expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		try {
			// Evaluate function arguments
			ValueArgs arguments = evalArgs(bdsThread);

			// Apply function to parameters
			functionDeclaration.apply(bdsThread, arguments);

			// Constructors return new objects regardless of type declaration
			Value vthis = arguments.getValue(0);
			bdsThread.push(vthis);
		} catch (Throwable t) {
			if (Config.get().isDebug()) t.printStackTrace();
			bdsThread.fatalError(this, t);
		}
	}

	//	@Override
	//	public void runStep(BdsThread bdsThread) {
	//		// Evaluate all expressions
	//		ValueArgs vargs = evalArgs(bdsThread);
	//
	//		// Create scope
	//		if (!bdsThread.isCheckpointRecover()) functionDeclaration.createScopeAddArgs(bdsThread, vargs);
	//
	//		// Initialize fields
	//		// Evaluate object
	//		Value vthis = initializeFields(bdsThread);
	//
	//		// Run method body
	//		functionDeclaration.runFunction(bdsThread);
	//
	//		if (!bdsThread.isCheckpointRecover()) {
	//			// Get return map
	//			Value retVal = bdsThread.getReturnValue();
	//
	//			// Back to old scope
	//			bdsThread.oldScope();
	//
	//			// Return result
	//			bdsThread.push(retVal);
	//		}
	//	}

	//	/**
	//	 * Evaluate an expression
	//	 */
	//	@Override
	//	public void runStep(BdsThread bdsThread) {
	//		//		// Evaluate object
	//		//		bdsThread.run(expresionObj);
	//		//		Value vthis = bdsThread.pop();
	//		//
	//		//		// Is object 'null'?
	//		//		if (vthis == null //
	//		//				|| (expresionObj.isClass() && ((ValueClass) vthis).isNull()) //
	//		//		) {
	//		//			bdsThread.fatalError(this, "Null pointer: Cannot call method '" + expresionObj.getReturnType() + "." + functionName + "' in null object");
	//		//		}
	//
	//		// Evaluate all expressions
	//		ValueArgs vargs = evalArgs(bdsThread);
	//
	//		Value vthis = initializeFields(bdsThread);
	//		vargs.setValue(0, vthis); // Set 'this' as first argument
	//
	//		// Create scope
	//		if (!bdsThread.isCheckpointRecover()) functionDeclaration.createScopeAddArgs(bdsThread, vargs);
	//
	//		// Run method body
	//		functionDeclaration.runFunction(bdsThread);
	//
	//		if (!bdsThread.isCheckpointRecover()) {
	//			// Get return map
	//			Value retVal = bdsThread.getReturnValue();
	//
	//			// Back to old scope
	//			bdsThread.oldScope();
	//
	//			// Return result
	//			bdsThread.push(retVal);
	//		}
	//	}

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
	public String toString() {
		return "new " + functionName + "( " + args + " )";
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Could not find the function?
		if (functionDeclaration == null) compilerMessages.add(this, "Constructor " + signature() + " cannot be resolved", MessageType.ERROR);
	}

}
