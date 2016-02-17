package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.util.Gpr;

/**
 * A real literal
 *
 * @author pcingola
 */
public class LiteralReal extends Literal {

	double value;

	public LiteralReal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public void runStep(BdsThread csThread) {
		csThread.push(value);
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

	@Override
	public String toString() {
		return "" + value;
	}
}
