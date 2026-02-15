package br.com.fiap.hackathon_auth.application.service;

import org.springframework.stereotype.Service;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import br.com.fiap.hackathon_auth.application.usecases.AuthUseCases;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthUseCases {

	@Override
	public LoginResponseDTO login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}
}
