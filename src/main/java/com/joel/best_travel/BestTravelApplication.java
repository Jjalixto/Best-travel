package com.joel.best_travel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.joel.best_travel.domain.repositories.mongo.AppUserRepository;

@SpringBootApplication

public class BestTravelApplication implements CommandLineRunner{

	@Autowired
	private AppUserRepository appUserRepository;

	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println(this.appUserRepository.findByUsername("misterX").orElseThrow());
	}
}
