package br.edu.fiap.banco.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

// @QuarkusTest inicia o contexto Quarkus e HTTP para o teste; no Spring,
// @SpringBootTest com RestAssured/WebTestClient cumpre papel semelhante.
@QuarkusTest
class PessoaResourceTest {
    @Test
    void cadastraPessoaComMesmoContratoDoSpring() {
        given().contentType(ContentType.JSON)
                .body("""
                        {"nome":"Mariana Costa","cpf":"11111111111","email":"mariana@example.test"}
                        """)
                .when().post("/api/pessoas")
                .then().statusCode(201)
                .header("Location", matchesPattern(".*/api/pessoas/\\d+"))
                .body("nome", equalTo("Mariana Costa"))
                .body("cpf", equalTo("11111111111"))
                .body("email", equalTo("mariana@example.test"));
    }

    @Test
    void rejeitaEmailInvalido() {
        given().contentType(ContentType.JSON)
                .body("""
                        {"nome":"Teste","cpf":"22222222222","email":"email-invalido"}
                        """)
                .when().post("/api/pessoas")
                .then().statusCode(400);
    }

    @Test
    void rejeitaCorpoNulo() {
        given().contentType(ContentType.JSON)
                .body("null")
                .when().post("/api/pessoas")
                .then().statusCode(400);
    }

    @Test
    void rejeitaCpfDuplicado() {
        String body = """
                {"nome":"Teste","cpf":"33333333333","email":"teste@example.test"}
                """;
        given().contentType(ContentType.JSON).body(body).post("/api/pessoas").then().statusCode(201);
        given().contentType(ContentType.JSON).body(body).post("/api/pessoas")
                .then().statusCode(409).body("mensagem", equalTo("CPF já cadastrado."));
    }
}
