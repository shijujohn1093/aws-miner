package com.cuckoo.miner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinerApplication {
	
	@Autowired
	private SQSNotificaitonService notificaitonProcessor ;

	public static void main(String[] args) {
		SpringApplication.run(MinerApplication.class, args);
	}
	
	@PostConstruct
	public void process() {
		System.out.println("Starting Application...");
		notificaitonProcessor.process();
		System.out.println("Main thread completedn...");
	}
}
