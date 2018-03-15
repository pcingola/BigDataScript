package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.value.ValueClass;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A reference to a field in a class
 *
 * @author pcingola
 */
public class ReferenceField extends ReferenceVar {

	Expression exprObj;

	public ReferenceField(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		classField = true;
	}

	/**
	 * Find 'type' for 'name'
	 * Also mark this as a 'classField' if the it refers to 'this.name'
	 */
	@Override
	protected Type findType(SymbolTable symtab) {
		// Is 'this' defined (is it a class?)
		TypeClass typeThis = (TypeClass) symtab.getType(ClassDeclaration.THIS);
		if (typeThis == null) return null;

		// Look up 'name' as a field in the class
		Type t = typeThis.getSymbolTable().getType(name);
		classField = (t != null);

		return t;
	}

	@Override
	protected void parse(ParseTree tree) {
		exprObj = (Expression) factory(tree, 0);
		name = tree.getChild(2).getText();
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		Type classType = exprObj.returnType(symtab);
		if (classType == null) return null;
		if (classType.isClass()) returnType = ((TypeClass) classType).getType(name);

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Evaluate expressions
		bdsThread.run(exprObj);

		if (bdsThread.isCheckpointRecover()) return;

		// Get result
		ValueClass vclass = (ValueClass) bdsThread.pop();

		// Push value to stack
		bdsThread.push(vclass.getValue(name));
	}

	@Override
	public String toString() {
		return exprObj + "." + name;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType(symtab);

		if ((exprObj.getReturnType() != null) && !exprObj.getReturnType().isClass()) compilerMessages.add(this, "Expression '" + exprObj + "' is not an object", MessageType.ERROR);
		if (returnType == null) compilerMessages.add(this, "Class '" + exprObj.getReturnType() + "' does not have memeber '" + name + "'", MessageType.ERROR);
	}
}
