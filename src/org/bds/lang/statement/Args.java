package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Arguments
 *
 * @author pcingola
 */
public class Args extends BdsNode {

	private static final long serialVersionUID = 2390753021158525223L;

	protected Expression arguments[];

	/**
	 * Create 'method' arguments by prepending 'this' argument expression
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
			for (int i = 0; i < args.arguments.length; i++) {
				Expression expr = args.arguments[i];
				argsThis.arguments[i + 1] = expr; // Assign to new arguments
				expr.setParent(argsThis); // Update parent
			}
		}

		return argsThis;
	}

	public Args(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Expression[] getArguments() {
		return arguments;
	}

	@Override
	protected void parse(ParseTree tree) {
		parse(tree, 0, tree.getChildCount());
	}

	public void parse(ParseTree tree, int offset, int max) {
		int num = (max - offset + 1) / 2;
		arguments = new Expression[num];

		for (int i = offset, j = 0; i < max; i += 2, j++) { // Note: Increment by 2 to skip separating commas
			arguments[j] = (Expression) factory(tree, i);
		}
	}

	/**
	 * Calculate return type for every expression
	 */
	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		if (arguments != null) {
			for (Expression e : arguments)
				if (e.getReturnType() == null) // Only assign this to show that calculation was already performed
					returnType = e.returnType(symtab);

		} else returnType = Types.VOID;

		return returnType;
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		// Check that all arguments are expressions
		int argNum = 1;
		for (BdsNode node : arguments) {
			if (!(node instanceof Expression)) compilerMessages.add(node, "Expression expected as argument number " + argNum + " (instead of '" + node.getClass().getSimpleName() + "')", MessageType.ERROR);
			argNum++;
		}
	}

	public int size() {
		return (arguments != null ? arguments.length : 0);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		for (Expression ex : arguments)
			sb.append(ex.toAsm());
		return sb.toString();
	}

	public String toAsmNoThis() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < arguments.length; i++) { // Skip first argument ('this')
			sb.append(arguments[i].toAsm());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arguments.length; i++) {
			sb.append(arguments[i]);
			if (i < arguments.length - 1) sb.append(",");
		}
		return sb.toString();
	}

}
