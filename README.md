# Mercado Académico

**Universidade Europeia – IADE**  
Faculdade de Engenharia  
Licenciatura em Engenharia Informática  

**Unidade Curricular:** Projeto de Desenvolvimento Móvel  
**Docente:** Pedro Rosa  


---

## Grupo D01-G11

- **Deodato Sebastião Luzayadio**  
- **Jeovani António**  
- **Jurandi Bendinha**  

**Local:** Lisboa  
Ultima Atualização do Relatorio: **14 dezembro 2025**

---

## Ligações Importantes

- **Figma (Protótipo):**  
  https://www.figma.com/proto/jAdwZAnjQbi05DygAsjtz5/PROTOTIPO-MERCADO-ACADEMICO?node-id=0-1&t=SjjzxkPePQZK1Gzl-1  

- **ClickUp (Gestão do Projeto):**  
  https://app.clickup.com/90151726092/v/s/90157139983  

- **Repositório GitHub:**  
  https://github.com/DeodatoSebasti1/Marketplace-Academico  

---

##  Nome do Projeto

**Mercado Académico**

---

## Palavras-Chave

- Mercado  
- Aplicativo móvel  
- Estudantes universitários  
- Compra e venda  
- Serviços académicos  

---

## Descrição da Aplicação e Problema a Resolver

Os estudantes universitários, docentes e funcionários enfrentam dificuldades na compra e venda de produtos e serviços dentro do ambiente académico. Atualmente, esses processos ocorrem de forma desorganizada, recorrendo a:

- Grupos de WhatsApp ou murais físicos, que são dispersos e pouco confiáveis;  
- Ausência de mecanismos eficazes de pesquisa e filtragem;  
- Risco de fraudes e má comunicação entre comprador e vendedor, devido à inexistência de um ambiente seguro e exclusivo.

Esses fatores resultam em perda de tempo, insegurança e frustração para os utilizadores.

O **Marketplace Académico** surge como uma solução centralizada, segura e dedicada à comunidade universitária.

---

## Objetivos e Motivação

### Objetivo Geral

Desenvolver uma aplicação móvel de marketplace direcionada à comunidade académica, integrando um backend em **Java (Spring Boot)** e um frontend em **Kotlin (Jetpack Compose)**, aplicando os princípios de **MVC, REST e MVVM**.

### Objetivos Específicos

- Implementar autenticação e gestão de utilizadores;  
- Permitir criação, edição e remoção de anúncios;  
- Disponibilizar listagem de produtos e serviços com filtros;  
- Criar sistema de comunicação (chat) entre comprador e vendedor;  
- Garantir interface responsiva e navegação intuitiva.  

### Motivação

Promover a economia colaborativa entre estudantes, incentivar a reutilização de materiais académicos e aplicar, na prática, os conhecimentos adquiridos nas unidades curriculares de **Programação Móvel** e **Programação Orientada a Objetos**.

---

## Público-Alvo

A aplicação destina-se principalmente a:

- Estudantes universitários;  
- Docentes;  
- Funcionários académicos.  

**Características principais:**

- Utilização predominante via smartphone;  
- Preferência por soluções rápidas, simples e seguras;  
- Interesse na compra e venda de produtos usados e serviços académicos.

---

## Pesquisa de Mercado

Foram analisadas aplicações de marketplace amplamente utilizadas:

| Aplicação | Descrição | Limitações |
|---------|----------|-----------|
| OLX | Marketplace generalista | Não é focado no contexto académico |
| Facebook Marketplace | Grande base de utilizadores e chat integrado | Falta de controlo e problemas de confiança |
| Vinted | Segurança reforçada | Público restrito e foco em moda |
| AliExpress | Pesquisa avançada | Comércio global, não local |
| eBay | Sistema de leilões | Complexidade excessiva |

O **Marketplace Académico** diferencia-se por ser restrito à comunidade universitária, oferecendo maior confiança, segurança e adequação ao contexto académico.

---

