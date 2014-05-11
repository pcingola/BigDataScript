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
public class BdsFramework {

	public static final String BDS_FRAMEWORK_NAME = "BDS_FRAMEWORK";

	public static void main(String[] args) throws Exception {

		if (args.length < 1 || args.length > 2) {
			usage();
			System.exit(1);
		}

		//---
		// Initialize executor
		//---
		File execScriptFile = new File(BdsExecutor.BDS_EXECUTOR_SCRIPT);
		if (!execScriptFile.exists()) throw new RuntimeException("Cannot find BDS_EXECUTOR_SCRIPT file '" + execScriptFile.getCanonicalPath() + "'");
		String uri = execScriptFile.getCanonicalPath();

		CommandInfo cmdInfo = CommandInfo.newBuilder().setValue(uri).build(); // Command (script) used to invoke executor's main method

		ExecutorID execId = ExecutorID.newBuilder().setValue("default").build(); // Executor ID

		ExecutorInfo executor = ExecutorInfo.newBuilder() //
				.setExecutorId(execId) //
				.setCommand(cmdInfo) //
				.setName(BDS_FRAMEWORK_NAME) //
				.build();

		//---
		// Initialize scheduler
		//---
		Scheduler scheduler = new BdsScheduler(executor);

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
		FrameworkInfo framework = frameworkBuilder.build();

		MesosSchedulerDriver driver = null;
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

			driver = new MesosSchedulerDriver(scheduler, framework, args[0], credential);
		} else {
			driver = new MesosSchedulerDriver(scheduler, framework, args[0]);
		}

		//---
		// Run driver
		//---
		int status = driver.run() == Status.DRIVER_STOPPED ? 0 : 1;
		driver.stop(); // Ensure that the driver process terminates.

		System.exit(status);
	}

	private static void usage() {
		String name = BdsFramework.class.getName();
		System.err.println("Usage: " + name + " master <tasks>");
	}
}
