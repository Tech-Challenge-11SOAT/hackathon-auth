package br.com.fiap.hackathon_auth.application.service;

import br.com.fiap.hackathon_auth.adapters.configuration.JwtProvider;
import br.com.fiap.hackathon_auth.adapters.inbound.dto.response.LoginResponseDTO;
import br.com.fiap.hackathon_auth.domain.user.InvalidCredentialsException;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserNotFoundException;
import br.com.fiap.hackathon_auth.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void deveFazerLoginComSucesso() {

        User user = new User();
        user.setEmail("endrew@teste.com");
        user.setPasswordHash("senha_criptografada");

        when(userRepository.findByEmail("endrew@teste.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "senha_criptografada")).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn("meu.token.jwt");

        LoginResponseDTO response = authService.login("endrew@teste.com", "123456");

        assertNotNull(response);
        assertEquals("meu.token.jwt", response.token());

        verify(userRepository, times(1)).findByEmail("endrew@teste.com");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {

        when(userRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login("naoexiste@teste.com", "qualquersenha"));

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {

        User user = new User();
        user.setEmail("endrew@teste.com");
        user.setPasswordHash("senha_criptografada");

        when(userRepository.findByEmail("endrew@teste.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("senha_errada", "senha_criptografada")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login("endrew@teste.com", "senha_errada"));

        verifyNoInteractions(jwtProvider);
    }
}