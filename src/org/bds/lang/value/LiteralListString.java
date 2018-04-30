package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Expression 'Literal'
 *
 * @author pcingola
 */
public class LiteralListString extends LiteralList {

	private static final long serialVersionUID = -4999985887360700695L;

	public LiteralListString(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		returnType = TypeList.get(Types.STRING);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		throw new RuntimeException("This should never happen!");
	}

	public void setValue(ValueList vals) {
		values = new Expression[vals.size()];
		int j = 0;
		for (Value val : vals) {
			LiteralString lit = new LiteralString(this, null);
			lit.setValue(val);
			values[j++] = lit;
		}
	}

}
