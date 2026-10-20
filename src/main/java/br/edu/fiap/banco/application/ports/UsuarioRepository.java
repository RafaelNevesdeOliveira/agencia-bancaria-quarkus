package br.edu.fiap.banco.application.ports;

import br.edu.fiap.banco.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {

    boolean existePorEmail(String email);

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscaPorEmail(String email);
}
