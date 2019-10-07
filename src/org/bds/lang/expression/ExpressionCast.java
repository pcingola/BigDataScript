package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Cast one type to another
 * E.g.
 *
 * 	a = (A) b
 *
 * @author pcingola
 */
public class ExpressionCast extends ExpressionUnary {

	private static final long serialVersionUID = 1043280143559100089L;
	protected String castTo;

	public ExpressionCast(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "cast";
	}

	@Override
	protected void parse(ParseTree tree) {
		castTo = tree.getChild(1).getText();
		expr = (Expression) factory(tree, 3);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		!!!!!!!!!!!
		//		// Calculate return types for expr and args
		//		// Note that expresionObj is null in ExpressionNew (which is a MethodCall)
		//		TypeClass thisType = (TypeClass) Types.get(functionName); // Constructors have same name as class
		//		if (thisType == null) return null;
		//		returnType = thisType;
		//
		//		// Prepend 'this' argument to method signature
		//		expresionThis = new ReferenceThis(this, thisType);
		//		args = Args.getArgsThis(args, expresionThis);
		//
		//		// Calculate return type for args
		//		args.returnType(symtab);
		//
		//		// Find method
		//		functionDeclaration = findMethod(symtab, thisType, args);

		return returnType;
	}

	@Override
	public String toAsm() {
		return "!!!"; // This does not perform any low level operation?
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Can we transform to 'castTo'?
		Type type = Types.get(castTo);
		System.out.println("TYPE: " + type);
		if (type == null) compilerMessages.add(this, "Unknown type '" + castTo + "'", MessageType.ERROR);

		expr.checkCanCastTo(type, compilerMessages);
	}

}
