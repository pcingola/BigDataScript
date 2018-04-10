package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueArgs;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * Function declaration
 *
 * @author pcingola
 */
public class FunctionDeclaration extends StatementWithScope {

	private static final long serialVersionUID = 4332975458857670311L;

	protected String functionName;
	protected TypeFunction funcType;
	protected Parameters parameters;
	protected Statement statement;
	protected String signature;
	protected List<String> parameterNames;

	public FunctionDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Apply function to arguments, return function's result
	 */
	public Value apply(BdsThread bdsThread, ValueArgs args) {
		//		// Create scope and add function arguments
		//		if (!bdsThread.isCheckpointRecover()) createScopeAddArgs(bdsThread, args);
		//
		//		// Run function body
		//		runFunction(bdsThread);
		//		if (bdsThread.isFatalError()) throw new RuntimeException("Fatal error");
		//
		//		// Get return value
		//		Value retVal = bdsThread.getReturnValue();
		//
		//		// Restore old scope
		//		if (!bdsThread.isCheckpointRecover()) bdsThread.oldScope();
		//
		//		// Return result
		//		return retVal;
		return null;
	}

	public String getFunctionName() {
		return functionName;
	}

	public List<String> getParameterNames() {
		if (parameterNames != null) return parameterNames;
		parameterNames = new ArrayList<>();

		for (VarDeclaration vd : parameters.getVarDecl())
			for (VariableInit vi : vd.getVarInit())
				parameterNames.add(vi.getVarName());

		return parameterNames;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public Statement getStatement() {
		return statement;
	}

	/**
	 * Get this function's type
	 */
	public TypeFunction getType() {
		if (funcType == null) funcType = new TypeFunction(this);
		return funcType;
	}

	public boolean isNative() {
		return false;
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

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		if (!returnType.isVoid()) {
			List<BdsNode> returnStatements = findNodes(Return.class, true, false);
			if (returnStatements.isEmpty()) compilerMessages.add(this, "Function has no return statement", MessageType.ERROR);
		}
	}

	public String signature() {
		if (signature != null) return signature;
		signature = functionName + getType().signature();
		return signature;
	}

	@Override
	public String toAsm() {
		String funcEndLabel = getClass().getSimpleName() + "_" + getId() + "_end";

		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());
		sb.append("jmp " + funcEndLabel + "\n"); // Make sure we skip function definition when running
		sb.append(signature() + ":\n");
		if (statement != null) sb.append(statement.toAsm());
		sb.append(funcEndLabel + ":\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(returnType + " " + functionName + "( " + parameters + " ) {\n");
		if (statement != null) sb.append(Gpr.prependEachLine("\t", statement.toString()));
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Function name collides with other names?
		if (symtab.getTypeLocal(functionName) != null) {
			compilerMessages.add(this, "Duplicate local name " + functionName, MessageType.ERROR);
		} else if ((functionName != null) && (getType() != null)) {
			// Add to parent symbol table, because the current
			// symbol table is for the function's body
			symtab.getParent().add(functionName, getType());
		}
	}
}
