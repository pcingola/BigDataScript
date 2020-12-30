package org.bds.lang.statement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.util.Gpr;

/**
 * A block of statements associated to a file (program's source)
 *
 * @author pcingola
 */
public class BlockWithFile extends Block {

	private static final long serialVersionUID = 3737857070704132096L;

	protected String fileName;
	protected String fileText;

	public BlockWithFile(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public File getFile() {
		if (fileName != null) return new File(fileName);
		else return super.getFile();
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	public String getFileText() {
		return fileText;
	}

	@Override
	public Statement[] getStatements() {
		return statements;
	}

	/**
	 * Save to a file
	 */
	public void save(boolean verbose) {
		String reBasedName = "./" + getFileName();

		// Write file
		log("Extracting file (program unit): '" + reBasedName);
		if (Gpr.exists(reBasedName)) {
			// Do not overwrite!
			log("File '" + reBasedName + "' already exists: Nothing done!");
		} else {
			String dirName = Gpr.dirName(reBasedName);
			(new File(dirName)).mkdirs(); // Create directories
			Gpr.toFile(reBasedName, getFileText()); // Save file
		}
	}

	public void setFile(File file) {
		if (file == null) setFileName(null);
		else setFileName(file.toString());
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		if (fileName != null) fileText = Gpr.readFile(fileName);
	}

	/**
	 * Find all variable declarations
	 */
	public List<VarDeclaration> varDeclarations(boolean sort) {
		Set<String> included = new HashSet<>();

		List<VarDeclaration> varDecls = new ArrayList<>();
		varDecls.addAll(varDeclarations(included));

		// Sort by variable name?
		if (sort) {
			Collections.sort(varDecls, new Comparator<VarDeclaration>() {
				@Override
				public int compare(VarDeclaration v1, VarDeclaration v2) {
					String vname1 = v1.getVarInit()[0].getVarName();
					String vname2 = v2.getVarInit()[0].getVarName();
					return vname1.compareTo(vname2);
				}
			});
		}

		return varDecls;
	}

	/**
	 * Find all global variable declarations within this block-statement
	 */
	protected List<VarDeclaration> varDeclarations(Set<String> included) {
		List<VarDeclaration> varDecls = new ArrayList<>();

		// Already added?
		String fileName = getFileName();
		if (included.contains(fileName)) return varDecls;
		included.add(fileName);

		for (Statement s : getStatements()) {
			// Add variable
			if (s instanceof VarDeclaration) varDecls.add((VarDeclaration) s);

			// Recurse
			if (s instanceof StatementInclude) {
				StatementInclude sincl = (StatementInclude) s;
				varDecls.addAll(sincl.varDeclarations(included));
			}
		}

		return varDecls;
	}

	/**
	 * Find a variable init by name
	 */
	public VariableInit varInit(String varName) {
		for (VarDeclaration varDecl : varDeclarations(false))
			for (VariableInit varInit : varDecl.getVarInit())
				if (varInit.getVarName().equals(varName)) return varInit;
		return null;
	}

}
