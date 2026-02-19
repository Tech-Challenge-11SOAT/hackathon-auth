package br.com.fiap.hackathon_auth.application.usecases;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.RegisterRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.RegisterResponseDTO;
import br.com.fiap.hackathon_auth.domain.user.User;

import java.util.UUID;

public interface UserUseCases {

	RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);

	User findByEmail(String email);

	User findById(UUID id);

}
