package br.com.fiap.hackathon_auth.domain.exceptions;

public class InvalidBasicAuthHeaderException extends RuntimeException {

	public InvalidBasicAuthHeaderException(String message) {
		super(message);
	}

	public InvalidBasicAuthHeaderException(String message, Throwable cause) {
		super(message, cause);
	}
}
