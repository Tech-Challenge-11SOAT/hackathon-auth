package br.com.fiap.hackathon_auth.adapters.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

	private boolean enabled = true;
	private long maxRequests = 100;
	private long windowSeconds = 60;
	private String keyPrefix = "rate_limit";
	private List<String> ignoredPaths = List.of();
}
