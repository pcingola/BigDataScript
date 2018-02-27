package org.bds.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.statement.BlockWithFile;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

/**
 * A program unit
 *
 * @author pcingola
 */
public class ProgramUnit extends BlockWithFile {

	BdsThread bdsThread;
	Scope runScope; // Scope used when running this program. Used in test cases

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

	@Override
	public BdsThread getBigDataScriptThread() {
		return bdsThread;
	}

	public Scope getRunScope() {
		return runScope;
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree);
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		super.runStep(bdsThread);
		runScope = bdsThread.getScope();
	}

	public void setBdsThread(BdsThread bdsThread) {
		this.bdsThread = bdsThread;
	}

	/**
	 * Return all functions whose name starts with 'test'
	 */
	public List<FunctionDeclaration> testsFunctions() {
		List<FunctionDeclaration> testFuncs = new ArrayList<>();
		List<BdsNode> allFuncs = findNodes(FunctionDeclaration.class, true);
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
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Add all functions
		List<BdsNode> funcs = findNodes(FunctionDeclaration.class, true);
		for (BdsNode func : funcs) {
			// Create scope symbol
			FunctionDeclaration fd = (FunctionDeclaration) func;
			TypeFunc typeFunc = new TypeFunc(fd);
			ScopeSymbol ssym = new ScopeSymbol(fd.getFunctionName(), typeFunc, fd);

			// Add it to scope
			scope.add(ssym);
		}
	}
}
