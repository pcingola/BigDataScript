package org.bds.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.BdsNodeWalker;
import org.bds.lang.statement.BlockWithFile;
import org.bds.lang.statement.ClassDeclaration;
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

	private static File discoverFileFromTree(ParseTree tree) { // should probably go somewhere else?
		try {
			CharStream cs = ((ParserRuleContext) tree).getStart().getInputStream();
			return new File(cs.getSourceName());
		} catch (Exception e) {
			e.printStackTrace();
			return new File("?");
		}
	}

	protected BdsThread bdsThread;

	public ProgramUnit(BdsNode parent, ParseTree tree) {
		super(parent, null); // little hack begin: parse is done later
		if (tree != null) setFile(discoverFileFromTree(tree));
		doParse(tree); // little hack end
	}

	/**
	 * Add local symbols to SymbolTable
	 * The idea is that you should be able to refer to functions
	 * and classes defined within the same scope, which may be defined
	 * after the current statement, e.g.:
	 *   i := f(42)    // Function 'f' is not defined yet
	 *   int f(int x) { return 2*x }
	 */
	public void addSymbols(SymbolTable symtab) {
		// Add all functions
		List<BdsNode> fdecls = BdsNodeWalker.findNodes(this, StatementFunctionDeclaration.class, false, true);
		for (BdsNode n : fdecls)
			symtab.addFunction((FunctionDeclaration) n);

		// Add classes
		List<BdsNode> cdecls = BdsNodeWalker.findNodes(this, ClassDeclaration.class, false, true);
		for (BdsNode n : cdecls) {
			ClassDeclaration cdecl = (ClassDeclaration) n;
			Types.add(cdecl.getType()); // This creates the type and adds it to Types
		}
	}

	/**
	 * Return all functions whose name starts with 'test'
	 */
	public List<FunctionDeclaration> findTestsFunctions() {
		List<FunctionDeclaration> testFuncs = new ArrayList<>();
		List<BdsNode> allFuncs = BdsNodeWalker.findNodes(this, StatementFunctionDeclaration.class, true, false);
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
