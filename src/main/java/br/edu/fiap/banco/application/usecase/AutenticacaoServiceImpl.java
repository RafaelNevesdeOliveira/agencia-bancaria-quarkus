package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.LoginRequest;
import br.edu.fiap.banco.presenter.dto.TokenResponse;
import br.edu.fiap.banco.domain.model.Usuario;
import br.edu.fiap.banco.application.ports.UsuarioRepository;
import br.edu.fiap.banco.infrastructure.security.JwtService;
import br.edu.fiap.banco.infrastructure.security.SenhaBCrypt;
import br.edu.fiap.banco.application.exception.CredenciaisInvalidasException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AutenticacaoServiceImpl implements AutenticacaoService {
    private final UsuarioRepository usuarios;
    private final SenhaBCrypt senhas;
    private final JwtService token;

    @Inject
    public AutenticacaoServiceImpl(
            UsuarioRepository usuarios,
            SenhaBCrypt senhas,
            JwtService token
    ){
        this.usuarios = usuarios;
        this.senhas = senhas;
        this.token = token;
    }

    @Override
    public TokenResponse login(LoginRequest request){
        Usuario usuario = usuarios.buscaPorEmail(request.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if(!usuario.isAtivo() || !senhas.confere(request.senha(), usuario.getSenha())){
            throw new CredenciaisInvalidasException();
        }

        return token.emitir(usuario);
    }
}
