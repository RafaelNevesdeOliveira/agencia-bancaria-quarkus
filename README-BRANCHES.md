# Um projeto, cinco branches de gabarito

Este repositório didático é independente do Spring original. Os commits formam
uma linha cumulativa: `aluno-inicio` → `dia-01` → `dia-02` →
`dia-03` → `dia-04` → `dia-05`. Os nomes são exatos. As branches
`dia-01` a `dia-05` contêm **respostas do professor**. O bundle com
todas elas é material do professor e não deve ser enviado à turma.

| Aula | Base do desafio sem resposta do próprio dia | Gabarito ao final |
|---|---|---|
| Dia 1 | `aluno-inicio` | `dia-01` |
| Dia 2 | `dia-01` | `dia-02` |
| Dia 3 | `dia-02` | `dia-03` |
| Dia 4 | `dia-03` | `dia-04` |
| Dia 5 | `dia-04` | `dia-05` |

O professor pode distribuir à turma apenas o ZIP do estado inicial daquele
dia, exportado da branch da coluna do meio. Um ZIP não contém as branches
posteriores. No Dia 1, `aluno-inicio` é o projeto vazio gerado pelo
plugin Maven Quarkus 3.40.1, com REST Jackson e Hibernate Validator.

## Abrir o histórico local do professor

```sh
git clone agencia-bancaria-quarkus-professor.bundle agencia-bancaria-quarkus
cd agencia-bancaria-quarkus
git branch --list
git switch dia-01
git log --oneline --all --graph --decorate
```

Para preparar o desafio seguinte, parta da branch do dia anterior, por
exemplo `git switch dia-02` antes da aula 3. Para trabalhar sem alterar o
gabarito, crie sua própria branch com `git switch -c meu-trabalho-dia-03`.
Antes de trocar de branch, confira `git status --short` e registre seu
trabalho em um commit local. Se houver mudanças não registradas, conclua
o commit ou use `git stash push -u` e depois `git stash pop` na branch
correta. Uma troca de branch bloqueada pelo Git indica arquivos que seriam
sobrescritos; não force a troca.

Não há remoto, publicação ou push neste material.
