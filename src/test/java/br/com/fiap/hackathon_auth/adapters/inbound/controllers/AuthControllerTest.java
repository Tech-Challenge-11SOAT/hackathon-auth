package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.LoginRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.extractors.BasicAuthExtractor;
import br.com.fiap.hackathon_auth.application.usecases.AuthUseCases;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthHeaderException;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthUseCases authUseCases;

	@Mock
	private BasicAuthExtractor basicAuthExtractor;

	@InjectMocks
	private AuthController authController;

	@Test
	void shouldLoginWithBasicAuthCredentials() {
		String authorizationHeader = "Basic dXNlcjpwYXNz";
		LoginRequestDTO credentials = LoginRequestDTO.builder()
				.username("user")
				.password("pass")
				.build();
		LoginResponseDTO expectedResponse = new LoginResponseDTO("jwt-token");

		when(basicAuthExtractor.extractCredentials(authorizationHeader)).thenReturn(credentials);
		when(authUseCases.login("user", "pass")).thenReturn(expectedResponse);

		LoginResponseDTO response = authController.login(authorizationHeader);

		assertSame(expectedResponse, response);
		assertEquals("jwt-token", response.token());
		verify(basicAuthExtractor).extractCredentials(authorizationHeader);
		verify(authUseCases).login("user", "pass");
	}

	@Test
	void shouldPropagateExceptionWhenAuthorizationHeaderIsInvalid() {
		String authorizationHeader = "Bearer token";
		InvalidBasicAuthHeaderException exception = new InvalidBasicAuthHeaderException("header inválido");

		when(basicAuthExtractor.extractCredentials(authorizationHeader)).thenThrow(exception);

		InvalidBasicAuthHeaderException thrown = assertThrows(
				InvalidBasicAuthHeaderException.class,
				() -> authController.login(authorizationHeader));

		assertSame(exception, thrown);
		verify(basicAuthExtractor).extractCredentials(authorizationHeader);
		verifyNoInteractions(authUseCases);
	}
}