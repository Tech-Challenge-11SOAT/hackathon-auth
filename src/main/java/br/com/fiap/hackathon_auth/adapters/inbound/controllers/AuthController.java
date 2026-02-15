package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.LoginRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import br.com.fiap.hackathon_auth.application.usecases.AuthUseCases;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints relacionados à autenticação de usuários")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthController {

	private final AuthUseCases authUseCases;

	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
		return this.authUseCases.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
	}

}
