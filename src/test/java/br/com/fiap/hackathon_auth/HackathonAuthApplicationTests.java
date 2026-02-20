package br.com.fiap.hackathon_auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HackathonAuthApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void deveChamarMetodoMain() {

        try {
            HackathonAuthApplication.main(new String[]{});
        } catch (Exception e) {

        }
    }
}