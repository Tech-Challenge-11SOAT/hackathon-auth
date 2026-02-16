package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Object> {

}
