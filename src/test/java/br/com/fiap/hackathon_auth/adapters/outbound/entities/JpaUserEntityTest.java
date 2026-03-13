package br.com.fiap.hackathon_auth.adapters.outbound.entities;

import br.com.fiap.hackathon_auth.domain.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JpaUserEntityTest {

    @Test
    void deveCriarEntidadeAtravesDoModeloDeDominio() {
        User user = new User(UUID.randomUUID(), "Abel", "abel@teste.com", "hash", LocalDateTime.now(), LocalDateTime.now());

        JpaUserEntity entity = new JpaUserEntity(user);

        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getPasswordHash(), entity.getPasswordHash());
        assertEquals(user.getCreatedAt(), entity.getCreatedAt());
        assertEquals(user.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void devePreencherDatasAutomaticamenteNoPrePersist() {
        JpaUserEntity entity = new JpaUserEntity();

        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());

        entity.prePersist();

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void deveManterDatasSeJaExistiremNoPrePersist() {
        LocalDateTime dataAntiga = LocalDateTime.of(2022, 1, 1, 10, 0);
        JpaUserEntity entity = new JpaUserEntity();
        entity.setCreatedAt(dataAntiga);
        entity.setUpdatedAt(dataAntiga);

        entity.prePersist();

        assertEquals(dataAntiga, entity.getCreatedAt());
        assertEquals(dataAntiga, entity.getUpdatedAt());
    }

    @Test
    void deveAtualizarDataDeModificacaoNoPreUpdate() {
        LocalDateTime dataAntiga = LocalDateTime.now().minusDays(2);
        JpaUserEntity entity = new JpaUserEntity();
        entity.setUpdatedAt(dataAntiga);

        entity.preUpdate();

        assertNotEquals(dataAntiga, entity.getUpdatedAt());
        assertTrue(entity.getUpdatedAt().isAfter(dataAntiga));
    }
}