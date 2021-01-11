package org.bds.data;

import java.util.HashMap;
import java.util.Map;

import org.bds.BdsLog;
import org.bds.Config;
import org.bds.util.GprAws;
import org.bds.util.Timer;

import software.amazon.awssdk.services.s3.S3Client;

/**
 * Create and keep alive S3 clients by thread
 *
 * Each region must have a different S3client
 *
 */
public class AwsS3ClientProvider implements BdsLog {

	public static final int MAX_S3_CLIENT_TIME = 60 * 60 * 1000; // We expire s3 clients after using them for a while

	private static final Map<Long, AwsS3ClientProvider> awsS3ClientProvider = new HashMap<>();

	boolean debug;
	private Map<String, S3Client> s3clientByRegion;
	private Map<String, Timer> timerByRegion;

	/**
	 * Get an S3Client for 'region'
	 */
	public static S3Client get(String region) {
		return getS3ClientProvider().getS3Client(region);
	}

	/**
	 * Get S3 client provider for this thread
	 */
	public static AwsS3ClientProvider getS3ClientProvider() {
		Long thId = Thread.currentThread().getId();
		AwsS3ClientProvider acp = awsS3ClientProvider.get(thId);
		if (acp == null) {
			acp = new AwsS3ClientProvider();
			acp.debug = Config.get().isDebug();
			awsS3ClientProvider.put(thId, acp);
		}
		return acp;
	}

	private AwsS3ClientProvider() {
		s3clientByRegion = new HashMap<>();
		timerByRegion = new HashMap<>();
	}

	/**
	 * Get s3 client for 'region'
	 * Make sure s3 clients are renewed after MAX_S3_CLIENT_TIME
	 * @return An S3Client for a region
	 */
	public S3Client getS3Client(String region) {
		if (region == null) region = ""; // Default region
		S3Client s3c = s3clientByRegion.get(region);

		// Do we need to create a client? If so, show debug messages
		boolean create = false;
		if (s3c == null) {
			debug("Creating new S3Client for region '" + region + "'");
			create = true;
		} else if (isExpired(region)) {
			debug("Creating new S3Client for region '" + region + "', expired after " + timerByRegion.get(region).elapsedSecs() + " secs.");
			create = true;
		}

		if (create) {
			// Create new client
			s3c = GprAws.s3Client(region);
			s3clientByRegion.put(region, s3c);

			// Refresh timer
			Timer timer = new Timer();
			timerByRegion.put(region, timer);
		}

		return s3c;
	}

	boolean isExpired(String region) {
		Timer timer = timerByRegion.get(region);
		return timer.elapsed() > MAX_S3_CLIENT_TIME;
	}

}
