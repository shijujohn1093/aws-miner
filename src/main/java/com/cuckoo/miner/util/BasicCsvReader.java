package com.cuckoo.miner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class BasicCsvReader {

	
	public void readCSVFile() throws IOException{
		BufferedReader reader = Files.newBufferedReader(Paths.get("student.csv"));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("Student Name", "Fees").withIgnoreHeaderCase().withTrim());
        for (CSVRecord csvRecord: csvParser) {
            // Accessing Values by Column Index
            String name = csvRecord.get(0);
            //Accessing the values by column header name
            String fees = csvRecord.get("fees");
            //Printing the record 
            System.out.println("Record Number - " + csvRecord.getRecordNumber());
            System.out.println("Name : " + name);
            System.out.println("Fees : " + fees);
            System.out.println("\n\n");
        }
	}
	
}
