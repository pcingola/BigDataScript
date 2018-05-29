package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.LiteralInt;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * A reference to a list/array expression.
 * E.g. list[3]
 *
 * @author pcingola
 */
public class ReferenceList extends Reference {

	private static final long serialVersionUID = 611130025318771524L;

	protected Expression exprList; // An arbitrary expression that returns a list
	protected Expression exprIdx; // An arbitrary expression that returns an int

	public ReferenceList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Get symbol from scope
	 */
	@Override
	public ValueList getValue(Scope scope) {
		if (exprList instanceof ReferenceVar) return (ValueList) ((ReferenceVar) exprList).getValue(scope);
		return null;
	}

	@Override
	public String getVariableName() {
		if (exprList instanceof Reference) return ((Reference) exprList).getVariableName();
		return null;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return returnType != null;
	}

	@Override
	public boolean isVariableReference(SymbolTable symtab) {
		if (exprList instanceof Reference) { return ((Reference) exprList).isVariableReference(symtab); }
		return false;
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
			exprIdx = Expression.factory(this, idxStr.substring(1));
		} else {
			// String literal
			LiteralInt litInt = new LiteralInt(this, null);
			litInt.setValue(new ValueInt(Gpr.parseLongSafe(idxStr)));
			exprIdx = litInt;
		}
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		exprIdx.returnType(symtab);
		Type nameType = exprList.returnType(symtab);

		if (nameType == null) return null;
		if (nameType.isList()) returnType = ((TypeList) nameType).getElementType();

		return returnType;
	}

	@Override
	public String toAsm() {
		return exprIdx.toAsm() //
				+ exprList.toAsm() //
				+ "reflist\n";
	}

	@Override
	public String toAsmSet() {
		return exprIdx.toAsm() //
				+ exprList.toAsm() //
				+ "setlist\n";
	}

	@Override
	public String toString() {
		return exprList + "[" + exprIdx + "]";
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(symtab);

		if ((exprList.getReturnType() != null) && !exprList.isList()) {
			compilerMessages.add(this, "Expression '" + exprList + "' is not a list/array", MessageType.ERROR);
		}
		if (exprIdx != null) exprIdx.checkCanCastToInt(compilerMessages);
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		throw new RuntimeException("This method should never be called!");
	}

}
