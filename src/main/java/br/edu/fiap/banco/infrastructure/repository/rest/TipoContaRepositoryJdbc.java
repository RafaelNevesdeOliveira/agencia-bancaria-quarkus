package br.edu.fiap.banco.infrastructure.repository.rest;

import br.edu.fiap.banco.application.ports.TipoContaRepository;
import br.edu.fiap.banco.domain.model.TipoConta;
import br.edu.fiap.banco.domain.exception.TipoContaEmUsoException;
import br.edu.fiap.banco.domain.exception.TipoContaJaCadastradoException;
import br.edu.fiap.banco.infrastructure.exception.FalhaInfraestruturaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação JDBC da camada de persistência de tipos de conta.
 * Expõe SQL, parâmetros, ResultSet e SQLSTATE para fins didáticos, mantendo
 * esses detalhes fora do service.
 */
@ApplicationScoped
public class TipoContaRepositoryJdbc implements TipoContaRepository {
    private final DataSource dataSource;

    /** Recebe o pool de conexões criado pelo Quarkus. */
    @Inject
    public TipoContaRepositoryJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Verifica o nome ignorando diferenças entre letras maiúsculas e minúsculas. */
    @Override
    public boolean existePorNome(String nome) {
        String sql = "SELECT 1 FROM tipos_conta WHERE UPPER(nome) = UPPER(?)";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw infraestrutura("consultar", e); }
    }

    /** Insere o tipo e recupera a chave gerada pela coluna IDENTITY. */
    @Override
    public TipoConta salvar(TipoConta tipo) {
        String sql = "INSERT INTO tipos_conta (nome) VALUES (?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo.nome());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new FalhaInfraestruturaException("INSERT não devolveu o id.");
                return tipo.comId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) throw new TipoContaJaCadastradoException();
            throw infraestrutura("salvar", e);
        }
    }

    /** Percorre o ResultSet e converte cada linha em uma entidade. */
    @Override
    public List<TipoConta> listar() {
        String sql = "SELECT id, nome FROM tipos_conta ORDER BY nome";
        List<TipoConta> tipos = new ArrayList<>();
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tipos.add(mapear(rs));
            return tipos;
        } catch (SQLException e) { throw infraestrutura("listar", e); }
    }

    /** Busca por chave primária e representa ausência com Optional.empty. */
    @Override
    public Optional<TipoConta> buscarPorId(Long id) {
        String sql = "SELECT id, nome FROM tipos_conta WHERE id = ?";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? Optional.of(mapear(rs)) : Optional.empty(); }
        } catch (SQLException e) { throw infraestrutura("buscar", e); }
    }

    /** Atualiza o nome e traduz violação UNIQUE em conflito de negócio. */
    @Override
    public TipoConta atualizar(TipoConta tipo) {
        String sql = "UPDATE tipos_conta SET nome = ? WHERE id = ?";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tipo.nome());
            ps.setLong(2, tipo.id());
            ps.executeUpdate();
            return tipo;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) throw new TipoContaJaCadastradoException();
            throw infraestrutura("atualizar", e);
        }
    }

    /**
     * Exclui pelo ID. SQLSTATE 23503 indica que uma conta ainda referencia o
     * tipo e, por isso, a operação vira conflito em vez de erro técnico.
     */
    @Override
    public boolean excluir(Long id) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM tipos_conta WHERE id = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) throw new TipoContaEmUsoException();
            throw infraestrutura("excluir", e);
        }
    }

    /** Centraliza a conversão de uma linha SQL para o modelo Java. */
    private TipoConta mapear(ResultSet rs) throws SQLException {
        return new TipoConta(rs.getLong("id"), rs.getString("nome"));
    }

    /** Preserva a causa técnica para diagnóstico sem expô-la no contrato HTTP. */
    private FalhaInfraestruturaException infraestrutura(String acao, SQLException e) {
        return new FalhaInfraestruturaException("Falha ao " + acao + " tipo de conta.", e);
    }
}
