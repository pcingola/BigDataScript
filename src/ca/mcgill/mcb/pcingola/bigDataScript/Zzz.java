package ca.mcgill.mcb.pcingola.bigDataScript;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Ssh;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class Zzz {

	public static void main(String[] args) throws Exception {
		Gpr.debug("Zzz: Start");

		String cmd = "ls -al zxzxzx";
		Host h = new Host("pcingola@localhost");
		Ssh ssh = new Ssh(h);
		ssh.setShowStdout(true);
		ssh.exec(cmd);

		Gpr.debug("Zzz: End");
	}
}
