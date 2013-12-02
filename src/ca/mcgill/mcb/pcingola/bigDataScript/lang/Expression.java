package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

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
	 * @param expr
	 * @param compilerMessages
	 */
	protected boolean canCastBool() {
		return ((returnType != null) && returnType.canCast(Type.BOOL));
	}

	/**
	 * Can returnType be casted to int?
	 * @param expr
	 * @param compilerMessages
	 */
	protected boolean canCastInt() {
		return ((returnType != null) && returnType.canCast(Type.INT));
	}

	/**
	 * Can returnType be casted to real?
	 * @param expr
	 * @param compilerMessages
	 */
	protected boolean canCastReal() {
		return ((returnType != null) && returnType.canCast(Type.REAL));
	}

	/**
	 * Check that this expression can be casted to bool
	 * Add a compile error otherwise
	 * @param expr
	 * @param compilerMessages
	 */
	protected void checkCanCastBool(CompilerMessages compilerMessages) {
		if ((returnType != null) && !returnType.canCast(Type.BOOL)) compilerMessages.add(this, "Cannot cast " + returnType + " to bool", MessageType.ERROR);
	}

	/**
	 * Check that this expression can be casted to int 
	 * Add a compile error otherwise
	 * @param expr
	 * @param compilerMessages
	 */
	protected void checkCanCastInt(CompilerMessages compilerMessages) {
		if ((returnType != null) && !returnType.canCast(Type.INT)) compilerMessages.add(this, "Cannot cast " + returnType + " to int", MessageType.ERROR);
	}

	/**
	 * Check that this expression can be casted to either int or real
	 * Add a compile error otherwise
	 * @param expr
	 * @param compilerMessages
	 */
	protected void checkCanCastIntOrReal(CompilerMessages compilerMessages) {
		if ((returnType != null) //
				&& (!returnType.canCast(Type.INT) && !returnType.canCast(Type.REAL)) //
		) compilerMessages.add(this, "Cannot cast " + returnType + " to int or real", MessageType.ERROR);
	}

	/**
	 * Evaluate an expression, return result
	 */
	public Object eval(BigDataScriptThread csThread) {
		throw new RuntimeException("Unplemented method for class " + getClass().getSimpleName());
	}

	/**
	 * Evaluate an expression as an 'bool'
	 * @param scope
	 * @return
	 */
	public boolean evalBool(BigDataScriptThread csThread) {
		Object ret = eval(csThread);
		return (Boolean) Type.BOOL.cast(ret);
	}

	/**
	 * Evaluate an expression as an 'int'
	 * @param scope
	 * @return
	 */
	public long evalInt(BigDataScriptThread csThread) {
		Object ret = eval(csThread);
		return (Long) Type.INT.cast(ret);
	}

	/**
	 * Evaluate an expression as an 'real'
	 * @param scope
	 * @return
	 */
	public double evalReal(BigDataScriptThread csThread) {
		Object ret = eval(csThread);
		return (Double) Type.REAL.cast(ret);
	}

	/**
	 * Evaluate an expression as an 'bool'
	 * @param scope
	 * @return
	 */
	public String evalString(BigDataScriptThread csThread) {
		Object ret = eval(csThread);
		return (String) Type.STRING.cast(ret);
	}

	/**
	 * Which type does this expression return?
	 * @return
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * Is return type bool?
	 * @return
	 */
	protected boolean isBool() {
		return (returnType != null) && returnType.isBool();
	}

	/**
	 * Is return type int?
	 * @return
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
	 * @return
	 */
	protected boolean isReal() {
		return (returnType != null) && returnType.isReal();
	}

	/**
	 * Do all subordinate expressions have a non-null return type?
	 * @return
	 */
	protected boolean isReturnTypesNotNull() {
		throw new RuntimeException("This method should never be invoked! Class: " + getClass().getSimpleName());
	}

	/**
	 * Is return type string?
	 * @return
	 */
	protected boolean isString() {
		return (returnType != null) && returnType.isString();
	}

	/**
	 * Calculate return type and assign it to 'returnType' variable.
	 * 
	 * @param scope
	 * @return
	 */
	public Type returnType(Scope scope) {
		throw new RuntimeException("This method should never be invoked! Missing implementation for class " + getClass().getSimpleName());
	}

	/**
	 * Run an expression: I.e. evaluate the expression
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		eval(csThread);
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
	 * 
	 * @param scope
	 * @param compilerMessages
	 * @return
	 */
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
