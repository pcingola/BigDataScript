package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.expression.ReferenceThis;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.value.ValueFunction;
import org.bds.symbol.SymbolTable;

/**
 * Function call
 *
 * Note: A method call is the same as a function call
 *       using 'this' as first argument.
 *
 * @author pcingola
 */
public class FunctionCall extends Expression {

	private static final long serialVersionUID = 6677584957911766940L;

	protected String functionName;
	protected Args args;
	protected FunctionDeclaration functionDeclaration;
	protected int argsStart;

	public FunctionCall(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		argsStart = 0;
	}

	/**
	 * Find method (or function) matching the signature
	 */
	protected FunctionDeclaration findMethod(SymbolTable symtab, Type type, Args args) {
		if (type == null) return null;

		// Find function in class or any super-class
		if (type.isClass()) {
			// A class' method
			for (TypeClass tc = (TypeClass) type; tc != null && tc.hasClassDeclaration(); tc = tc.getParentTypeClass()) {
				// Get symbol table
				SymbolTable classSymTab = tc.getSymbolTable();
				if (classSymTab == null) return null;

				// Find method in class symbol table
				ValueFunction vfunc = classSymTab.findFunction(functionName, args);
				if (vfunc != null) return vfunc.getFunctionDeclaration();

				// Try a 'regular' function, e.g. 'this.funcName()' => 'funcName(this)'
				vfunc = symtab.findFunction(functionName, args);
				if (vfunc != null) return vfunc.getFunctionDeclaration();
			}
		} else {
			// Another type of method call, e.g.: string.length()
			SymbolTable classSymTab = type.getSymbolTable();
			if (classSymTab == null) return null;

			// Find method in class symbol table
			ValueFunction vfunc = classSymTab.findFunction(functionName, args);
			if (vfunc != null) return vfunc.getFunctionDeclaration();
		}

		// Try a 'regular' function
		ValueFunction vfunc = symtab.findFunction(functionName, args);
		if (vfunc != null) return vfunc.getFunctionDeclaration();

		// Not found
		return null;
	}

	public Args getArgs() {
		return args;
	}

	public FunctionDeclaration getFunctionDeclaration() {
		return functionDeclaration;
	}

	public String getFunctionName() {
		return functionName;
	}

	protected boolean isMethodCall() {
		return false;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return true;
	}

	/**
	 * Is this a 'super.f()' method call?
	 */
	protected boolean isSuper() {
		return false;
	}

	@Override
	protected void parse(ParseTree tree) {
		functionName = tree.getChild(0).getText();
		// child[1] = '('
		args = new Args(this, null);
		args.parse(tree, 2, tree.getChildCount() - 1);
		// child[tree.getChildCount()] = ')'

		if (args == null) args = new Args(this, null);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		args.returnType(symtab);

		ValueFunction tfunc = symtab.findFunction(functionName, args);
		if (tfunc != null) {
			functionDeclaration = tfunc.getFunctionDeclaration();
			returnType = functionDeclaration.getReturnType();
		} else if (symtab.hasType(ClassDeclaration.VAR_THIS)) {
			// Is this function call within a class?
			// Try "this.functionName(...)", i.e. implicit 'this' object
			TypeClass typeThis = (TypeClass) symtab.resolve(ClassDeclaration.VAR_THIS);
			// Add first argument ('this')
			Expression expresionThis = new ReferenceThis(this, typeThis);
			Args argsThis = Args.getArgsThis(args, expresionThis);
			functionDeclaration = findMethod(symtab, typeThis, argsThis);

			if (functionDeclaration != null) { // Found method
				returnType = functionDeclaration.getReturnType();
				args = argsThis;
			}
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
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());
		sb.append(args.toAsm());
		sb.append(toAsmCall());
		return sb.toString();
	}

	public String toAsmCall() {
		return toAsmCallType() + ' ' //
				+ functionDeclaration.signature() //
				+ "\n";
	}

	public String toAsmCallType() {
		if (functionDeclaration.isNative()) return "callnative";
		if (functionDeclaration.isMethod()) { //
			if (isMethodCall() && isSuper()) return "callsuper";
			return "callmethod";
		}
		return "call";
	}

	@Override
	public String toString() {
		return functionName + "( " + args + " )";
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Could not find the function?
		if (functionDeclaration == null) {
			compilerMessages.add(this, "Function " + signature() + " cannot be resolved", MessageType.ERROR);
		}
	}
}
