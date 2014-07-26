package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A program unit
 *
 * @author pcingola
 */
public class ProgramUnit extends BlockWithFile {

	BigDataScriptThread bigDataScriptThread;
	Scope runScope; // Scope used when running this program. Used in test cases

	private static File discoverFileFromTree(ParseTree tree) { // should probably go somewhere else?
		try {
			CharStream x = ((ParserRuleContext) tree).getStart().getInputStream();
			return new File(((ANTLRFileStream) x).getSourceName());
		} catch (Exception e) {
			return new File("?");
		}
	}

	public ProgramUnit(BigDataScriptNode parent, ParseTree tree) {
		super(parent, null); // little hack begin: parse is done later
		if (tree != null) setFile(discoverFileFromTree(tree));
		doParse(tree); // little hack end
	}

	@Override
	public BigDataScriptThread getBigDataScriptThread() {
		return bigDataScriptThread;
	}

	public Scope getRunScope() {
		return runScope;
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree);
	}

	@Override
	protected void runBegin(BigDataScriptThread bdsThread) {
		super.runBegin(bdsThread);
		runScope = bdsThread.getScope();
		Gpr.debug("SETTING RUN SCOPE");
	}

	public void setBigDataScriptThread(BigDataScriptThread bigDataScriptThread) {
		this.bigDataScriptThread = bigDataScriptThread;
	}

	public void setRunScope(Scope runScope) {
		this.runScope = runScope;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Add all functions
		List<BigDataScriptNode> funcs = findNodes(FunctionDeclaration.class, true);
		for (BigDataScriptNode func : funcs) {
			// Create scope symbol
			FunctionDeclaration fd = (FunctionDeclaration) func;
			TypeFunc typeFunc = new TypeFunc(fd);
			ScopeSymbol ssym = new ScopeSymbol(fd.getFunctionName(), typeFunc, fd);
			Gpr.debug("Adding function: '" + fd.getFunctionName() + "'");

			// Add it to scope
			scope.add(ssym);
		}
	}
}
