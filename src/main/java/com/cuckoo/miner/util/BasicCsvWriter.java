package com.cuckoo.miner.util;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class BasicCsvWriter {

	
	public void writeCSVToFileExample(String csvFileNameToWrite) {
        Writer writer;
		try {
			writer = Files.newBufferedWriter(Paths.get(csvFileNameToWrite));
	        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Student Name", "Fees"));
            //Writing records in the generated CSV file
            csvPrinter.printRecord("Akshay Sharma", 1000);
            csvPrinter.printRecord("Rahul Gupta", 2000);
            csvPrinter.printRecord("Jay Karn", 3000);
            //Writing records in the form of a list
            csvPrinter.printRecord(Arrays.asList("Dev Bhatia", 4000));
            csvPrinter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
