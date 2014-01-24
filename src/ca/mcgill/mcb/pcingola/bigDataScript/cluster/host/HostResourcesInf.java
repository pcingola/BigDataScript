package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

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

	public HostResourcesInf() {
		cpus = Integer.MAX_VALUE; // Max cpus
		mem = Long.MAX_VALUE; // Max memory
		timeout = 0; // No timeout
	}

	@Override
	public int compareTo(HostResources hr) {
		if (hr instanceof HostResourcesInf) return 0;
		return 1;
	}

	/**
	 * Consume resources (subtract resources)
	 * @param hr
	 */
	public void consume(HostResourcesInf hr) {
		// Nothing to do, resources a infinte so they don't get consumed
	}

	/**
	 * Does this resource have at least 'hr' resources?
	 * @param hr
	 * @return
	 */
	public boolean hasResources(HostResourcesInf hr) {
		return true;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
