package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A reference to a list/array expression.
 * E.g. list[3]
 *
 * @author pcingola
 */
public class ReferenceList extends Reference {

	// protected VarReference variable; // !!!! This should be an arbitrary expression that returns a list
	protected Expression exprList; // !!!! This should be an arbitrary expression that returns a list
	protected Expression exprIdx;

	public ReferenceList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getList(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		if (ss == null) return null;
		return (ArrayList) ss.getValue();
	}

	/**
	 * Get symbol from scope
	 */
	@Override
	public ScopeSymbol getScopeSymbol(Scope scope) {
		if (exprList instanceof ReferenceVar) return ((ReferenceVar) exprList).getScopeSymbol(scope);
		return null;
	}

	public Type getType(Scope scope) {
		ScopeSymbol ss = getScopeSymbol(scope);
		return ss.getType();
	}

	@Override
	public String getVariableName() {
		if (exprList instanceof ReferenceVar) return ((ReferenceVar) exprList).getVariableName();
		return null;
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	public boolean isVariableReference() {
		return exprList instanceof ReferenceVar;
	}

	@Override
	protected void parse(ParseTree tree) {
		exprList = (Expression) factory(tree, 0);
		// child[1] = '['
		exprIdx = (Expression) factory(tree, 2);
		// child[3] = ']'
	}

	@Override
	public void parse(String str) {
		int idx1 = str.indexOf('[');
		int idx2 = str.lastIndexOf(']');
		if ((idx1 <= 0) || (idx2 <= idx1)) throw new RuntimeException("Cannot parse list reference '" + str + "'");

		// Create VarReference
		String varName = str.substring(0, idx1);
		ReferenceVar refVar = new ReferenceVar(this, null);
		refVar.parse(varName);
		exprList = refVar;

		// Create index expression
		String idxStr = str.substring(idx1 + 1, idx2);

		if (idxStr.startsWith("$")) {
			// We have to interpolate this string
			exprIdx = ReferenceVar.factory(this, idxStr.substring(1));
		} else {
			// String literal
			LiteralInt litInt = new LiteralInt(this, null);
			litInt.setValue(Gpr.parseLongSafe(idxStr));
			exprIdx = litInt;
		}
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		exprIdx.returnType(scope);
		Type nameType = exprList.returnType(scope);

		if (nameType == null) return null;
		if (nameType.isList()) returnType = ((TypeList) nameType).getBaseType();

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void runStep(BdsThread bdsThread) {
		// Evaluate expressions
		bdsThread.run(exprList);
		bdsThread.run(exprIdx);

		if (bdsThread.isCheckpointRecover()) return;

		// Get results
		int idx = (int) popInt(bdsThread);
		List list = (List) bdsThread.pop();

		//ArrayList list = getList(bdsThread.getScope());
		if ((idx < 0) || (idx >= list.size())) throw new RuntimeException("Trying to access element number " + idx + " from list '" + getVariableName() + "' (list size: " + list.size() + ").");
		bdsThread.push(list.get(idx));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setValue(BdsThread bdsThread, Object value) {
		if (value == null) return;

		bdsThread.run(exprIdx);
		int idx = (int) popInt(bdsThread);
		if (bdsThread.isCheckpointRecover()) return;

		ArrayList<Object> list = getList(bdsThread.getScope());
		if (list == null) bdsThread.fatalError(this, "Cannot assign to non-variable '" + this + "'");

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
		return exprList + "[" + exprIdx + "]";
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(scope);

		if ((exprList.getReturnType() != null) && !exprList.getReturnType().isList()) compilerMessages.add(this, "Expression '" + exprList + "' is not a list/array", MessageType.ERROR);
		if (exprIdx != null) exprIdx.checkCanCastInt(compilerMessages);
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
