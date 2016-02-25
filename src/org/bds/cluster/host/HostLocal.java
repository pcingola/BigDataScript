package org.bds.cluster.host;

import java.lang.management.ManagementFactory;

import org.bds.cluster.Cluster;
import org.bds.util.Gpr;

import com.sun.management.OperatingSystemMXBean;

/**
 * Local host information
 *
 * @author pcingola@mcgill.ca
 */
public class HostLocal extends Host {

	public HostLocal(Cluster cluster) {
		super(cluster, "localhost");

		resources.setCpus(Gpr.NUM_CORES);
		resources.setMem(mem());
	}

	public long mem() {
		OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		return bean.getTotalPhysicalMemorySize();
	}

}
