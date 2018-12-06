package com.cuckoo.miner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cuckoo.miner.dynamo.DynamoRepository;
import com.cuckoo.miner.sns.SNSMessagingRepository;
import com.cuckoo.miner.sqs.SQSMessaginRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import software.amazon.awssdk.services.sqs.model.Message;

@Service
public class SQSNotificaitonService {
	private final Environment environment;

	public SQSNotificaitonService(Environment environment) {
		this.environment = environment;
	}

	public void process() {
		SQSMessaginRepository sqsMessaginRepository = new SQSMessaginRepository();
		SNSMessagingRepository snsMessagingRepository = new SNSMessagingRepository();
		DynamoRepository dynamoRepository = new DynamoRepository();

		String sourceQueueName = environment.getProperty("source.queue.name");
		String sourceBucketName = environment.getProperty("source.bucket.name");
		String targetDynamoTable = environment.getProperty("target.dynamo.table");
		String targetTopicARN = environment.getProperty("target.topic.arn");
		int sourcePollingThreadPoolCount = Integer
				.valueOf(environment.getProperty("sourcepolling.threadpool.count", "20"));

		while (true) {

			ExecutorService executor = Executors.newFixedThreadPool(sourcePollingThreadPoolCount);
			System.out.println("Fetching mesages from queue ...");
			List<Message> receiveRequest = sqsMessaginRepository.receiveRequest(sourceQueueName);
			System.out.println("Number of messages received fron queue " + receiveRequest.size());
			
			try {
				for (Message message : receiveRequest) {
					JsonObject jsonObject = new JsonParser().parse(message.body()).getAsJsonObject();
					JsonElement jsonElement = jsonObject.get("fileName");
					if (jsonElement != null && jsonElement.getAsString() != null) {
						String s3FileName = jsonElement.getAsString();
						S3FileUploaderService sqsNotificationProcessor = new S3FileUploaderService(
								sqsMessaginRepository, snsMessagingRepository, dynamoRepository, sourceQueueName,
								targetTopicARN, sourceBucketName, targetDynamoTable, message, s3FileName);
						executor.submit(sqsNotificationProcessor);
						executor.shutdown();
						while (!executor.isTerminated()) {
						}
						System.out.println("A thread finished");
					}
				}
				Thread.sleep(TimeUnit.SECONDS.toMillis(5));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
