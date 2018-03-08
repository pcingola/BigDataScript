package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.util.Gpr;

/**
 * Variable declaration
 *
 * @author pcingola
 */
public class ClassDeclaration extends Block {

	String className, extendsName;
	protected VarDeclaration varDecl[];
	protected FunctionDeclaration funcDecl[];
	protected ClassDeclaration classParent;

	public ClassDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public String getClassName() {
		return className;
	}

	public ClassDeclaration getClassParent() {
		return classParent;
	}

	public String getExtendsName() {
		return extendsName;
	}

	public FunctionDeclaration[] getFuncDecl() {
		return funcDecl;
	}

	/**
	 * Get this class type
	 * Note: We use 'returnType' for storing the
	 */
	public Type getType() {
		return returnType;
	}

	public VarDeclaration[] getVarDecl() {
		return varDecl;
	}

	@Override
	public boolean isStopDebug() {
		return false;
	}

	@Override
	protected void parse(ParseTree tree) {
		tree = tree.getChild(0);
		int idx = 0;

		// Class name
		if (isTerminal(tree, idx, "class")) idx++; // 'class'
		className = tree.getChild(idx++).getText();

		// Extends?
		if (isTerminal(tree, idx, "extends")) {
			idx++; // 'extends'
			extendsName = tree.getChild(idx++).getText();
		}

		// Class body
		if (isTerminal(tree, idx, "{")) {
			parse(tree, ++idx);
			parseSortStatements();
		}
	}

	protected void parseSortStatements() {
		List<VarDeclaration> lvd = new ArrayList<>();
		List<FunctionDeclaration> lfd = new ArrayList<>();
		List<Statement> ls = new ArrayList<>();

		// Sift statements
		for (Statement s : statements) {
			if (s instanceof VarDeclaration) lvd.add((VarDeclaration) s);
			else if (s instanceof FunctionDeclaration) lfd.add((FunctionDeclaration) s);
			else ls.add(s);
		}

		// Convert to arrays
		varDecl = lvd.toArray(new VarDeclaration[0]);
		funcDecl = lfd.toArray(new FunctionDeclaration[0]);
		statements = ls.toArray(new Statement[0]);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		//		// Create class type
		//		returnType = TypeClass.get(className);

		//		// TODO: Add local class scope
		//		for (VarDeclaration vd : varDecl)
		//			vd.returnType(scope);
		//
		//		for (FunctionDeclaration fd : funcDecl)
		//			fd.returnType(scope);
		//
		//		for (Statement s : statements)
		//			s.returnType(scope);

		// Create type within scope
		Gpr.debug("!!! CREATE CLASS IN SCOPE: " + className);
		TypeClass.factory(this, scope);

		return returnType;
	}

	/**
	 * Run
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Nothing to do (it's just a declaration)
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class " + className);
		if (extendsName != null) sb.append("extends " + extendsName);

		sb.append(" {\n");

		if (varDecl != null && varDecl.length > 0) {
			sb.append("\t# Variables\n");
			for (int i = 0; i < varDecl.length; i++)
				sb.append("\t" + varDecl[i] + "\n");
			sb.append("\n");
		}

		if (funcDecl != null && funcDecl.length > 0) {
			sb.append("\t# Methods\n");
			for (int i = 0; i < funcDecl.length; i++)
				sb.append("\t" + funcDecl[i].signatureWithName() + "\n");
			sb.append("\n");
		}

		if (statements != null && statements.length > 0) {
			sb.append("\t# Constructor statements\n");
			for (int i = 0; i < statements.length; i++)
				sb.append("\t" + statements[i] + "\n");
			sb.append("\n");
		}

		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);

		// Class name collides with other names?
		if (scope.getSymbolLocal(className) != null) compilerMessages.add(this, "Duplicate local name " + className, MessageType.ERROR);

		// Add all symbols
		//		for (VarDeclaration vi : varDecl) {
		//			String varName = vi.varName;
		//
		//			// Already declared?
		//			if (scope.hasSymbolLocal(varName)) {
		//				String other = "";
		//				if (scope.getFunctionsLocal(varName) != null) {
		//					ScopeSymbol ssf = scope.getFunctionsLocal(varName).get(0);
		//					FunctionDeclaration fdecl = (FunctionDeclaration) ssf.getValue().get();
		//					other = " (function '" + varName + "' declared in " + fdecl.getFileName() + ", line " + fdecl.getLineNum() + ")";
		//				}
		//
		//				compilerMessages.add(this, "Duplicate local name '" + varName + "'" + other, MessageType.ERROR);
		//			} else {
		//				throw new RuntimeException("!!! UNIMPLEMENTED");
		//				//				// Calculate implicit data type
		//				//				if (implicit && type == null) type = vi.getExpression().returnType(scope);
		//				//
		//				//				if (type != null && type.isVoid()) {
		//				//					compilerMessages.add(this, "Cannot declare variable '" + varName + "' type 'void'", MessageType.ERROR);
		//				//					type = null;
		//				//				}
		//				//
		//				//				// Add variable to scope
		//				//				if ((varName != null) && (type != null)) scope.add(new ScopeSymbol(varName, type));
		//			}
		//		}
		//		}
	}
}
