package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.Pessoa;
import br.edu.fiap.banco.exception.CpfJaCadastradoException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Armazenamento didático temporário: reiniciar a JVM apaga os cadastros.
 * A anotação @ApplicationScoped torna esta implementação injetável via CDI; no Spring,
 * @Repository registraria a implementação como bean.
 */
@ApplicationScoped
public class PessoaRepositoryEmMemoria implements PessoaRepository {
    private final ConcurrentHashMap<String, Pessoa> pessoasPorCpf = new ConcurrentHashMap<>();
    private final AtomicLong proximoId = new AtomicLong();

    @Override
    public boolean existePorCpf(String cpf) {
        return pessoasPorCpf.containsKey(cpf);
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        Pessoa criada = pessoa.comId(proximoId.incrementAndGet());
        Pessoa anterior = pessoasPorCpf.putIfAbsent(pessoa.cpf(), criada);
        if (anterior != null) {
            throw new CpfJaCadastradoException();
        }
        return criada;
    }
}
