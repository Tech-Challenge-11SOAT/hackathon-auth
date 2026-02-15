package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaVideoEntity;

public interface JpaVideoRepository extends JpaRepository<JpaVideoEntity, Object> {

}
