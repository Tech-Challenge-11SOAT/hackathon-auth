package br.com.fiap.hackathon_auth.adapters.outbound.entities;

import br.com.fiap.hackathon_auth.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JpaUserEntity {

	@Id
	@GeneratedValue
	private UUID id;

	private String name;
	private String email;
	private String passwordHash;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public JpaUserEntity(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.passwordHash = user.getPasswordHash();
		this.createdAt = user.getCreatedAt();
		this.updatedAt = user.getUpdatedAt();
	}

	@PrePersist
	public void prePersist() {
		updatedAt = LocalDateTime.now();
	}
}