package br.com.fiap.hackathon_auth.adapters.configuration;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.hackathon_auth.adapters.outbound.cache.RedisSessionService;
import br.com.fiap.hackathon_auth.domain.exceptions.RedisUnavailableException;
import br.com.fiap.hackathon_auth.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final RedisSessionService redisSessionService;

	public JwtAuthenticationFilter(JwtProvider jwtProvider, RedisSessionService redisSessionService) {
		this.jwtProvider = jwtProvider;
		this.redisSessionService = redisSessionService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		log.info("[JWT Filter] Nova requisição recebida: {}", requestURI);

		String token = this.parseToken(request);

		if (SecurityUtils.isPublicEndpoint(requestURI)) {
			logger.info("Endpoint não necessita de autorização, continuando...");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			if (token == null) {
				log.info("[JWT Filter] Nenhum token JWT encontrado.");
				filterChain.doFilter(request, response);
				return;
			}

			log.info("[JWT Filter] Token JWT encontrado.");

			if (!jwtProvider.validateToken(token)) {
				log.warn("[JWT Filter] Token inválido.");
				this.sendForbiddenResponse(response, "Token inválido. Por favor, realize o login novamente.");
				return;
			}

			Claims claims = jwtProvider.getClaims(token);
			String email = claims.getSubject();
			List<?> roles = claims.get("roles", List.class);
			UUID userId = jwtProvider.getUserIdFromToken(token);

			if (!redisSessionService.isSessionValid(userId, token)) {
				log.warn("[JWT Filter] Sessão inválida ou expirada para userId: {}", userId);
				sendForbiddenResponse(response, "Sessão inválida ou expirada. Realize o login novamente.");
				return;
			}

			List<SimpleGrantedAuthority> authorities = (roles == null ? Collections.emptyList() : roles).stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
					.toList();

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null,
					authorities);

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			log.info("[JWT Filter] ✅ Autenticação bem-sucedida para usuário: {} com roles: {}", email, roles);

			filterChain.doFilter(request, response);
		} catch (io.jsonwebtoken.ExpiredJwtException ex) {
			log.warn("[JWT Filter] Token expirado.", ex);
			sendForbiddenResponse(response, "Token expirado. Por favor, realize o login novamente.");
		} catch (RedisUnavailableException ex) {
			log.warn("[JWT Filter] Redis indisponível para validação de sessão.");
			sendServiceUnavailableResponse(response,
					"Serviço de sessão temporariamente indisponível. Tente novamente em instantes.");
		} catch (ServletException | IOException ex) {
			log.error("[JWT Filter] Erro no processamento do token.", ex);
			sendForbiddenResponse(response, "Erro de autenticação. Por favor, realize o login novamente.");
		}
	}

	private String parseToken(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.replace("Bearer ", "");
		}

		return null;
	}

	private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.getWriter().write("""
				{
				    "error": "Forbidden",
				    "message": "%s",
				    "status": 403
				}
				""".formatted(message));
	}

	private void sendServiceUnavailableResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		response.setContentType("application/json");
		response.getWriter().write("""
				{
				    "error": "Service Unavailable",
				    "message": "%s",
				    "status": 503
				}
				""".formatted(message));
	}

}