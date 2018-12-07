package com.cuckoo.miner.s3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Repository
public class S3Repository {

	private S3Client getS3Client(Region region) {
		S3Client s3 = S3Client.builder().region(region).build();
		return s3;
	}

	public void createBucket(String bucket, Region region) {
		getS3Client(region).createBucket(CreateBucketRequest.builder().bucket(bucket)
				.createBucketConfiguration(CreateBucketConfiguration.builder().locationConstraint(region.id()).build())
				.build());
	}

	public void deleteBucket(String bucket, Region region) {
		DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucket).build();
		getS3Client(region).deleteBucket(deleteBucketRequest);
	}

	public void readBucketByFile(String bucket, Region region, String fileNameToRead) {
		S3Client s3 = getS3Client(region);
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(fileNameToRead).build();
		ResponseInputStream<GetObjectResponse> object = s3.getObject(getObjectRequest);

		BufferedReader reader = new BufferedReader(new InputStreamReader(object));

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				String[] words = line.split(",");
				System.out.println(Arrays.asList(words));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}

	public void readBucketByFile1(String bucket, Region region, String fileNameToRead) {
		S3Client s3 = S3Client.builder().region(region).build();
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(fileNameToRead).build();
		ResponseInputStream<GetObjectResponse> object = s3.getObject(getObjectRequest);

		BufferedReader reader = new BufferedReader(new InputStreamReader(object));

		String line;
		try {
			String[] headers = { "enrollment_id", "exam_name", "percentage", "passing_status" };
			CSVParser csvParser = new CSVParser(reader,
					CSVFormat.DEFAULT.withHeader(headers).withIgnoreHeaderCase().withTrim());
			for (CSVRecord csvRecord : csvParser) {
				System.out.println("Record Number - " + csvRecord.getRecordNumber());

				// Accessing Values by Column Index
				String enrollment_id = csvRecord.get(0);
				// Accessing the values by column header name
				String exam_name = csvRecord.get("exam_name");
				// Printing the record
				String percentage = csvRecord.get("percentage");

				System.out.println("enrollment_id : " + enrollment_id);
				System.out.println("exam_name : " + exam_name);
				System.out.println("percentage : " + percentage);

				System.out.println("\n\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}

	

	private static ByteBuffer getRandomByteBuffer(int size) throws IOException {
		byte[] b = new byte[size];
		new Random().nextBytes(b);
		return ByteBuffer.wrap(b);
	}
}
