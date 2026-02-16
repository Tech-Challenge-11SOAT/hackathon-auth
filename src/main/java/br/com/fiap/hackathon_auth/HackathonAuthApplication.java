package br.com.fiap.hackathon_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.fiap.hackathon_auth")
@EnableJpaRepositories(basePackages = "br.com.fiap.hackathon_auth.adapters.outbound.repositories")
@EntityScan(basePackages = "br.com.fiap.hackathon_auth.adapters.outbound.entities")
public class HackathonAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackathonAuthApplication.class, args);
	}

}
