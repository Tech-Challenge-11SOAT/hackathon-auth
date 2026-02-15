package br.com.fiap.hackathon_auth.application.usecases;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;

public interface AuthUseCases {
	LoginResponseDTO login(String username, String password);
}
