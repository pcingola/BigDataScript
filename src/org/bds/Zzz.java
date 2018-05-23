package org.bds;

import org.bds.cluster.host.Host;
import org.bds.osCmd.Ssh;

public class Zzz {

	public static boolean debug = true;
	public static boolean verbose = true;

	public static final String USER_DIR = "user.dir";

	public static void main(String[] args) throws Exception {
		System.out.println("Start");

		Host lh = new Host("localhost");
		Ssh ssh = new Ssh(lh);
		System.out.println(ssh.exec("echo $PATH; pwd"));

		System.out.println("End");
	}
}
