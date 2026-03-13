package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.utils.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    void deveSalvarUsuarioComSucesso() {

        User domainUser = new User();
        JpaUserEntity jpaEntity = new JpaUserEntity();
        User savedDomainUser = new User();

        when(userMapper.domainToJpa(domainUser)).thenReturn(jpaEntity);
        when(jpaUserRepository.saveAndFlush(jpaEntity)).thenReturn(jpaEntity);
        when(userMapper.jpaToDomain(jpaEntity)).thenReturn(savedDomainUser);

        User result = userRepositoryImpl.save(domainUser);

        assertNotNull(result);
        assertEquals(savedDomainUser, result);
        verify(userMapper).domainToJpa(domainUser);
        verify(jpaUserRepository).saveAndFlush(jpaEntity);
        verify(userMapper).jpaToDomain(jpaEntity);
    }

    @Test
    void deveEncontrarPorIdQuandoExistir() {

        UUID id = UUID.randomUUID();
        JpaUserEntity jpaEntity = new JpaUserEntity();
        User domainUser = new User();

        when(jpaUserRepository.findById(id)).thenReturn(Optional.of(jpaEntity));
        when(userMapper.jpaToDomain(jpaEntity)).thenReturn(domainUser);

        Optional<User> result = userRepositoryImpl.findById(id);

        assertTrue(result.isPresent());
        assertEquals(domainUser, result.get());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorId() {

        UUID id = UUID.randomUUID();
        when(jpaUserRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryImpl.findById(id);

        assertTrue(result.isEmpty());
        verify(userMapper, never()).jpaToDomain(any());
    }

    @Test
    void deveEncontrarPorEmailQuandoExistir() {

        String email = "joao@teste.com";
        JpaUserEntity jpaEntity = new JpaUserEntity();
        User domainUser = new User();

        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(jpaEntity));
        when(userMapper.jpaToDomain(jpaEntity)).thenReturn(domainUser);

        Optional<User> result = userRepositoryImpl.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(domainUser, result.get());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorEmail() {

        String email = "naoexiste@teste.com";
        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryImpl.findByEmail(email);

        assertTrue(result.isEmpty());
        verify(userMapper, never()).jpaToDomain(any());
    }

    @Test
    void deveRetornarListaDeUsuariosNoFindAll() {

        JpaUserEntity jpaEntity1 = new JpaUserEntity();
        jpaEntity1.setId(UUID.randomUUID());

        JpaUserEntity jpaEntity2 = new JpaUserEntity();
        jpaEntity2.setId(UUID.randomUUID());

        User domainUser1 = new User();
        User domainUser2 = new User();

        when(jpaUserRepository.findAll()).thenReturn(List.of(jpaEntity1, jpaEntity2));

        when(userMapper.jpaToDomain(jpaEntity1)).thenReturn(domainUser1);
        when(userMapper.jpaToDomain(jpaEntity2)).thenReturn(domainUser2);

        List<User> result = userRepositoryImpl.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(domainUser1));
        assertTrue(result.contains(domainUser2));
    }

    @Test
    void deveDeletarPorId() {

        UUID id = UUID.randomUUID();

        userRepositoryImpl.deleteById(id);

        verify(jpaUserRepository, times(1)).deleteById(id);
    }
}