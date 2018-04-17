package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * While statement
 *
 * @author pcingola
 */
public class While extends Statement {

	private static final long serialVersionUID = 4230873819642138184L;

	Expression condition;
	Statement statement;

	public While(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "while")) idx++; // 'while'
		if (isTerminal(tree, idx, "(")) idx++; // '('
		if (!isTerminal(tree, idx, ")")) condition = (Expression) factory(tree, idx++); // Is this a 'while:condition'? (could be empty)
		if (isTerminal(tree, idx, ")")) idx++; // ')'
		statement = (Statement) factory(tree, idx);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		String labelBase = baseLabelName();
		String loopStartLabel = labelBase + "start";
		String loopContinueLabel = labelBase + "continue";
		String loopEndLabel = labelBase + "end";

		if (isNeedsScope()) sb.append("scopepush\n");
		sb.append(loopStartLabel + ":\n");
		sb.append(loopContinueLabel + ":\n");
		if (condition != null) {
			sb.append(condition.toAsm());
			sb.append("jmpf " + loopEndLabel + "\n");
		}
		sb.append(statement.toAsm());
		sb.append("jmp " + loopStartLabel + "\n");
		sb.append(loopEndLabel + ":\n");
		if (isNeedsScope()) sb.append("scopepop\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		return "while(  " + condition + " ) {\n" //
				+ Gpr.prependEachLine("\t", statement.toString()) //
				+ "\n}" //
		;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (condition != null) condition.returnType(symtab);
		if ((condition != null) && !condition.isBool()) compilerMessages.add(this, "While loop condition must be a bool expression", MessageType.ERROR);
	}

}
