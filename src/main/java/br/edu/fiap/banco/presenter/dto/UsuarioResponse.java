package br.edu.fiap.banco.presenter.dto;

import br.edu.fiap.banco.domain.model.Usuario;

/**
 * DTO público de usuário. A ausência da senha neste record impede que o hash
 * BCrypt seja serializado no JSON por engano.
 */
public record UsuarioResponse(Long id, String nome, String email, boolean ativo) {
    /** Copia somente os campos seguros da entidade para a resposta. */
    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.isAtivo());
    }
}
