package org.bds.mesos;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;

import org.apache.mesos.Executor;
import org.apache.mesos.ExecutorDriver;
import org.apache.mesos.MesosExecutorDriver;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.SlaveInfo;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Protos.TaskInfo;
import org.apache.mesos.Protos.TaskState;
import org.apache.mesos.Protos.TaskStatus;
import org.bds.executioner.NotifyTaskState;
import org.bds.executioner.PidParser;
import org.bds.osCmd.Cmd;
import org.bds.osCmd.CmdLocal;
import org.bds.task.Task;
import org.bds.util.Gpr;

/**
 * BDS executor for Mesos cluster framework
 *
 * References:
 * 		http://mesos.apache.org/documentation/latest/app-framework-development-guide/
 *
 *
 * Notes: 	This scheduler's 'main' method is invoked by Mesos framework upon executing BDS_EXECUTOR_SCRIPT.
 * 			So both the script and this class must be available to all Mesos slaves
 *
 *
 * @author pcingola
 */
public class BdsMesosExecutor implements Executor, NotifyTaskState, PidParser {

	class CmdInfo {
		public final ExecutorDriver executorDriver;
		public final TaskInfo taskInfo;
		public final Task task;
		public final Cmd cmd;

		public CmdInfo(ExecutorDriver executorDriver, TaskInfo taskInfo, Task task, Cmd cmd) {
			this.executorDriver = executorDriver;
			this.taskInfo = taskInfo;
			this.task = task;
			this.cmd = cmd;
		}
	}

	public static boolean debug = false;
	HashMap<String, CmdInfo> cmdInfoById;

	// Script used to fire up BDS executor
	// This is script invoked by Mesos when a task is executed
	// The script invokes this class' main method
	public static String BDS_EXECUTOR_SCRIPT = BdsMesosFramework.BDS_FRAMEWORK_DIR + "/bds_mesos_executor.sh";

	/**
	 * Main: Entry point for executor, invoked by Mesos framework
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Starting executor: " + BdsMesosExecutor.class.getSimpleName());
		MesosExecutorDriver driver = new MesosExecutorDriver(new BdsMesosExecutor());
		System.out.println("Finished executor: " + BdsMesosExecutor.class.getSimpleName());
		System.exit(driver.run() == Status.DRIVER_STOPPED ? 0 : 1);
	}

	public BdsMesosExecutor() {
		cmdInfoById = new HashMap<String, BdsMesosExecutor.CmdInfo>();
	}

	/**
	 * Change task state
	 */
	protected void changeTaskState(ExecutorDriver driver, TaskInfo task, TaskState state) {
		TaskStatus status = TaskStatus.newBuilder().setTaskId(task.getTaskId()).setState(state).build();
		driver.sendStatusUpdate(status);
		if (debug) Gpr.debug("Task " + task.getTaskId().getValue() + " is now '" + state + "'");
	}

	/**
	 * Invoked when the executor becomes "disconnected" from the slave
	 * (e.g., the slave is being restarted due to an upgrade).
	 */
	@Override
	public void disconnected(ExecutorDriver driver) {
		if (debug) Gpr.debug("Executor: Disconnected");
		killAll();
	}

	/**
	 * Invoked when a fatal error has occured with the executor and/or
	 * executor driver. The driver will be aborted BEFORE invoking this
	 * callback.
	 */
	@Override
	public void error(ExecutorDriver driver, String message) {
		if (debug) Gpr.debug("Executor: Error '" + message + "'");
		killAll();
	}

	/**
	 * Invoked when a framework message has arrived for this
	 * executor. These messages are best effort; do not expect a
	 * framework message to be retransmitted in any reliable fashion.
	 */
	@Override
	public void frameworkMessage(ExecutorDriver driver, byte[] data) {
		if (debug) Gpr.debug("Executor: Framework message '" + new String(data) + "'");
	}

	/**
	 * Kill all pending tasks
	 */
	void killAll() {
		// Kill all tasks
		for (CmdInfo ci : cmdInfoById.values())
			ci.cmd.kill();

		// Clean up
		cmdInfoById = new HashMap<String, BdsMesosExecutor.CmdInfo>();
	}

