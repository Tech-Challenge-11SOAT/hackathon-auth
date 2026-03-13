package br.com.fiap.hackathon_auth.adapters.outbound.cache;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import br.com.fiap.hackathon_auth.domain.exceptions.RedisUnavailableException;

@Service
public class RedisSessionService {

	private static final String SESSION_KEY_PREFIX = "session:";

	private final StringRedisTemplate redisTemplate;

	public RedisSessionService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void saveSession(UUID userId, String token, Duration ttl) {
		try {
			redisTemplate.opsForValue().set(buildKey(userId), token, ttl);
		} catch (RedisConnectionFailureException ex) {
			throw new RedisUnavailableException("Serviço de sessão indisponível no momento.", ex);
		}
	}

	public boolean isSessionValid(UUID userId, String token) {
		try {
			String storedToken = redisTemplate.opsForValue().get(buildKey(userId));
			return Objects.equals(storedToken, token);
		} catch (RedisConnectionFailureException ex) {
			throw new RedisUnavailableException("Serviço de sessão indisponível no momento.", ex);
		}
	}

	private String buildKey(UUID userId) {
		return SESSION_KEY_PREFIX + userId;
	}
}
