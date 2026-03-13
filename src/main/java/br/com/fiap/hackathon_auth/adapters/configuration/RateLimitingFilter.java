package br.com.fiap.hackathon_auth.adapters.configuration;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.hackathon_auth.adapters.outbound.cache.RedisRateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

	private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

	private final RedisRateLimiterService redisRateLimiterService;
	private final RateLimitProperties rateLimitProperties;

	public RateLimitingFilter(RedisRateLimiterService redisRateLimiterService,
			RateLimitProperties rateLimitProperties) {
		this.redisRateLimiterService = redisRateLimiterService;
		this.rateLimitProperties = rateLimitProperties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!rateLimitProperties.isEnabled() || isIgnoredPath(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}

		String identifier = resolveClientIdentifier(request);
		try {
			if (!redisRateLimiterService.isAllowed(identifier)) {
				log.warn("Rate limit excedido para identificador: {}", identifier);
				response.setStatus(429);
				response.setContentType("application/json");
				response.getWriter().write("""
						{
						    "error": "Too Many Requests",
						    "message": "Limite de requisições excedido. Tente novamente em instantes.",
						    "status": 429
						}
						""");
				return;
			}
		} catch (RedisConnectionFailureException ex) {
			log.warn("Redis indisponível: rate limiting em fail-open para a requisição {} {}",
					request.getMethod(),
					request.getRequestURI());
			filterChain.doFilter(request, response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isIgnoredPath(String requestUri) {
		return rateLimitProperties.getIgnoredPaths().stream()
				.anyMatch(path -> PATH_MATCHER.match(path, requestUri));
	}

	private String resolveClientIdentifier(HttpServletRequest request) {
		String forwardedFor = request.getHeader("X-Forwarded-For");
		if (forwardedFor != null && !forwardedFor.isBlank()) {
			return "ip:" + Arrays.stream(forwardedFor.split(","))
					.map(String::trim)
					.findFirst()
					.orElse(request.getRemoteAddr());
		}
		return "ip:" + request.getRemoteAddr();
	}
}
