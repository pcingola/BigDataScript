package org.bds.compile;

/**
 * Store compilation error / warning or just an info message
 *
 * @author pcingola
 */
public class CompilerMessage implements Comparable<CompilerMessage> {

	public enum MessageType {
		ERROR, WARNING, INFO
	};

	protected final String fileName;
	protected final int lineNum, charPosInLine;
	protected final String message;
	protected final MessageType type;

	public CompilerMessage(String fileName, int lineNum, int charPosInLine, String message, MessageType type) {
		this.fileName = fileName != null ? fileName.replace('\\', '/') : "";
		// hack to make windows filenames coincide with linuxes, to ease testing
		this.lineNum = lineNum;
		this.charPosInLine = charPosInLine;
		this.message = message;
		this.type = type;
	}

	@Override
	public int compareTo(CompilerMessage o) {
		if (o == null) return 1;

		if ((getFileName() != null) && (o.getFileName() != null)) {
			int cmp = getFileName().compareTo(o.getFileName());
			if (cmp != 0) return cmp;
		}

		if (getLineNum() != o.getLineNum()) return getLineNum() - o.getLineNum();

		if (getCharPosInLine() != o.getCharPosInLine()) return getCharPosInLine() - o.getCharPosInLine();

		return type.compareTo(o.type);
	}

	public int getCharPosInLine() {
		return charPosInLine;
	}

	public String getFileName() {
		return fileName;
	}

	public int getLineNum() {
		return lineNum;
	}

	public String getMessage() {
		return message;
	}

	public MessageType getType() {
		return type;
	}

	@Override
	public String toString() {
		String coordinates = (getFileName() != null ? "file '" + getFileName() + "'" : "") //
				+ (getLineNum() >= 0 ? ", line " + getLineNum() : "") //
		;

		return type //
				+ (!coordinates.isEmpty() ? " [ " + coordinates + " ]" : "") //
				+ (message != null ? " :\t" + message : "");
	}
}
