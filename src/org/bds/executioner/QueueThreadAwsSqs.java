package org.bds.executioner;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bds.Config;
import org.bds.util.Gpr;

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

	private static final String OS_DELETE_QUEUE_COMMAND = "aws sqs delete-queue --queue-url"; // Command line to delete a queue
	public static final int AWS_SQS_WAIT_TIME_SECONDS = 10; // Long polling parameter

	private SqsClient sqsClient;
	protected String queueUrl;
	protected String queueName;

	public QueueThreadAwsSqs(Config config, MonitorTaskQueue monitorTasks, TaskLogger taskLogger) {
		super(config, monitorTasks, taskLogger);
	}

	/**
	 * Create queue
	 */
	@Override
	public boolean createQueue() {
		if (hasQueue()) return false; // Queue already exists

		queueName = getQueueName();
		CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName(queueName).build();
		getSqsClient().createQueue(createQueueRequest);

		GetQueueUrlResponse getQueueUrlResponse = getSqsClient().getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
		queueUrl = getQueueUrlResponse.queueUrl();
		log("Created queue '" + queueName + "', url: '" + queueUrl + "'");

		return hasQueue(); // Success if the queue was created
	}

	/**
	 * Delete queue
	 */
	@Override
	public boolean deleteQueue() {
		if (!hasQueue()) return false; // Nothing to delete

		try {
			log("Deleting queue '" + queueName + "', url: '" + queueUrl + "'");
			DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(queueUrl).build();
			getSqsClient().deleteQueue(deleteQueueRequest);
			queueUrl = null;
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
	private String getQueueName() {
		if (queueName == null) {
			String r = Long.toHexString(Math.abs((new Random()).nextLong())); // Long random number in hex
			queueName = String.format("bds_%1$tY%1$tm%1$td_%1$tH%1$tM%1$tS_%1$tL_%2s", Calendar.getInstance(), r);
			Gpr.debug("QUEUE NAME: " + queueName);
		}
		return queueName;
	}

	private SqsClient getSqsClient() {
		if (sqsClient == null) sqsClient = SqsClient.builder().build();
		return sqsClient;
	}

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
	private void processMessage(Message message) {
		Gpr.debug("RPOCESS MESSAGE: " + message);
	}

	/**
	 * Process all messages
	 * @param messages
	 */
	private void processMessages(List<Message> messages) {
		for (Message message : messages) {
			processMessage(message);
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
