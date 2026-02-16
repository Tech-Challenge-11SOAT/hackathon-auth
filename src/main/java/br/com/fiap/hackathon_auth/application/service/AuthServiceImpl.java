package br.com.fiap.hackathon_auth.application.service;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import br.com.fiap.hackathon_auth.application.usecases.AuthUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthUseCases {

	@Override
	public LoginResponseDTO login(String username, String password) {

		return null;
	}
}
