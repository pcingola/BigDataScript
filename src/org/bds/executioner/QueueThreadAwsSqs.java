package org.bds.executioner;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bds.Config;
import org.bds.scope.GlobalScope;

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

	public static String QUEUE_NAME_SUFFIX_DEBUG = "test_123456789";
	public static boolean USE_QUEUE_NAME_DEBUG = false; // Fix queue name when running tests or debugging

	private static final String OS_DELETE_QUEUE_COMMAND = "@aws_sqs_delete_queue"; // Command line to delete a queue
	public static final int AWS_SQS_WAIT_TIME_SECONDS = 10; // Long polling parameter

	private SqsClient sqsClient;
	protected String queueUrl;
	protected String queueName;
	protected String queueNamePrefix;

	public QueueThreadAwsSqs(Config config, MonitorTaskQueue monitorTasks, TaskLogger taskLogger, String queueNamePrefix) {
		super(config, monitorTasks, taskLogger);
		this.queueNamePrefix = queueNamePrefix;
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
			log("Creating AWS SQS queue '" + queueName + "'");
			getSqsClient().createQueue(createQueueRequest);
		} catch (Exception e) {
			error("Error creating SQS queue '" + queueName + "', exception message: " + e.getMessage());
			error = e;
			return false;
		}

		GetQueueUrlResponse getQueueUrlResponse = getSqsClient().getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
		queueUrl = getQueueUrlResponse.queueUrl();
		debug("Created AWS SQS queue '" + queueName + "', url: '" + queueUrl + "'");

		return hasQueue(); // Success if the queue was created
	}

	/**
	 * Delete queue
	 */
	@Override
	public boolean deleteQueue() {
		if (USE_QUEUE_NAME_DEBUG) {
			// This is used for developing / debugging
			warning("USE_QUEUE_NAME_DEBUG is set, queue will not be deleted");
			return false;
		}

		if (!hasQueue()) return false; // Nothing to delete

		try {
			log("Deleting AWS SQS queue '" + queueName + "', url: '" + queueUrl + "'");
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
				queueName = queueNamePrefix + QUEUE_NAME_SUFFIX_DEBUG;
				warning("QUEUE_NAME_SUFFIX_DEBUG is set, using queue name '" + queueName + "', prefix variable " + GlobalScope.GLOBAL_VAR_EXECUTIONER_QUEUE_NAME_PREFIX + " set to '" + queueNamePrefix + "'");
			} else {
				String r = Long.toHexString(Math.abs((new Random()).nextLong())); // Long random number in hex
				queueName = String.format("%s%2$tY%2$tm%2$td_%2$tH%2$tM%2$tS_%3$s", queueNamePrefix, Calendar.getInstance(), r);
				debug("Queue name '" + queueName + "', prefix variable " + GlobalScope.GLOBAL_VAR_EXECUTIONER_QUEUE_NAME_PREFIX + " set to '" + queueNamePrefix + "'");
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
		debug("Process SQS message: " + message);
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
