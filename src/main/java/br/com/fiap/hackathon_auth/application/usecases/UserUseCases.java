package br.com.fiap.hackathon_auth.application.usecases;

import java.util.UUID;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.RegisterRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.RegisterResponseDTO;
import br.com.fiap.hackathon_auth.domain.user.User;

public interface UserUseCases {

	RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);

	User findByEmail(String email);

	User findById(UUID id);

}
