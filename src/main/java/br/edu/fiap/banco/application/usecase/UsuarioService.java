package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.UsuarioRequest;
import br.edu.fiap.banco.presenter.dto.UsuarioResponse;

/** Caso de uso da identidade de acesso. A implementação fica em {@link UsuarioServiceImpl}. */
public interface UsuarioService {

    UsuarioResponse cadastrarUsuario(UsuarioRequest request);
}
