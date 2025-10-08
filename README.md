# Marketplace-Academico

# 🎓 Universidade EUROPEIA - IADE
## Faculdade de Engenharia / Licenciatura em Engenharia Informática  
### Unidade Curricular: Projeto de Desenvolvimento Móvel  
### Docente: Pedro Rosa  

---

## 🧑‍💻 Grupo
- **Deodato Sebastião Luzayadio**

📍 **Local:** Lisboa  
📅 **Data:** 05 de Outubro de 2025  

---

## 🧾 Nome do Projeto
# **Marketplace Académico**

📂 **Repositório GitHub:** [https://github.com/DeodatoSebasti1/Marketplace-Academico](https://github.com/DeodatoSebasti1/Marketplace-Academico)

📄 **Versão PDF do Relatório:**  
O relatório completo em formato PDF encontra-se disponível no diretório do projeto sob o nome:  
`RELATORIO_OFFICIAL_RETIFICADO.pdf`

---

## 🔑 Palavras-Chave
- Mercado  
- Aplicativo móvel  
- Estudantes universitários  
- Compra e venda  
- Serviços académicos  

---

## 📱 Breve Descrição da Aplicação e Problema que Pretende Resolver

O **Marketplace Académico** é uma aplicação móvel para Android que permite a estudantes, docentes e funcionários de instituições académicas comprar e vender produtos ou serviços de forma simples, rápida e segura.

Os estudantes enfrentam frequentemente dificuldades para comercializar materiais ou serviços dentro do ambiente universitário, recorrendo a plataformas dispersas como grupos de WhatsApp ou murais físicos. Estas soluções apresentam problemas de desorganização, falta de confiança e ausência de mecanismos de pesquisa e filtragem.

O projeto propõe uma **plataforma centralizada e exclusiva para a comunidade académica**, que garante um ambiente confiável e intuitivo para troca de bens e serviços.

---

## 🎯 Objetivos e Motivação

### Objetivo Geral
Desenvolver uma aplicação móvel de marketplace para a comunidade académica, integrando backend Java (Spring Boot) e frontend Kotlin (Jetpack Compose), aplicando os princípios de MVC, REST e MVVM.

### Objetivos Específicos
- Implementar autenticação e gestão de utilizadores.  
- Permitir criação, edição e remoção de anúncios.  
- Disponibilizar listagem de produtos e serviços com filtros.  
- Criar sistema de chat entre comprador e vendedor.  
- Garantir interface responsiva e navegação intuitiva.  

### Motivação
Promover a economia colaborativa entre estudantes, incentivar a reutilização de materiais e aplicar na prática os conhecimentos adquiridos em **Programação Móvel** e **Programação Orientada a Objetos**.

---

## 👥 Público-Alvo

A aplicação destina-se principalmente a **estudantes universitários**, mas também pode ser utilizada por **docentes** e **funcionários**.

**Características:**
- Utilizadores com acesso predominante via smartphone.  
- Preferência por soluções rápidas, seguras e simples.  
- Interesse em comprar/vender produtos usados ou serviços académicos.  

---

## 🔍 Pesquisa de Mercado em Aplicações Semelhantes

Foram analisadas várias plataformas populares de marketplace:

| Aplicação | Descrição | Limitações |
|------------|------------|-------------|
| **OLX** | Grande alcance e variedade de categorias | Não é voltada ao contexto académico |
| **Facebook Marketplace** | Base de utilizadores ampla e chat integrado | Problemas de confiança e fraudes frequentes |
| **Vinted** | Especializado em roupa usada com segurança reforçada | Público restrito e foco em moda |
| **AliExpress** | Pesquisa avançada e avaliações | Foco em comércio global, não local |
| **eBay** | Sistema de leilões e proteção de transações | Complexo e genérico para o contexto académico |

O **Marketplace Académico** diferencia-se por ser **restrito à comunidade universitária**, proporcionando confiança, segurança e simplicidade de uso.

---

## 🧪 Guiões de Teste (Versão Preliminar)

### UC1 – Criar Anúncio e Contactar Comprador
**Ator:** Vendedor e comprador  
1. Vendedor entra no feed e clica em “+ Criar Anúncio”.  
2. Preenche título, descrição, preço e fotos.  
3. Publica o anúncio.  
4. Comprador visualiza no feed e contacta o vendedor via chat.  
**Resultado esperado:** Anúncio visível e chat estabelecido com sucesso.

---

### UC2 – Pesquisar e Filtrar Anúncios
**Ator:** Comprador  
1. Digita “Livro de Cálculo” na barra de pesquisa.  
2. Filtra por categoria “Livros” e preço 10–50€.  
3. Adiciona aos favoritos.  
**Resultado esperado:** Lista filtrada e item favoritado.

---

### UC3 – Editar e Excluir Anúncio
**Ator:** Vendedor  
1. Acede ao perfil e abre “Meus Anúncios”.  
2. Edita preço e descrição.  
3. Guarda alterações e elimina o anúncio.  
**Resultado esperado:** Anúncio atualizado e depois removido do feed.

---

## ⚙️ Descrição da Solução a Implementar

### i. Descrição Genérica
Aplicação composta por um **backend REST em Java (Spring Boot)** e uma **app Android em Kotlin (Jetpack Compose)**.  
A comunicação é feita via **JSON** sobre **HTTPS**.

---

### ii. Enquadramento nas Unidades Curriculares
| Unidade Curricular | Aplicação no Projeto |
|--------------------|----------------------|
| Programação Orientada a Objetos | Desenvolvimento do backend em Java com Spring Boot |
| Programação Móvel | Implementação da app Android com Kotlin e Jetpack Compose |
| Engenharia de Software | Aplicação dos padrões MVC, MVVM e REST |
| Bases de Dados | Modelação e integração com MySQL |
| IHC / UX Design | Criação de interface intuitiva e usabilidade |

---

### iii. Requisitos Técnicos (provisórios)
**Backend:**  
- Java 17  
- Spring Boot (MVC + REST)  
- Spring Data JPA + MySQL  
- Spring Security + JWT  
- Maven  

**Frontend:**  
- Kotlin + Jetpack Compose (Material3)  
- Retrofit, Coil, Hilt  
- MVVM + ViewModel + LiveData  
- Android Studio Koala 2024.1.2  

**Outros:**  
- GitHub (controlo de versões)  
- Figma (design)  
- Draw.io (diagramas)  
- Firebase Storage e Realtime Database (fotos e chat)  

---

### iv. Arquitetura da Solução

---

### v. Tecnologias a Utilizar
- **Backend:** Java, Spring Boot, JPA, MySQL, JWT  
- **Frontend:** Kotlin, Jetpack Compose, MVVM, Retrofit, Coil, Hilt  
- **Infraestrutura:** GitHub, Firebase, Figma, Draw.io  

---

## 🧩 Conclusão
O Marketplace Académico propõe uma solução moderna e segura para facilitar a compra e venda entre estudantes.  
Combina **Java + Spring Boot (backend)** e **Kotlin + Compose (frontend)**, respeitando os princípios de arquitetura e boas práticas de programação móvel e orientada a objetos.

---

## 📚 Referências
- Android Developers. (2025). *Android Developers: Official Documentation.* Google.  
  https://developer.android.com/docs  
- Firebase. (2025). *Firebase Documentation.* Google.  
  https://firebase.google.com/docs  
- Sommerville, I. (2016). *Software Engineering* (10th ed.). Pearson.  
- Pressman, R. S., & Maxim, B. R. (2014). *Software Engineering: A Practitioner’s Approach* (8th ed.). McGraw-Hill.  
- Nielsen, J. (1994). *Usability Engineering.* Morgan Kaufmann.  
- OLX. (2025). *OLX Portugal: Compra e Venda Online.* https://www.olx.pt  
- Wallapop. (2025). *Wallapop: Compra e Venda de Segunda Mão.* https://es.wallapop.com  
- Vinted. (2025). *Buy and Sell Pre-Loved Fashion.* https://www.vinted.com  
- Facebook Marketplace. (2025). *Meta Platforms.* https://www.facebook.com/marketplace  

---

🧾 **Nota:**  
Este documento em Markdown é a versão digital do relatório académico.  
A versão PDF atualizada deve ser anexada ao repositório e submetida no **Canvas** conforme as instruções do docente.

