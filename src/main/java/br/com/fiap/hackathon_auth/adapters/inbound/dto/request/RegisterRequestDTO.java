package br.com.fiap.hackathon_auth.adapters.inbound.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

	@NotBlank(message = "Nome é obrigatório")
	@Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
	private String name;

	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email deve ser válido")
	private String email;

	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 6, max = 255, message = "Senha deve ter entre 6 e 255 caracteres")
	private String password;

}
