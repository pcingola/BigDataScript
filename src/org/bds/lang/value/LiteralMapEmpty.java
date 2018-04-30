package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.expression.ExpressionAssignment;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Expression: Literal empty map
 *
 * @author pcingola
 */
public class LiteralMapEmpty extends LiteralMap {

	private static final long serialVersionUID = 7742952716041459654L;

	public LiteralMapEmpty(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		keys = new Expression[0];
		values = new Expression[0];
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		returnType = TypeMap.get(Types.ANY, Types.ANY);
		return returnType;
	}

	@Override
	public String toAsm() {
		Type mtype = returnType;
		if (parent instanceof VariableInit) mtype = parent.getReturnType();
		if (parent instanceof ExpressionAssignment) mtype = ((ExpressionAssignment) parent).getLeft().getReturnType();
		return "new " + mtype + "\n";
	}

}
