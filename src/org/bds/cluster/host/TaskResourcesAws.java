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

import software.amazon.awssdk.services.ec2.model.IamInstanceProfileSpecification;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.ShutdownBehavior;

/**
 * Represents resources consumed by a task running on AWS
 *
 * @author pcingola
 */
public class TaskResourcesAws extends TaskResources {

	private static final long serialVersionUID = 20199650690952235L;

	protected final String[] KEY_NAME = { "key-name", "keyName" };
	protected final String[] IMAGE_ID = { "image-id", "imageId" };
	protected final String[] INSTANCE_TYPE = { "instance-type", "instanceType" };
	protected final String[] IP_ADDR = { "associate-public-ip-address", "public-ip-address", "public-ip", "associatePublicIpAddress", "publicIpAddress", "publicIp" };
	protected final String[] PROFILE = { "iam-instance-profile", "instance-profile", "instanceProfile", "profile" };
	protected final String[] SECURITY_GROUP_IDS = { "security-group-ids", "securityGroupIds", "security-group-id", "securityGroupId" };
	protected final String[] SUBNET_ID = { "subnet-id", "subnetId" };
	protected final String[] TAGS = { "tag-specifications", "tagSpecifications", "tags" };

	String awsRole;
	String instanceType;
	String imageId;
	String keyName;
	String shutdownBehavior;
	String securityGroupIds;
	String subnetId;
	String blockDevMap;
	Map<String, String> tags;
	boolean usePublicIpAddr;

	public TaskResourcesAws() {
		super();
		shutdownBehavior = "terminate";
	}

	public TaskResourcesAws(TaskResourcesAws hr) {
		super(hr);
	}

	/**
	 * Create an instance request
	 */
	public RunInstancesRequest ec2InstanceRequest() {
		RunInstancesRequest.Builder requestBuilder = RunInstancesRequest.builder();

		requestBuilder.maxCount(1).minCount(1);

		if (awsRole != null && !awsRole.isEmpty()) {
			IamInstanceProfileSpecification role = IamInstanceProfileSpecification.builder().name(awsRole).build();
			requestBuilder.iamInstanceProfile(role);
		}

		if (imageId != null && !imageId.isEmpty()) requestBuilder.instanceType(instanceType);
		else if (Config.get().isVerbose()) Timer.showStdErr("WARNINING: Instance type undefined when attempting to run AWS task.");

		if (imageId != null && !imageId.isEmpty()) requestBuilder.imageId(imageId);
		else if (Config.get().isVerbose()) Timer.showStdErr("WARNINING: ImageId undefined when attempting to run AWS task.");

		if (keyName != null && !keyName.isEmpty()) requestBuilder.kernelId(keyName);

		if (shutdownBehavior != null && !shutdownBehavior.isEmpty()) requestBuilder.instanceInitiatedShutdownBehavior(ShutdownBehavior.valueOf(shutdownBehavior));
		else requestBuilder.instanceInitiatedShutdownBehavior(ShutdownBehavior.TERMINATE);

		if (securityGroupIds != null && !securityGroupIds.isEmpty()) {
			String[] groupIds;
			if (securityGroupIds.indexOf('\t') > 0) {
				// Tab-separated list
				groupIds = securityGroupIds.split("\t");
			} else if (securityGroupIds.indexOf(',') > 0) {
				// Comma-separated list
				groupIds = securityGroupIds.split(",");
			} else {
				// Only one item in the list
				groupIds = new String[1];
				groupIds[0] = securityGroupIds;
			}
			requestBuilder.securityGroups(groupIds);
		}

		if (blockDevMap != null && !blockDevMap.isEmpty()) {
			// TODO: Block device mappings
			Gpr.debug("UNIMPLEMENTED!!!");
		}

		return requestBuilder.build();
	}

	public Map<String, String> getTags() {
		return tags;
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
		if (taskResources == null) return; // Not found? Nothing else to set

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
		// TODO: Parse bds data structure
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
	}

}
