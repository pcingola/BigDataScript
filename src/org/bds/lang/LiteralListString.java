package org.bds.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.scope.Scope;

/**
 * Expression 'Literal'
 * 
 * @author pcingola
 */
public class LiteralListString extends LiteralList {

	public LiteralListString(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		returnType = TypeList.get(Type.STRING);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;
		throw new RuntimeException("This should never happen!");
	}

	public void setValue(ArrayList<String> vals) {
		values = new Expression[vals.size()];
		int j = 0;
		for (String val : vals) {
			LiteralString lit = new LiteralString(this, null);
			lit.setValue(val);
			values[j++] = lit;
		}
	}

}
