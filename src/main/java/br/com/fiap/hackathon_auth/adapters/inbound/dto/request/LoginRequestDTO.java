package br.com.fiap.hackathon_auth.adapters.inbound.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {
	@NotNull(message = "É obrigatório informar o nome de usuário.")
	private String username;
	@NotNull(message = "É obrigatório informar a senha.")
	private String password;
}
