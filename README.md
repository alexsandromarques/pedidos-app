# Sistema de Pedidos - Frontend (Java Swing)

Este é o cliente desktop da aplicação de pedidos, desenvolvido com Java Swing. Ele permite que usuários cadastrem pedidos e acompanhem seu status de forma automática, consumindo os serviços REST fornecidos pelo backend em Spring Boot.

## Funcionalidades

- Cadastro de novos pedidos (produto + quantidade).
- Exibição em tempo real do status dos pedidos.
- Atualização automática a cada 5 segundos.
- Status possíveis: `ENVIADO`, `AGUARDANDO_PROCESSO`, `SUCESSO` e `FALHA`.

## Tecnologias Utilizadas

- Java 17
- Swing (GUI)
- Java HTTP Client (Java 11+)
- Jackson (para manipulação JSON)