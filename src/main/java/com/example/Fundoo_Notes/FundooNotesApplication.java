package com.example.Fundoo_Notes;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FundooNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooNotesApplication.class, args);
	}
	@PostConstruct
	public void checkEnv() {
		System.out.println("USERNAME=" + System.getenv("DB_USERNAME") + "|");
	}
}
