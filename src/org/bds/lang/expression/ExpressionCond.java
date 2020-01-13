package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * Conditional expression
 *
 * 		expr ? exprTrue : exprFalse
 *
 * @author pcingola
 */
public class ExpressionCond extends Expression {

	private static final long serialVersionUID = 7334801802344186931L;

	Expression expr;
	Expression exprTrue;
	Expression exprFalse;

	public ExpressionCond(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isReturnTypesNotNull() {
		if (expr == null || expr.getReturnType() == null) return false;
		if (exprTrue == null || exprTrue.getReturnType() == null) return false;
		if (exprFalse == null || exprFalse.getReturnType() == null) return false;
		return true;
	}

	//	@Override
	//	public boolean isStopDebug() {
	//		return true;
	//	}

	@Override
	protected void parse(ParseTree tree) {
		expr = (Expression) factory(tree, 0);
		exprTrue = (Expression) factory(tree, 2); // Child 1 is '?'
		exprFalse = (Expression) factory(tree, 4); // Child 3 is ':'
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		expr.returnType(symtab);
		returnType = exprTrue.returnType(symtab);
		exprFalse.returnType(symtab);

		return returnType;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		String labelTrue = baseLabelName() + "true";
		String labelFalse = baseLabelName() + "false";
		String labelEnd = baseLabelName() + "end";

		sb.append(expr.toAsm());
		sb.append("jmpf " + labelFalse + "\n");

		sb.append(labelTrue + ":\n");
		sb.append(exprTrue.toAsmNode());
		sb.append(exprTrue.toAsm());
		sb.append("jmp " + labelEnd + "\n");

		sb.append(labelFalse + ":\n");
		sb.append(exprFalse.toAsmNode());
		sb.append(exprFalse.toAsm());

		sb.append(labelEnd + ":\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		return expr.toString() + " ? " + exprTrue + " : " + exprFalse;
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (expr != null) expr.checkCanCastToBool(compilerMessages);

		if (exprTrue != null //
				&& exprFalse != null //
				&& !exprFalse.getReturnType().canCastTo(exprTrue.getReturnType()) //
		) compilerMessages.add(this, "Both expressions must be the same type. Expression for 'true': " + exprTrue.getReturnType() + ", expression for 'false' " + exprFalse.getReturnType(), MessageType.ERROR);
	}
}
