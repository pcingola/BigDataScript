package org.bds.executioner;

import java.io.Serializable;

import org.bds.util.Gpr;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;

/**
 * Monitor a task:
 *   - Read messages from the queue
 *   - Check if a task finished
 *
 * @author pcingola
 */
public class MonitorTaskAws extends MonitorTaskQueue implements Serializable {

	private static final long serialVersionUID = 85396803332085797L;
	private static final String OS_DELETE_QUEUE_COMMAND = "aws sqs rm";

	protected SqsClient sqsClient;
	protected String queueUrl;

	public MonitorTaskAws() {
		super();
	}

	/**
	 * Run once every SLEEP_TIME
	 */
	@Override
	public synchronized void check() {
		// Nothing to do?
		Gpr.debug("MonitorTaskQueue: CHECK !?");
	}

	/**
	 * Create queue
	 */
	@Override
	public void createQueue() {
		if (hasQueue()) return; // Queue already exists
		queueId = getQueueId();

		CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName(queueId).build();
		sqsClient.createQueue(createQueueRequest);
		GetQueueUrlResponse getQueueUrlResponse = getSqsClient().getQueueUrl(GetQueueUrlRequest.builder().queueName(queueId).build());
		queueUrl = getQueueUrlResponse.queueUrl();

		// TODO: Add to 'taskLogger' so that Go bds process can delete the queue if Java dies
		Gpr.debug("");
	}

	/**
	 * Delete queue
	 */
	@Override
	public void deleteQueue() {
		// TODO: Delete queue
		// TODO: Remove entry from 'taskLogger' (Go bds process doesn't need to delete the queue)
		Gpr.debug("UNIMPLEMENTED !!!");
	}

	public String getQueueUrl() {
		return queueUrl;
	}

	private SqsClient getSqsClient() {
		if (sqsClient == null) sqsClient = SqsClient.builder().build();
		return sqsClient;
	}

	@Override
	public boolean hasQueue() {
		return queueUrl != null;
	}

	@Override
	public String osDeleteQueueCommand() {
		return OS_DELETE_QUEUE_COMMAND;
	}

	@Override
	protected void updateFinished() {
		// TODO: !!!!!!!!!!
		Gpr.debug("UNIMPLEMENTED!!!!");

	}

}
