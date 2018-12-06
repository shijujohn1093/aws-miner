package com.cuckoo.miner;

import com.cuckoo.miner.dynamo.DynamoRepository;
import com.cuckoo.miner.sns.SNSMessagingRepository;
import com.cuckoo.miner.sqs.SQSMessaginRepository;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.model.Message;

public class S3FileUploaderService implements Runnable {
	

	private final SQSMessaginRepository sqsMessaginRepository;
	private final SNSMessagingRepository snsMessagingRepository;
	private final DynamoRepository dynamoRepository;
	private final String sourceQueueName;
	private final String targetTopicARN;
	private final String sourceBucketName;
	private final String targetDynamoTable;
	private final Message message;
	private final String s3FileName;

	public S3FileUploaderService(SQSMessaginRepository sqsMessaginRepository,
			SNSMessagingRepository snsMessagingRepository, DynamoRepository dynamoRepository, String queueName,
			String topicArn, String bucketName, String tableToWrite, Message message, String s3FileName) {
		this.sqsMessaginRepository = sqsMessaginRepository;
		this.dynamoRepository = dynamoRepository;
		this.snsMessagingRepository = snsMessagingRepository;
		this.sourceQueueName = queueName;
		this.targetTopicARN = topicArn;
		this.sourceBucketName = bucketName;
		this.targetDynamoTable = tableToWrite;
		this.message = message;
		this.s3FileName = s3FileName;
	}

	@Override
	public void run() {						
		dynamoRepository.putItemReadingFromS3(targetDynamoTable, sourceBucketName, Region.AP_SOUTH_1, s3FileName);
		snsMessagingRepository.pusblishToSNS(targetTopicARN, message.body());
		sqsMessaginRepository.deleteMessage(sourceQueueName, message);
		System.out.println("Thread completed " + Thread.currentThread().getName());
	}

}
