package br.com.fiap.hackathon_auth.adapters.configuration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtPropertiesTest {

    @Test
    void deveTestarGettersESetters() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("meu-segredin");
        properties.setAccessTokenExpiration(3600000L);
        properties.setRefreshTokenExpiration(7200000L);

        assertEquals("meu-segredin", properties.getSecret());
        assertEquals(3600000L, properties.getAccessTokenExpiration());
        assertEquals(7200000L, properties.getRefreshTokenExpiration());
    }
}