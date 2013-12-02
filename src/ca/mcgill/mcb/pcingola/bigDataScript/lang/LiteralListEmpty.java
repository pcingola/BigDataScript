package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Expression: Literal empty list '[]'
 * 
 * @author pcingola
 */
public class LiteralListEmpty extends LiteralList {

	public LiteralListEmpty(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		values = new Expression[0];
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;
		returnType = TypeList.get(Type.VOID);
		return returnType;
	}
}
