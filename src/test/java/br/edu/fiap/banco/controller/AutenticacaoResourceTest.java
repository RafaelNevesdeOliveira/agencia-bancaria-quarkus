package br.edu.fiap.banco.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@QuarkusTest
class AutenticacaoResourceTest {

    @Test
    void retornaUsuarioAutenticadoComBearerEmitidoNoLogin() {
        String email = "ana.me@example.test";
        given().contentType(ContentType.JSON)
                .body("""
                        {"nome":"Ana Lima","email":"%s","senha":"Senha@123"}
                        """.formatted(email))
                .when().post("/api/usuarios")
                .then().statusCode(201);

        String token = given().contentType(ContentType.JSON)
                .body("""
                        {"email":"%s","senha":"Senha@123"}
                        """.formatted(email))
                .when().post("/api/auth/login")
                .then().statusCode(200)
                .extract().path("accessToken");

        given().header("Authorization", "Bearer " + token)
                .when().get("/api/auth/me")
                .then().statusCode(200)
                .body("email", equalTo(email))
                .body("nome", equalTo("Ana Lima"))
                .body("papeis", hasItem("usuario"));
    }

    @Test
    void rejeitaMeSemToken() {
        given().when().get("/api/auth/me")
                .then().statusCode(401);
    }
}
