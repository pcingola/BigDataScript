package org.bds.cluster.host;

import java.util.HashMap;
import java.util.Map;

import org.bds.Config;
import org.bds.lang.expression.ExpressionTask;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;
import org.bds.util.Timer;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.IamInstanceProfileSpecification;
import software.amazon.awssdk.services.ec2.model.InstanceNetworkInterfaceSpecification;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.ShutdownBehavior;

/**
 * Represents resources consumed by a task running on AWS
 *
 * @author pcingola
 */
public class TaskResourcesAws extends TaskResources {

	private static final long serialVersionUID = 20199650690952235L;

	public static final String[] KEY_NAME = { "key-name", "keyName" };

	public static final String[] IMAGE_ID = { "image-id", "imageId" };
	public static final String[] INSTANCE_TYPE = { "instance-type", "instanceType" };
	public static final String[] IP_ADDR = { "associate-public-ip-address", "public-ip-address", "public-ip", "associatePublicIpAddress", "publicIpAddress", "publicIp" };
	public static final String[] PROFILE = { "iam-instance-profile", "instance-profile", "instanceProfile", "profile" };
	public static final String[] SECURITY_GROUP_IDS = { "security-group-ids", "securityGroupIds", "security-group-id", "securityGroupId" };
	public static final String[] SHUTDOWN_BEHAVIOUR = { "shutdown-behavior", "shutdownBehavior" };
	public static final String[] KEEP_INSTANCE_ALIVE_AFTER_FINISH = { "keep-instance-alive-after-finish", "keepInstanceAliveAfterFinish" };
	public static final String[] SUBNET_ID = { "subnet-id", "subnetId" };
	public static final String[] TAGS = { "tag-specifications", "tagSpecifications", "tags" };
	public static final String[] REGION = { "region" };
	public static final String[] S3_TMP = { "s3tmp", "s3_tmp", "s3-tmp" };
	protected final String[] LIST_SEPARATORS = { "\t", ",", ";" };

	protected String awsRole;
	protected String blockDevMap;
	protected String keyName;
	protected String imageId;
	protected String instanceType;
	protected String region;
	protected String securityGroupIds;
	protected String shutdownBehavior;
	protected String s3tmp;
	protected String subnetId;
	protected Map<String, String> tags;
	protected boolean keepInstanceAliveAfterFinish; // Do NOT shutdown the instance after bds finishes execution (this can be set to 'true' for debugging the instances)
	protected boolean usePublicIpAddr;

	public TaskResourcesAws() {
		super();
		keepInstanceAliveAfterFinish = false;
		shutdownBehavior = ShutdownBehavior.TERMINATE.toString();
	}

	public TaskResourcesAws(TaskResourcesAws hr) {
		super(hr);
	}

	/**
	 * Create an ec2 client
	 */
	public Ec2Client ec2Client() {
		if (region == null || region.isEmpty()) missingValue("region", REGION);
		Region r = Region.of(region);
		return Ec2Client.builder().region(r).build();
	}

	/**
	 * Create an instance request
	 */
	public RunInstancesRequest.Builder ec2InstanceRequest() {
		RunInstancesRequest.Builder requestBuilder = RunInstancesRequest.builder();

		// We only want one instance
		requestBuilder.maxCount(1).minCount(1);

		// AWS security role
		if (awsRole != null && !awsRole.isEmpty()) {
			IamInstanceProfileSpecification role = IamInstanceProfileSpecification.builder().name(awsRole).build();
			requestBuilder.iamInstanceProfile(role);
		}

		// Image ID
		if (imageId != null && !imageId.isEmpty()) requestBuilder.imageId(imageId);
		else if (Config.get().isVerbose()) missingValue("imageId", IMAGE_ID);

		// Key name
		if (keyName != null && !keyName.isEmpty()) requestBuilder.kernelId(keyName);

		// Instance type
		requestBuilder.instanceType(parseInstanceType(instanceType));

		// Shutdown behavior
		requestBuilder.instanceInitiatedShutdownBehavior(parseShutdownBehavior(shutdownBehavior));

		if (blockDevMap != null && !blockDevMap.isEmpty()) {
			// FIXME: Block device mappings
			throw new RuntimeException("UNIMPLEMENTED!!!");
		}

		// Add network interface
		requestBuilder.networkInterfaces(networkInterface());

		return requestBuilder;
	}

