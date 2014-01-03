package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * A reference to a list/array variable. E.g. list[3]
 * 
 * @author pcingola
 */
public class VarReferenceList extends Reference {

	VarReference variable;
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
		if ((idx < 0) || (idx >= list.size())) throw new RuntimeException("Trying to access element number " + idx + " from list '" + getVariableName() + "' (list size: " + list.size() + ").");
		return list.get(idx);
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

	/**
	 * Get symbol from scope
	 * @param scope
	 * @return
	 */
	@Override
	public ScopeSymbol getScopeSymbol(Scope scope) {
		return variable.getScopeSymbol(scope);
	}

	public Type getType(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return ss.getType();
	}

	@Override
	public String getVariableName() {
		return variable.getVariableName();
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		variable = (VarReference) factory(tree, 0);
		// child[1] = '['
		expressionIdx = (Expression) factory(tree, 2);
		// child[3] = ']'
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		expressionIdx.returnType(scope);
		Type nameType = variable.returnType(scope);

		if (nameType.isList()) returnType = ((TypeList) nameType).getBaseType();

		return returnType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setValue(BigDataScriptThread csThread, Object value) {
		int idx = evalIndex(csThread);
		ArrayList<Object> list = getList(csThread.getScope());

		// Make sure the array is big enough to hold the data
		if (idx >= list.size()) {
			TypeList type = (TypeList) getType(csThread.getScope());
			Type baseType = type.getBaseType();
			list.ensureCapacity(idx + 1);

			while (list.size() <= idx)
				list.add(baseType.defaultValue());
		}

		list.set(idx, value);
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		if ((variable.getReturnType() != null) && !variable.getReturnType().isList()) compilerMessages.add(this, "Symbol '" + variable + "' is not a list/array", MessageType.ERROR);
		if (expressionIdx != null) expressionIdx.checkCanCastInt(compilerMessages);
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
