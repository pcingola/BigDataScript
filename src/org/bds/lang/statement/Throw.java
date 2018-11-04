package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.nativeClasses.exception.ClassDeclarationException;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.symbol.SymbolTable;

/**
 * Throw statement
 *
 * @author pcingola
 */
public class Throw extends StatementWithScope {

	private static final long serialVersionUID = -861450592187243401L;

	Expression expr;

	public Throw(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Is the expression derived from 'Exception'?
	 */
	boolean isExceptionClass() {
		Type t = expr.getReturnType();
		if (t == null) return false;
		if (!t.isClass()) return false;

		for (TypeClass tc = (TypeClass) t; tc != null; tc = tc.getClassDeclaration().getClassTypeParent()) {
			if (tc.getCanonicalName().equals(ClassDeclarationException.CLASS_NAME_EXCEPTION)) return true;
		}

		return false;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "throw")) idx++;
		expr = (Expression) factory(tree, idx);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		returnType = expr.returnType(symtab);
		return returnType;
	}

	@Override
	public String toAsm() {
		return expr.toAsm() + "throw\n";
	}

	@Override
	public String toString() {
		return "throw " + (expr != null ? expr.toString() : "");
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (!isExceptionClass()) {
			compilerMessages.add(this, "Trying to 'throw' a non-Exception object", MessageType.ERROR);
		}
	}

}
