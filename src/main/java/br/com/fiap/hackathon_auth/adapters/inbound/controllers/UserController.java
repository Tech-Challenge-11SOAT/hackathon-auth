package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.RegisterRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.RegisterResponseDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.UserResponseDTO;
import br.com.fiap.hackathon_auth.application.usecases.UserUseCases;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements IUserControllerSwagger {

	private final UserUseCases userUseCases;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
		log.info("Recebendo requisição de registro: {}", registerRequestDTO.getEmail());

		RegisterResponseDTO response = userUseCases.register(registerRequestDTO);

		log.info("Usuário registrado com sucesso: {}", response.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
		log.info("Buscando usuário por ID: {}", id);

		User user = userUseCases.findById(id);
		if (user == null) {
			log.warn("Usuário não encontrado: {}", id);
			throw new UserNotFoundException("Usuário não encontrado");
		}

		log.info("Usuário encontrado: {}", id);

		UserResponseDTO response = UserResponseDTO.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();

		return ResponseEntity.ok(response);
	}

}
