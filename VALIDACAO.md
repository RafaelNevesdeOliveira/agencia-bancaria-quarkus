# Validação do gabarito Dia 1

Executado em 2 de outubro de 2026 no Mac da tarefa, com JDK Temurin 17.0.18,
Maven 3.9.9 e Quarkus 3.40.1.

Comando: `mvn -o -B -q -Dmaven.repo.local=<cache-isolado-da-tarefa> test package`.

Resultado: build concluído e quatro testes HTTP passaram, sem falhas nem erros:
201 com `Location`, 400 para e-mail inválido, 400 para corpo JSON nulo e 409
para CPF repetido. O teste `@QuarkusTest` iniciou HTTP local temporariamente;
nenhum banco, Docker ou container foi iniciado. O pacote JVM foi criado em
`target/quarkus-app`, que não acompanha este ZIP de código-fonte.

Não foram executados testes do projeto Spring original nem testes com PostgreSQL.
