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

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos.CommandInfo;
import org.apache.mesos.Protos.Credential;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Protos.TaskID;
import org.bds.Config;
import org.bds.executioner.ExecutionerMesos;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * A sample class to run BDS' Mesos framework
 *
 * IMPORTANT:
 * Since Mesos is implemented in C++, Mesos's Java library (mesos.jar) is just a wrapper that
 * invokes native code. As such, a binary implementation must be linked to JAR file.
 * In Eclipse, this is done by right clicking on 'mesos.jar' -> Properties -> Native libraries -> Add folder containing 'libmesos.so'
 *
 * @author pcingola
 */
public class BdsMesosFramework extends Thread {

	public static final String BDS_FRAMEWORK_NAME = "BDS_FRAMEWORK";
	public static final String BDS_FRAMEWORK_DIR = Config.DEFAULT_CONFIG_DIR + "/mesos";

	boolean verbose, debug;
	String master;
	int status;
	String executorUri;
	CommandInfo executorCmdInfo;
	ExecutorID executorId;
	ExecutorInfo executorInfo;
	BdsMesosScheduler scheduler;
	MesosSchedulerDriver schedulerDriver;
	FrameworkInfo frameworkInfo;
	ExecutionerMesos executionerMesos;

	/**
	 * Pack an array into a string
	 */
	public static String packArray(String[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i > 0) sb.append('\0');
			sb.append(array[i]);
		}
		return sb.toString();
	}

	// Mesos does not accept '/' in task IDs
	public static String taskIdMesos(Task task) {
		return task.getId().replaceAll("/", "_");
	}

	/**
	 * Unpack an array from a string
	 */
	public static String[] unpackArray(String string) {
		return string.split("\0");
	}

	public BdsMesosFramework(ExecutionerMesos executionerMesos, String master) {
		super();
		this.master = master;
		this.executionerMesos = executionerMesos;
	}

	public void add(Task task) {
		scheduler.add(task);
	}

	public MesosSchedulerDriver getDriver() {
		return schedulerDriver;
	}

	public FrameworkInfo getFrameworkInfo() {
		return frameworkInfo;
	}

	String getMasterIp() {
		String m[] = master.split(":");
		String name = m[0];
		String port = (m.length > 0 ? m[1] : "");

		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(name);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

		String ipPort = inetAddress.getHostAddress() + (!port.isEmpty() ? ":" + port : "");
		if (debug) Gpr.debug("Master name: '" + name + "'\tport: '" + port + "'\tipPort: '" + ipPort + "'");
		return ipPort;
	}

	public BdsMesosScheduler getScheduler() {
		return scheduler;
	}

	public int getStatus() {
		return status;
	}

	public boolean isReady() {
		return schedulerDriver != null;
	}

	/**
	 * Kill framework driver
	 */
	public void kill() {
		schedulerDriver.abort();
	}

	/**
	 * Kill framework driver
	 */
	public void kill(Task task) {
		if (schedulerDriver != null) {
			String taskId = taskIdMesos(task);
			schedulerDriver.killTask(TaskID.newBuilder().setValue(taskId).build());
		}
	}

	@Override
	public void run() {
		try {
			String masterIpPort = getMasterIp();

			//---
			// Initialize executor
			//---
			File execScriptFile = new File(BdsMesosExecutor.BDS_EXECUTOR_SCRIPT);
			if (!execScriptFile.exists()) throw new RuntimeException("Cannot find BDS_EXECUTOR_SCRIPT file '" + execScriptFile.getCanonicalPath() + "'");
			executorUri = execScriptFile.getCanonicalPath();

			executorCmdInfo = CommandInfo.newBuilder().setValue(executorUri).build(); // Command (script) used to invoke executor's main method

			executorId = ExecutorID.newBuilder().setValue("default").build(); // Executor ID

			executorInfo = ExecutorInfo.newBuilder() //
					.setExecutorId(executorId) //
					.setCommand(executorCmdInfo) //
					.setName(BDS_FRAMEWORK_NAME) //
					.build();

			//---
			// Initialize scheduler
			//---
			scheduler = new BdsMesosScheduler(executionerMesos, executorInfo);

			//---
			// Initialize framework
			//---
			FrameworkInfo.Builder frameworkBuilder = FrameworkInfo.newBuilder() //
					.setUser("") // Have Mesos fill in the current user.
					.setName(BDS_FRAMEWORK_NAME);

			// Enable checkpointing
			if (System.getenv("MESOS_CHECKPOINT") != null) {
				if (verbose) Timer.showStdErr("Enabling checkpoint for Mesos framework");
				frameworkBuilder.setCheckpoint(true);
			}

			// Create framework
			frameworkInfo = frameworkBuilder.build();

			if (System.getenv("MESOS_AUTHENTICATE") != null) {
				if (verbose) Timer.showStdErr("Enabling authentication for Mesos framework");

				if (System.getenv("DEFAULT_PRINCIPAL") == null) throw new RuntimeException("Expecting authentication principal in the environment");
				if (System.getenv("DEFAULT_SECRET") == null) throw new RuntimeException("Expecting authentication secret in the environment");

				Credential credential = Credential.newBuilder() //
						.setPrincipal(System.getenv("DEFAULT_PRINCIPAL")) //
						.setSecret(System.getenv("DEFAULT_SECRET")) //
						.build();

				if (verbose) Timer.showStdErr("Setting up Mesos Driver with credentials, master = '" + masterIpPort + "'");
				schedulerDriver = new MesosSchedulerDriver(scheduler, frameworkInfo, masterIpPort, credential);
			} else {
				if (verbose) Timer.showStdErr("Setting up Mesos Driver, master = '" + masterIpPort + "'");
				schedulerDriver = new MesosSchedulerDriver(scheduler, frameworkInfo, masterIpPort);
			}

			//---
			// Run driver
			//---
			status = schedulerDriver.run() == Status.DRIVER_STOPPED ? 0 : 1;
		} catch (Exception e) {
			e.printStackTrace();
			status = 1;
			if (schedulerDriver != null) schedulerDriver.stop(); // Ensure that the driver process terminates.
		}
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
