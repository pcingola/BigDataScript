package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A reference to a list/array variable. E.g. list[3]
 * 
 * @author pcingola
 */
public class VarReferenceList extends Expression {

	VarReference name;
	Expression expressionIdx;

	public VarReferenceList(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object eval(BigDataScriptThread csThread) {
		int idx = evalIndex(csThread);
		ArrayList list = getList(csThread.getScope());
		return list.get(idx);
	}

	
	@SuppressWarnings("unchecked")
	public void setValue(BigDataScriptThread csThread,Object value) {
	int idx = evalIndex(csThread);
	ArrayList<Object> list = getList(csThread.getScope());
	
	// Make sure the array is big enough to hold the data
	if( idx>= list.size()) {
		TypeList type = (TypeList) getType(csThread.getScope());
		Type baseType = type.getBaseType();
		list.ensureCapacity(idx+1);
		
		while(list.size()<=idx )list.add(baseType.defaultValue());
	}
	
	list.set(idx, value);
	}
	
	
	/**
	 * Return index evaluation
	 * @param csThread
	 * @return
	 */
	public int evalIndex(BigDataScriptThread csThread) {
		return (int) expressionIdx.evalInt(csThread);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getList(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		ss.getType();
		return (ArrayList) ss.getValue();
	}

	public Type getType(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return ss.getType();
	}

	/**
	 * Get symbol from scope
	 * @param scope
	 * @return
	 */
	public ScopeSymbol getScopeSymbol(Scope scope) {
		return name.getScopeSymbol(scope);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		name = (VarReference) factory(tree, 0);
		// child[1] = '['
		expressionIdx = (Expression) factory(tree, 2);
		// child[3] = ']'
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		expressionIdx.returnType(scope);
		Type nameType = name.returnType(scope);

		if (nameType.isList()) returnType = ((TypeList) nameType).getBaseType();

		return returnType;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		if ((name.getReturnType() != null) && !name.getReturnType().isList()) compilerMessages.add(this, "Symbol '" + name + "' is not a list/array", MessageType.ERROR);
		if (expressionIdx != null) expressionIdx.checkCanCastInt(compilerMessages);
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
