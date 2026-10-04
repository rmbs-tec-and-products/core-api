# Banco de dados — SysOdonto

Este diretório guarda os scripts SQL manuais do `core-api`.

## Banco novo / Aurora MySQL

Para criar um ambiente novo do zero, execute somente:

```
00_schema_completo.sql
```

Esse arquivo representa o schema atual completo da aplicação.

## Banco já existente

Os scripts numerados seguintes registram as alterações incrementais que fizemos durante o desenvolvimento:

- `01_create_usuario.sql`
- `02_add_email_usuario.sql`
- `03_financeiro_contas_origem_caixa.sql`
- `04_create_prontuario_arquivo.sql`

Não execute os scripts incrementais depois de `00_schema_completo.sql` em um banco novo, pois as alterações já estão incluídas no schema completo.

## Regra daqui para frente

Sempre que uma alteração estrutural for necessária no banco:

1. criar um novo script numerado neste diretório;
2. aplicar o script no ambiente desejado;
3. atualizar também `00_schema_completo.sql` para que ele continue representando um banco novo completo.

O Hibernate permanece com `ddl-auto=validate`: a aplicação valida o schema, mas não deve criar ou alterar tabelas automaticamente em produção.
