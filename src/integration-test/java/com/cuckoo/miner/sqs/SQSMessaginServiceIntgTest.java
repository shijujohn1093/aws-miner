package com.cuckoo.miner.sqs;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import software.amazon.awssdk.services.sqs.model.Message;

@Ignore
public class SQSMessaginServiceIntgTest {

	private SQSMessaginRepository sqsMessaginService;

	private final String queuePrefix = "TEST_";

	private String queueName;

	@Before
	public void init() {
		queueName = queuePrefix + "SQSMessaginSampleQueue"+System.currentTimeMillis();
		
		sqsMessaginService = new SQSMessaginRepository();
	}

	@Test
	public void shouldCreateSQSOnAWS() {
		sqsMessaginService.createSQS(queueName);
		Assert.assertTrue(sqsMessaginService.listAvailableQueues(queuePrefix).size() == 2);
		Assert.assertTrue(sqsMessaginService.getQueueUrlResponse(queueName) != null);
	}
	
	

	@Test
	public void shouldWriteMessagesQSOnAWS() {
		sqsMessaginService.createSQS(queueName);
		sqsMessaginService.sendMessagesToSQS(queueName, null);
		List<Message> receiveRequest = sqsMessaginService.receiveRequest(queueName);
		for(Message message : receiveRequest) {
			// all the code goes here
			System.out.println(message.messageId() );
			System.out.println(message.body() );

			sqsMessaginService.deleteMessage(queueName, message);

		}
		Assert.assertTrue(receiveRequest.size() == 2);

	}

	@After
	public void cleanup() {
		sqsMessaginService.deleteQueueWithPrefix(queuePrefix);

	}

}
