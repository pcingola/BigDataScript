package org.bds.executioner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashSet;

import org.bds.BdsLog;
import org.bds.data.Data;
import org.bds.data.DataS3;
import org.bds.task.Task;
import org.bds.util.Gpr;

/**
 * TaskLogger: This class adds entries to the `bds.pid.$PID` log file.
 *
 * Background: bds commmnad consists of two parts:
 * 	- `bds-exec`: A binary executable programmed in Go
 * 	- `bds-java`: A java program (this code)
 *
 * The `bds-exec` controls execution of `bds-java` and catches any errors
 * or exceptions.
 *
 * When Java process dies (e.g. die to `Ctrl-C`, fatal error, uncaught
 * exception, etc.), 'bds go' takes control and cleans up. This 'clean up'
 * includes managing several different types of entities, for example:
 * 	- deleting stale files: For example if a task fails, the tasks' output file are in undefined state, so we must clean them up
 * 	- kill processes: If the user performs a `Ctrl-C`, bds tries to clean up all running process, e.g. `task` or `sys` commands that are currently running locally
 *  - kill cluster jobs: Same as before, but for clusters (i.e. kill all `task` running in a cluster when a `Ctrl-C` is received)
 *  - terminate cloud instances: Same as before, but for cloud (i.e. kill all `task` running in a cloud when a `Ctrl-C` is received)
 *  - delete cloud queues: Communication queues for cloud processes must be cleaned up
 *
 * The ways this is done is simply by registering all these entities in a
 * file. This file is usually named `bds.pid.$PID` (where PID is the local
 * process ID). bds-exec created the file name and passes the name of the
 * file to bds-java using the (hidden) `-pid` command line option.
 *
 * This class takes care of registering all the entities that need to be logged
 * in that file. The format is one line per entry:
 * ```
 * entityId \t {'+','-'} \t command_to_delete
 * ```
 * where:
 * 	- entityId: Something that uniquely denotes the entity to remove (process ID, cluster job ID, file absolute path, instance ID, etc.)
 *  - `{+,-}`: This can be either
 *		- '+' indicates where we are adding the entity to be cleaned up
 *		- '-' indicates where we removing the entity (it has already been cleaned up by `bds-java`, so `bds-exec` doesn't need to take care of this entry (e.g. a finished task)
 *	- command: A command the has to be executed in order to clean up (e.g. `rm` to delete a file, `scancel` in s slurm cluster, etc.). Internal commands (e.g. deleting a local file, killing a local process) start with '@'
 *
 * When `bds-exec` takes control (i.e. when `bds-java` dies), it will add to a dictionary (key
 * is `entityId`, value is `command`) all entries having a '+' and delete from the dictionary all
 * `enityId` having a '-'. Then it will execute all `command` to delete the remaining entries.
 *
 * @author pcingola
 */
public class TaskLogger implements Serializable, BdsLog {

	private static final long serialVersionUID = -7712445468457053526L;

	public static final String CMD_REMOVE_FILE = "@rm";
	public static final String CMD_REMOVE_FILE_AWS_S3 = "@aws_s3_rm";
	public static final String CMD_KILL = "@kill";

	protected String fileName;
	protected HashSet<String> ids;

	public TaskLogger(String fileName) {
		if (fileName == null) throw new RuntimeException("Cannot initialize using a null file!");
		this.fileName = fileName;
		ids = new HashSet<>();
		debug("Creating Task logger, file: '" + fileName + "'");
	}

	public synchronized void add(String id, String command) {
		debug("Adding id: '" + id + "', command: '" + command + "'");
		append(createEntry(id, true, command));
	}

	/**
	 * Add a task and the corresponding executioner
	 */
	public synchronized void add(Task task, Executioner executioner) {
		debug("Adding task: '" + task.getId() + "'");
		StringBuilder lines = new StringBuilder();

		// Add pid
		String pid = task.getPid();
		ids.add(pid);

		// Append process PID
		// Prepare command
		StringBuilder cmdsb = new StringBuilder();
		String[] osKillCommand = executioner.osKillCommand(task);
		if (osKillCommand != null) {
			for (String c : executioner.osKillCommand(task))
				cmdsb.append(" " + c);
		}
		String cmd = cmdsb.toString().trim();

		// Append  entry
		lines.append(createEntry(task.getPid(), true, cmd));

		// Append task output files.
		// Note: If this task does not finish (e.g. Ctrl-C), we have to remove these files.
		//       If the task finished OK, we mark them not to be removed
		if (task.getOutputs() != null) {
			for (Data file : task.getOutputs())
				if (file.isRemote()) {
					// Remote file: Delete local copy (it's probably corrupted by the unfinished task
					lines.append(createEntry(file.getLocalPath(), true, CMD_REMOVE_FILE));

					// Can we delete the remote copy of the file?
					switch (file.getDataType()) {
					case S3:
						// We use a custom format "region,bucket,key" to make it easier to parso in bds-exec
						DataS3 d = (DataS3) file;
						String csv = d.getRegion() + ',' + d.getBucket() + ',' + d.getKey();
						lines.append(createEntry(csv, true, CMD_REMOVE_FILE_AWS_S3));
						break;

					case HTTP:
						// Cannot delete HTTP files, these should not be "output"
						break;

					case SFTP:
					case FTP:
						// Note: bds-exec doesn't handle FTP files
						break;

					case TASK:
						// This is not a real "file" data type
						break;

					default:
						throw new RuntimeException("Unhandled data type: '" + file.getDataType() + "', this should never happen!");
					}
				} else {
					// Local file, use remove command
					lines.append(createEntry(file.getAbsolutePath(), true, CMD_REMOVE_FILE));
				}
		}

		// Append all lines to file
		append(lines.toString());
	}

	/**
	 * Append a string to the pidFile
	 */
	protected void append(String str) {
		try {
			debug("Appending to Task logger file '" + fileName + "', lines:\n" + Gpr.prependEachLine("\t\t|", str));
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
			out.print(str);
			out.close(); // We need to flush this as fast as possible to avoid missing PID values in the file
		} catch (Exception e) {
			throw new RuntimeException("Error appending information to file '" + fileName + "'\n", e);
		}
	}

	/**
	 * Create a log entry (a line in the log file)
	 * @param id : Item ID (e.g. file path, process ID, instance ID, etc)
	 * @param add : Add or remove
	 * @param command : Command line to remove item
	 */
	protected String createEntry(String id, boolean add, String command) {
		char sign = add ? '+' : '-';
		String line = id + "\t" + sign + "\t" + command;
		debug("Creating entry: '" + line + "'");
		return line + '\n';
	}

	public HashSet<String> getPids() {
		return ids;
	}

	public synchronized void remove(String id) {
		debug("Removing id: '" + id + "'");
		append(createEntry(id, false, ""));
	}

	/**
	 * Remove a task
	 */
	public synchronized void remove(Task task) {
		debug("Removing task: '" + task.getId() + "'");
		// Remove PID
		String pid = task.getPid();
		ids.remove(pid);

		StringBuilder lines = new StringBuilder();

		// Append process PID
		lines.append(createEntry(task.getPid(), false, ""));

		// Append task output files.
		if (task.getOutputs() != null) {
			for (Data file : task.getOutputs())
				lines.append(createEntry(file.url(), false, ""));
		}

		// Append all lines to file
		append(lines.toString());
	}
}
