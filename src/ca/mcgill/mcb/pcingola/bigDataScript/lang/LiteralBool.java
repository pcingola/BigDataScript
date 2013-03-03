package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A boolean literal
 * 
 * @author pcingola
 */
public class LiteralBool extends Literal {

	boolean value;

	public LiteralBool(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		return value;
	}

	public boolean isValue() {
		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		value = Gpr.parseBoolSafe(tree.getChild(0).getText());
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = Type.BOOL;
		return returnType;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
}
