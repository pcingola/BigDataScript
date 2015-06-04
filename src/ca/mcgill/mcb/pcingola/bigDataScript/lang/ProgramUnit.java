package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.util.ArrayList;
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

	public static final String HELP_UNSORTED_VAR_NAME = "helpUnsorted";

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

	/**
	 * Create and print automatic 'help' message
	 */
	public void printHelp() {
		StringBuilder sb = new StringBuilder();
		String formatOpt = "%s %s";

		// Find all variable's help
		List<String> varNames = new ArrayList<String>();
		List<String> varHelps = new ArrayList<String>();
		List<String> varTypes = new ArrayList<String>();
		int maxOptLen = 0;

		// Use unsorted variables if 'helpUnsorted' exists (regardless of its value)
		boolean sortVars = (varInit(HELP_UNSORTED_VAR_NAME) == null);

		// Add help on each variable declaration
		for (VarDeclaration varDecl : varDeclarations(sortVars)) {
			Type type = varDecl.getType();
			String typeStr = null;

			// Show types
			if (type.isBool()) typeStr = "<bool>";
			else if (type.isInt()) typeStr = "<int>";
			else if (type.isReal()) typeStr = "<real>";
			else if (type.isString()) typeStr = "<string>";
			else if (type.isList(Type.STRING)) typeStr = "<string ... string>";

			if (typeStr == null) continue;

			// Get variable's name & help
			for (VariableInit vi : varDecl.getVarInit()) {
				if (vi != null && vi.getVarName() != null && vi.getHelp() != null) {
					varNames.add(vi.getVarName());
					varHelps.add(vi.getHelp());
					varTypes.add(typeStr);

					// Format output and calculate length
					int optLen = String.format(formatOpt, vi.getVarName(), typeStr).length();
					maxOptLen = Math.max(maxOptLen, optLen);
				}
			}
		}

		// Show using appropriately formated string
		for (int i = 0; i < varNames.size(); i++) {
			int len = maxOptLen - varNames.get(i).length();
			String format = "\t-%s %-" + len + "s : %s\n";
			sb.append(String.format(format, varNames.get(i), varTypes.get(i), varHelps.get(i)));
		}

		// Show command line options
		String programName = Gpr.baseName(getFileName());
		if (sb.length() > 0) sb.insert(0, "Command line options '" + programName + "' :\n");
		else sb.append("No help available for script '" + programName + "'");

		System.out.println(sb);
	}

	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		super.runStep(bdsThread);
		runScope = bdsThread.getScope();
	}

	public void setBigDataScriptThread(BigDataScriptThread bigDataScriptThread) {
		this.bigDataScriptThread = bigDataScriptThread;
	}

	/**
	 * Return all functions whose name starts with 'test'
	 */
	public List<FunctionDeclaration> testsFunctions() {
		List<FunctionDeclaration> testFuncs = new ArrayList<FunctionDeclaration>();
		List<BigDataScriptNode> allFuncs = findNodes(FunctionDeclaration.class, true);
		for (BigDataScriptNode func : allFuncs) {
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
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Add all functions
		List<BigDataScriptNode> funcs = findNodes(FunctionDeclaration.class, true);
		for (BigDataScriptNode func : funcs) {
			// Create scope symbol
			FunctionDeclaration fd = (FunctionDeclaration) func;
			TypeFunc typeFunc = new TypeFunc(fd);
			ScopeSymbol ssym = new ScopeSymbol(fd.getFunctionName(), typeFunc, fd);

			// Add it to scope
			scope.add(ssym);
		}
	}
}
