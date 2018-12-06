package com.cuckoo.miner.s3;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import software.amazon.awssdk.regions.Region;

@Ignore
public class S3RepositoryIntgTest {

	private S3Repository s3Repository;

	String tableName = "";

	@Before
	public void init() {
		s3Repository = new S3Repository();
	}

	@Test
	public void shouldReadDataFromBucket() {
		s3Repository.readBucketByFile1("resultsclientrepository", Region.AP_SOUTH_1, "results.csv");
		System.out.println();
	}

}
