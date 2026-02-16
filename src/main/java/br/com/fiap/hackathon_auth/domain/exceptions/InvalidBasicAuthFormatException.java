package br.com.fiap.hackathon_auth.domain.exceptions;

public class InvalidBasicAuthFormatException extends RuntimeException {

	public InvalidBasicAuthFormatException(String message) {
		super(message);
	}

	public InvalidBasicAuthFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
