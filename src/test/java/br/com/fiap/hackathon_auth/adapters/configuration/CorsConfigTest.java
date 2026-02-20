package br.com.fiap.hackathon_auth.adapters.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void deveConfigurarCorsComCabecalhoCoringa() {

        ReflectionTestUtils.setField(corsConfig, "allowedOrigins", "http://localhost:3000,http://meusite.com");
        ReflectionTestUtils.setField(corsConfig, "allowedMethods", "GET,POST,PUT");

        ReflectionTestUtils.setField(corsConfig, "allowedHeaders", "*");
        ReflectionTestUtils.setField(corsConfig, "allowCredentials", true);

        UrlBasedCorsConfigurationSource source = (UrlBasedCorsConfigurationSource) corsConfig.corsConfigurationSource();

        CorsConfiguration config = source.getCorsConfigurations().get("/**");

        assertNotNull(config);
        assertEquals(2, config.getAllowedOrigins().size());
        assertTrue(config.getAllowedOrigins().contains("http://localhost:3000"));

        assertEquals(3, config.getAllowedMethods().size());

        assertEquals(1, config.getAllowedHeaders().size());
        assertTrue(config.getAllowedHeaders().contains("*"));

        assertTrue(config.getAllowCredentials());

        assertTrue(config.getExposedHeaders().contains("Authorization"));
    }

    @Test
    void deveConfigurarCorsComCabecalhosEspecificos() {

        ReflectionTestUtils.setField(corsConfig, "allowedOrigins", "*");
        ReflectionTestUtils.setField(corsConfig, "allowedMethods", "GET,OPTIONS");

        ReflectionTestUtils.setField(corsConfig, "allowedHeaders", "Authorization,Content-Type");
        ReflectionTestUtils.setField(corsConfig, "allowCredentials", false);

        UrlBasedCorsConfigurationSource source = (UrlBasedCorsConfigurationSource) corsConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfigurations().get("/**");

        assertNotNull(config);
        assertEquals(1, config.getAllowedOrigins().size());
        assertEquals(2, config.getAllowedMethods().size());

        assertEquals(2, config.getAllowedHeaders().size());
        assertTrue(config.getAllowedHeaders().contains("Authorization"));
        assertTrue(config.getAllowedHeaders().contains("Content-Type"));

        assertFalse(config.getAllowCredentials());
    }
}