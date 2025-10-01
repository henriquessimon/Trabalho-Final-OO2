package br.edu.ifrs.canoas.gamestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "br.edu.ifrs.canoas.gamestore.model.domain")
public class GamestoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(GamestoreApplication.class, args);
	}
}