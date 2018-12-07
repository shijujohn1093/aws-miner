package com.cuckoo.miner.sqs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.ListQueuesRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.QueueNameExistsException;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Repository
public class SQSMessaginRepository {
	private static final String QUEUE_NAME = "testQueue" + new Date().getTime();
	
	

	public void createSQS(String queueName) {
		queueName = queueName == null ? QUEUE_NAME : queueName;
		SqsClient sqs = SqsClient.builder().build();

		try {
			CreateQueueRequest request = CreateQueueRequest.builder().queueName(queueName).build();
			CreateQueueResponse create_result = sqs.createQueue(request);
		} catch (QueueNameExistsException e) {
			throw e;

		}
	}

	public void sendMessageToSQS(String queueName, String message) {
		queueName = queueName == null ? QUEUE_NAME : queueName;
		if (message == null) {
			message = "hello world";
		}
		SqsClient sqs = SqsClient.builder().build();
		GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(queueName).build();
		String queueUrl = sqs.getQueueUrl(getQueueRequest).queueUrl();

		SendMessageRequest send_msg_request = SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message)
				.delaySeconds(5).build();
		sqs.sendMessage(send_msg_request);
	}

	public void sendMessagesToSQS(String queueName, Map<String, String> messages) {
		List<SendMessageBatchRequestEntry> list = new ArrayList<>();

		queueName = queueName == null ? QUEUE_NAME : queueName;
		if (messages == null) {
			
			list.add(SendMessageBatchRequestEntry.builder().messageBody("Hello from message 1").id("msg_1").build());
			list.add(SendMessageBatchRequestEntry.builder().messageBody("Hello from message 2").delaySeconds(10)
					.id("msg_2").build());
		} else {
			for (Entry<String, String> entry : messages.entrySet()) {
				list.add(SendMessageBatchRequestEntry.builder().messageBody(entry.getValue()).delaySeconds(10)
						.id(entry.getKey()).build());
			}
		}
		SqsClient sqs = SqsClient.builder().build();

		GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(queueName).build();
		String queueUrl = sqs.getQueueUrl(getQueueRequest).queueUrl();

		SendMessageBatchRequest send_batch_request = SendMessageBatchRequest.builder().queueUrl(queueUrl)
				.entries(list.toArray(new SendMessageBatchRequestEntry[list.size()])).build();
		sqs.sendMessageBatch(send_batch_request);
	}

	public List<Message> receiveRequest(String queueName) {
		queueName = queueName == null ? QUEUE_NAME : queueName;

		SqsClient sqs = SqsClient.builder().build();

		GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(queueName).build();
		String queueUrl = sqs.getQueueUrl(getQueueRequest).queueUrl();
		ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl).build();
		
		List<Message> messages = sqs.receiveMessage(receiveRequest).messages();

	
		return messages;
	}
	
	public void deleteMessage(String queueName, Message message) {
		queueName = queueName == null ? QUEUE_NAME : queueName;
		System.out.println("Deleting messages from queue");

		SqsClient sqs = SqsClient.builder().build();

		GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(queueName).build();
		String queueUrl = sqs.getQueueUrl(getQueueRequest).queueUrl();
		DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder().queueUrl(queueUrl)
				.receiptHandle(message.receiptHandle()).build();
		sqs.deleteMessage(deleteRequest);
		System.out.println("Messages deleted from queue");

		
	}

	public List<String> listAvailableQueues(String prefix) {
		SqsClient sqs = SqsClient.builder().build();
		ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix(prefix).build();
		ListQueuesResponse listQueuesResponse = sqs.listQueues(listQueuesRequest);
		List<String> sqsUrls = new ArrayList<>();
		for (String url : listQueuesResponse.queueUrls()) {
			System.out.println(url);
			sqsUrls.add(url);
		}
		return sqsUrls;
	}

	public String getQueueUrlResponse(String queueName) {

		queueName = queueName == null ? QUEUE_NAME : queueName;

		SqsClient sqs = SqsClient.builder().build();

		GetQueueUrlResponse getQueueUrlResponse = sqs
				.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
		String queueUrl = getQueueUrlResponse.queueUrl();
		System.out.println("====>"+queueUrl);
		return queueUrl;
	}
	
	public void deleteQueue(String queueName) {
		queueName = queueName == null ? QUEUE_NAME : queueName;

		SqsClient sqs = SqsClient.builder().build();
		GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(queueName).build();

		String queueUrl = sqs.getQueueUrl(getQueueRequest).queueUrl();

		DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(queueUrl).build();
		sqs.deleteQueue(deleteQueueRequest);
	}
	
	public List<String>  deleteQueueWithPrefix(String queuePrefix) {



		SqsClient sqs = SqsClient.builder().build();
		ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix(queuePrefix).build();
		ListQueuesResponse listQueuesResponse = sqs.listQueues(listQueuesRequest);
		List<String> sqsUrls = new ArrayList<>();
		for (String url : listQueuesResponse.queueUrls()) {
			System.out.println("Deleting url -- > "+url);
			DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(url).build();
			sqs.deleteQueue(deleteQueueRequest);

		}
		return sqsUrls;
	}
	
	


}