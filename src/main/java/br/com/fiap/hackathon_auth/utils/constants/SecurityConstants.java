package br.com.fiap.hackathon_auth.utils.constants;

public class SecurityConstants {

	public static final String[] AUTHORIZED_URLS = new String[]{"/admin/auth/**", "/swagger-ui/**", "/v3/api-docs/**",
			"/open/**", "/health", "/ws/**", "/api/v1/users/**"};
	public static final String ADMIN_ROLE = "ROLE_ADMIN";

	private SecurityConstants() {
		throw new IllegalStateException("Utility class");
	}

}