	public String getS3tmp() {
		return s3tmp;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public boolean isKeepInstanceAliveAfterFinish() {
		return keepInstanceAliveAfterFinish;
	}

	/**
	 * Get a value (as a string) from a ValueMap
	 */
	protected String mapGet(ValueMap map, String key) {
		ValueString k = new ValueString(key);
		if (!map.hasKey(k)) return null;
		return map.getValue(k).asString();
	}

	/**
	 * Get any value (as a string) from a ValueMap.
	 * Several keys are provided in an array, the first found will be returned
	 */
	protected String mapGet(ValueMap map, String[] keys) {
		for (String k : keys) {
			String ret = mapGet(map, k);
			if (ret != null) return ret;
		}
		return null; // Not found
	}

	/**
	 * Missing value error message
	 */
	public void missingValue(String name, String[] keys) {
		StringBuilder entries = new StringBuilder();
		for (String k : keys)
			entries.append("'" + k + "' ");
		throw new RuntimeException("ERROR: Missing parameter '" + name + "'  when attempting to run AWS task. Use map '" + ExpressionTask.TASK_OPTION_RESOURCES + "' entries" + (keys.length > 1 ? " (any of) " : "") + ": " + entries);
	}

	/**
	 * Create a network interface
	 */
	InstanceNetworkInterfaceSpecification networkInterface() {
		InstanceNetworkInterfaceSpecification.Builder nifb = InstanceNetworkInterfaceSpecification.builder();

		// Network interface number 0 is the only one that can specify "associatePublicIpAddress"
		nifb.deviceIndex(0);
		nifb.deleteOnTermination(true);

		// Public IP?
		nifb.associatePublicIpAddress(usePublicIpAddr);

		// Security group IDs
		String[] groupIds = parseList(securityGroupIds);
		if (groupIds != null) nifb.groups(groupIds);

		// Subnet
		if (subnetId != null && !subnetId.isEmpty()) nifb.subnetId(subnetId);;

		return nifb.build();
	}

	/**
	 * Parse instance type
	 */
	String parseInstanceType(String instanceType) {
		if (instanceType == null || instanceType.isEmpty()) missingValue("instanceType", INSTANCE_TYPE);
		// TODO: If not explicitly provided, it should pick one based on CPU and MEM
		return instanceType;
	}

	/**
	 * Parse a list of items separated by tab, comma or semicolon
	 * @return An array of items or null if nothing found
	 */
	String[] parseList(String str) {
		if (str == null || str.isEmpty()) return null;

		// Is this a list of items
		for (String sep : LIST_SEPARATORS) {
			if (str.indexOf(sep) > 0) return str.split(sep);
		}

		// Only one item in the list
		String[] items = new String[1];
		items[0] = str;
		return items;
	}

	/**
	 * Parse a 'shutdownBehavior'.
	 * Use ShutdownBehavior.TERMINATE on any error or invalid
	 */
	ShutdownBehavior parseShutdownBehavior(String shutdownBehavior) {
		if (shutdownBehavior == null || shutdownBehavior.isEmpty()) return ShutdownBehavior.TERMINATE;
		try {
			return ShutdownBehavior.valueOf(shutdownBehavior.toUpperCase());
		} catch (Exception e) {
			throw new RuntimeException("Invalid shutdown behaviour '" + shutdownBehavior + "', valid options are: " + ShutdownBehavior.knownValues());
		}
	}

	/**
	 * Parse tags string
	 * Format: One entry per line, tab-separated: "key \t value"
	 * @param tagsStr
	 * @return A map of key values for each tag
	 */
	protected Map<String, String> parseTags(String tagsStr) {
		if (tagsStr == null) return null;
		Map<String, String> tags = new HashMap<>();
		for (String line : tagsStr.split("\n")) {
			String[] f = line.split("\t", 2);
			tags.put(f[0], f[1]);
		}
		return tags;
	}

	/**
	 * Set resources from bdsThread
	 * @param bdsThread
	 */
	@Override
	public void setFromBdsThread(BdsThread bdsThread) {
		super.setFromBdsThread(bdsThread);

		// Try to find a 'resources' value
		Value taskResources = bdsThread.getValue(ExpressionTask.TASK_OPTION_RESOURCES);
		if (taskResources == null) {
			// Not found? Nothing else to set
			throw new RuntimeException("Cannot find '" + ExpressionTask.TASK_OPTION_RESOURCES + "' map (or object) for AWS resources");
		}

		// If the value a hash?
		if (taskResources.getType().isMap()) setFromBdsThreadMap((ValueMap) taskResources, bdsThread.isDebug());
		else if (taskResources.getType().isClass()) setFromBdsThreadClassResources(taskResources, bdsThread.isDebug());
	}

	/**
	 * Set parameters from an 'AwsResources' class
	 */
	protected void setFromBdsThreadClassResources(Value taskResources, boolean debug) {
		if (debug) Timer.showStdErr(this.getClass().getName() + " : Setting resources from object '" + ExpressionTask.TASK_OPTION_RESOURCES + "': " + taskResources);
		// TODO: Check that this class is 'AwsResources'
		throw new RuntimeException("Cannot parse 'taskResources' type " + taskResources.getType());
	}

	/**
	 * Set parameters from a 'string{string}' dictionary
	 */
	protected void setFromBdsThreadMap(ValueMap taskResources, boolean debug) {
		if (debug) Timer.showStdErr(this.getClass().getName() + " : Setting resources from map '" + ExpressionTask.TASK_OPTION_RESOURCES + "': " + taskResources);
		awsRole = mapGet(taskResources, PROFILE);
		instanceType = mapGet(taskResources, INSTANCE_TYPE);
		imageId = mapGet(taskResources, IMAGE_ID);
		keyName = mapGet(taskResources, KEY_NAME);
		securityGroupIds = mapGet(taskResources, SECURITY_GROUP_IDS);
		subnetId = mapGet(taskResources, SUBNET_ID);
		tags = parseTags(mapGet(taskResources, TAGS));
		usePublicIpAddr = Gpr.parseBoolSafe(mapGet(taskResources, IP_ADDR));
		region = mapGet(taskResources, REGION);
		shutdownBehavior = mapGet(taskResources, SHUTDOWN_BEHAVIOUR);
		keepInstanceAliveAfterFinish = Gpr.parseBoolSafe(mapGet(taskResources, KEEP_INSTANCE_ALIVE_AFTER_FINISH));
		s3tmp = mapGet(taskResources, S3_TMP);
	}

}
