package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

/**
 * A boolean literal
 * 
 * @author pcingola
 */
public class Literal extends Expression {

	public Literal(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
