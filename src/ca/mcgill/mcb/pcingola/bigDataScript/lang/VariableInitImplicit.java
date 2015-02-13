package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * Variable declaration
 *
 * @author pcingola
 */
public class VariableInitImplicit extends VariableInit {

	public static VariableInitImplicit get(String name) {
		VariableInitImplicit vi = new VariableInitImplicit(null, null);
		vi.varName = name;
		return vi;
	}

	public VariableInitImplicit(BigDataScriptNode parent, ParseTree tree) {
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
		return varName + (expression != null ? " := " + expression : "");
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Variable type
		ScopeSymbol varSym = scope.getSymbolLocal(varName);
		Type varType = null;
		if (varSym != null) varType = varSym.getType();

		// Calculate expression type
		if (expression != null) {
			Type exprRetType = expression.returnType(scope);

			// Compare types
			if ((varType == null) || (exprRetType == null)) {
				// Variable not found, nothing else to do
			} else if (varType.isList() && exprRetType.isList() && (expression instanceof LiteralListEmpty)) {
				// OK, Empty list literal can be assigned to any list
			} else if (varType.isMap() && exprRetType.isMap() && (expression instanceof LiteralMapEmpty)) {
				// OK, Empty map literal can be assigned to any map
			} else if (!exprRetType.canCast(varType)) {
				// We cannot cast expression's type to variable's type: Error
				compilerMessages.add(this, "Cannot cast " + exprRetType + " to " + varType, MessageType.ERROR);
			}
		}
	}

}