	/**
	 * Invoked when a task running within this executor has been killed
	 * (via SchedulerDriver::killTask). Note that no status update will
	 * be sent on behalf of the executor, the executor is responsible
	 * for creating a new TaskStatus (i.e., with TASK_KILLED) and
	 * invoking ExecutorDriver::sendStatusUpdate.
	 */
	@Override
	public void killTask(ExecutorDriver driver, TaskID taskId) {
		String tid = taskId.getValue();
		CmdInfo cmdInfo = cmdInfoById.get(tid);
		if (cmdInfo == null) return;
		cmdInfo.cmd.kill();
	}

	/**
	 * Invoked when a task has been launched on this executor (initiated
	 * via Scheduler::launchTasks). Note that this task can be realized
	 * with a thread, a process, or some simple computation, however, no
	 * other callbacks will be invoked on this executor until this
	 * callback has returned.
	 */
	@Override
	public void launchTask(final ExecutorDriver driver, final TaskInfo taskInfo) {
		try {
			String tid = taskInfo.getTaskId().getValue();
			if (debug) Gpr.debug("Executor: Launching task '" + tid + "'");

			// Unpack command line arguments
			String packedCmd = taskInfo.getData().toStringUtf8();
			String cmdArgs[] = BdsMesosFramework.unpackArray(packedCmd);

			if (debug) {
				System.out.println("Executning command:");
				for (int i = 0; i < cmdArgs.length; i++)
					System.out.println("\targs[" + i + "]:\t'" + cmdArgs[i] + "'");
			}

			// Create a command, task
			Task task = new Task(tid);
			CmdLocal cmd = new CmdLocal(tid, cmdArgs);
			cmd.setTask(task);
			cmd.setNotifyTaskState(this);
			cmd.setPidParser(this);
			cmd.setDebug(debug);

			// Store information
			CmdInfo cmdInfo = new CmdInfo(driver, taskInfo, task, cmd);
			cmdInfoById.put(tid, cmdInfo);

			// Run command
			if (debug) System.out.println("Running Cmd thread");
			cmd.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String parsePidLine(String line) {
		return line.trim();
	}

	/**
	 * Invoked once the executor driver has been able to successfully
	 * connect with Mesos. In particular, a scheduler can pass some
	 * data to it's executors through the FrameworkInfo.ExecutorInfo's
	 * data field.
	 */
	@Override
	public void registered(ExecutorDriver driver, ExecutorInfo executorInfo, FrameworkInfo frameworkInfo, SlaveInfo slaveInfo) {
		if (debug) Gpr.debug("Executor: Registered on " + slaveInfo.getHostname());
	}

	/**
	 * Invoked when the executor re-registers with a restarted slave.
	 */
	@Override
	public void reregistered(ExecutorDriver driver, SlaveInfo slaveInfo) {
		if (debug) Gpr.debug("Executor: Re-Registered on " + slaveInfo.getHostname());
	}

	/**
	 * Invoked when the executor should terminate all of it's currently
	 * running tasks. Note that after a Mesos has determined that an
	 * executor has terminated any tasks that the executor did not send
	 * terminal status updates for (e.g., TASK_KILLED, TASK_FINISHED,
	 * TASK_FAILED, etc) a TASK_LOST status update will be created.
	 */
	@Override
	public void shutdown(ExecutorDriver driver) {
		if (debug) Gpr.debug("Executor: Shutdown");
		killAll();
	}

	@Override
	public void taskFinished(Task task, org.bds.task.TaskState taskState) {
		String tid = task.getId();
		if (debug) Gpr.debug("Task " + tid + " finished");

		CmdInfo cmdInfo = cmdInfoById.get(tid);
		if (cmdInfo == null) {
			if (debug) Gpr.debug("Task '" + tid + "' not found");
			return;
		}

		// Change Mesos task status to FINISHED
		changeTaskState(cmdInfo.executorDriver, cmdInfo.taskInfo, TaskState.TASK_FINISHED);

		// Clean up
		cmdInfoById.remove(tid);
	}

	@Override
	public void taskRunning(Task task) {
		String tid = task.getId();
		if (debug) Gpr.debug("Task " + tid + " running");

		CmdInfo cmdInfo = cmdInfoById.get(tid);
		if (cmdInfo == null) {
			if (debug) Gpr.debug("Task '" + tid + "' not found");
			return;
		}

		// Change Mesos task status to FINISHED
		changeTaskState(cmdInfo.executorDriver, cmdInfo.taskInfo, TaskState.TASK_RUNNING);
	}

	@Override
	public void taskStarted(Task task) {
		String tid = task.getId();
		if (debug) Gpr.debug("Task " + tid + " started");
	}

}
