package org.bds.executioner;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bds.Config;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.QueueNameExistsException;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

/**
 * A thread that handles queue messages
 *
 * @author pcingola
 */
public class QueueThreadAwsSqs extends QueueThread {

	public static final String QUEUE_NAME_DEBUG = "bds_test_123456789";
	public static boolean USE_QUEUE_NAME_DEBUG = true; // Fix queue name when running tests or debugging

	private static final String OS_DELETE_QUEUE_COMMAND = "@aws_sqs_delete-queue"; // Command line to delete a queue
	public static final int AWS_SQS_WAIT_TIME_SECONDS = 10; // Long polling parameter

	private SqsClient sqsClient;
	protected String queueUrl;
	protected String queueName;

	public QueueThreadAwsSqs(Config config, MonitorTaskQueue monitorTasks, TaskLogger taskLogger) {
		super(config, monitorTasks, taskLogger);
	}

	public QueueThreadAwsSqs(Config config, MonitorTaskQueue monitorTasks, TaskLogger taskLogger, String queueName) {
		this(config, monitorTasks, taskLogger);
		this.queueName = queueName;
	}

	/**
	 * Create queue
	 */
	@Override
	public boolean createQueue() {
		if (hasQueue()) return false; // Queue already exists

		try {
			queueName = getQueueName();
			CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName(queueName).build();
			if (verbose) log("Creating AWS SQS queue '" + queueName + "'");
			getSqsClient().createQueue(createQueueRequest);
		} catch (Exception e) {
			error = e;
			return false;
		}

		GetQueueUrlResponse getQueueUrlResponse = getSqsClient().getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
		queueUrl = getQueueUrlResponse.queueUrl();
		if (debug) log("Created AWS SQS queue '" + queueName + "', url: '" + queueUrl + "'");

		return hasQueue(); // Success if the queue was created
	}

	/**
	 * Delete queue
	 */
	@Override
	public boolean deleteQueue() {
		if (!hasQueue()) return false; // Nothing to delete

		try {
			if (verbose) log("Deleting AWS SQS queue '" + queueName + "', url: '" + queueUrl + "'");
			DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(queueUrl).build();
			queueUrl = null;
			getSqsClient().deleteQueue(deleteQueueRequest);
		} catch (QueueNameExistsException e) {
			if (verbose || debug) e.printStackTrace();
		}
		return !hasQueue(); // Success if the queue was deleted
	}

	@Override
	public String getQueueId() {
		return queueUrl;
	}

	/**
	 * Create a unique name according to queue name restrictions
	 */
	public String getQueueName() {
		if (queueName == null) {
			if (USE_QUEUE_NAME_DEBUG) {
				queueName = QUEUE_NAME_DEBUG;
			} else {
				String r = Long.toHexString(Math.abs((new Random()).nextLong())); // Long random number in hex
				queueName = String.format("bds_%1$tY%1$tm%1$td_%1$tH%1$tM%1$tS_%2$s", Calendar.getInstance(), r);
			}
		}
		return queueName;
	}

	public String getQueueUrl() {
		return queueUrl;
	}

	private SqsClient getSqsClient() {
		if (sqsClient == null) sqsClient = SqsClient.builder().build();
		return sqsClient;
	}

	@Override
	protected boolean hasQueue() {
		return queueUrl != null;
	}

	@Override
	public String osDeleteQueueCommand() {
		return OS_DELETE_QUEUE_COMMAND;
	}

	/**
	 * Process a single message
	 */
	private void process(Message message) {
		if (debug) log("Process SQS message: " + message);
		processMessage(message.body());
	}

	/**
	 * Process all messages
	 * @param messages
	 */
	private void processMessages(List<Message> messages) {
		for (Message message : messages) {
			process(message);
			DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build();
			sqsClient.deleteMessage(deleteMessageRequest);
		}
	}

	/**
	 * Receive messages from the queue
	 * Note: We use a "long polling method" to perform less API calls
	 * References:
	 * 	- https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-short-and-long-polling.html#sqs-long-polling
	 * 	- https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/working-with-messages.html#setting-up-long-polling
	 */
	private List<Message> receiveMessages() {
		ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl).waitTimeSeconds(AWS_SQS_WAIT_TIME_SECONDS).build();
		List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
		return messages;
	}

	@Override
	protected void runLoop() {
		List<Message> messages = receiveMessages();
		processMessages(messages);
	}

}
