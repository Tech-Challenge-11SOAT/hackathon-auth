package br.com.fiap.hackathon_auth.adapters.inbound.extractors;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.LoginRequestDTO;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthFormatException;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthHeaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class BasicAuthExtractor {

	private static final String BASIC_PREFIX = "Basic ";
	private static final String CREDENTIALS_SEPARATOR = ":";

	public LoginRequestDTO extractCredentials(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BASIC_PREFIX)) {
			throw new InvalidBasicAuthHeaderException(
					"Header de autorização inválido. O header deve começar com 'Basic '");
		}

		String base64Credentials = authorizationHeader.substring(BASIC_PREFIX.length()).trim();

		try {
			byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
			String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

			String[] parts = credentials.split(CREDENTIALS_SEPARATOR, 2);

			if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
				throw new InvalidBasicAuthFormatException(
						"Formato de credenciais inválido. Esperado 'username:password'");
			}

			log.debug("Credenciais extraídas com sucesso para o usuário: {}", parts[0]);

			return LoginRequestDTO.builder()
					.username(parts[0])
					.password(parts[1])
					.build();

		} catch (IllegalArgumentException e) {
			log.error("Falha ao decodificar credenciais Basic Auth", e);
			throw new InvalidBasicAuthFormatException(
					"Codificação Base64 inválida no header de autorização", e);
		}
	}
}
