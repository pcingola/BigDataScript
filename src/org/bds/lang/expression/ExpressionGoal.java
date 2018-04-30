package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * A 'goal' expression
 *
 * @author pcingola
 */
public class ExpressionGoal extends ExpressionUnary {

	private static final long serialVersionUID = -9046942009574839347L;

	public ExpressionGoal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "goal";
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		returnType = TypeList.get(Types.STRING);
		return returnType;
	}

	/**
	 * Commands (i.e. task)
	 */
	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(expr.toAsm());
		sb.append("goal\n");
		return sb.toString();
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		super.typeCheckNotNull(symtab, compilerMessages);

		Type et = expr.getReturnType();
		if (!(et.isString() || et.isList(Types.STRING))) {
			compilerMessages.add(this, "Goal expression should be either a string or string[], but it is " + expr.getReturnType(), MessageType.ERROR);
		}
	}
}
