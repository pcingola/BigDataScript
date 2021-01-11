package org.bds.cluster.host;

import java.lang.management.ManagementFactory;

import org.bds.cluster.ComputerSystem;
import org.bds.util.Gpr;

import com.sun.management.OperatingSystemMXBean;

/**
 * Local host information
 *
 * @author pcingola@mcgill.ca
 */
public class HostLocal extends Host {

	public HostLocal(ComputerSystem system) {
		super(system, "localhost");

		resources.setCpus(Gpr.NUM_CORES);
		resources.setMem(mem());
	}

	public long mem() {
		OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		return bean.getTotalPhysicalMemorySize();
	}

}