## Guiões de Teste 

### UC1 – Criar Anúncio e Contactar Vendedor

**Ator:** Vendedor e Comprador  

1. O vendedor acede ao feed e seleciona “+ Criar Anúncio”;  
2. Preenche título, descrição, preço e imagens;  
3. Publica o anúncio;  
4. O comprador visualiza o anúncio e inicia contacto via chat.  

**Resultado Esperado:**  
Anúncio publicado com sucesso e canal de comunicação ativo.

---

### UC2 – Pesquisar e Filtrar Anúncios

**Ator:** Comprador  

1. Pesquisa por “Livro de Cálculo”;  
2. Aplica filtros por categoria e intervalo de preço;  
3. Adiciona anúncio aos favoritos.  

**Resultado Esperado:**  
Lista filtrada corretamente e item favoritado.

---

### UC3 – Editar e Excluir Anúncio

**Ator:** Vendedor  

1. Acede a “Meus Anúncios”;  
2. Edita preço e descrição;  
3. Guarda alterações;  
4. Remove o anúncio.  

**Resultado Esperado:**  
Anúncio atualizado e posteriormente removido do feed.

---

## Descrição da Solução

### i. Descrição Geral

A solução é composta por:

- Backend REST desenvolvido em **Java com Spring Boot**;  
- Aplicação Android desenvolvida em **Kotlin com Jetpack Compose**;  
- Comunicação via **JSON sobre HTTPS**.

---

### ii. Enquadramento nas Unidades Curriculares

| Unidade Curricular | Aplicação no Projeto |
|-------------------|---------------------|
| Programação Orientada a Objetos | Backend em Java com Spring Boot |
| Programação Móvel | App Android com Kotlin e Compose |
| Engenharia de Software | Padrões MVC, MVVM e REST |
| Bases de Dados | Modelação e integração com MySQL |
| IHC / UX Design | Interface intuitiva e usável |

---

### iii. Requisitos Técnicos

**Backend:**
- Java 17  
- Spring Boot (MVC + REST)  
- Spring Data JPA  
- MySQL  
- Spring Security + JWT  
- Maven  

**Frontend:**
- Kotlin  
- Jetpack Compose (Material 3)  
- Retrofit  
- Coil  
- Hilt  
- Arquitetura MVVM  

**Outros:**
- GitHub  
- Figma  
- Draw.io  

---

### iv. Arquitetura da Solução

Arquitetura cliente-servidor baseada em API REST, separando claramente frontend, backend e persistência de dados.

---

## Conclusão

O **Marketplace Académico** apresenta uma solução moderna, segura e eficiente para a compra e venda de produtos e serviços no contexto universitário.

O projeto integra tecnologias atuais, respeita boas práticas de engenharia de software e consolida conhecimentos adquiridos ao longo do curso, nomeadamente em **Programação Móvel**, **Programação Orientada a Objetos** e **Arquitetura de Software**.

---

## Referências

- Android Developers. (2025). *Official Documentation*. Google.  
  https://developer.android.com/docs  

- Sommerville, I. (2016). *Software Engineering* (10th ed.). Pearson.  

- Pressman, R. S., & Maxim, B. R. (2014). *Software Engineering: A Practitioner’s Approach* (8th ed.). McGraw-Hill.  

- Nielsen, J. (1994). *Usability Engineering*. Morgan Kaufmann.  

- OLX Portugal. (2025). https://www.olx.pt  
- Vinted. (2025). https://www.vinted.com  
- Facebook Marketplace. (2025). https://www.facebook.com/marketplace  

---

## Nota Final

Este documento em **Markdown** representa a versão digital do relatório académico.  
A versão **PDF** deve ser gerada a partir deste conteúdo e submetida no **Canvas**, conforme as instruções do docente.


🧾 **Nota:**  
Este documento em Markdown é a versão digital do relatório académico.  
A versão PDF atualizada deve ser anexada ao repositório e submetida no **Canvas** conforme as instruções do docente.

