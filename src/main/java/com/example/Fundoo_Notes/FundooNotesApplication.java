package com.example.Fundoo_Notes;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;


@SpringBootApplication
@EnableCaching
@EnableJms
@EnableRabbit
public class FundooNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooNotesApplication.class, args);
	}
	@PostConstruct
	public void checkEnv() {
		System.out.println("USERNAME=" + System.getenv("DB_USERNAME") + "|");
	}
}
