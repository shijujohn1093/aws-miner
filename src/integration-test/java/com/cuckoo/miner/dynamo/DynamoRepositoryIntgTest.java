package com.cuckoo.miner.dynamo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cuckoo.miner.s3.S3Repository;

@Ignore
public class DynamoRepositoryIntgTest {

	private DynamoRepository dynamoRepository;

	String tableName = "";

	@Before
	public void init() {
		dynamoRepository = new DynamoRepository();
	}

	@Test
	public void ShouldReadDataFromBucket() {
		S3Repository s3Repository = new S3Repository();
//		dynamoRepository.putItemReadingFromS3("resultsclientrepository", Region.AP_SOUTH_1, "results.csv");

	}

}
