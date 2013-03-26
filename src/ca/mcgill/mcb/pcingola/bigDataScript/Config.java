package ca.mcgill.mcb.pcingola.bigDataScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Config file
 * 
 * @author pcingola

 */
public class Config {

	public static final String DEFAULT_CONFIG_FILE = Gpr.HOME + "/.bds/" + BigDataScript.class.getSimpleName().toLowerCase() + ".config";

	private static Config configInstance = null; // Config is some kind of singleton because we want to make it accessible from everywhere

	boolean debug = false; // Debug mode?
	boolean verbose = false; // Verbose mode?
	String configDirName;
	String pidFile;
	Properties properties;
	ArrayList<String> sshNodes;

	public static Config get() {
		return configInstance;
	}

	public Config() {
		read(DEFAULT_CONFIG_FILE); // Read config file 
		configInstance = this;
	}

	/**
	 * Create a configuration from 'configFileName'
	 * @param genomeVersion
	 * @param configFileName
	 */
	public Config(String configFileName, boolean verbose) {
		this.verbose = verbose;
		read(configFileName); // Read config file 
		configInstance = this;
	}

	/**
	 * Get a property as a double
	 * @param propertyName
	 * @return
	 */
	protected double getDouble(String propertyName, double defaultValue) {
		String val = getString(propertyName);
		if (val == null) return defaultValue;
		return Gpr.parseDoubleSafe(val);
	}

	/**
	 * Get a property as a long
	 * @param propertyName
	 * @return
	 */
	protected long getLong(String propertyName, long defaultValue) {
		String val = getString(propertyName);
		if (val == null) return defaultValue;
		return Gpr.parseLongSafe(val);
	}

	public String getPidFile() {
		return pidFile;
	}

	public ArrayList<String> getSshNodes() {
		return sshNodes;
	}

	/**
	 * Get a property as a string
	 * @param propertyName
	 * @return
	 */
	protected String getString(String propertyName) {
		return properties.getProperty(propertyName);
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Read configuration file and create all 'genomes' 
	 * @return
	 */
	private void read(String configFileName) {

		if (!Gpr.exists(configFileName)) {
			if (verbose) Timer.showStdErr("Config file '" + configFileName + "' not found");
			return;
		}

		//---
		// Read properties file
		//---
		properties = new Properties();
		try {
			properties.load(new FileReader(new File(configFileName)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Cannot find config file '" + configFileName + "'");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Config directory
		configDirName = "";
		try {
			File configDir = new File(configFileName).getAbsoluteFile().getParentFile();
			configDirName = configDir.getCanonicalPath();
		} catch (IOException e1) {
		}

		//---
		// Set attributes
		//---

		// Set properties
		setFromProperties();
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Set from parameter properties
	 */
	void setFromProperties() {
		//---
		// Parse Ssh nodes option
		//---
		sshNodes = new ArrayList<String>();
		String sshNodesStr = getString("ssh.nodes");
		if (sshNodesStr != null) {
			for (String sshNode : sshNodesStr.split(",")) {
				sshNode = sshNode.trim();
				if (!sshNode.isEmpty()) sshNodes.add(sshNode);
			}
		}
	}

	public void setPidFile(String pidFile) {
		this.pidFile = pidFile;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}
}
