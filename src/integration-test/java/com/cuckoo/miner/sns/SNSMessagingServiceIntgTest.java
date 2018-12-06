package com.cuckoo.miner.sns;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SNSMessagingServiceIntgTest {
	private SNSMessagingRepository snsMessagingService;

	private final String prefix = "TEST_";

	List<String> topicsToDelete = new ArrayList<>();

	private String topicName;

	@Before
	public void init() {
		topicName = prefix + "SNSSampleTopic" + System.currentTimeMillis();
		snsMessagingService = new SNSMessagingRepository();
		topicsToDelete.clear();
	}

	@Test
	public void shouldCreateSNSOnAWS() {
		topicName = prefix + "SNSSampleTopic" + System.currentTimeMillis();
		String arn = snsMessagingService.createTopic(topicName);
		topicsToDelete.add(arn);
		System.out.println();
	}

	
	@Test
	public void shouldSubscribeToTopic() {
		String message = "Helllo Worldddddd";
		String topicArn = "arn:aws:sns:ap-south-1:358696438331:MyTestTopic";
		snsMessagingService.pusblishToSNS(topicArn, message);
		System.out.println();
	}
	@Test
	public void shouldPublishToTopic() {
		String message = "Helllo Worldddddd";
		String topicArn = "arn:aws:sns:ap-south-1:358696438331:MyTestTopic";
		snsMessagingService.pusblishToSNS(topicArn, message);
		System.out.println();
	}

	@After
	public void cleanup() {

		for (String arn : topicsToDelete) {
			snsMessagingService.deleteTopic(arn);
		}

	}
}
