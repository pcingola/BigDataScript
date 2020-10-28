package org.bds.cluster.host;

/**
 * Represents the resources in a host: Cpus, memory, etc.
 *
 * @author pcingola
 */
public class HostResources extends Resources {

	private static final long serialVersionUID = 645808590576931751L;

	public HostResources() {
		super();
	}

	public HostResources(Resources hr) {
		super(hr);
	}

}
