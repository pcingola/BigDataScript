package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A "return" statement
 * 
 * @author pcingola
 */
public class Return extends Statement {

	Expression expr;
	Type returnType;

	public Return(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'return'
		if (tree.getChildCount() > 1) expr = (Expression) factory(tree, 1);
	}

	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		// Calculate expression's return type
		if (expr != null) expr.returnType(scope);

		// Find enclosing function
		FunctionDeclaration func = (FunctionDeclaration) findParent(FunctionDeclaration.class);
		// Get funtion's expected return type
		if (func == null) returnType = Type.INT; // Function not found? This is not inside any function...must be in 'main' => return type is 'int'
		else returnType = func.getReturnType();

		return returnType;
	}

	/**
	 * Run the program
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		if (expr != null) csThread.setReturnValue(expr.eval(csThread)); // Set return value to scope
		else csThread.setReturnValue(null);
		return RunState.RETURN;
	}

	@Override
	public String toString() {
		return "return( " + expr + " )";
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);

		if (expr == null) {
			if (!returnType.isVoid()) compilerMessages.add(this, "Cannot cast " + Type.VOID + " to " + returnType, MessageType.ERROR);
		} else if ((expr.getReturnType() != null) && (!expr.getReturnType().canCast(returnType))) compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + returnType, MessageType.ERROR);

	}
}
