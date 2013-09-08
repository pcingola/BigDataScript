package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;

/**
 * Variable declaration
 * 
 * @author pcingola
 */
public class VarDeclaration extends Statement {

	Type type;
	VariableInit varInit[];

	public static VarDeclaration get(Type type, String varName) {
		VarDeclaration vd = new VarDeclaration(null, null);
		vd.type = type;
		vd.varInit = new VariableInit[1];
		vd.varInit[0] = VariableInit.get(varName);
		return vd;
	}

	public VarDeclaration(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Type getType() {
		return type;
	}

	public VariableInit[] getVarInit() {
		return varInit;
	}

	@Override
	protected void parse(ParseTree tree) {
		type = (Type) factory(tree, 0);

		// Create VarInit nodes
		int num = tree.getChildCount() / 2;
		varInit = new VariableInit[num];

		// Parse all VarInit nodes
		for (int i = 1, j = 0; i < tree.getChildCount(); i++) {
			varInit[j++] = (VariableInit) factory(tree, i);
			i++; // ',' 
		}
	}

	/**
	 * Run 
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		for (VariableInit vi : varInit) {
			csThread.getScope().add(new ScopeSymbol(vi.varName, type)); // Add variable to scope
			vi.run(csThread); // Run varInit
		}

		return RunState.OK;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Add all symbols
		for (VariableInit vi : varInit) {
			String varName = vi.varName;

			// Already declared?
			if (scope.hasSymbol(varName, true)) compilerMessages.add(this, "Duplicate local name " + varName, MessageType.ERROR);

			scope.add(new ScopeSymbol(varName, type)); // Add variable
		}
	}

}
