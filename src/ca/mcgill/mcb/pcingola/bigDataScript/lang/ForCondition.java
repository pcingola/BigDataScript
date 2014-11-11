package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 * 
 * @author pcingola
 */
public class ForCondition extends ExpressionWrapper {

	public ForCondition(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (!isBool()) compilerMessages.add(this, "For loop condition must be a bool expression", MessageType.ERROR);
	}

}
