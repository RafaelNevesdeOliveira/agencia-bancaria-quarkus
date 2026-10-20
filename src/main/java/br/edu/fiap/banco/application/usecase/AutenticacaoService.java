package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.LoginRequest;
import br.edu.fiap.banco.presenter.dto.TokenResponse;

/** Caso de uso de login. A implementação fica em {@link AutenticacaoServiceImpl}. */
public interface AutenticacaoService {

    TokenResponse login(LoginRequest request);
}
