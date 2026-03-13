package br.com.fiap.hackathon_auth.domain.exceptions;

public class RedisUnavailableException extends RuntimeException {

	public RedisUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
