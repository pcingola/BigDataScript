package ca.mcgill.mcb.pcingola.bigDataScript.compile;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.GenericNode;

/**
 * Store compilation error / warning or just an info message
 * 
 * @author pcingola
 */
public class CompilerMessageBdsNode extends CompilerMessage {

	BigDataScriptNode node;

	public CompilerMessageBdsNode(BigDataScriptNode node, String message, MessageType type) {
		super(null, -1, -1, message, type);
		this.node = node; // can be null
		this.message = message;
		this.type = type;

		if (node == null) this.node = new GenericNode(null, null);
		else {
			fileName = node.getFileName();
			lineNum = node.getLineNum();
			charPosInLine = node.getCharPosInLine() + 1;
		}
	}

	@Override
	public String getFileName() {
		if (fileName == null) fileName = node.getFileName();
		return fileName;
	}

	public BigDataScriptNode getNode() {
		return node;
	}
}
