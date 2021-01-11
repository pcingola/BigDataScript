package org.bds.lang.statement;

import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * A "return" statement
 *
 * @author pcingola
 */
public class Return extends Statement {

	private static final long serialVersionUID = 1225216199721027945L;

	protected Expression expr;
	protected FunctionDeclaration functionDecl; // Enclosing function or method

	public Return(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Find enclosing function
	 */
	@SuppressWarnings("rawtypes")
	FunctionDeclaration findParentFunction() {
		Set<Class> classSet = new HashSet<>();
		classSet.add(FunctionDeclaration.class);
		classSet.add(MethodDeclaration.class);
		classSet.add(StatementFunctionDeclaration.class);
		// Note: Cannot be inside a 'try/catch/finally' statement
		return (FunctionDeclaration) findParent(classSet);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'return'
		if (tree.getChildCount() > 1) expr = (Expression) factory(tree, 1);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		// Calculate expression's return type
		if (expr != null) expr.returnType(symtab);

		// Find enclosing function / method
		functionDecl = findParentFunction();

		if (functionDecl != null) {
			returnType = functionDecl.getReturnType();
		} else {
			// Function not found? This is not inside any function...must be in 'main' => return type is 'int'
			returnType = Types.INT;
		}

		return returnType;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());
		sb.append((expr != null ? expr.toAsm() : returnType.toAsmDefaultValue()));
		sb.append("ret\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "return " + expr;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		returnType(symtab);

		if (expr == null) {
			if (!returnType.isVoid()) compilerMessages.add(this, "Cannot cast " + Types.VOID + " to " + returnType, MessageType.ERROR);
		} else if ((expr.getReturnType() != null) && (!expr.getReturnType().canCastTo(returnType))) compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + returnType, MessageType.ERROR);

	}
}
