package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.expression.ExpressionAssignment;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Expression: Literal empty list '[]'
 *
 * @author pcingola
 */
public class LiteralListEmpty extends LiteralList {

	private static final long serialVersionUID = -5126406542273677554L;

	public LiteralListEmpty(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		values = new Expression[0];
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		returnType = TypeList.get(Types.ANY);
		return returnType;
	}

	@Override
	public String toAsm() {
		Type ltype = returnType;
		if (parent instanceof VariableInit) ltype = parent.getReturnType();
		if (parent instanceof ExpressionAssignment) ltype = ((ExpressionAssignment) parent).getLeft().getReturnType();
		return "new " + ltype + "\n";
	}

}
