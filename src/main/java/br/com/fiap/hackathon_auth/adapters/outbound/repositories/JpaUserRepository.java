package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

	Optional<JpaUserEntity> findByEmail(String email);

}
