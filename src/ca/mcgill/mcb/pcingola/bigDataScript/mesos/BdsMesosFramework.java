package ca.mcgill.mcb.pcingola.bigDataScript.mesos;

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

import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos.CommandInfo;
import org.apache.mesos.Protos.Credential;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Scheduler;

import com.google.protobuf.ByteString;

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

	String master;
	int status;
	String executorUri;
	CommandInfo executorCmdInfo;
	ExecutorID executorId;
	ExecutorInfo executorInfo;
	Scheduler scheduler;
	MesosSchedulerDriver driver;
	FrameworkInfo frameworkInfo;

	public static void main(String[] args) throws Exception {
		if (args.length < 1 || args.length > 2) {
			usage();
			System.exit(1);
		}

		BdsMesosFramework bdsMesosFramework = new BdsMesosFramework(args[0]);
		bdsMesosFramework.run();

		System.exit(bdsMesosFramework.status);
	}

	private static void usage() {
		String name = BdsMesosFramework.class.getName();
		System.err.println("Usage: " + name + " master <tasks>");
	}

	public BdsMesosFramework(String master) {
		super();
		this.master = master;
	}

	/**
	 * Kill framework driver
	 */
	public void kill() {
		driver.abort();
		// driver.stop();
	}

	@Override
	public void run() {
		try {
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
			scheduler = new BdsMesosScheduler(executorInfo);

			//---
			// Initialize framework
			//---
			FrameworkInfo.Builder frameworkBuilder = FrameworkInfo.newBuilder() //
					.setUser("") // Have Mesos fill in the current user.
					.setName(BDS_FRAMEWORK_NAME);

			// Enable checkpointing
			if (System.getenv("MESOS_CHECKPOINT") != null) {
				System.out.println("Enabling checkpoint for the framework");
				frameworkBuilder.setCheckpoint(true);
			}

			// Create framework
			frameworkInfo = frameworkBuilder.build();

			if (System.getenv("MESOS_AUTHENTICATE") != null) {
				System.out.println("Enabling authentication for the framework");

				if (System.getenv("DEFAULT_PRINCIPAL") == null) {
					System.err.println("Expecting authentication principal in the environment");
					System.exit(1);
				}

				if (System.getenv("DEFAULT_SECRET") == null) {
					System.err.println("Expecting authentication secret in the environment");
					System.exit(1);
				}

				Credential credential = Credential.newBuilder() //
						.setPrincipal(System.getenv("DEFAULT_PRINCIPAL")) //
						.setSecret(ByteString.copyFrom(System.getenv("DEFAULT_SECRET").getBytes())) //
						.build();

				driver = new MesosSchedulerDriver(scheduler, frameworkInfo, master, credential);
			} else {
				driver = new MesosSchedulerDriver(scheduler, frameworkInfo, master);
			}

			//---
			// Run driver
			//---
			status = driver.run() == Status.DRIVER_STOPPED ? 0 : 1;
		} catch (Exception e) {
			e.printStackTrace();
			status = 1;
			if (driver != null) driver.stop(); // Ensure that the driver process terminates.
		}
	}
}
