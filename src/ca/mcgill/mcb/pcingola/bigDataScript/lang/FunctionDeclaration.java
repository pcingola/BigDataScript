package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

/**
 * Function declaration 
 * 
 * @author pcingola
 */
public class FunctionDeclaration extends StatementWithScope {

	protected String functionName;
	protected Type returnType;
	protected Parameters parameters;
	protected Statement statement;
	protected String signature;

	public FunctionDeclaration(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public String getFunctionName() {
		return functionName;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public Type getReturnType() {
		return returnType;
	}

	/**
	 * Get this function's type
	 * @return
	 */
	public Type getType() {
		return TypeFunc.get(this);
	}

	@Override
	protected void parse(ParseTree tree) {
		returnType = (Type) factory(tree, 0);
		functionName = tree.getChild(1).getText();

		// Parameters
		// child[2] = '('
		int maxParams = indexOf(tree, ")");
		parameters = new Parameters(this, null);
		parameters.parse(tree, 3, maxParams);
		// child[maxParams] = ')'

		statement = (Statement) factory(tree, maxParams + 1);
	}

	/**
	 * Run this function's statement
	 * @param csThread
	 * @return
	 */
	protected RunState runFunction(BigDataScriptThread csThread) {
		return statement.run(csThread);
	}

	/**
	 * Run: Does nothing. A function declaration only declares 
	 * a function, it doesn't do any real work.
	 * A FunctionCall actually makes the function 'run'. This 
	 * is done by evaluating the call, which invokes 
	 * 'FunctionDeclaration.runFunction()' method.
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		// Nothing to do (it's just a declaration)
		return RunState.OK;
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		List<BigDataScriptNode> returnStatements = findNodes(Return.class, true);
		if (returnStatements.isEmpty()) compilerMessages.add(this, "Function has no return statement", MessageType.ERROR);
	}

	@SuppressWarnings("unused")
	public String signature() {
		if (signature != null) return signature;

		StringBuilder sb = new StringBuilder();
		sb.append(functionName);
		sb.append("(");
		for (VarDeclaration vdecl : parameters.getVarDecl()) {
			Type type = vdecl.type;
			for (VariableInit vi : vdecl.getVarInit()) {
				sb.append(type + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(")");
		signature = sb.toString();

		return signature;
	}
}
