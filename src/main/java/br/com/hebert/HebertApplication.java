package br.com.hebert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class HebertApplication {

	public static void main(String[] args) {
		SpringApplication.run(HebertApplication.class, args);
	}

}
