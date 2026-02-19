package br.com.fiap.hackathon_auth.adapters.inbound.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {

	private LocalDateTime timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
}
