package br.com.fiap.hackathon_auth.domain;

import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthFormatException;
import br.com.fiap.hackathon_auth.domain.exceptions.InvalidBasicAuthHeaderException;
import br.com.fiap.hackathon_auth.domain.user.EmailAlreadyExistsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidCredentialsException;
import br.com.fiap.hackathon_auth.domain.user.InvalidUserDataException;
import br.com.fiap.hackathon_auth.domain.user.UserNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionsTest {

    @Test
    void deveCobrirConstrutoresComCausa() {

        Throwable causa = new RuntimeException("Erro original");

        UserNotFoundException ex1 = new UserNotFoundException("Erro", causa);
        InvalidCredentialsException ex2 = new InvalidCredentialsException("Erro", causa);
        EmailAlreadyExistsException ex3 = new EmailAlreadyExistsException("Erro", causa);
        InvalidUserDataException ex4 = new InvalidUserDataException("Erro", causa);
        InvalidBasicAuthHeaderException ex5 = new InvalidBasicAuthHeaderException("Erro", causa);
        InvalidBasicAuthFormatException ex6 = new InvalidBasicAuthFormatException("Erro", causa);

        assertEquals(causa, ex1.getCause());
        assertEquals(causa, ex2.getCause());
        assertEquals(causa, ex3.getCause());
        assertEquals(causa, ex4.getCause());
        assertEquals(causa, ex5.getCause());
        assertEquals(causa, ex6.getCause());
    }
}