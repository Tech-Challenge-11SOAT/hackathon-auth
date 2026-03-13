package br.com.fiap.hackathon_auth.application.service;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.RegisterRequestDTO;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.RegisterResponseDTO;
import br.com.fiap.hackathon_auth.domain.user.EmailAlreadyExistsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidUserDataException;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    // --- TESTES DO METODO REGISTER ---

    @Test
    void deveRegistrarUsuarioComSucesso() {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .name("Maria Silva")
                .email("maria@teste.com")
                .password("senha123")
                .build();

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setName("Maria Silva");
        savedUser.setEmail("maria@teste.com");
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("senha_criptografada");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponseDTO response = userService.register(request);

        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getId());
        assertEquals("Maria Silva", response.getName());
        assertEquals("maria@teste.com", response.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNuloOuVazio() {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .name("") // Nome vazio
                .email("maria@teste.com")
                .password("senha123")
                .build();

        InvalidUserDataException exception = assertThrows(InvalidUserDataException.class, () -> userService.register(request));
        assertEquals("Nome do usuário é obrigatório", exception.getMessage());

        verifyNoInteractions(userRepository);
    }

    @Test
    void deveLancarExcecaoQuandoEmailForNuloOuVazio() {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .name("Maria Silva")
                .email(null)
                .password("senha123")
                .build();

        assertThrows(InvalidUserDataException.class, () -> userService.register(request));
        verifyNoInteractions(userRepository);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForNulaOuVazia() {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .name("Maria Silva")
                .email("maria@teste.com")
                .password("   ")
                .build();

        assertThrows(InvalidUserDataException.class, () -> userService.register(request));
        verifyNoInteractions(userRepository);
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .name("Maria Silva")
                .email("maria@teste.com")
                .password("senha123")
                .build();

        when(userRepository.findByEmail("maria@teste.com")).thenReturn(Optional.of(new User()));

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> userService.register(request));
        assertEquals("Email já cadastrado no sistema", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    // --- TESTES DO METODO FINDBYEMAIL ---

    @Test
    void deveRetornarUsuarioQuandoBuscarPorEmailExistente() {

        User user = new User();
        user.setEmail("maria@teste.com");
        when(userRepository.findByEmail("maria@teste.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("maria@teste.com");

        assertNotNull(result);
        assertEquals("maria@teste.com", result.getEmail());
    }

    @Test
    void deveRetornarNuloQuandoBuscarPorEmailInexistente() {

        when(userRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        User result = userService.findByEmail("naoexiste@teste.com");

        assertNull(result);
    }

    // --- TESTES DO METODO FINDBYID ---

    @Test
    void deveRetornarUsuarioQuandoBuscarPorIdExistente() {

        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void deveRetornarNuloQuandoBuscarPorIdInexistente() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        User result = userService.findById(id);

        assertNull(result);
    }
}