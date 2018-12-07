package com.cuckoo.miner.s3;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import software.amazon.awssdk.regions.Region;

@Ignore
public class S3RepositoryIntgTest {

	private S3Repository s3Repository;

	String tableName = "";
	
    public static Region region = Region.of("us-east-1");

	@Before
	public void init() {
		s3Repository = new S3Repository();
		region = Region.of("us-east-1");
		
	}

	@Test
	public void shouldReadDataFromBucket() {
//		s3Repository.readBucketByFile1("resultsclientrepository", region, "results.csv");
//		System.out.println();
	}

}
