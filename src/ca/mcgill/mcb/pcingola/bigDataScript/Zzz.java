package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.executioner.ExecutionerSsh;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		// Create a task
		String id = "task_zzz";
		String programFileName = Gpr.HOME + "/z.sh";
		String programTxt = "ls -al";
		String bdsFileName = "z.bds";
		int bdsLineNum = 42;
		Task task = new Task(id, programFileName, programTxt, bdsFileName, bdsLineNum);
		System.out.println("Task: " + task);

		Config config = Config.get();
		ExecutionerSsh essh = new ExecutionerSsh(config);
		essh.start();
		//		essh.add(task);

		Thread.sleep(10000);
		Gpr.debug("Kill");
		essh.kill();

		Gpr.debug("Zzz: End");
	}
}
