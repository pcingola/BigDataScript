package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.ReferenceThis;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueArgs;
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

	@Override
	public ValueArgs evalArgs(BdsThread bdsThread) {
		ValueArgs vargs = super.evalArgs(bdsThread);

		// Create and add empty 'this' object
		Value newValue = expresionObj.getReturnType().newValue();
		vargs.setValue(0, newValue);

		return vargs;
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

		// Prepend 'this' argument to method signature
		expresionObj = new ReferenceThis(this, thisType);
		args = Args.getArgsThis(args, expresionObj);

		// Calculate return type for args
		args.returnType(symtab);

		// Find method
		functionDeclaration = findMethod(symtab, thisType);
		if (functionDeclaration != null) returnType = functionDeclaration.getReturnType();

		return returnType;
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
	public String toString() {
		return "new " + functionName + "( " + args + " )";
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Could not find the function?
		if (functionDeclaration == null) compilerMessages.add(this, "Constructor " + signature() + " cannot be resolved", MessageType.ERROR);
	}

}
