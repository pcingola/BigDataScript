package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * An int literal
 *
 * @author pcingola
 */
public class LiteralInt extends Literal {

	long value;

	public LiteralInt(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Object eval(BigDataScriptThread csThread) {
		return value;
	}

	public long getValue() {
		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		String intStr = tree.getChild(0).getText().toLowerCase();
		if (intStr.startsWith("0x")) value = Long.parseLong(intStr.substring(2), 16);
		else value = Gpr.parseLongSafe(tree.getChild(0).getText());
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = Type.INT;
		return returnType;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "" + value;
	}

}
