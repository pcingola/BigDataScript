package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.BdsNodeWalker;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.lang.type.Types;
import org.bds.symbol.GlobalSymbolTable;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * Function declaration
 *
 * @author pcingola
 */
public class FunctionDeclaration extends StatementWithScope {

	private static final long serialVersionUID = 4332975458857670311L;

	protected int pc = -1;
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
	 * Create function declaration form signature
	 * Note: This only works with simple signatures
	 */
	public FunctionDeclaration(String signature) {
		super(null, null);

		Pattern p = Pattern.compile("(\\S+)\\((.*)\\) -> (\\S+)");
		Matcher m = p.matcher(signature);
		if (m.find()) {
			// Function name
			functionName = m.group(1);

			// Parameters
			String paramsStr = m.group(2);

			// Return type
			String retType = m.group(3);
			returnType = Types.get(retType);

			// Parse parameters
			p = Pattern.compile("\\s*([a-zA-Z_0-9\\_\\[\\]\\{\\}]+)\\s+([a-zA-Z_0-9\\_]+)\\s*,?");
			m = p.matcher(paramsStr);
			List<String> varNames = new LinkedList<>();
			List<Type> varTypes = new LinkedList<>();
			while (m.find()) {
				String varTypeStr = m.group(1);
				Type varType = Types.get(varTypeStr);
				String varName = m.group(2);
				varTypes.add(varType);
				varNames.add(varName);
			}
			parameters = Parameters.get(varTypes.toArray(new Type[0]), varNames.toArray(new String[0]));
			parameterNames = parameterNames();
		}
	}

	public String getFunctionName() {
		return functionName;
	}

	public List<String> getParameterNames() {
		return parameterNames;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public int getPc() {
		return pc;
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

	/**
	 * Does the function have a return statement (at the end)?
	 */
	boolean hasReturn(Statement s) {
		if (s instanceof Return) return true;
		if (s instanceof Exit) return true;
		if (s instanceof Error) return true;
		if (s instanceof Block) {
			Statement[] ss = ((Block) s).getStatements();
			Statement slast = ss.length > 0 ? ss[ss.length - 1] : null;
			if (slast != null) return hasReturn(slast);
		}
		return false;
	}

	/**
	 * Has this function already been declared?
	 * @return True if a function with the same signature already exists
	 */
	boolean isDuplicateFunction(SymbolTable symtab) {
		return symtab.hasOtherFunction(this) || GlobalSymbolTable.get().hasOtherFunction(this);
	}

	public boolean isMethod() {
		return false;
	}

	public boolean isNative() {
		return false;
	}

	protected List<String> parameterNames() {
		if (parameters == null) return null;

		List<String> names = new ArrayList<>();
		for (VarDeclaration vd : parameters.getVarDecl())
			for (VariableInit vi : vd.getVarInit())
				names.add(vi.getVarName());
		return names;
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

		parameterNames = parameterNames();
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		if (!returnType.isVoid()) {
			List<BdsNode> returnStatements = BdsNodeWalker.findNodes(this, Return.class, true, false);
			if (returnStatements.isEmpty()) compilerMessages.add(this, "Function has no return statement", MessageType.ERROR);
		}
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public String signature() {
		if (signature != null) return signature;
		signature = functionName + getType().signature();
		return signature;
	}

	/**
	 * A signature including parameter names (only used for test cases)
	 */
	public String signatureVarNames() {
		StringBuilder sb = new StringBuilder();
		sb.append(functionName + "(");
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				sb.append((i > 0 ? ", " : "") //
						+ parameters.getType(i) //
						+ " " + parameters.getVarName(i) //
				);
			}
		}
		sb.append(") -> " + returnType);
		return sb.toString();
	}

	@Override
	public String toAsm() {
		String funcEndLabel = baseLabelName() + "end";

		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());
		sb.append("jmp " + funcEndLabel + "\n"); // Make sure we skip function definition when running
		sb.append(signature() + ":\n");

		if (statement != null) {
			sb.append(toAsmStatement());
		} else {
			sb.append(toAsmDefaultReturn());
		}

		sb.append(funcEndLabel + ":\n");
		return sb.toString();
	}

	String toAsmDefaultReturn() {
		return returnType.toAsmDefaultValue() //
				+ "ret\n" //
		;
	}

	String toAsmStatement() {
		StringBuilder sb = new StringBuilder();
		sb.append(statement.toAsm());
		if (!hasReturn(statement)) sb.append(toAsmDefaultReturn());
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
		SymbolTable symtabParent = symtab.getParent();
		if (symtabParent.getVariableTypeLocal(functionName) != null) {
			compilerMessages.add(this, "Duplicate local name '" + functionName + "'", MessageType.ERROR);
		} else if (isDuplicateFunction(symtabParent)) {
			compilerMessages.add(this, "Duplicate function '" + signature() + "'", MessageType.ERROR);
		} else if ((functionName != null) && (getType() != null)) {
			// Add to parent symbol table, because the current
			// symbol table is for the function's body
			symtabParent.addFunction(this);
		}
	}
}
