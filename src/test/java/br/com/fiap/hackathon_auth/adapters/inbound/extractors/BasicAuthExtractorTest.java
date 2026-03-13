package br.com.fiap.hackathon_auth.adapters.inbound.extractors;

import br.com.fiap.hackathon_auth.adapters.inbound.dto.request.LoginRequestDTO;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthFormatException;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthHeaderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class BasicAuthExtractorTest {

    private BasicAuthExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new BasicAuthExtractor();
    }

    @Test
    void deveExtrairCredenciaisComSucesso() {

        String base64Credentials = Base64.getEncoder().encodeToString("joao:senha123".getBytes());
        String header = "Basic " + base64Credentials;

        LoginRequestDTO credentials = extractor.extractCredentials(header);

        assertNotNull(credentials);
        assertEquals("joao", credentials.getUsername());
        assertEquals("senha123", credentials.getPassword());
    }

    @Test
    void deveLancarExcecaoQuandoHeaderForNulo() {
        assertThrows(InvalidBasicAuthHeaderException.class, () -> extractor.extractCredentials(null));
    }

    @Test
    void deveLancarExcecaoQuandoHeaderNaoComecarComBasic() {
        assertThrows(InvalidBasicAuthHeaderException.class, () -> extractor.extractCredentials("Bearer token"));
    }

    @Test
    void deveLancarExcecaoQuandoBase64ForInvalido() {
        assertThrows(InvalidBasicAuthFormatException.class, () -> extractor.extractCredentials("Basic !@#$%"));
    }

    @Test
    void deveLancarExcecaoQuandoFaltarSeparadorDoisPontos() {
        String base64Credentials = Base64.getEncoder().encodeToString("joaosenha".getBytes());
        String header = "Basic " + base64Credentials;

        assertThrows(InvalidBasicAuthFormatException.class, () -> extractor.extractCredentials(header));
    }

    @Test
    void deveLancarExcecaoQuandoUsernameForVazio() {
        String base64Credentials = Base64.getEncoder().encodeToString(":senha123".getBytes());
        String header = "Basic " + base64Credentials;

        assertThrows(InvalidBasicAuthFormatException.class, () -> extractor.extractCredentials(header));
    }

    @Test
    void deveLancarExcecaoQuandoPasswordForVazio() {
        String base64Credentials = Base64.getEncoder().encodeToString("joao:".getBytes());
        String header = "Basic " + base64Credentials;

        assertThrows(InvalidBasicAuthFormatException.class, () -> extractor.extractCredentials(header));
    }
}