package br.com.fiap.hackathon_auth.utils;

import br.com.fiap.hackathon_auth.utils.constants.SecurityConstants;
import org.springframework.util.AntPathMatcher;

import java.util.stream.Stream;

public class SecurityUtils {

	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	public static boolean isPublicEndpoint(String uri) {
		return Stream.of(SecurityConstants.AUTHORIZED_URLS)
				.anyMatch(pattern -> pathMatcher.match(pattern, uri));
	}

}
