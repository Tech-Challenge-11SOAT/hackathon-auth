package br.com.fiap.hackathon_auth.adapters.inbound.handlers;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.ErrorResponseDTO;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthFormatException;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthHeaderException;
import br.com.fiap.hackathon_auth.domain.user.EmailAlreadyExistsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidCredentialsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidUserDataException;
import br.com.fiap.hackathon_auth.domain.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        when(webRequest.getDescription(false)).thenReturn("uri=/api/v1/teste");
    }

    @Test
    void deveTratarUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("Usuário não existe");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleUserNotFoundException(ex, webRequest);

        validarResposta(response, HttpStatus.BAD_REQUEST, "Usuário não existe");
    }

    @Test
    void deveTratarInvalidCredentialsException() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Senha errada");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleInvalidCredentialsException(ex, webRequest);

        validarResposta(response, HttpStatus.UNAUTHORIZED, "Senha errada");
    }

    @Test
    void deveTratarEmailAlreadyExistsException() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email duplicado");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleEmailAlreadyExistsException(ex, webRequest);

        validarResposta(response, HttpStatus.CONFLICT, "Email duplicado");
    }

    @Test
    void deveTratarInvalidUserDataException() {
        InvalidUserDataException ex = new InvalidUserDataException("Dados errados");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleInvalidUserDataException(ex, webRequest);

        validarResposta(response, HttpStatus.BAD_REQUEST, "Dados errados");
    }

    @Test
    void deveTratarMethodArgumentNotValidException() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError erro1 = new FieldError("user", "email", "não pode estar vazio");
        FieldError erro2 = new FieldError("user", "nome", "é obrigatório");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(erro1, erro2));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleValidationException(ex, webRequest);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("email: não pode estar vazio, nome: é obrigatório", response.getBody().getMessage());
        assertEquals("/api/v1/teste", response.getBody().getPath());
    }

    @Test
    void deveTratarInvalidBasicAuthHeaderException() {
        InvalidBasicAuthHeaderException ex = new InvalidBasicAuthHeaderException("Header inválido");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleInvalidBasicAuthHeaderException(ex, webRequest);

        validarResposta(response, HttpStatus.BAD_REQUEST, "Header inválido");
    }

    @Test
    void deveTratarInvalidBasicAuthFormatException() {
        InvalidBasicAuthFormatException ex = new InvalidBasicAuthFormatException("Formato inválido");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleInvalidBasicAuthFormatException(ex, webRequest);

        validarResposta(response, HttpStatus.BAD_REQUEST, "Formato inválido");
    }

    @Test
    void deveTratarIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento ilegal");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleIllegalArgumentException(ex, webRequest);

        validarResposta(response, HttpStatus.BAD_REQUEST, "Argumento ilegal");
    }

    @Test
    void deveTratarGenericException() {
        Exception ex = new Exception("Falha catastrófica no banco");

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleGenericException(ex, webRequest);

        validarResposta(response, HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }

    private void validarResposta(ResponseEntity<ErrorResponseDTO> response, HttpStatus statusEsperado, String mensagemEsperada) {
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(statusEsperado, response.getStatusCode());
        assertEquals(statusEsperado.value(), response.getBody().getStatus());
        assertEquals(statusEsperado.getReasonPhrase(), response.getBody().getError());
        assertEquals(mensagemEsperada, response.getBody().getMessage());
        assertEquals("/api/v1/teste", response.getBody().getPath());
    }
}