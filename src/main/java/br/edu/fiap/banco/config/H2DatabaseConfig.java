package br.edu.fiap.banco.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import java.util.Optional;

/**
 * Contrato do datasource lido pelo Agroal.
 *
 * <p>Os valores ficam em {@code application.properties}. No perfil {@code h2}
 * e nos testes, {@code db-kind} é {@code h2} e a URL aponta para um banco em
 * memória. No Spring, o equivalente é {@code application-h2.properties} com
 * {@code spring.datasource.*}; aqui {@code @ConfigMapping} substitui a classe
 * {@code @Configuration} que só republicaria essas propriedades.</p>
 */
@ConfigMapping(prefix = "quarkus.datasource")
public interface H2DatabaseConfig {

    String dbKind();

    String username();

    Optional<String> password();

    Jdbc jdbc();

    Devservices devservices();

    interface Jdbc {
        String url();
    }

    interface Devservices {
        @WithDefault("false")
        boolean enabled();
    }
}
