package br.com.fiap.hackathon_auth.adapters.inbound.handlers;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.ErrorResponseDTO;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthFormatException;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthHeaderException;
import br.com.fiap.hackathon_auth.domain.user.EmailAlreadyExistsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidCredentialsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidUserDataException;
import br.com.fiap.hackathon_auth.domain.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(
			UserNotFoundException ex, WebRequest request) {
		log.error("Usuário não encontrado: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(
			InvalidCredentialsException ex, WebRequest request) {
		log.error("Credenciais inválidas: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.value())
				.error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(
			EmailAlreadyExistsException ex, WebRequest request) {
		log.error("Email já existe: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.error(HttpStatus.CONFLICT.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(InvalidUserDataException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidUserDataException(
			InvalidUserDataException ex, WebRequest request) {
		log.error("Dados de usuário inválidos: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationException(
			MethodArgumentNotValidException ex, WebRequest request) {
		log.error("Erro de validação: {}", ex.getMessage());

		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.reduce((a, b) -> a + ", " + b)
				.orElse("Erro de validação");

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(errorMessage)
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidBasicAuthHeaderException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidBasicAuthHeaderException(
			InvalidBasicAuthHeaderException ex, WebRequest request) {
		log.error("Header Basic Auth inválido: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidBasicAuthFormatException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidBasicAuthFormatException(
			InvalidBasicAuthFormatException ex, WebRequest request) {
		log.error("Formato Basic Auth inválido: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
			IllegalArgumentException ex, WebRequest request) {
		log.error("Argumento inválido: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGenericException(
			Exception ex, WebRequest request) {
		log.error("Erro interno do servidor: {}", ex.getMessage(), ex);

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.message("Erro interno do servidor")
				.path(request.getDescription(false).replace("uri=", ""))
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
