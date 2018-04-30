package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForLoop extends StatementWithScope {

	private static final long serialVersionUID = -6096699624548472306L;

	// Note:	It is important that 'begin' node is type-checked before the others in order to
	//			add variables to the scope before ForCondition, ForEnd or Statement uses them.
	//			So the field name should be alphabetically sorted before the other (that's why
	//			I call it 'begin' and not 'init').
	//			Yes, it's a horrible hack.
	ForInit begin;
	ForCondition condition;
	ForEnd end;
	Statement statement;

	public ForLoop(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;

		if (isTerminal(tree, idx, "for")) idx++; // 'for'
		if (isTerminal(tree, idx, "(")) idx++; // '('
		if (!isTerminal(tree, idx, ";")) begin = (ForInit) factory(tree, idx++); // Is this a 'for:begin'? (could be empty)
		if (isTerminal(tree, idx, ";")) idx++; // ';'
		if (!isTerminal(tree, idx, ";")) condition = (ForCondition) factory(tree, idx++); // Is this a 'for:condition'? (could be empty)
		if (isTerminal(tree, idx, ";")) idx++; // ';'
		if (!isTerminal(tree, idx, ")")) end = (ForEnd) factory(tree, idx++); // Is this a 'for:end'? (could be empty)
		if (isTerminal(tree, idx, ")")) idx++; // ')'

		statement = (Statement) factory(tree, idx++);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		String labelBase = baseLabelName();
		String loopInitLabel = labelBase + "init";
		String loopStartLabel = labelBase + "start";
		String loopContinueLabel = labelBase + "continue";
		String loopEndLabel = labelBase + "end";

		if (isNeedsScope()) sb.append("scopepush\n");
		sb.append(loopInitLabel + ":\n");
		if (begin != null) sb.append(begin.toAsm());
		sb.append(loopStartLabel + ":\n");
		if (condition != null) {
			sb.append(condition.toAsm());
			sb.append("jmpf " + loopEndLabel + "\n");
		}

		if (statement != null) sb.append(statement.toAsm());
		sb.append(loopContinueLabel + ":\n");
		if (end != null) sb.append(end.toAsm());

		sb.append("jmp " + loopStartLabel + "\n");

		sb.append(loopEndLabel + ":\n");
		if (isNeedsScope()) sb.append("scopepop\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		return "for( " + begin + " ; " + condition + " ; " + end + " ) {\n" //
				+ Gpr.prependEachLine("\t", statement.toString()) //
				+ "}" //
		;
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (statement == null) {
			compilerMessages.add(this, "Empty for statement", MessageType.ERROR);
		}
	}
}
