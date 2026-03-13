package br.com.fiap.hackathon_auth.adapters.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void devePassarDiretoParaEndpointPublico() throws ServletException, IOException {

        when(request.getRequestURI()).thenReturn("/health");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void devePassarDiretoSeRequisicaoNaoTiverToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/private");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void deveRetornarForbiddenSeTokenForInvalido() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/private");
        when(request.getHeader("Authorization")).thenReturn("Bearer token_invalido");
        when(jwtProvider.validateToken("token_invalido")).thenReturn(false);
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void deveRetornarForbiddenSeTokenForExpirado() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/private");
        when(request.getHeader("Authorization")).thenReturn("Bearer token_expirado");

        when(jwtProvider.validateToken("token_expirado")).thenReturn(true);
        when(jwtProvider.getClaims("token_expirado")).thenThrow(new ExpiredJwtException(null, null, "Expirado"));
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void deveAutenticarComSucessoSeTokenValido() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/private");
        when(request.getHeader("Authorization")).thenReturn("Bearer token_valido");
        when(jwtProvider.validateToken("token_valido")).thenReturn(true);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("admin@teste.com");
        when(claims.get("roles", List.class)).thenReturn(List.of("ADMIN", "USER"));
        when(jwtProvider.getClaims("token_valido")).thenReturn(claims);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("admin@teste.com", auth.getName());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void deveRetornarForbiddenQuandoOcorrerErroInternoNoFiltro() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/private");
        when(request.getHeader("Authorization")).thenReturn("Bearer token_valido");
        when(jwtProvider.validateToken("token_valido")).thenReturn(true);
        when(response.getWriter()).thenReturn(printWriter);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("admin@teste.com");
        when(claims.get("roles", List.class)).thenReturn(List.of());
        when(jwtProvider.getClaims("token_valido")).thenReturn(claims);

        doThrow(new ServletException("Erro forçado")).when(filterChain).doFilter(request, response);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}