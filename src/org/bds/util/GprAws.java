package org.bds.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

/**
 * General purpose routines for AWS
 */
public class GprAws {

	/**
	 * Create an ec2 client
	 */
	public static Ec2Client ec2Client(String region) {
		if (region == null || region.isEmpty()) Ec2Client.builder().build();
		Region r = Region.of(region);
		return Ec2Client.builder().region(r).build();
	}

}
