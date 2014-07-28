package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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
	public Object eval(BigDataScriptThread bdsThread) {
		int idx = evalIndex(bdsThread);
		ArrayList list = getList(bdsThread.getScope());
		if ((idx < 0) || (idx >= list.size())) throw new RuntimeException("Trying to access element number " + idx + " from list '" + getVariableName() + "' (list size: " + list.size() + ").");
		return list.get(idx);
	}

	/**
	 * Return index evaluation
	 */
	public int evalIndex(BigDataScriptThread bdsThread) {
		return (int) expressionIdx.evalInt(bdsThread);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getList(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		ss.getType();
		return (ArrayList) ss.getValue();
	}

	/**
	 * Get symbol from scope
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
	public void parse(String str) {
		int idx1 = str.indexOf('[');
		int idx2 = str.lastIndexOf(']');
		if ((idx1 <= 0) || (idx2 <= idx1)) throw new RuntimeException("Cannot parse list reference '" + str + "'");

		// Create VarReference
		String varName = str.substring(0, idx1);
		variable = new VarReference(this, null);
		variable.parse(varName);

		// Create index expression
		String idxStr = str.substring(idx1 + 1, idx2);

		if (idxStr.startsWith("$")) {
			// We have to interpolate this string
			expressionIdx = InterpolateVars.factory(this, idxStr.substring(1));
		} else {
			// String literal
			LiteralInt exprIdx = new LiteralInt(this, null);
			exprIdx.setValue(Gpr.parseLongSafe(idxStr));
			expressionIdx = exprIdx;
		}
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		expressionIdx.returnType(scope);
		Type nameType = variable.returnType(scope);

		if (nameType == null) return null;
		if (nameType.isList()) returnType = ((TypeList) nameType).getBaseType();

		return returnType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setValue(BigDataScriptThread bdsThread, Object value) {
		int idx = evalIndex(bdsThread);
		ArrayList<Object> list = getList(bdsThread.getScope());

		// Make sure the array is big enough to hold the data
		if (idx >= list.size()) {
			TypeList type = (TypeList) getType(bdsThread.getScope());
			Type baseType = type.getBaseType();
			list.ensureCapacity(idx + 1);

			while (list.size() <= idx)
				list.add(baseType.defaultValue());
		}

		list.set(idx, value);
	}

	@Override
	public String toString() {
		return variable + "[" + expressionIdx + "]";
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
