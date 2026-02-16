package br.com.fiap.hackathon_auth.domain.user;

public class EmailAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException(String message) {
		super(message);
	}

	public EmailAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
