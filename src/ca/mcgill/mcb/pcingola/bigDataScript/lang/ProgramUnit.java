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

/**
 * A program unit 
 * 
 * @author pcingola
 */
public class ProgramUnit extends Block {

	BigDataScriptThread bigDataScriptThread;
	Scope scope; // Keep track of the scope. mostly for debugging and testing

	public ProgramUnit(BigDataScriptNode parent, ParseTree tree) {
		super(parent, null); // little hack begin: parse is done later
		setFile(discoverFileFromTree(tree));
		doParse(tree); // little hack end
	}

	private static File discoverFileFromTree(ParseTree tree) { // should probably go somewhere else?
		try {
			CharStream x = ((ParserRuleContext)tree).getStart().getInputStream();
			return new File(((ANTLRFileStream)x).getSourceName());
		} catch(Exception e) { 
			return new File("?");
		}
	}
	
	@Override
	public BigDataScriptThread getBigDataScriptThread() {
		return bigDataScriptThread;
	}

	public Scope getScope() {
		return scope;
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree);
	}

	/**
	 * Run before running the node
	 * @param csThread
	 */
	@Override
	protected void runBegin(BigDataScriptThread csThread) {
		super.runBegin(csThread);
		scope = csThread.getScope();
	}

	public void setBigDataScriptThread(BigDataScriptThread bigDataScriptThread) {
		this.bigDataScriptThread = bigDataScriptThread;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Add all functions
		List<BigDataScriptNode> funcs = findNodes(FunctionDeclaration.class, true);
		for (BigDataScriptNode node : funcs) {
			// Create scope symbol
			FunctionDeclaration fd = (FunctionDeclaration) node;
			TypeFunc typeFunc = new TypeFunc(fd);
			ScopeSymbol ssym = new ScopeSymbol(fd.signature(), typeFunc);

			// Add it to scope
			scope.add(ssym);
		}
	}
}
