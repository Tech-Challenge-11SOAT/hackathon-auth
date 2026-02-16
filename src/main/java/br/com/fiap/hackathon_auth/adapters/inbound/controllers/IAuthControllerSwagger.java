package br.com.fiap.hackathon_auth.adapters.inbound.controllers;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticação", description = "Endpoints relacionados à autenticação de usuários")
public interface IAuthControllerSwagger {

        @Operation(summary = "Realizar login", description = "Autentica um usuário usando Basic Auth (Base64) e retorna um token JWT de acesso. "
                        +
                        "O header Authorization deve conter 'Basic ' seguido de 'username:password' em Base64. " +
                        "Exemplo: 'Basic cGVkcm86MTIz' (pedro:123)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou header malformado."),
                        @ApiResponse(responseCode = "401", description = "Não autorizado. Usuário ou senha incorretos."),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
        })
        LoginResponseDTO login(
                        @Parameter(description = "Header de autenticação Basic Auth no formato: Basic <base64(username:password)>", required = true, example = "Basic cGVkcm86MTIz") String authorizationHeader);

}
