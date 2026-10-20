package br.edu.fiap.banco.infrastructure.security;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SenhaBCrypt {
    private final int custo;

    public SenhaBCrypt(@ConfigProperty(name = "security.password.bcrypt-cost") int custo){
        this.custo = custo;
    }

    //gera salt e hash BCrypt para senha reebida no cadastro
    public String criarHash(String senha){
        return BcryptUtil.bcryptHash(senha, custo);
    }

    //Comparar a tentativa em texto com o hash persistido sem descriptografar
    public boolean confere(String senha, String hash){
        return BcryptUtil.matches(senha, hash);
    }

}
