# Agência Bancária em Quarkus — gabarito incremental do Dia 1

## Aula 1 — ambiente e primeiro projeto Quarkus

### 1. Conferir Java e Maven

Use **Temurin JDK 25.0.2**, **Maven 3.9.9** e **Quarkus 3.40.1**. O Maven
precisa usar o mesmo JDK que o terminal e a IDE. O POM compila com
`maven.compiler.release=25`: o número 25 é o nível do bytecode Java, enquanto
25.0.2 é a atualização do JDK instalada na máquina.

No macOS, abra um terminal novo e execute:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 25.0.2)
export PATH="$JAVA_HOME/bin:$PATH"
java -version
javac -version
mvn -version
```

No Windows PowerShell, substitua o caminho de exemplo pela pasta real do JDK:

```powershell
$env:JAVA_HOME = 'C:\caminho\real\jdk-25.0.2'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version
javac -version
mvn -version
```

`java` e `javac` devem indicar **25.0.2**; `mvn -version` deve mostrar Maven e
Java **25.0.2**. Se mostrar outro Java, corrija `JAVA_HOME` e reabra o terminal
da IDE. Para instalação, use os sites oficiais do
[Temurin](https://adoptium.net/temurin/releases/?version=25) e do
[Maven](https://maven.apache.org/install.html).

### 2. Escolher o ponto de partida

Para fazer o desafio, extraia `DESAFIO_INICIO_CODIGO.zip` da pasta `AULA_01` e
abra `agencia-bancaria-quarkus` na IDE. Esse ZIP já contém o projeto inicial;
pule o comando de geração abaixo. Se preferir o GitHub:

```sh
git clone https://github.com/RafaelNevesdeOliveira/api-quarkus.git
cd api-quarkus
git switch aluno-inicio
```

Para **criar outro projeto do zero**, execute o comando em uma pasta de trabalho
que ainda não contenha `agencia-bancaria-quarkus`:

```sh
mvn io.quarkus.platform:quarkus-maven-plugin:3.40.1:create \
  -DprojectGroupId=br.edu.fiap \
  -DprojectArtifactId=agencia-bancaria-quarkus \
  -DprojectVersion=1.0.0-SNAPSHOT \
  -Dextensions=rest-jackson,hibernate-validator \
  -DjavaVersion=25 \
  -DnoCode
cd agencia-bancaria-quarkus
```

No Windows PowerShell, use uma linha; as opções `-D` ficam entre aspas:

```powershell
mvn io.quarkus.platform:quarkus-maven-plugin:3.40.1:create "-DprojectGroupId=br.edu.fiap" "-DprojectArtifactId=agencia-bancaria-quarkus" "-DprojectVersion=1.0.0-SNAPSHOT" "-Dextensions=rest-jackson,hibernate-validator" "-DjavaVersion=25" "-DnoCode"
cd agencia-bancaria-quarkus
```

`rest-jackson` prepara REST e JSON; `hibernate-validator` prepara validação;
`-DnoCode` deixa a API bancária para o desafio. Confira no `pom.xml` a
plataforma `3.40.1` e `<maven.compiler.release>25</maven.compiler.release>`.
O gerador inclui exemplos em `src/main/docker` e `.dockerignore`: apague esses
arquivos na IDE. Eles não fazem parte das aulas.

### 3. Iniciar e construir na JVM

No projeto recém-gerado, coloque esta linha em
`src/main/resources/application.properties`. No ZIP inicial, confira o arquivo
e acrescente a linha se ele estiver vazio:

```properties
quarkus.http.port=8081
```

Na pasta que contém o `pom.xml`, execute:

```sh
mvn quarkus:dev
```

O log deve indicar a aplicação JVM em `http://localhost:8081`. Pare com
`Ctrl+C`. No projeto recém-gerado ou no ZIP inicial, `/api/pessoas` ainda
responde **404**; o recurso será criado durante o desafio. No gabarito ele já
está implementado. Quando o cadastro estiver pronto, execute:

```sh
mvn test package
java -jar target/quarkus-app/quarkus-run.jar
```

O pacote JVM usa toda a pasta `target/quarkus-app`, não apenas o JAR. Na aula 1
os dados ficam em memória; não é preciso iniciar PostgreSQL. O curso não usa
Docker nem compilação nativa.

| No projeto Spring | Nesta aula em Quarkus |
|---|---|
| `mvn spring-boot:run` | `mvn quarkus:dev` |
| `server.port=8081` | `quarkus.http.port=8081` |
| `@RestController` e `@PostMapping` | `@Path` e `@POST` no `PessoaResource` |

Consulte o [guia oficial de início do Quarkus](https://quarkus.io/guides/getting-started/)
para os comandos de desenvolvimento e empacotamento JVM.

Reconstrução do cadastro de Pessoa do projeto Spring `projeto-agencia-bancaria-gabarito`.
Este primeiro estágio usa memória para isolar REST, JSON, validação, CDI e camadas.
Os dados se perdem ao reiniciar. PostgreSQL entra no Dia 2, em instância didática
isolada; JPA, BCrypt e JWT entram depois.

## Stack validada

- Quarkus 3.40.1, Maven 3.9.9 e Temurin JDK 25.0.2.
- `quarkus-rest-jackson`, `quarkus-hibernate-validator`, `quarkus-arc`.
- Testes: `quarkus-junit` e RestAssured.
- Sem datasource, Dev Services, containers ou build nativo.

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
