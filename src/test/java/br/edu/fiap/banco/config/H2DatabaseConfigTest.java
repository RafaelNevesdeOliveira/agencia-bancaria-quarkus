package br.edu.fiap.banco.config;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class H2DatabaseConfigTest {

    @Inject
    H2DatabaseConfig config;

    @Test
    void perfilDeTesteUsaH2EmMemoria() {
        assertEquals("h2", config.dbKind());
        assertEquals("sa", config.username());
        assertTrue(config.password().isEmpty());
        assertFalse(config.devservices().enabled());
        assertTrue(config.jdbc().url().startsWith("jdbc:h2:mem:agencia_bancaria_test"));
    }
}
