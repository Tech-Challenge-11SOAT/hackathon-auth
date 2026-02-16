package br.com.fiap.hackathon_auth.domain.user;

public class InvalidUserDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidUserDataException(String message) {
		super(message);
	}

	public InvalidUserDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
