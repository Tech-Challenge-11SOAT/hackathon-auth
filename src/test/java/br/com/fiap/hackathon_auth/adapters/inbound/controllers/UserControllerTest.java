package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.RegisterRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.RegisterResponseDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.UserResponseDTO;
import br.com.fiap.hackathon_auth.application.usecases.UserUseCases;
import br.com.fiap.hackathon_auth.domain.user.InvalidUserDataException;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
	private UserUseCases userUseCases;

	@InjectMocks
	private UserController userController;

	@Test
	void shouldRegisterUserAndReturnCreatedResponse() {
		RegisterRequestDTO request = RegisterRequestDTO.builder()
				.name("Maria Silva")
				.email("maria@teste.com")
				.password("123456")
				.build();

		UUID userId = UUID.randomUUID();
		LocalDateTime createdAt = LocalDateTime.now();
		RegisterResponseDTO registerResponse = RegisterResponseDTO.builder()
				.id(userId)
				.name("Maria Silva")
				.email("maria@teste.com")
				.createdAt(createdAt)
				.build();

		when(userUseCases.register(request)).thenReturn(registerResponse);

		ResponseEntity<RegisterResponseDTO> response = userController.register(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertSame(registerResponse, response.getBody());
		assertEquals(userId, response.getBody().getId());
		verify(userUseCases).register(request);
	}

	@Test
	void shouldPropagateExceptionWhenRegisterFails() {
		RegisterRequestDTO request = RegisterRequestDTO.builder()
				.name("")
				.email("email-invalido")
				.password("123")
				.build();

		InvalidUserDataException exception = new InvalidUserDataException("dados inválidos");

		when(userUseCases.register(request)).thenThrow(exception);

		InvalidUserDataException thrown = assertThrows(
				InvalidUserDataException.class,
				() -> userController.register(request));

		assertSame(exception, thrown);
		verify(userUseCases).register(request);
	}

	@Test
	void shouldReturnUserByIdWhenUserExists() {
		UUID id = UUID.randomUUID();
		LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
		LocalDateTime updatedAt = LocalDateTime.now();

		User user = new User();
		user.setId(id);
		user.setName("João");
		user.setEmail("joao@teste.com");
		user.setCreatedAt(createdAt);
		user.setUpdatedAt(updatedAt);

		when(userUseCases.findById(id)).thenReturn(user);

		ResponseEntity<UserResponseDTO> response = userController.getUserById(id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().getId());
		assertEquals("João", response.getBody().getName());
		assertEquals("joao@teste.com", response.getBody().getEmail());
		assertEquals(createdAt, response.getBody().getCreatedAt());
		assertEquals(updatedAt, response.getBody().getUpdatedAt());
		verify(userUseCases).findById(id);
	}

	@Test
	void shouldThrowUserNotFoundWhenUserDoesNotExist() {
		UUID id = UUID.randomUUID();

		when(userUseCases.findById(id)).thenReturn(null);

		UserNotFoundException exception = assertThrows(
				UserNotFoundException.class,
				() -> userController.getUserById(id));

		assertEquals("Usuário não encontrado", exception.getMessage());
		verify(userUseCases).findById(id);
	}
}