package ca.mcgill.mcb.pcingola.bigDataScript.util;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.GenericNode;

/**
 * Store compilation error / warning or just an info message
 * 
 * @author pcingola
 */
public class CompilerMessage implements Comparable<CompilerMessage> {

	public enum MessageType {
		ERROR, WARNING, INFO
	};

	BigDataScriptNode node;
	String message;
	MessageType type;

	
	
	public CompilerMessage(BigDataScriptNode node, String message, MessageType type) {
		this.node = node; // can be null
		if(node ==null) this.node = new GenericNode(null, null);
		this.message = message;
		this.type = type;
	}

	@Override
	public int compareTo(CompilerMessage o) {
		if(o==null) return 1;
		if (node !=null && o.node != null && (node.getFileName() != null) && (o.node.getFileName() != null)) {
			int cmp = node.getFileName().compareTo(o.node.getFileName());
			if (cmp != 0) return cmp;
		}
		if (node.getLineNum() != o.node.getLineNum()) return node.getLineNum() - o.node.getLineNum();

		if (node.getCharPosInLine() != o.node.getCharPosInLine()) return node.getCharPosInLine() - o.node.getCharPosInLine();

		return type.compareTo(o.type);
	}

	public String getMessage() {
		return message;
	}

	public BigDataScriptNode getNode() {
		return node;
	}

	public MessageType getType() {
		return type;
	}

	@Override
	public String toString() {
		return type //
				+ " [ " //
				+ (node.getFileName() != null ? "file '" + node.getFileName() + "'" : "") //
				+ (node.getLineNum() >= 0 ? ", line " + node.getLineNum() : "") //
				// + (node.getCharPosInLine() >= 0 ? ", char " + (node.getCharPosInLine() + 1) : "")//
				+ " ] :\t" + message;
	}
}
