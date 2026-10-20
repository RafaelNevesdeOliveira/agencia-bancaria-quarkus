package br.edu.fiap.banco.infrastructure.security;

import br.edu.fiap.banco.presenter.dto.TokenResponse;
import br.edu.fiap.banco.domain.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.smallrye.jwt.build.Jwt;
import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class JwtService {
    public static final String PAPEL_USUARIO = "usuario";

    private final String segredo;
    private final String emissor;
    private final long duracao;

   public JwtService(
           @ConfigProperty(name = "security.jwt.secret") String segredo,
           @ConfigProperty(name = "security.jwt.issuer") String emissor,
           @ConfigProperty(name = "security.jwt.expiration-seconds") long duracao){
       this.segredo = segredo;
       this.emissor = emissor;
       this.duracao = duracao;
   }

   public TokenResponse emitir(Usuario usuario){
       long agora = Instant.now().getEpochSecond();
       long expiracao = agora + duracao;

       String token = Jwt.subject(usuario.getEmail())
               .issuer(emissor)
               .issuedAt(agora)
               .expiresAt(expiracao)
               .claim("usuarioId", usuario.getId())
               .claim("nome", usuario.getNome())
               .groups(Set.of(PAPEL_USUARIO, usuario.getEmail()))
               .signWithSecret(segredo);
       return new TokenResponse(token, "Bearer", duracao, expiracao);
   }
}
