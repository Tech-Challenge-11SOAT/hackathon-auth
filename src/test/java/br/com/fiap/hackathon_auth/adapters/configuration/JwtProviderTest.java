package br.com.fiap.hackathon_auth.adapters.configuration;

import br.com.fiap.hackathon_auth.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {

        JwtProperties properties = new JwtProperties();
        properties.setSecret("minha-chave-secreta-muito-longa-para-o-algoritmo-hs256-funcionar");
        properties.setAccessTokenExpiration(3600000L); // 1 hora
        properties.setRefreshTokenExpiration(7200000L); // 2 horas

        jwtProvider = new JwtProvider(properties);

        jwtProvider.init();
    }

    @Test
    void deveGerarEValidarAccessTokenComSucesso() {
        User user = new User();
        user.setEmail("joao@teste.com");
        user.setId(UUID.randomUUID());

        String token = jwtProvider.generateAccessToken(user);

        assertNotNull(token);
        assertTrue(jwtProvider.validateToken(token));
        assertEquals("joao@teste.com", jwtProvider.getEmailFromToken(token));
        assertEquals(user.getId().toString(), jwtProvider.getClaims(token).get("idUser"));
    }

    @Test
    void deveGerarEValidarRefreshTokenComSucesso() {
        User user = new User();
        user.setEmail("maria@teste.com");

        String token = jwtProvider.generateRefreshToken(user);

        assertNotNull(token);
        assertTrue(jwtProvider.validateToken(token));
        assertEquals("maria@teste.com", jwtProvider.getEmailFromToken(token));
    }

    @Test
    void deveRetornarFalseAoValidarTokenInvalido() {
        assertFalse(jwtProvider.validateToken("um.token.invalido"));
    }

    @Test
    void deveExtrairTokenDoCabecalhoBearer() {
        String token = jwtProvider.extractToken("Bearer meutoken123");
        assertEquals("meutoken123", token);
    }

    @Test
    void deveLancarExcecaoAoTentarExtrairTokenInvalido() {
        assertThrows(RuntimeException.class, () -> jwtProvider.extractToken("TokenSemBearer"));
        assertThrows(RuntimeException.class, () -> jwtProvider.extractToken(null));
    }

    @Test
    void deveRetornarEmailDoUsuarioAutenticadoNoContexto() {

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("logado@teste.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertEquals("logado@teste.com", jwtProvider.getCurrentUserEmail());

        SecurityContextHolder.clearContext(); // Limpa no final
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverUsuarioNoContexto() {
        SecurityContextHolder.clearContext();
        assertThrows(RuntimeException.class, () -> jwtProvider.getCurrentUserEmail());

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(RuntimeException.class, () -> jwtProvider.getCurrentUserEmail());

        SecurityContextHolder.clearContext();
    }
}