package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;

/**
 * Method declaration
 *
 * @author pcingola
 */
public class MethodDeclaration extends FunctionDeclaration {

	public static final String THIS_KEYWORD = "this";

	protected Type classType;

	public MethodDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Type getClassType() {
		return classType;
	}

	@Override
	@SuppressWarnings("unused")
	public String signature() {
		if (signature != null) return signature;

		StringBuilder sigsb = new StringBuilder();
		sigsb.append(classType != null ? classType : "null");
		sigsb.append(".");
		sigsb.append(functionName);
		sigsb.append("(");
		int count = 0;
		for (VarDeclaration vdecl : parameters.getVarDecl()) {
			if (count > 0) {
				Type type = vdecl.getType();
				for (VariableInit vi : vdecl.getVarInit()) {
					sigsb.append(type + ",");
				}
				sigsb.deleteCharAt(sigsb.length() - 1);
			}

			count++;
		}
		sigsb.append(")");
		signature = sigsb.toString();

		return signature;
	}
}
