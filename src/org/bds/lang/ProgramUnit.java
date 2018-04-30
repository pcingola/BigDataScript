package org.bds.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.statement.BlockWithFile;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.StatementFunctionDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * A program unit
 *
 * @author pcingola
 */
public class ProgramUnit extends BlockWithFile {

	private static final long serialVersionUID = 3819936306695046515L;

	protected BdsThread bdsThread;

	private static File discoverFileFromTree(ParseTree tree) { // should probably go somewhere else?
		try {
			CharStream x = ((ParserRuleContext) tree).getStart().getInputStream();
			return new File(((ANTLRFileStream) x).getSourceName());
		} catch (Exception e) {
			return new File("?");
		}
	}

	public ProgramUnit(BdsNode parent, ParseTree tree) {
		super(parent, null); // little hack begin: parse is done later
		if (tree != null) setFile(discoverFileFromTree(tree));
		doParse(tree); // little hack end
	}

	/**
	 * Return all functions whose name starts with 'test'
	 */
	public List<FunctionDeclaration> findTestsFunctions() {
		List<FunctionDeclaration> testFuncs = new ArrayList<>();
		List<BdsNode> allFuncs = findNodes(StatementFunctionDeclaration.class, true, false);
		for (BdsNode func : allFuncs) {
			// Create scope symbol
			FunctionDeclaration fd = (FunctionDeclaration) func;

			String fname = fd.getFunctionName();
			if (fname.length() > 4 //
					&& fname.substring(0, 4).equalsIgnoreCase("test") // Starts with 'test'
					&& fd.getParameters().getVarDecl() != null //
					&& fd.getParameters().getVarDecl().length == 0 // There are no arguments to this function (e.g. 'test01()')
			) testFuncs.add(fd);
		}

		return testFuncs;
	}

	@Override
	public BdsThread getBigDataScriptThread() {
		return bdsThread;
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		return Types.INT;
	}

	public void setBdsThread(BdsThread bdsThread) {
		this.bdsThread = bdsThread;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		sb.append(toAsmNode());
		sb.append("main:\n");

		if (isNeedsScope()) sb.append("scopepush\n");

		for (Statement s : statements)
			sb.append(s.toAsm());

		// Note: We don't pop the scope.
		//       We leave the last scope when because it is useful for
		//       checking variable values in test cases. Since the program
		//       finished, it makes no difference (we are cleaning up later).
		sb.append("halt\n");

		return sb.toString();
	}

}
