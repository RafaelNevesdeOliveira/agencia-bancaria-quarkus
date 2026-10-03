# Agência Bancária em Quarkus — gabarito incremental do Dia 1

Reconstrução do cadastro de Pessoa do projeto Spring `projeto-agencia-bancaria-gabarito`.
Este primeiro estágio usa memória para isolar REST, JSON, validação, CDI e camadas.
Os dados se perdem ao reiniciar. PostgreSQL entra no Dia 2, em instância didática
isolada; JPA, BCrypt e JWT entram depois.

## Stack validada

- Quarkus 3.40.1, Maven 3.9.9 e Temurin JDK 17.0.18.
- `quarkus-rest-jackson`, `quarkus-hibernate-validator`, `quarkus-arc`.
- Testes: `quarkus-junit` e RestAssured.
- Sem datasource, Dev Services, containers ou build nativo.

## Executar e empacotar em JVM

```sh
java -version
mvn -version
mvn quarkus:dev
```

Em outro terminal, use `requests.http` ou um cliente HTTP. Para encerrar o modo
dev, use Ctrl+C. Depois:

```sh
mvn test package
java -jar target/quarkus-app/quarkus-run.jar
```

O fast-jar precisa da pasta `target/quarkus-app` completa. A API usa a porta
8081 para manter o endereço do projeto Spring original.

## Contrato preservado

`POST /api/pessoas` recebe `nome`, `cpf` (11 dígitos) e `email` válido.
Devolve 201, `Location: /api/pessoas/{id}` e
`PessoaResponse(id, nome, cpf, email)`. Entrada inválida devolve 400; CPF
repetido devolve 409. `PessoaRequest` contém as mesmas anotações Jakarta de
validação usadas no projeto Spring.

## Onde está cada responsabilidade

| Classe | Papel |
|---|---|
| `controller/PessoaResource` | HTTP e status; corresponde a `PessoaController` no Spring. |
| `dto/PessoaRequest`, `PessoaResponse` | Contrato JSON de entrada e saída. |
| `service/PessoaService` | Caso de uso e verificação de CPF único. |
| `repository/PessoaRepository` | Fronteira para armazenamento; JDBC a implementará no Dia 2. |
| `repository/PessoaRepositoryEmMemoria` | Implementação temporária com estrutura concorrente. |
| `entity/Pessoa` | Modelo Java, ainda sem anotações JPA. |

O Quarkus não impõe esses pacotes. A organização reproduz a separação de
responsabilidades que os alunos já conhecem do projeto Spring. Em REST, o
JSON de `PessoaResponse` é a representação entregue ao cliente; não há tela
HTML na “View” deste exemplo.

## Verificação executada

`mvn test package` passou com quatro testes HTTP: cadastro 201 com Location,
entrada inválida 400, corpo JSON nulo 400 e CPF duplicado 409. O teste inicia Quarkus localmente
em perfil de teste, sem PostgreSQL e sem containers.
