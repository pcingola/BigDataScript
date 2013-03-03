package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Represents the resources in a host: Cpus, memory, etc.
 * 
 * Can be either a host's resources (how many CPU it has) or a job
 * resources (how much time it needs)
 * 
 * Any negative number means "information not available"
 * 
 * @author pcingola
 */
public class HostResources implements Comparable<HostResources>, BigDataScriptSerialize {

	int cpus; // Number of CPUs 
	long mem; // Total memory (in Bytes)
	long timeout; // Time before the process is killed (in seconds)

	public HostResources() {
		cpus = 1; // One cpu
		mem = -1; // No mem insfo
		timeout = -1;
	}

	public HostResources(HostResources hr) {
		cpus = hr.cpus;
		mem = hr.mem;
		timeout = -1;
	}

	@Override
	public int compareTo(HostResources o) {

		if (cpus >= 0) {
			int cmp = cpus - o.cpus;
			if (cmp < 0) return -1;
		}

		if (mem >= 0) {
			long cmp = mem - o.mem;
			if (cmp < 0) return -1;
		}

		return 1;
	}

	public int getCpus() {
		return cpus;
	}

	public long getMem() {
		return mem;
	}

	public long getTimeout() {
		return timeout;
	}

	/**
	 * Subtract resources
	 * @param hr
	 */
	public void minus(HostResources hr) {
		if ((cpus >= 0) && (hr.cpus >= 0)) cpus = Math.max(0, cpus - hr.cpus);
		if ((mem >= 0) && (hr.mem >= 0)) mem = Math.max(0, mem - hr.mem);
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		cpus = (int) serializer.getNextFieldInt();
		mem = serializer.getNextFieldInt();
		timeout = serializer.getNextFieldInt();
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		return cpus + "\t" + mem + "\t" + timeout;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public void setMem(long mem) {
		this.mem = mem;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "cpus: " + cpus + "\tmem: " + Gpr.toStringMem(mem) + (timeout > 0 ? "\ttimeout: " + timeout : "");
	}
}
