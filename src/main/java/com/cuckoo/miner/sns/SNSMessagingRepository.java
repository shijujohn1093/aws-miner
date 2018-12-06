package com.cuckoo.miner.sns;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.DeleteTopicRequest;
import software.amazon.awssdk.services.sns.model.GetTopicAttributesRequest;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sqs.model.Message;

@Repository
public class SNSMessagingRepository {
	private static final String TOPIC_NAME = "testTopic" + new Date().getTime();

	public String createTopic(String topicName) {
		topicName = topicName == null ? TOPIC_NAME : topicName;
		SnsClient snsClient = SnsClient.builder().build();
		CreateTopicRequest request = CreateTopicRequest.builder().name(topicName).build();
		CreateTopicResponse createTopicResult = snsClient.createTopic(request);	
		return createTopicResult.topicArn();
		
	}
	
	
	public void deleteTopic(String topicArn) {
		SnsClient snsClient = SnsClient.builder().build();
		DeleteTopicRequest deleteTopicRequest = DeleteTopicRequest.builder().topicArn(topicArn).build();
		snsClient.deleteTopic(deleteTopicRequest);
	}
	
	
	//----------  delete
	public void subscribeToTopic(String topicArn, String email) {
		
//		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "email", email);
//		snsClient.subscribe(subRequest);
	}
	
	public void pusblishToSNS(String topicArn, String message) {
		//publish to an SNS topic
		SnsClient snsClient = SnsClient.builder().build();
		PublishRequest publishRequest  = PublishRequest.builder().topicArn(topicArn).message(message).build();
		PublishResponse publish = snsClient.publish(publishRequest);
	}

	public void sendMessageToSQS(String topicName, String message) {
		topicName = topicName == null ? TOPIC_NAME : topicName;
		if (message == null) {
			message = "hello world";
		}
		SnsClient snsClient = SnsClient.builder().build();

		GetTopicAttributesRequest getTopicAttributesRequest = GetTopicAttributesRequest.builder().topicArn("").build();

	}

	public void sendMessagesToSQS(String queueName, Map<String, String> messages) {

	}

	public List<Message> receiveRequest(String queueName) {

		return null;
	}

	public List<String> listAvailableQueues(String prefix) {
		return null;
	}

	public String getQueueUrlResponse(String queueName) {

		return null;
	}

	public void deleteQueue(String queueName) {}

	public List<String> deleteQueueWithPrefix(String queuePrefix) {

		return null;
	}

}