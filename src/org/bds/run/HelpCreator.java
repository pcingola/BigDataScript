package org.bds.run;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bds.lang.ProgramUnit;
import org.bds.lang.statement.Block;
import org.bds.lang.statement.Help;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.StatementInclude;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.util.AutoHashMap;
import org.bds.util.Gpr;

/**
 * Create BDS's automatic help messages
 *
 * @author pcingola
 */
public class HelpCreator {

	public static final String HELP_UNSORTED_VAR_NAME = "helpUnsorted";
	public static final String formatOpt = "%s %s";

	boolean sortVars;
	int maxNameTypeLen;
	String helpString;
	Set<String> included;
	AutoHashMap<String, LinkedList<VarDeclaration>> varDeclBySection;
	List<String> helpStrings;
	ProgramUnit programUnit;

	public HelpCreator(ProgramUnit programUnit) {
		this.programUnit = programUnit;
		helpStrings = new LinkedList<>();
		included = new HashSet<>();
		varDeclBySection = new AutoHashMap<>(new LinkedList<VarDeclaration>());

		addSection(null); // Initialize default section
	}

	/**
	 * Add variable declaration to current section
	 */
	void add(VarDeclaration varDecl) {
		varDeclBySection.getOrCreate(helpString).add(varDecl);
	}

	/**
	 * New 'help section'
	 */
	void addSection(Help help) {
		helpString = help != null ? help.getHelpString() : "";
		helpStrings.add(helpString);
	}

	/**
	 * Create and print automatic 'help' message
	 */
	String createHelp() {
		StringBuilder sb = new StringBuilder();

		// Find variable declarations and help sections
		Set<String> included = new HashSet<>();
		findHelpEntries(programUnit, included);

		// Use unsorted variables if 'helpUnsorted' exists (regardless of its map)
		sortVars = (programUnit.varInit(HELP_UNSORTED_VAR_NAME) == null);

		// Find maximum name-type length
		maxNameTypeLen();

		// Create help for each section
		String programName = Gpr.baseName(programUnit.getFileName());
		for (String section : helpStrings) {
			// Create help for this section
			String helpSection = createHelp(section);

			if (!helpSection.isEmpty()) {
				// Default header when not help statement is given before
				if (section.isEmpty()) sb.append("Command line options '" + programName + "' :\n");

				sb.append(helpSection);
			}
		}

		// No command line options?
		if (sb.length() <= 0) sb.append("No help available for script '" + programName + "'");

		return sb.toString();
	}

	/**
	 * Create help section
	 */
	String createHelp(String section) {
		StringBuilder sb = new StringBuilder();

		// Add section help
		if (!section.isEmpty()) sb.append(section + "\n");

		// Get all variable declarations in this 'help section'
		List<VarDeclaration> varDecls = varDeclBySection.get(section);
		if (varDecls == null) return sb.toString();

		// Sort by variable name?
		if (sortVars) {
			Collections.sort(varDecls, new Comparator<VarDeclaration>() {
				@Override
				public int compare(VarDeclaration v1, VarDeclaration v2) {
					String vname1 = v1.getVarInit()[0].getVarName();
					String vname2 = v2.getVarInit()[0].getVarName();
					return vname1.compareTo(vname2);
				}
			});
		}

		// Add help on each variable declaration
		for (VarDeclaration varDecl : varDecls) {
			// Type string
			Type type = varDecl.getType();
			String typeStr = typeString(type);
			if (typeStr == null) continue;

			// Add help for each variable
			for (VariableInit vi : varDecl.getVarInit())
				sb.append(createHelpLine(typeStr, vi));

		}

		return sb.toString();
	}

	/**
	 * Create help line (variable 'vi')
	 */
	String createHelpLine(String typeStr, VariableInit vi) {
		if (!hasHelp(vi)) return "";

		// Create help line
		String varName = vi.getVarName();
		int len = maxNameTypeLen - varName.length();
		String format = "\t-%s %-" + len + "s : %s\n";
		return String.format(format, varName, typeStr, vi.getHelp());
	}

	/**
	 * Create help for variable 'vi' (only variable name and type part)
	 */
	String createHelpNameType(String typeStr, VariableInit vi) {
		if (hasHelp(vi)) return String.format(formatOpt, vi.getVarName(), typeStr);
		return "";
	}

	/**
	 * Find help declarations
	 */
	protected void findHelpEntries(Block block, Set<String> included) {
		// Already added?
		String fileName = block.getFileName();
		if (included.contains(fileName)) return;
		included.add(fileName);

		for (Statement s : block.getStatements()) {
			if (s instanceof VarDeclaration) {
				add((VarDeclaration) s); // Add variable
			} else if (s instanceof Help) {
				addSection((Help) s);
			} else if (s instanceof StatementInclude) {
				// Recurse into include statements
				StatementInclude sincl = (StatementInclude) s;
				findHelpEntries(sincl, included);
			}
		}
	}

	/**
	 * Does this variable have 'help' section?
	 */
	boolean hasHelp(VariableInit vi) {
		return vi != null && vi.getVarName() != null && vi.getHelp() != null;
	}

	/**
	 * Calculate max option string length
	 */
	void maxNameTypeLen() {
		maxNameTypeLen = 0;
		for (List<VarDeclaration> varDecls : varDeclBySection.values()) {
			for (VarDeclaration varDecl : varDecls) {
				Type type = varDecl.getType();
				String typeStr = typeString(type);
				if (typeStr == null) continue;

				// Get variable's name & help
				for (VariableInit vi : varDecl.getVarInit()) {
					// Format name and type, calculate length
					String helpLine = createHelpNameType(typeStr, vi);
					int optLen = helpLine.length();
					maxNameTypeLen = Math.max(maxNameTypeLen, optLen);
				}
			}
		}
	}

	@Override
	public String toString() {
		return createHelp();
	}

	String typeString(Type type) {
		// Show types
		if (type.isBool()) return "<bool>";
		else if (type.isInt()) return "<int>";
		else if (type.isReal()) return "<real>";
		else if (type.isString()) return "<string>";
		else if (type.isList(Types.STRING)) return "<string ... string>";

		return null;
	}

}
