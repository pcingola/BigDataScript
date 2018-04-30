package org.bds.util;

/**
 * Show a data table
 * @author pablocingolani
 */
public class TextTable {

	String columnNames[];
	String data[][];
	int columnLen[];
	String prepend;

	public TextTable(String columnNames[], String data[][]) {
		this.columnNames = columnNames;
		this.data = data;
		prepend = "";
	}

	public TextTable(String columnNames[], String data[][], String prepend) {
		this.columnNames = columnNames;
		this.data = data;
		this.prepend = prepend;
	}

	String repeat(String str, int times) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++)
			sb.append(str);
		return sb.toString();
	}

	@Override
	public String toString() {
		int maxRow = data.length;
		int maxCol = data[0].length;
		columnLen = new int[maxCol];

		// Calculate column length
		for (int colNum = 0; colNum < maxCol; colNum++) {
			columnLen[colNum] = columnNames[colNum].length();

			for (int rowNum = 0; rowNum < maxRow; rowNum++) {
				int len = data[rowNum][colNum] != null ? data[rowNum][colNum].length() : 0;
				columnLen[colNum] = Math.max(columnLen[colNum], len);
			}
		}

		// Add title
		StringBuilder tablesb = new StringBuilder();
		tablesb.append(toStringRow(-1));

		// Spacer
		tablesb.append(prepend);
		tablesb.append("| ");
		for (int colNum = 0; colNum < maxCol; colNum++)
			tablesb.append(repeat("-", columnLen[colNum]) + " | ");
		tablesb.append('\n');

		// Add rows
		for (int rowNum = 0; rowNum < maxRow; rowNum++) {
			tablesb.append(toStringRow(rowNum));
		}

		return tablesb.toString();
	}

	/**
	 * Convert a map to a string
	 * @param len
	 * @param map
	 * @return
	 */
	String toString(int len, String value) {
		return String.format("%-" + len + "s", (value != null ? value : ""));
	}

	/**
	 * Create a row
	 * @param rowNum
	 * @return
	 */
	String toStringRow(int rowNum) {
		// Create format string
		StringBuilder rowsb = new StringBuilder();

		rowsb.append(prepend);
		rowsb.append("| ");
		int maxRow = (rowNum >= 0 ? data[rowNum].length : columnNames.length);
		for (int colNum = 0; colNum < maxRow; colNum++) {
			if (rowNum >= 0) rowsb.append(toString(columnLen[colNum], data[rowNum][colNum]) + " | ");
			else rowsb.append(toString(columnLen[colNum], columnNames[colNum]) + " | "); // Negative row number means 'title'
		}
		rowsb.append("\n");

		return rowsb.toString();
	}
}
