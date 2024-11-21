package com.pavlov.first_rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * класс для запуска приложения
 */
@Slf4j
@SpringBootApplication
public class FirstRestApplication {

	public static void main(String[] args) {
		log.info("First Rest Application Started");
		SpringApplication.run(FirstRestApplication.class, args);
	}

}
