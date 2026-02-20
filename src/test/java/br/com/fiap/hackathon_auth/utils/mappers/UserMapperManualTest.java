package br.com.fiap.hackathon_auth.utils.mappers;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.hackathon_auth.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperManualTest {

    private UserMapperManual mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperManual();
    }

    @Test
    void deveMapearJpaParaDomainComSucesso() {

        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("João");
        entity.setEmail("joao@teste.com");
        entity.setPasswordHash("hash123");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        User user = mapper.jpaToDomain(entity);

        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getName(), user.getName());
        assertEquals(entity.getEmail(), user.getEmail());
        assertEquals(entity.getPasswordHash(), user.getPasswordHash());
        assertEquals(entity.getCreatedAt(), user.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    void deveRetornarNullQuandoJpaParaDomainReceberNull() {
        assertNull(mapper.jpaToDomain(null));
    }

    @Test
    void deveMapearDomainParaJpaComSucesso() {

        User user = new User(UUID.randomUUID(), "João", "joao@teste.com", "hash123", LocalDateTime.now(), LocalDateTime.now());

        JpaUserEntity entity = mapper.domainToJpa(user);

        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getPasswordHash(), entity.getPasswordHash());
        assertEquals(user.getCreatedAt(), entity.getCreatedAt());
        assertEquals(user.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void deveRetornarNullQuandoDomainParaJpaReceberNull() {
        assertNull(mapper.domainToJpa(null));
    }
}