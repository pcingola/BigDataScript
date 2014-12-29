package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

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
public class HostResources implements Comparable<HostResources>, BigDataScriptSerialize, Cloneable {

	private static int hostResourcesNum = 0;

	int id;
	int cpus; // Number of CPUs
	long mem; // Total memory (in Bytes)
	long timeout; // Time before the process is killed (in seconds). Only processing time, it does not include the time the process is queued for execution by the cluster scheduler.
	long wallTimeout; // Real time (wall time) before the process is killed (in seconds). This includes the time the process is waiting to be executed.

	protected static int nextId() {
		return ++hostResourcesNum;
	}

	public HostResources() {
		cpus = 1; // One cpu
		mem = -1; // No mem insfo
		timeout = 0; // No timeout
		wallTimeout = 0; // No timeout
		id = nextId();
	}

	public HostResources(HostResources hr) {
		cpus = hr.cpus;
		mem = hr.mem;
		timeout = hr.timeout;
		wallTimeout = hr.wallTimeout;
		id = nextId();
	}

	@Override
	public HostResources clone() {
		try {
			return (HostResources) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int compareTo(HostResources hr) {
		int cmpCpu = 0;
		if (cpus >= 0) {
			cmpCpu = cpus - hr.cpus;
			if (cmpCpu < 0) return -1;
		}

		long cmpMem = 0;
		if (mem >= 0) {
			cmpMem = mem - hr.mem;
			if (cmpMem < 0) return -1;
		}

		if ((cmpCpu > 0) || (cmpMem > 0)) return 1;
		return 0;
	}

	/**
	 * Consume resources (subtract resources)
	 */
	public void consume(HostResources hr) {
		if (hr == null) return;
		if ((cpus >= 0) && (hr.cpus >= 0)) cpus = Math.max(0, cpus - hr.cpus);
		if ((mem >= 0) && (hr.mem >= 0)) mem = Math.max(0, mem - hr.mem);
	}

	public int getCpus() {
		return cpus;
	}

	public long getMem() {
		return mem;
	}

	@Override
	public String getNodeId() {
		return getClass().getSimpleName() + ":" + id;
	}

	public long getTimeout() {
		return timeout;
	}

	public long getWallTimeout() {
		return wallTimeout;
	}

	/**
	 * Does this resource have at least 'hr' resources?
	 */
	public boolean hasResources(HostResources hr) {
		return compareTo(hr) >= 0;
	}

	public boolean isValid() {
		if (cpus == 0) return false;
		if (mem == 0) return false;
		return true;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		cpus = (int) serializer.getNextFieldInt();
		mem = serializer.getNextFieldInt();
		timeout = serializer.getNextFieldInt();
		wallTimeout = serializer.getNextFieldInt();
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		return cpus + "\t" + mem + "\t" + timeout + "\t" + wallTimeout;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public void setMem(long mem) {
		this.mem = mem;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;

		// Wall-timeout cannot be shorter than timeout
		if (wallTimeout > 0 && timeout > wallTimeout) wallTimeout = timeout;
	}

	public void setWallTimeout(long walltimeout) {
		wallTimeout = walltimeout;
	}

	@Override
	public String toString() {
		return "cpus: " + cpus //
				+ "\tmem: " + Gpr.toStringMem(mem) //
				+ (timeout > 0 ? "\ttimeout: " + timeout : "") //
				+ (wallTimeout > 0 ? "\twall-timeout: " + wallTimeout : "") //
				;
	}

	public String toStringMultiline() {
		StringBuilder sb = new StringBuilder();
		if (cpus > 0) sb.append("cpus: " + cpus + ", ");
		if (mem > 0) sb.append((sb.length() > 0 ? ", " : "") + "mem: " + Gpr.toStringMem(mem));
		if (timeout > 0) sb.append((sb.length() > 0 ? ", " : "") + "timeout: " + Timer.toDDHHMMSS(timeout * 1000));
		if (wallTimeout > 0) sb.append((sb.length() > 0 ? ", " : "") + "walltimeout: " + Timer.toDDHHMMSS(wallTimeout * 1000));
		return sb.toString();
	}

}
