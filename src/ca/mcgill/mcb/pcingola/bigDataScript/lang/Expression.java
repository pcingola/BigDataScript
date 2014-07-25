package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Expression: A statement that returns a value
 *
 * @author pcingola
 */
public class Expression extends Statement {

	Type returnType;

	public Expression(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Can returnType be casted to bool?
	 */
	protected boolean canCastBool() {
		return ((returnType != null) && returnType.canCast(Type.BOOL));
	}

	/**
	 * Can returnType be casted to int?
	 */
	protected boolean canCastInt() {
		return ((returnType != null) && returnType.canCast(Type.INT));
	}

	/**
	 * Can returnType be casted to real?
	 */
	protected boolean canCastReal() {
		return ((returnType != null) && returnType.canCast(Type.REAL));
	}

	/**
	 * Check that this expression can be casted to bool
	 * Add a compile error otherwise
	 */
	protected void checkCanCastBool(CompilerMessages compilerMessages) {
		if ((returnType != null) && !returnType.canCast(Type.BOOL)) compilerMessages.add(this, "Cannot cast " + returnType + " to bool", MessageType.ERROR);
	}

	/**
	 * Check that this expression can be casted to int
	 * Add a compile error otherwise
	 */
	protected void checkCanCastInt(CompilerMessages compilerMessages) {
		if ((returnType != null) && !returnType.canCast(Type.INT)) compilerMessages.add(this, "Cannot cast " + returnType + " to int", MessageType.ERROR);
	}

	/**
	 * Check that this expression can be casted to either int or real
	 * Add a compile error otherwise
	 */
	protected void checkCanCastIntOrReal(CompilerMessages compilerMessages) {
		if ((returnType != null) //
				&& (!returnType.canCast(Type.INT) //
				&& !returnType.canCast(Type.REAL)) //
		) compilerMessages.add(this, "Cannot cast " + returnType + " to int or real", MessageType.ERROR);
	}

	/**
	 * Evaluate an expression, return result
	 */
	public Object eval(BigDataScriptThread bdsThread) {
		throw new RuntimeException("Unplemented method 'eval' for class " + getClass().getSimpleName());
	}

	/**
	 * Evaluate an expression as an 'bool'
	 */
	public boolean evalBool(BigDataScriptThread bdsThread) {
		Object ret = eval(bdsThread);
		return (Boolean) Type.BOOL.cast(ret);
	}

	/**
	 * Evaluate an expression as an 'int'
	 */
	public long evalInt(BigDataScriptThread bdsThread) {
		Object ret = eval(bdsThread);
		return (Long) Type.INT.cast(ret);
	}

	/**
	 * Evaluate an expression as an 'real'
	 */
	public double evalReal(BigDataScriptThread bdsThread) {
		Object ret = eval(bdsThread);
		return (Double) Type.REAL.cast(ret);
	}

	/**
	 * Evaluate an expression as an 'bool'
	 */
	public String evalString(BigDataScriptThread bdsThread) {
		Object ret = eval(bdsThread);
		return (String) Type.STRING.cast(ret);
	}

	/**
	 * Which type does this expression return?
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * Is return type bool?
	 */
	protected boolean isBool() {
		return (returnType != null) && returnType.isBool();
	}

	/**
	 * Is return type int?
	 */
	protected boolean isInt() {
		return (returnType != null) && returnType.isInt();
	}

	protected boolean isList() {
		return (returnType != null) && returnType.isList();
	}

	protected boolean isList(Type baseType) {
		return (returnType != null) && returnType.isList(baseType);
	}

	protected boolean isMap() {
		return (returnType != null) && returnType.isMap();
	}

	protected boolean isMap(Type baseType) {
		return (returnType != null) && returnType.isMap(baseType);
	}

	/**
	 * Is return type real?
	 */
	protected boolean isReal() {
		return (returnType != null) && returnType.isReal();
	}

	/**
	 * Do all subordinate expressions have a non-null return type?
	 */
	protected boolean isReturnTypesNotNull() {
		throw new RuntimeException("This method should never be invoked! Class: " + getClass().getSimpleName());
	}

	/**
	 * Is return type string?
	 */
	protected boolean isString() {
		return (returnType != null) && returnType.isString();
	}

	/**
	 * Calculate return type and assign it to 'returnType' variable.
	 */
	public Type returnType(Scope scope) {
		throw new RuntimeException("This method should never be invoked! Missing implementation for class " + getClass().getSimpleName());
	}

	/**
	 * Run an expression: I.e. evaluate the expression
	 */
	@Override
	protected RunState runStep(BigDataScriptThread bdsThread) {
		try {
			eval(bdsThread);
		} catch (Throwable t) {
			if (Config.get().isDebug()) t.printStackTrace();
			bdsThread.fatalError(this, t);
			return RunState.FATAL_ERROR;
		}
		return RunState.OK;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		// Are return types non-null?
		// Note: null returnTypes happen if variables are missing.
		if (isReturnTypesNotNull()) typeCheckNotNull(scope, compilerMessages);
	}

	/**
	 * Type checking.
	 * This is invoked once we made sure all return types are non null (so we don't have to check for null every time)
	 */
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
