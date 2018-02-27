package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionWrapper;
import org.bds.scope.Scope;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForCondition extends ExpressionWrapper {

	public ForCondition(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isStopDebug() {
		return true;
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (!isBool()) compilerMessages.add(this, "For loop condition must be a bool expression", MessageType.ERROR);
	}

}
