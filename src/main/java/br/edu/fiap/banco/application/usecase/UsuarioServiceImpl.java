package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.UsuarioRequest;
import br.edu.fiap.banco.presenter.dto.UsuarioResponse;
import br.edu.fiap.banco.domain.model.Usuario;
import br.edu.fiap.banco.domain.exception.EmailJaCadastradoException;
import br.edu.fiap.banco.application.ports.UsuarioRepository;
import br.edu.fiap.banco.infrastructure.security.SenhaBCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuario;
    private final SenhaBCrypt senhas;

    @Inject
    public UsuarioServiceImpl(UsuarioRepository usuarios, SenhaBCrypt senhas){
        this.usuario = usuarios;
        this.senhas = senhas;
    }

    @Override
    public UsuarioResponse cadastrarUsuario(UsuarioRequest request){
        if(usuario.existePorEmail(request.email())){
            throw new EmailJaCadastradoException();
        }

        String hash = senhas.criarHash(request.senha());

        Usuario novoUsuario = Usuario.novo(request.nome(), request.email(), hash);

        return UsuarioResponse.de(usuario.salvar(novoUsuario));
    }
}
