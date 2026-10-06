package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.Pessoa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;

@ApplicationScoped
public class PessoaRepositoryImpl implements PessoaRepository{
    private final DataSource dataSource;

    @Inject
    public PessoaRepositoryImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public boolean existePorCpf(String cpf){
        String sqlQuery = "SELECT 1 FROM pessoas Where cpf = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, cpf);
            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next();
            }
        }catch(SQLException err){
            throw new IllegalStateException("Falha ao consultar pessoas.", err);
        }
    }

    @Override
    public Pessoa salvar(Pessoa pessoa){
        String sql = "INSERT INTO pessoas (nome, cpf, email) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, pessoa.nome());
            statement.setString(2, pessoa.cpf());
            statement.setString(3, pessoa.email());
            statement.executeUpdate();
            try (ResultSet resultado = statement.getGeneratedKeys()) {
                if(!resultado.next()){
                    throw new IllegalStateException("O INSERT não devolveu o ID da Pessoa.");
                }
                return pessoa.comId(resultado.getLong("id"));
            }
        }catch(SQLException err){
            throw new IllegalStateException("Falha ao persistir pessoa.", err);
        }
    }
}
