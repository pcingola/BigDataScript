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
		sb.append(super.toAsm());

		String finallyLabel = baseLabelName() + "finally";

		if (isNeedsScope()) sb.append("scopepush\n");

		// Register all catch blocks
		for (Catch catchStatement : catchStatements)
			sb.append(catchStatement.toAsmAeh());

		// Add 'try' statement
		sb.append(tryStatement.toAsm());

		// Remove exception handler and execute 'finally' block
		sb.append("reh\n");
		sb.append("jmp '" + finallyLabel + "'\n");

		// Add catch blocks
		for (Catch catchStatement : catchStatements) {
			sb.append(catchStatement.toAsm());
		}

		// Add finally block
		sb.append(finallyLabel + ":\n");
		if (finallyStatement != null) sb.append(finallyStatement.toAsm());

		if (isNeedsScope()) sb.append("scopepop\n");

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
