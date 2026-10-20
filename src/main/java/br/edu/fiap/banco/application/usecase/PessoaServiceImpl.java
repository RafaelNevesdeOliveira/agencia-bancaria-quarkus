package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.PessoaRequest;
import br.edu.fiap.banco.presenter.dto.PessoaResponse;
import br.edu.fiap.banco.domain.model.Pessoa;
import br.edu.fiap.banco.domain.exception.CpfJaCadastradoException;
import br.edu.fiap.banco.application.exception.PessoaNaoEncontradaException;
import br.edu.fiap.banco.application.ports.PessoaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Caso de uso: cadastrar um titular sem duplicar CPF.
 * A anotação CDI @ApplicationScoped cria um bean compartilhado da aplicação; no Spring,
 * a classe seria tipicamente marcada com @Service.
 */
@ApplicationScoped
public class PessoaServiceImpl implements PessoaService {
    private final PessoaRepository repository;

    // @Inject escolhe o construtor para CDI; corresponde à injeção por
    // construtor que o Spring também faz com @Autowired.
    @Inject
    public PessoaServiceImpl(PessoaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PessoaResponse cadastrar(PessoaRequest request) {
        if (repository.existePorCpf(request.cpf())) {
            throw new CpfJaCadastradoException();
        }
        Pessoa pessoa = new Pessoa(null, request.nome(), request.cpf(), request.email());
        return PessoaResponse.de(repository.salvar(pessoa));
    }

    @Override
    public PessoaResponse getPessoaById(Long id) {
        return repository.getPessoaById(id)
                .map(PessoaResponse::de)
                .orElseThrow(PessoaNaoEncontradaException::new);
    }

//    Substituir nome, cpf, email do titular
//    cpf soh é recusado quando ja pertence a outra pessoa

    @Override
    public PessoaResponse atualizar(Long id, PessoaRequest request){
        Pessoa pessoaAtual = repository.getPessoaById(id).orElseThrow(PessoaNaoEncontradaException::new);

        if(pessoaAtual.cpfAlterado(request.cpf()) && repository.existePorCpf(request.cpf())){
            throw new CpfJaCadastradoException();
        }

        Pessoa pessoa = pessoaAtual.atualizar(request.nome(), request.cpf(), request.email());

//        /api/pessoa/{3} --> ParamRequest
//        BodyRequest
//        {
//            "nome": "Raquel"
//            "cpf" "12312312312"
//            "email" "raquelmtorica@email.com"
//        }
        return  PessoaResponse.de(repository.atualizar(pessoa));
    }

    @Override
    public void excluir(Long id){
        if(!repository.excluir(id)){
            throw new PessoaNaoEncontradaException();
        }
        repository.excluir(id);
    }
}
