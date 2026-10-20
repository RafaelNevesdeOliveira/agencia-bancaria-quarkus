package br.edu.fiap.banco.infrastructure.repository.rest;

import br.edu.fiap.banco.application.ports.UsuarioRepository;
import br.edu.fiap.banco.domain.model.Usuario;
import br.edu.fiap.banco.domain.exception.EmailJaCadastradoException;
import br.edu.fiap.banco.infrastructure.exception.FalhaInfraestruturaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepositoryJpa implements UsuarioRepository {
    private static final String BUSCA_POR_EMAIL = """
        SELECT usuario FROM Usuario usuario
        WHERE lower(usuario.email) = lower(:email)
    """;

    private final EntityManager entityManager;

    @Inject
    public UsuarioRepositoryJpa(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public boolean existePorEmail(String email){
        return buscaPorEmail(email).isPresent();
    }

    @Override
    @Transactional
    public Optional<Usuario> buscaPorEmail(String email){
        return entityManager.createQuery(BUSCA_POR_EMAIL, Usuario.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .map(Usuario::paraDominio);
    }

    @Override
    @Transactional
    public Usuario salvar(Usuario usuario){
        // No JDBC o ISERT lista colunas e usa aquele SETSTRING
        //PERSISTE copia o objeto mapeado
        Usuario entity = Usuario.de(usuario);

        try{
            entityManager.persist(entity);

            //Flush força o InSERT, No JDBC isso é o executeUpdate
            // O identity só devolve o id depous que o banco executa o insert
            entityManager.flush();
            return entity.paraDominio();

        }catch(RuntimeException erro){
            if(violouChaveUnica(erro)){
                throw new EmailJaCadastradoException();
            }
            throw new FalhaInfraestruturaException("Falhou", erro);
        }
    }

    private boolean violouChaveUnica(Throwable erro){
        Throwable atual = erro;
        while(atual != null){
            if(atual instanceof SQLException sql && "23505".equals(sql.getSQLState())){
                return true;
            }
            atual = atual.getCause();
        }
        return false;
    }

}