package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A real literal
 * 
 * @author pcingola
 */
public class LiteralReal extends Literal {

	double value;

	public LiteralReal(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Object eval(BigDataScriptThread csThread) {
		return value;
	}

	public double getValue() {
		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		value = Gpr.parseDoubleSafe(tree.getChild(0).getText());
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = Type.REAL;
		return returnType;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
