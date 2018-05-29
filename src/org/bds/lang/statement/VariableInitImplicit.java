package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.value.LiteralListEmpty;
import org.bds.lang.value.LiteralMapEmpty;
import org.bds.symbol.SymbolTable;

/**
 * Variable declaration
 *
 * @author pcingola
 */
public class VariableInitImplicit extends VariableInit {

	private static final long serialVersionUID = 3570089204782596851L;

	public static VariableInitImplicit get(String name) {
		VariableInitImplicit vi = new VariableInitImplicit(null, null);
		vi.varName = name;
		return vi;
	}

	public VariableInitImplicit(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		varName = tree.getChild(idx++).getText();

		// Initialization expression
		if (isTerminal(tree, idx, ":=")) {
			idx++;
			expression = (Expression) factory(tree, idx++);
		}

		// Help string
		ParseTree node = tree.getChild(idx++);
		if (node != null && node.getText().startsWith("help")) {
			help = node.getText().substring("help ".length()).trim();
		}
	}

	@Override
	public String toString() {
		return varName //
				+ (expression != null ? " := " + expression : "") //
				+ (help != null ? " help " + help : "") //
		;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Variable type
		Type varType = symtab.getVariableTypeLocal(varName);

		// Calculate expression type
		if (expression != null) {
			Type exprRetType = expression.returnType(symtab);

			// Compare types
			if ((varType == null) || (exprRetType == null)) {
				// Variable not found, nothing else to do
			} else if (varType.isList() && exprRetType.isList() && (expression instanceof LiteralListEmpty)) {
				// OK, Empty list literal can be assigned to any list
			} else if (varType.isMap() && exprRetType.isMap() && (expression instanceof LiteralMapEmpty)) {
				// OK, Empty map literal can be assigned to any map
			} else if (!exprRetType.canCastTo(varType)) {
				// We cannot cast expression's type to variable's type: Error
				compilerMessages.add(this, "Cannot cast " + exprRetType + " to " + varType, MessageType.ERROR);
			}
		}
	}

}
