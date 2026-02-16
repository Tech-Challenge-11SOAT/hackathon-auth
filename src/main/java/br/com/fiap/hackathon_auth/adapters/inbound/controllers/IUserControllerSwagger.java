package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.RegisterRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.RegisterResponseDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Usuários", description = "Endpoints relacionados ao gerenciamento de usuários")
public interface IUserControllerSwagger {

	@Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema com os dados fornecidos")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso", content = @Content(schema = @Schema(implementation = RegisterResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos"),
			@ApiResponse(responseCode = "409", description = "Email já cadastrado no sistema"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
	})
	ResponseEntity<RegisterResponseDTO> register(RegisterRequestDTO registerRequestDTO);

	@Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico pelo ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	ResponseEntity<UserResponseDTO> getUserById(
			@Parameter(description = "ID do usuário a ser buscado", required = true) UUID id);

}
