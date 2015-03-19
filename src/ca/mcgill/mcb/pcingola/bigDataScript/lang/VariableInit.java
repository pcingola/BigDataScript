package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * Variable initialization
 * E.g.:
 * 			i = 3
 *
 * @author pcingola
 */
public class VariableInit extends BigDataScriptNode {

	String varName;
	Expression expression;
	String help;

	public static VariableInit get(BigDataScriptNode parent, String name, Expression expression) {
		VariableInit vi = new VariableInit(null, null);
		vi.parent = parent;
		vi.varName = name;
		vi.expression = expression;
		return vi;
	}

	public static VariableInit get(String name) {
		VariableInit vi = new VariableInit(null, null);
		vi.varName = name;
		return vi;
	}

	public static VariableInit get(String name, Expression expression) {
		VariableInit vi = new VariableInit(null, null);
		vi.varName = name;
		vi.expression = expression;
		return vi;
	}

	public VariableInit(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Expression getExpression() {
		return expression;
	}

	public String getHelp() {
		return help;
	}

	public String getVarName() {
		return varName;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		varName = tree.getChild(idx++).getText();

		// Initialization expression
		if (isTerminal(tree, idx, "=")) {
			idx++;
			expression = (Expression) factory(tree, idx++);
		}

		// Help string
		ParseTree node = tree.getChild(idx++);
		if (node != null && node.getText().startsWith("help")) {
			help = node.getText().substring("help ".length()).trim();
		}
	}

	/**
	 * Run
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		if (expression != null) {
			bdsThread.run(expression);
			if (bdsThread.isCheckpointRecover()) return;

			// Error running expression?
			Object value = bdsThread.pop();
			if (value == null) {
				bdsThread.setRunState(RunState.FATAL_ERROR);
				return;
			}

			// Change variable's value
			Scope scope = bdsThread.getScope();
			ScopeSymbol ssym = scope.getSymbol(varName);
			value = ssym.getType().cast(value);
			ssym.setValue(value);
		}

	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return varName //
				+ (expression != null ? " = " + expression : "") //
				+ (help != null ? " help " + help : "") //
		;
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
			} else if (varSym.getType().isList() && exprRetType.isList() && (expression instanceof LiteralListEmpty)) {
				// OK, Empty list literal can be assigned to any list
			} else if (varSym.getType().isMap() && exprRetType.isMap() && (expression instanceof LiteralMapEmpty)) {
				// OK, Empty map literal can be assigned to any map
			} else if (!exprRetType.canCast(varType)) {
				// We cannot cast expression's type to variable's type: Error
				compilerMessages.add(this, "Cannot cast " + exprRetType + " to " + varType, MessageType.ERROR);
			}
		}
	}
}
