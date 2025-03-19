package com.rentspace.listingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ListingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListingserviceApplication.class, args);
	}

}
