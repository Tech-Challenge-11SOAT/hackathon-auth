package br.com.fiap.hackathon_auth.adapters.outbound.cache;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import br.com.fiap.hackathon_auth.adapters.configuration.RateLimitProperties;

@Service
public class RedisRateLimiterService {

	private final StringRedisTemplate redisTemplate;
	private final RateLimitProperties rateLimitProperties;

	public RedisRateLimiterService(StringRedisTemplate redisTemplate, RateLimitProperties rateLimitProperties) {
		this.redisTemplate = redisTemplate;
		this.rateLimitProperties = rateLimitProperties;
	}

	public boolean isAllowed(String identifier) {
		long windowSeconds = rateLimitProperties.getWindowSeconds();
		long currentWindow = System.currentTimeMillis() / 1000 / windowSeconds;

		String key = "%s:%s:%s".formatted(
				rateLimitProperties.getKeyPrefix(),
				identifier,
				currentWindow);

		Long currentCount = redisTemplate.opsForValue().increment(key);
		if (currentCount != null && currentCount == 1L) {
			redisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
		}

		return currentCount != null && currentCount <= rateLimitProperties.getMaxRequests();
	}
}
