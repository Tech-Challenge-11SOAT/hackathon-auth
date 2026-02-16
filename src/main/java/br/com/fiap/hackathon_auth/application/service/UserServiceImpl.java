package br.com.fiap.hackathon_auth.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.RegisterRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.RegisterResponseDTO;
import br.com.fiap.hackathon_auth.application.usecases.UserUseCases;
import br.com.fiap.hackathon_auth.domain.user.EmailAlreadyExistsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidUserDataException;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserUseCases {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
		log.info("Iniciando registro de novo usuário: {}", registerRequestDTO.getEmail());

		this.validateUserData(registerRequestDTO);
		this.checkIfEmailAlreadyExists(registerRequestDTO.getEmail());

		String passwordHash = passwordEncoder.encode(registerRequestDTO.getPassword());
		LocalDateTime now = LocalDateTime.now();

		User user = new User(
				UUID.randomUUID(),
				registerRequestDTO.getName(),
				registerRequestDTO.getEmail(),
				passwordHash,
				now,
				now);

		User savedUser = userRepository.save(user);

		log.info("Usuário registrado com sucesso: {}", savedUser.getId());

		return RegisterResponseDTO.builder()
				.id(savedUser.getId())
				.name(savedUser.getName())
				.email(savedUser.getEmail())
				.createdAt(savedUser.getCreatedAt())
				.build();
	}

	@Override
	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email).orElse(null);
	}

	@Override
	public User findById(UUID id) {
		return userRepository.findById(id).orElse(null);
	}

	private void validateUserData(RegisterRequestDTO registerRequestDTO) {
		if (registerRequestDTO.getName() == null || registerRequestDTO.getName().isBlank()) {
			log.warn("Tentativa de registro com nome vazio");
			throw new InvalidUserDataException("Nome do usuário é obrigatório");
		}

		if (registerRequestDTO.getEmail() == null || registerRequestDTO.getEmail().isBlank()) {
			log.warn("Tentativa de registro com email vazio");
			throw new InvalidUserDataException("Email é obrigatório");
		}

		if (registerRequestDTO.getPassword() == null || registerRequestDTO.getPassword().isBlank()) {
			log.warn("Tentativa de registro com senha vazia");
			throw new InvalidUserDataException("Senha é obrigatória");
		}
	}

	private void checkIfEmailAlreadyExists(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			log.warn("Tentativa de registro com email já existente: {}", email);
			throw new EmailAlreadyExistsException("Email já cadastrado no sistema");
		}
	}

}
