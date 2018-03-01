package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * A boolean literal
 *
 * @author pcingola
 */
public abstract class Literal extends Expression {

	protected Value value;

	public Literal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Value getValue() {
		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		setValue(parseValue(tree));
	}

	protected abstract Object parseValue(ParseTree tree);

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;
		returnType = value.getType();
		return returnType;
	}

	/**
	 * Evaluate an expression: Push map to stack
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.push(value);
	}

	public void setValue(Object v) {
		value.set(v);
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
