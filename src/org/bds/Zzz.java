package org.bds;

public class Zzz {

	public static boolean debug = true;
	public static final String USER_DIR = "user.dir";

	public static boolean verbose = true;

	public static void main(String[] args) throws Exception {
		System.out.println("Start");

		//		// Create client
		//		SqsClient sqsClient = SqsClient.builder().region(Region.US_WEST_2).build();
		//        
		//		// Get 
		//		GetQueueUrlResponse getQueueUrlResponse =
		//                sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
		//        String queueUrl = getQueueUrlResponse.queueUrl();
		//        System.out.println(queueUrl);
		//        
		//		// Create queue
		//		CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName(queueName).build();
		//		sqsClient.createQueue(createQueueRequest);
		//
		//		// Purge & delete queue
		//		sqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody("Hello world!").delaySeconds(10).build());
		//
		//		// Receive messages
		//		ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(5).build();
		//		List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
		//
		//		// Delete queue
		//		DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(queueUrl).build();
		//		sqsClient.deleteQueue(deleteQueueRequest);

		System.out.println("End");
	}

}
