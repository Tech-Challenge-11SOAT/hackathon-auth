package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

	Optional<JpaUserEntity> findByEmail(String email);

}
