package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.TypeClass;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class TryCatchFinally extends StatementWithScope {

	private static final long serialVersionUID = 1874966304662651073L;

	Try tryStatement;
	Catch[] catchStatements;
	Finally finallyStatement;

	public TryCatchFinally(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Is this class derived from Exception class?
	 */
	boolean isExceptionType(TypeClass typeClass) {
		return false;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;

		// Parse 'try'
		tryStatement = new Try(this, tree);

		// Parse 'catch' statements
		List<Catch> catchStatementsList = new ArrayList<>();

		while (!isTerminal(tree, idx, "catch"))
			idx++;

		while (isTerminal(tree, idx, "catch")) {
			Catch catchStatement = new Catch(this, tree);
			idx = catchStatement.parse(tree, idx);
			catchStatementsList.add(catchStatement);
		}
		catchStatements = catchStatementsList.toArray(new Catch[0]);

		// Parse 'finally' statement
		finallyStatement = new Finally(this, tree);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		//		sb.append(super.toAsm());
		//
		//		String labelBase = baseLabelName();
		//		String loopInitLabel = labelBase + "init";
		//		String loopStartLabel = labelBase + "start";
		//		String loopContinueLabel = labelBase + "continue";
		//		String loopEndLabel = labelBase + "end";
		//
		//		if (isNeedsScope()) sb.append("scopepush\n");
		//		sb.append(loopInitLabel + ":\n");
		//		if (begin != null) sb.append(begin.toAsm());
		//		sb.append(loopStartLabel + ":\n");
		//		if (condition != null) {
		//			sb.append(condition.toAsm());
		//			sb.append("jmpf " + loopEndLabel + "\n");
		//		}
		//
		//		if (statement != null) sb.append(statement.toAsm());
		//		sb.append(loopContinueLabel + ":\n");
		//		if (end != null) sb.append(end.toAsm());
		//
		//		sb.append("jmp " + loopStartLabel + "\n");
		//
		//		sb.append(loopEndLabel + ":\n");
		//		if (isNeedsScope()) sb.append("scopepop\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (tryStatement != null) sb.append(tryStatement);

		if (catchStatements != null) {
			for (Catch c : catchStatements)
				sb.append(c);
		}

		if (finallyStatement != null) sb.append(finallyStatement);

		return sb.toString();
	}

}
