# Marketplace Académico

Licenciatura em Engenharia Informática | LEIF003D02 | 2025–2026  
Unidade Curricular: Projeto de Desenvolvimento Móvel  
Docente: Pedro Rosa  

Repositório:  
https://github.com/DeodatoSebasti1/Marketplace-Academico  

ClickUp: 
https://app.clickup.com/90151726092/v/o/f/901510316525?pr=90157139983

Figma:
https://www.figma.com/design/jAdwZAnjQbi05DygAsjtz5/PROTOTIPO-MERCADO-ACADEMICO?node-id=0-1&t=ynvgXQTczBW1MuJ8-1

Grupo: G11 – Marketplace Académico  

Autores:  
- Deodato Sebastião Luzayadio  
- Jeovani António  
- Jurandi Bendinha  

Local: Lisboa  
Data: 14 de dezembro de 2025  

---

## Índice

1. [Introdução](#introdução)  
2. [Objetivos](#objetivos)  
   1. [Objetivo Geral](#objetivo-geral)  
   2. [Objetivos Específicos](#objetivos-específicos)  
3. [Público-Alvo](#público-alvo)  
4. [Pesquisa de Mercado](#pesquisa-de-mercado)  
5. [Descrição da Solução Implementada](#descrição-da-solução-implementada)  
   1. [Descrição Geral da Solução](#descrição-geral-da-solução)  
   2. [Enquadramento nas Unidades Curriculares](#enquadramento-nas-unidades-curriculares)  
6. [Requisitos Técnicos](#requisitos-técnicos)  
7. [Arquitetura da Solução](#arquitetura-da-solução)  
8. [Tecnologias Utilizadas](#tecnologias-utilizadas)  
9. [Diagrama de Classes](#diagrama-de-classes)  
10. [Dicionário de Dados](#dicionário-de-dados)  
11. [Documentação REST](#documentação-rest)  
12. [Manual do Utilizador](#manual-do-utilizador)  
13. [Planeamento e Calendarização](#planeamento-e-calendarização)  
14. [Mudanças Implementadas](#mudanças-implementadas)  
15. [Quadro de Contribuições](#quadro-de-contribuições)  
16. [Conclusão](#conclusão)  
17. [Referências](#referências)  

---

## Introdução

No âmbito da unidade curricular de Projeto de Desenvolvimento Móvel, foi desenvolvido o projeto **Marketplace Académico**, uma aplicação móvel destinada à compra e venda de materiais académicos entre estudantes.

Este projeto surge como resposta a uma necessidade real identificada no contexto universitário, onde muitos estudantes possuem materiais que já não utilizam, enquanto outros procuram alternativas mais económicas para adquirir esses mesmos recursos.

Ao longo do semestre, o projeto foi evoluindo de forma incremental, permitindo a aplicação prática de conhecimentos adquiridos em programação, bases de dados, desenvolvimento mobile e organização de software.

---

## Objetivos

### Objetivo Geral

Desenvolver uma aplicação móvel funcional e intuitiva que permita a compra e venda de produtos académicos entre membros da comunidade universitária, recorrendo a uma arquitetura cliente-servidor baseada em API REST.

### Objetivos Específicos

- Criar um sistema de publicação e pesquisa de produtos académicos;  
- Permitir a negociação entre utilizadores através de propostas;  
- Registar o histórico de compras e vendas;  
- Integrar um sistema de pagamentos online;  

---

## Público-Alvo

O público-alvo da aplicação é constituído maioritariamente por estudantes do ensino superior, com idades entre os 18 e os 30 anos.

Os utilizadores possuem conhecimentos básicos de tecnologia, utilizam frequentemente dispositivos móveis e demonstram interesse na compra e venda de materiais académicos a preços acessíveis.

---

## Pesquisa de Mercado

Foram analisadas plataformas existentes como OLX, Facebook Marketplace e Vinted. Apesar de permitirem a compra e venda de produtos, estas plataformas são genéricas e não oferecem funcionalidades específicas para o contexto académico.

O Marketplace Académico diferencia-se por ser direcionado exclusivamente à comunidade universitária, oferecendo funcionalidades como propostas de negociação, mensagens associadas a produtos e histórico de transações.

---

## Descrição da Solução Implementada

### Descrição Geral da Solução

A solução implementada consiste numa aplicação Android baseada numa arquitetura cliente-servidor. O frontend móvel comunica com um backend através de uma API REST, permitindo a gestão de utilizadores, produtos, categorias, propostas, mensagens e transações.

### Enquadramento nas Unidades Curriculares

O projeto integrou conhecimentos das seguintes unidades curriculares:

- Programação Orientada a Objetos: modelação das entidades e estrutura do backend;  
- Programação de Dispositivos Móveis: desenvolvimento da aplicação Android;  
- Matemática Discreta: definição de regras lógicas e estados do sistema;  
- Competências Comunicacionais: elaboração da documentação técnica.  

---

## Requisitos Técnicos

Para o desenvolvimento do projeto foram utilizados:

- Android Studio;  
- Linguagem Kotlin;  
- Jetpack Compose;  
- Base de dados relacional MySQL;  
- API REST;  
- Integração com Stripe para pagamentos;  
- Integração com Brevo para envio de emails;  
- Ligação à internet.  

---

## Arquitetura da Solução

A arquitetura adotada segue o modelo cliente-servidor.  
O frontend móvel é responsável pela interface e interação com o utilizador, enquanto o backend gere a lógica de negócio e a persistência de dados.

A comunicação é realizada através de pedidos HTTP para uma API REST, utilizando o formato JSON.

---

## Tecnologias Utilizadas

- Kotlin  
- Android Jetpack Compose  
- MySQL  
- Spring Boot  
- API REST  
- Retrofit  
- Stripe API  

---

## Diagrama de Classes

O diagrama de classes representa as principais entidades do sistema, nomeadamente Utilizador, Produto, Categoria, Proposta e Mensagem, bem como as relações entre elas.

O diagrama completo encontra-se disponível no repositório GitHub.

---

## Dicionário de Dados

O dicionário de dados descreve as entidades da base de dados, os seus atributos e tipos de dados.  
As principais tabelas incluem utilizadores, produtos, categorias, propostas e mensagens.

O documento completo encontra-se disponível no repositório.

---

## Documentação REST

A documentação REST descreve todos os endpoints da API, incluindo métodos, parâmetros e respostas.

---

## Manual do Utilizador

O Manual do Utilizador descreve passo a passo a utilização da aplicação, desde o registo até à realização de compras, incluindo capturas de ecrã.

---

## Planeamento e Calendarização

O projeto foi desenvolvido ao longo do semestre, seguindo uma abordagem incremental.  
As tarefas foram distribuídas entre os elementos do grupo e ajustadas conforme a evolução do projeto.
---

## Mudanças Implementadas

Algumas funcionalidades previstas inicialmente, como a utilização de localização geográfica, não foram implementadas devido a limitações técnicas e de tempo.

Por outro lado, foram adicionadas funcionalidades como o sistema de propostas, mensagens internas, histórico de compras e vendas e integração de pagamentos através da Stripe API.

---

## Quadro de Contribuições

O projeto foi desenvolvido de forma colaborativa, sendo coordenado por **Deodato Sebastião Luzayadio**, que assumiu a maior responsabilidade técnica.

| Tarefa | Deodato | Jurandi | Jeovani |
|------|--------|--------|--------|
| Análise e requisitos | 60% | 20% | 20% |
| Planeamento | 60% | 20% | 20% |
| Base de dados | 70% | 15% | 15% |
| Backend | 70% | 15% | 15% |
| Frontend | 70% | 15% | 15% |
| Integração | 65% | 20% | 15% |
| Testes | 60% | 20% | 20% |
| Documentação | 80% | 10% | 10% |

---

## Conclusão

O desenvolvimento do projeto Marketplace Académico permitiu aplicar, de forma prática, os conhecimentos adquiridos ao longo do curso.

Apesar das dificuldades encontradas, o resultado final cumpre os objetivos definidos e apresenta potencial para futuras melhorias e expansão de funcionalidades.

---

## Referências

Android Developers. (2025). Android Developers: Official Documentation.  
https://developer.android.com/docs  

Nielsen, J., & Budiu, R. (2012). Mobile usability. New Riders.  

Sommerville, I. (2016). Software Engineering (10th ed.). Pearson.  

Pressman, R. S., & Maxim, B. R. (2014). Software Engineering: A Practitioner’s Approach. McGraw-Hill.  

Vinted. (2025). https://www.vinted.com  

OLX, Wallapop.


