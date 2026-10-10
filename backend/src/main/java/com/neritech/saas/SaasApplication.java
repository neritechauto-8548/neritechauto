package com.neritech.saas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaasApplication {
 
	private static final Logger log = LoggerFactory.getLogger(SaasApplication.class);

	public static void main(String[] args) {
		log.info("Starting SaasApplication with component scanning from: com.neritech.saas");
		SpringApplication.run(SaasApplication.class, args);
	}

}
