package br.com.fiap.hackathon_auth.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @Test
    void deveRetornarTrueParaEndpointPublico() {
        assertTrue(SecurityUtils.isPublicEndpoint("/api/v1/users/register"));
        assertTrue(SecurityUtils.isPublicEndpoint("/health"));
        assertTrue(SecurityUtils.isPublicEndpoint("/swagger-ui/index.html"));
    }

    @Test
    void deveRetornarFalseParaEndpointPrivado() {
        assertFalse(SecurityUtils.isPublicEndpoint("/api/v1/produtos"));
        assertFalse(SecurityUtils.isPublicEndpoint("/api/v1/pagamentos/123"));
    }

    @Test
    void deveCobrirConstrutorPadrao() {
        SecurityUtils utils = new SecurityUtils();
        assertNotNull(utils);
    }
}