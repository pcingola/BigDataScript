package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * If statement
 * 
 * @author pcingola
 */
public class If extends Statement {

	Expression condition;
	Statement statement;
	Statement elseStatement;

	public If(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		//child[0] = 'if'
		//child[1] = '('
		condition = (Expression) factory(tree, 2);
		//child[3] = ')'
		statement = (Statement) factory(tree, 4);
		//child[5] = 'else'
		elseStatement = (Statement) factory(tree, 6);
	}

	/**
	 * Run the program
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		if (condition.evalBool(csThread)) return statement.run(csThread);
		if (elseStatement != null) return elseStatement.run(csThread);
		return RunState.OK;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		condition.returnType(scope);

		if ((condition != null) && !condition.isBool()) compilerMessages.add(this, "Condition in 'if' statement must be a bool expression", MessageType.ERROR);
	}

}
