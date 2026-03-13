package br.com.fiap.hackathon_auth.application.service;

import br.com.fiap.hackathon_auth.adapters.configuration.JwtProvider;
import br.com.fiap.hackathon_auth.adapters.outbound.cache.RedisSessionService;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import br.com.fiap.hackathon_auth.application.usecases.AuthUseCases;
import br.com.fiap.hackathon_auth.domain.user.InvalidCredentialsException;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserNotFoundException;
import br.com.fiap.hackathon_auth.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthUseCases {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final RedisSessionService redisSessionService;

	@Override
	public LoginResponseDTO login(String username, String password) {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			throw new InvalidCredentialsException("Usuário ou senha inválidos.");
		}

		String accessToken = jwtProvider.generateAccessToken(user);
		redisSessionService.saveSession(user.getId(), accessToken, jwtProvider.getTokenTtl(accessToken));

		return new LoginResponseDTO(accessToken);
	}
}
