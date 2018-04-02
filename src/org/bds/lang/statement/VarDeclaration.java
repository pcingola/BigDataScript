package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.type.TypeFunction;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * Variable declaration
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
		if ((varName != null) && (type != null)) symtab.add(varName, type);
	}

	public Type getType() {
		return type;
	}

	public VariableInit[] getVarInit() {
		return varInit;
	}

	@Override
	public boolean isStopDebug() {
		return false;
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
			for (int i = idx, j = 0; i < tree.getChildCount(); i++) {
				varInit[j++] = (VariableInit) factory(tree, i);
				i++; // ','
			}
		}
	}

	/**
	 * Replace 'stub' typeClass with real typeClass (from symtab)
	 */
	protected void replaceStubTypeClass(SymbolTable symtab, CompilerMessages compilerMessages, String varName) {
		if (type == null || !type.isClass()) return;

		TypeClass tc = (TypeClass) type;
		if (tc.getClassDeclaration() != null) return; // Class information avilable, this is not a 'stub' type

		String cname = tc.getClassName();
		TypeClass tcReal = (TypeClass) symtab.getType(cname);
		if (tcReal == null) {
			compilerMessages.add(this, "Cannot find class '" + cname + "'", MessageType.ERROR);
			return;
		}

		// Sanity checks
		if (!tcReal.isClass()) throw new RuntimeException("Type '" + tc.getClassName() + "' is not a class. This should never happen!");
		if (tcReal.getClassDeclaration() == null) throw new RuntimeException("Type '" + tc.getClassName() + "' is does not have class declaration info. This should never happen!");

		type = tcReal;
	}

	/**
	 * Run
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		for (VariableInit vi : varInit) {
			if (!bdsThread.isCheckpointRecover()) {
				bdsThread.getScope().add(vi.varName, type.newDefaultValue()); // Add variable to scope
			}

			bdsThread.run(vi);

			// Act based on run state
			switch (bdsThread.getRunState()) {
			case OK: // OK do nothing
			case CHECKPOINT_RECOVER:
				break;

			case BREAK: // Break form this block immediately
			case CONTINUE:
			case RETURN:
			case EXIT:
			case FATAL_ERROR:
				return;

			default:
				throw new RuntimeException("Unhandled RunState: " + bdsThread.getRunState());
			}
		}
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		// Default value initialization
		String init = "";
		if (type != null) {
			if (type.isBool()) init = "pushb false";
			else if (type.isInt()) init = "pushi 0";
			else if (type.isReal()) init = "pushr 0.0";
			else if (type.isString()) init = "pushs ''";
			else if (type.isList() || type.isMap() || type.isClass()) init = "new " + type.toString();
			else throw new RuntimeException("Unknown default value for type '" + type + "'");
		}

		if (varInit != null) {
			for (VariableInit vi : varInit) {
				if (vi.getExpression() == null) {
					// Use default value initialization
					sb.append(init + "\nstore " + vi.getVarName() + "\n");
				} else {
					// Initialize using an expression
					sb.append(vi.toAsm());
				}
			}
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
				if (symtab.getTypeFunctionsLocal(varName) != null) {
					TypeFunction tf = symtab.getTypeFunctionsLocal(varName).get(0);
					FunctionDeclaration fd = tf.getFunctionDeclaration();
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

				// Replace TypeClass 'stub'
				replaceStubTypeClass(symtab, compilerMessages, varName);

				// Add variable
				addVar(symtab, compilerMessages, varName);
			}
		}
	}
}
