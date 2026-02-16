package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.LoginRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticação", description = "Endpoints relacionados à autenticação de usuários")
public interface IAuthControllerSwagger {

    @Operation(
            summary = "Realizar login",
            description = "Autentica um administrador e retorna tokens de acesso e refresh."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas."),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token inválido ou expirado."),
            @ApiResponse(responseCode = "403", description = "Proibido. Acesso negado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

}
