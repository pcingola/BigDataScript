package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * A reference to a list/array variable. E.g. list[3]
 * 
 * @author pcingola
 */
public class VarReferenceMap extends Expression {

	VarReference name;
	Expression expressionKey;

	public VarReferenceMap(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object eval(BigDataScriptThread csThread) {
		String key = evalKey(csThread);
		HashMap map = getMap(csThread.getScope());
		return map.get(key);
	}

	/**
	 * Return index evaluation
	 * @param csThread
	 * @return
	 */
	public String evalKey(BigDataScriptThread csThread) {
		return expressionKey.evalString(csThread);
	}

	@SuppressWarnings("rawtypes")
	public HashMap getMap(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return (HashMap) ss.getValue();
	}

	/**
	 * Get symbol from scope
	 * @param scope
	 * @return
	 */
	public ScopeSymbol getScopeSymbol(Scope scope) {
		return name.getScopeSymbol(scope);
	}

	public Type getType(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return ss.getType();
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		name = (VarReference) factory(tree, 0);
		// child[1] = '{'
		expressionKey = (Expression) factory(tree, 2);
		// child[3] = '}'
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		expressionKey.returnType(scope);
		Type nameType = name.returnType(scope);

		if (nameType.isMap()) returnType = ((TypeMap) nameType).getBaseType();

		return returnType;
	}

	@SuppressWarnings("unchecked")
	public void setValue(BigDataScriptThread csThread, Object value) {
		String key = evalKey(csThread);
		HashMap map = getMap(csThread.getScope());
		map.put(key, value);
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		// Note: We do not perform type checking on 'key' since everything can be cast to a string
		if ((name.getReturnType() != null) && !name.getReturnType().isMap()) compilerMessages.add(this, "Symbol '" + name + "' is not a map", MessageType.ERROR);
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
