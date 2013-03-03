package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Arguments
 * 
 * @author pcingola
 */
public class Args extends BigDataScriptNode {

	Type returnType;
	Expression arguments[];

	/**
	 * Create 'method' arguments by prepending 'this' argument expression
	 *  
	 * @param args
	 * @param exprThis
	 * @return
	 */
	public static Args getArgsThis(Args args, Expression exprThis) {
		Args argsThis = new Args(null, null);
		argsThis.parent = args.parent;

		// Create new arguments
		int len = (args.arguments == null ? 0 : args.arguments.length) + 1;
		argsThis.arguments = new Expression[len];

		// Copy arguments
		argsThis.arguments[0] = exprThis;
		if (args.arguments != null) {
			for (int i = 0; i < args.arguments.length; i++)
				argsThis.arguments[i + 1] = args.arguments[i];
		}

		return argsThis;
	}

	public Args(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Expression[] getArguments() {
		return arguments;
	}

	@Override
	protected void parse(ParseTree tree) {
		parse(tree, 0, tree.getChildCount());
	}

	protected void parse(ParseTree tree, int offset, int max) {
		int num = (max - offset + 1) / 2;
		arguments = new Expression[num];

		for (int i = offset, j = 0; i < max; i += 2, j++) { // Note: Increment by 2 to skip separating commas
			arguments[j] = (Expression) factory(tree, i);
		}
	}

	/**
	 * Calculate return type for every expression
	 * @param scope
	 * @return
	 */
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		if (arguments != null) {
			for (Expression e : arguments) {
				if (e == null) //
					Gpr.debug("NULL!");
				if (e.getReturnType() == null) returnType = e.returnType(scope); // Only assign this to show that calculation was already performed
			}
		} else returnType = Type.VOID;

		return returnType;
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		// Check that all arguments are expressions
		int argNum = 1;
		for (BigDataScriptNode node : arguments) {
			if (!(node instanceof Expression)) compilerMessages.add(node, "Expression expected as argument number " + argNum + " (instead of '" + node.getClass().getSimpleName() + "')", MessageType.ERROR);
			argNum++;
		}
	}

	public int size() {
		return (arguments != null ? arguments.length : 0);
	}

}
