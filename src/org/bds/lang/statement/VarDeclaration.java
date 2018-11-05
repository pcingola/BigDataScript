package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.value.ValueFunction;
import org.bds.symbol.SymbolTable;

/**
 * Variable declaration
 * E.g.:
 *
 *     int i,j;         // Note that we declare two variables
 *     string s = 'hi'
 *
 * @author pcingola
 */
public class VarDeclaration extends Statement {

	private static final long serialVersionUID = -3860625762449262210L;

	protected boolean implicit;
	protected Type type;
	protected VariableInit varInit[];

	public static VarDeclaration get(BdsNode parent, Type type, String varName, Expression expression) {
		VarDeclaration vd = new VarDeclaration(parent, null);
		vd.type = type;
		vd.varInit = new VariableInit[1];
		vd.varInit[0] = VariableInit.get(varName, expression);
		return vd;
	}

	public static VarDeclaration get(Type type, String varName) {
		return get(null, type, varName, null);
	}

	public VarDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add variable to symbol table
	 */
	protected void addVar(SymbolTable symtab, CompilerMessages compilerMessages, String varName) {
		if ((varName != null) && (type != null)) symtab.addVariable(varName, type);
	}

	public Type getType() {
		return type;
	}

	public VariableInit[] getVarInit() {
		return varInit;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;

		String classname = tree.getChild(0).getClass().getSimpleName();
		if (classname.equals("VariableInitImplicitContext")) {
			// Variable 'short' declaration
			// Format : varMame := initValue
			// E.g.   : i := 2
			implicit = true;
			varInit = new VariableInit[1];
			varInit[0] = (VariableInit) factory(tree, idx);
		} else {
			// Variable 'classic' declaration
			// Format : type varMame = initValue
			// E.g.   : int i = 2
			implicit = false;
			type = (Type) factory(tree, idx++);

			// Create VarInit nodes
			int num = tree.getChildCount() / 2;
			varInit = new VariableInit[num];

			// Parse all VarInit nodes
			for (int i = idx, j = 0; i < tree.getChildCount(); i += 2, j++) { // Note: i steps over the comma delimiter, that's why "+= 2"
				varInit[j] = (VariableInit) factory(tree, i);
				varInit[j].setVarDeclaration(this);
			}

			for (VariableInit vi : varInit)
				vi.setType(type);
		}
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		if (varInit != null) {
			for (VariableInit vi : varInit)
				sb.append(vi.toAsm());
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (type != null) sb.append(type + " ");
		if (varInit != null) {
			for (int i = 0; i < varInit.length; i++) {
				sb.append(varInit[i]);
				if (i < varInit.length - 1) sb.append(",");
			}
		}
		return sb.toString();
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Add all symbols
		for (VariableInit vi : varInit) {
			String varName = vi.varName;

			// Already declared?
			if (symtab.hasTypeLocal(varName)) {
				String other = "";
				if (symtab.getValueFunctionsLocal(varName) != null) {
					ValueFunction vf = symtab.getValueFunctionsLocal(varName).get(0);
					FunctionDeclaration fd = vf.getFunctionDeclaration();
					other = " (function '" + varName + "' declared in " + fd.getFileName() + ", line " + fd.getLineNum() + ")";
				}

				compilerMessages.add(this, "Duplicate local name '" + varName + "'" + other, MessageType.ERROR);
			} else {
				// Calculate implicit data type
				if (implicit && type == null) type = vi.getExpression().returnType(symtab);

				// Sanity check: Don't declare void variables
				if (type != null && type.isVoid()) {
					compilerMessages.add(this, "Cannot declare variable '" + varName + "' type 'void'", MessageType.ERROR);
					type = null;
				}

				// Other checks for TypeClass
				typeCheckClass(symtab, compilerMessages, varName);

				// Add variable
				addVar(symtab, compilerMessages, varName);
			}
		}
	}

	/**
	 * Check that a class has a propper definition
	 */
	protected void typeCheckClass(SymbolTable symtab, CompilerMessages compilerMessages, String varName) {
		if (type == null || !type.isClass()) return;

		TypeClass tc = (TypeClass) type;
		if (tc.getClassDeclaration() == null) {
			compilerMessages.add(this, "Cannot find class '" + type.getCanonicalName() + "'", MessageType.ERROR);
		}
	}
}
