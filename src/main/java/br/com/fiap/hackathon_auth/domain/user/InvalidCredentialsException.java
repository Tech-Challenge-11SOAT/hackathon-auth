package br.com.fiap.hackathon_auth.domain.user;

public class InvalidCredentialsException extends RuntimeException {

	public InvalidCredentialsException(String message) {
		super(message);
	}

	public InvalidCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}
}
