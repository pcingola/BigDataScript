package org.bds.cluster.host;

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
public class HostResourcesInf extends HostResources {

	private static final long serialVersionUID = 477316324922523648L;

	public HostResourcesInf() {
		cpus = Integer.MAX_VALUE; // Max cpus
		mem = Long.MAX_VALUE; // Max memory
		timeout = 0; // No timeout
		wallTimeout = 0; // No timeout
	}

	@Override
	public int compareTo(Resources hr) {
		if (hr instanceof HostResourcesInf) return 0;
		return 1;
	}

	/**
	 * Consume resources (subtract resources)
	 * @param hr
	 */
	@Override
	public void consume(Resources hr) {
		// Nothing to do, resources a infinite so they don't get consumed
	}

	@Override
	public boolean hasResources(Resources hr) {
		return true; // Infinite resource, so it's always true
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
