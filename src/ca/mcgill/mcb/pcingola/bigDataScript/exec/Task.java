package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.TaskStats;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

/**
 * A task to be executed by an Executioner
 * 
 * @author pcingola
 */
public abstract class Task implements BigDataScriptSerialize, TaskStats {

	protected String id; // Task ID
	protected boolean verbose, debug;
	protected boolean canFail; // Allow execution to fail
	protected boolean done; // Is this task finished
	protected int exitValue; // Exit (error) code
	protected String programFileName; // Program file name
	protected String programTxt; // Program's text (program's code)
	protected String node; // Preferred execution node
	protected String queue; // Preferred execution queue
	protected String stdoutFile, stderrFile; // STDOUT & STDERR Files
	protected HostResources resources;

	public Task() {
		resources = new HostResources();
		reset();
	}

	public Task(String id, String programFileName, String programTxt) {
		this.id = id;
		this.programFileName = programFileName;
		this.programTxt = programTxt;
		resources = new HostResources();
	}

	/**
	 * Create a CmdRunner
	 * @return
	 */
	public abstract CmdRunner cmdRunner();

	public abstract ExecutionerType getExecutionerType();

	public int getExitValue() {
		return exitValue;
	}

	public String getId() {
		return id;
	}

	public String getNode() {
		return node;
	}

	public String getQueue() {
		return queue;
	}

	public HostResources getResources() {
		return resources;
	}

	public String getStderrFile() {
		return stderrFile;
	}

	public String getStdoutFile() {
		return stdoutFile;
	}

	public boolean isCanFail() {
		return canFail;
	}

	/**
	 * Has this task finished?
	 * @return
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Has this task been executed successfully?
	 * @return
	 */
	public boolean isDoneOk() {
		return done && (exitValue == 0);
	}

	/**
	 * Has this task been executed and failed?
	 * @return
	 */
	public boolean isFailed() {
		return done && (exitValue != 0);
	}

	/**
	 * Reset parameters and allow a task to be re-executed
	 */
	public void reset() {
		done = false;
		exitValue = 0;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		// Note that "Task classname" field has been consumed at this point
		id = serializer.getNextField();
		canFail = serializer.getNextFieldBool();
		done = serializer.getNextFieldBool();
		exitValue = (int) serializer.getNextFieldInt();
		node = serializer.getNextField();
		queue = serializer.getNextField();
		programFileName = serializer.getNextFieldString();
		programTxt = serializer.getNextFieldString();
		stdoutFile = serializer.getNextFieldString();
		stderrFile = serializer.getNextFieldString();

		resources = new HostResources();
		resources.serializeParse(serializer);
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		return getClass().getSimpleName() //
				+ "\t" + id // 
				+ "\t" + canFail // 
				+ "\t" + done // 
				+ "\t" + exitValue // 
				+ "\t" + node // 
				+ "\t" + queue // 
				+ "\t" + serializer.serializeSaveValue(programFileName) //
				+ "\t" + serializer.serializeSaveValue(programTxt) //
				+ "\t" + serializer.serializeSaveValue(stdoutFile) //
				+ "\t" + serializer.serializeSaveValue(stderrFile) //
				+ "\t" + resources.serializeSave(serializer) //
				+ "\n";
	}

	public void setCanFail(boolean canFail) {
		this.canFail = canFail;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
