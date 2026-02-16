package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.LoginRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.extractors.BasicAuthExtractor;
import br.com.fiap.hackathon_auth.application.usecases.AuthUseCases;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AuthController implements IAuthControllerSwagger {

	private final AuthUseCases authUseCases;
	private final BasicAuthExtractor basicAuthExtractor;

	@PostMapping("/login")
	public LoginResponseDTO login(@RequestHeader("Authorization") String authorizationHeader) {
		log.info("Recebendo requisição de login via Basic Auth");

		LoginRequestDTO credentials = basicAuthExtractor.extractCredentials(authorizationHeader);

		log.info("Login para usuário: {}", credentials.getUsername());
		return this.authUseCases.login(credentials.getUsername(), credentials.getPassword());
	}

}
