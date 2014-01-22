package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Ssh;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		//		String cmd = "/Users/pcingola/.bds/bds " //
		//				+ "exec " //
		//				+ "86400 " //
		//				+ "/Users/pcingola/Documents/workspace/BigDataScript/z.bds.20140121_203503_453/task.line_9.id_1.stdout "//
		//				+ "/Users/pcingola/Documents/workspace/BigDataScript/z.bds.20140121_203503_453/task.line_9.id_1.stderr " //
		//				+ "- " // 
		//				+ "/Users/pcingola/Documents/workspace/BigDataScript/z.bds.20140121_203503_453/task.line_9.id_1.sh";

		String cmd = "bds " //
				+ "exec " //
				+ "86400 " //
				+ "- "//
				+ "- " // 
				+ "- " // 
				+ "/Users/pcingola/Documents/workspace/BigDataScript/z.bds.20140121_203503_453/task.line_9.id_1.sh" //
		;

		Host h = new Host("pcingola@localhost");
		Ssh ssh = new Ssh(h);
		ssh.setShowStdout(true);
		ssh.exec(cmd);

		//		// Create a task
		//		String id = "task_zzz";
		//		String programFileName = Gpr.HOME + "/z.sh";
		//		String programTxt = "ls -al";
		//		String bdsFileName = "z.bds";
		//		int bdsLineNum = 42;
		//		Task task = new Task(id, programFileName, programTxt, bdsFileName, bdsLineNum);
		//		System.out.println("Task: " + task);
		//
		//		Config config = Config.get();
		//		ExecutionerSsh essh = new ExecutionerSsh(config);
		//		essh.start();
		//
		//		essh.add(task);
		//
		//		Thread.sleep(60000);
		//		Gpr.debug("Kill");
		//		essh.kill();

		Gpr.debug("Zzz: End");
	}
}
