package br.edu.fiap.banco.infrastructure.repository.rest;

import br.edu.fiap.banco.application.ports.ContaBancariaRepository;
import br.edu.fiap.banco.domain.model.ContaBancaria;
import br.edu.fiap.banco.domain.model.Pessoa;
import br.edu.fiap.banco.domain.model.TipoConta;
import br.edu.fiap.banco.domain.exception.ContaJaCadastradaException;
import br.edu.fiap.banco.infrastructure.exception.FalhaInfraestruturaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ContaBancariaRepositoryImpl implements ContaBancariaRepository {

    private static final String SELECT_BASE = """
            SELECT c.id, c.agencia, c.numero, c.saldo, c.ativa,
                   p.id AS pessoa_id, p.nome AS pessoa_nome, p.cpf, p.email,
                   t.id AS tipo_id, t.nome AS tipo_nome
                FROM contas_bancarias c
                JOIN pessoas p ON p.id = c.titular_id
                JOIN tipos_conta t ON t.id = c.tipo_conta_id
            """;
    private final DataSource dataSource;

    @Inject
    public ContaBancariaRepositoryImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }


//    "DELETE FROM pessoas Where id = ?";
    @Override
    public boolean existePorAgenciaENumero(String agencia, String numero){
        String sql = "SELECT 1 FROM contas_bancarias WHERE agencia = ? AND numero = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, agencia);
            statement.setString(2, numero);

            try(ResultSet resultSet = statement.executeQuery()){
                return resultSet.next();
            }
        }catch(SQLException err){
            throw new FalhaInfraestruturaException("Falha ao encontrar conta bancaria.", err);
        }

    }

    @Override
    public List<ContaBancaria> listar(){
        return consultarLista(SELECT_BASE + "ORDER BY c.id", null);
    }

    @Override
    public List<ContaBancaria> listarPorTitular(Long titularId){
        return consultarLista(SELECT_BASE + "WHERE c.titular_id = ? ORDER BY c.id", titularId);
    }

    @Override
    public Optional<ContaBancaria> buscarPorId(Long id) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " WHERE c.id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw infraestrutura("buscar", e);
        }
    }

    @Override
    public ContaBancaria atualizar(ContaBancaria conta) {
        String sql = "UPDATE contas_bancarias SET agencia = ?, numero = ?, tipo_conta_id = ? WHERE id = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, conta.agencia());
            ps.setString(2, conta.numero());
            ps.setLong(3, conta.tipoConta().id());
            ps.setLong(4, conta.id());
            ps.executeUpdate();
            return conta;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new ContaJaCadastradaException();
            }
            throw infraestrutura("atualizar", e);
        }
    }

    @Override
    public boolean encerrar(Long id) {
        String sql = "UPDATE contas_bancarias SET ativa = FALSE WHERE id = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw infraestrutura("encerrar", e);
        }
    }


    @Override
    public ContaBancaria salvar(ContaBancaria conta){
        String sql = "INSERT INTO contas_bancarias (agencia, numero, saldo, ativa, titular_id, tipo_conta_id) VALUES (?, ?, ?, ?, ? ,?)";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS))
        {
            montarObjeto(statement, conta);

            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()){
                if(!rs.next()){
                    throw new FalhaInfraestruturaException("INSERT não devolveu id da conta");
                }
                return conta.comId(rs.getLong("id"));
            }

        }catch(SQLException err){
            // SQLSTATE 23505 é uma violação de unicidade no POSTGRESQL
            if("23505".equals(err.getSQLState())){
                throw new ContaJaCadastradaException();
            }
            throw new FalhaInfraestruturaException("Falha ao salvar conta(s) bancaria.", err);
        }
    }


    private void montarObjeto(PreparedStatement ps, ContaBancaria conta) throws SQLException{
        ps.setString(1, conta.agencia());
        ps.setString(2, conta.numero());
        ps.setBigDecimal(3, conta.saldo());
        ps.setBoolean(4, conta.ativa());
        ps.setLong(5, conta.titular().id());
        ps.setLong(6, conta.tipoConta().id());
    }


//    Centralizar o padrão de listagem para evitar duplicação do retorno do nosso ResultSet

    private List<ContaBancaria> consultarLista(String sql, Long parametro){
        List<ContaBancaria> contas = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql))
        {

            if(parametro != null){
                statement.setLong(1, parametro);
            }

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    contas.add(mapear(resultSet));
                }
            }

            return contas;
        }catch(SQLException err){
            throw new FalhaInfraestruturaException("Falha ao encontrar conta(s) bancaria.", err);
        }
    }


    private ContaBancaria mapear(ResultSet resultSet) throws SQLException{
        Pessoa pessoa = new Pessoa(
                resultSet.getLong("pessoa_id"),
                resultSet.getString("pessoa_nome"),
                resultSet.getString("cpf"),
                resultSet.getString("email")
        );
        TipoConta tipo = new TipoConta(
                resultSet.getLong("tipo_id"),
                resultSet.getString("tipo_nome")
        );
        return new ContaBancaria(
                resultSet.getLong("id"),
                resultSet.getString("agencia"),
                resultSet.getString("numero"),
                resultSet.getBigDecimal("saldo"),
                resultSet.getBoolean("ativa"),
                pessoa,
                tipo
        );
    }

    private FalhaInfraestruturaException infraestrutura(String acao, SQLException e) {
        return new FalhaInfraestruturaException("Falha ao " + acao + " conta bancária.", e);
    }
}
