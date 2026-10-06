package br.edu.fiap.banco.config;

import io.quarkus.smallrye.openapi.OpenApiFilter;
import java.util.List;
import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.info.Contact;
import org.eclipse.microprofile.openapi.models.info.Info;
import org.eclipse.microprofile.openapi.models.info.License;
import org.eclipse.microprofile.openapi.models.tags.Tag;

/**
 * Metadados exibidos pelo Swagger.
 *
 * <p>No Spring, a classe {@code @Configuration} usa {@code @OpenAPIDefinition}.
 * Nesta versão do Quarkus, o SmallRye só lê essa anotação em
 * {@code package-info}. O filtro abaixo é o equivalente na camada
 * {@code config} e entra no documento gerado na compilação.</p>
 */
@OpenApiFilter(stages = OpenApiFilter.RunStage.BUILD)
public class OpenApiConfig implements OASFilter {

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        Contact contato = OASFactory.createContact();
        contato.setName("FIAP");

        License licenca = OASFactory.createLicense();
        licenca.setName("Uso didático");

        Info info = OASFactory.createInfo();
        info.setTitle("API Agência Bancária");
        info.setVersion("1.0.0");
        info.setDescription("Cadastro de titulares. O perfil h2 sobe um H2 em memória "
                + "para praticar sem instalar o PostgreSQL.");
        info.setContact(contato);
        info.setLicense(licenca);
        openAPI.setInfo(info);

        Tag pessoas = OASFactory.createTag();
        pessoas.setName("Pessoas");
        pessoas.setDescription("Cadastro dos titulares das contas");
        openAPI.setTags(List.of(pessoas));
    }
}
