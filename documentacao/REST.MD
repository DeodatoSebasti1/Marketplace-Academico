


# 📘 API REST – Marketplace Académico

**Autor:** Deodato Sebastião Luzayadio, Jurandi Bendinha, Jeovani Antonio  
**Curso:** Engenharia Informática  
**Projeto:** Marketplace Académico  
**Tecnologias:** Spring Boot · MySQL · Kotlin · Retrofit · Jetpack Compose  


---

## 🌍 Base URL

```bash
http://localhost:8080/api
````

> 💡 No Android Emulator:
> `http://10.0.2.2:8080/api`

---

## 🧩 Entidade Principal: Produto

### Estrutura JSON

```json
{
  "idProduto": 1,
  "usuario": {
    "id": 1,
    "nome": "Deodato Sebastião Luzayadio",
    "email": "deolastro@gmail.com"
  },
  "nome": "Livro de Redes",
  "titulo": "Livro de Redes",
  "descricao": "Livro usado, em bom estado",
  "preco": 12.50,
  "categoria": "Material Escolar",
  "imagens": "foto123.jpg,foto456.jpg",
  "criadoEm": "2025-11-12T00:00:00"
}
```

---

## 🚀 Endpoints da API

### 1️⃣ Criar Produto

**POST** `/produtos`

#### Request Body

```json
{
  "usuario": { "id": 1 },
  "nome": "Livro de Redes",
  "titulo": "Livro de Redes",
  "descricao": "Usado mas em bom estado",
  "preco": 12.5,
  "categoria": "Material Escolar",
  "imagens": "foto123.jpg,foto456.jpg"
}
```

#### Response 200 OK

```json
{
  "message": "Produto criado com sucesso!"
}
```

#### Response 400 Bad Request

```json
{
  "error": "Usuário não encontrado"
}
```

---

### 2️⃣ Listar Todos os Produtos

**GET** `/produtos`

#### Response 200 OK

```json
[
  {
    "idProduto": 1,
    "usuario": { "id": 1, "nome": "Deodato", "email": "deolastro@gmail.com" },
    "nome": "Livro de Redes",
    "titulo": "Livro de Redes",
    "descricao": "Usado mas em bom estado",
    "preco": 12.5,
    "categoria": "Material Escolar",
    "imagens": "foto123.jpg"
  },
  {
    "idProduto": 2,
    "usuario": { "id": 1, "nome": "Deodato", "email": "deolastro@gmail.com" },
    "nome": "Calculadora",
    "titulo": "Calculadora Científica",
    "descricao": "TI-84 Plus",
    "preco": 25.0,
    "categoria": "Tecnologia e Acessórios",
    "imagens": "calc.jpg"
  }
]
```

---

### 3️⃣ Buscar Produto por ID

**GET** `/produtos/{id}`

#### Exemplo

```bash
GET /produtos/1
```

#### Response 200 OK

```json
{
  "idProduto": 1,
  "usuario": { "id": 1, "nome": "Deodato", "email": "deolastro@gmail.com" },
  "nome": "Livro de Redes",
  "titulo": "Livro de Redes",
  "descricao": "Usado mas em bom estado",
  "preco": 12.5,
  "categoria": "Material Escolar",
  "imagens": "foto123.jpg"
}
```

#### Response 404 Not Found

```json
{
  "error": "Produto não encontrado"
}
```

---

### 4️⃣ Upload de Imagem

**POST** `/uploads`

#### Tipo de envio

`multipart/form-data`

#### Parâmetros

| Campo  | Tipo            | Obrigatório | Descrição         |
| ------ | --------------- | ----------- | ----------------- |
| `file` | `MultipartFile` | ✅           | Arquivo de imagem |

#### Response 200 OK

```text
http://localhost:8080/uploads/img_1731420348293.jpg
```

#### Response 500 Internal Server Error

```json
{
  "error": "Erro ao salvar arquivo: Permissão negada"
}
```

---

## 👤 Entidade Usuário

### Estrutura JSON

```json
{
  "id": 1,
  "nome": "Deodato Sebastião Luzayadio",
  "email": "deolastro@gmail.com"
}
```

> ⚠️ A senha (`senhaHash`) é protegida com `@JsonProperty(WRITE_ONLY)` e **não é devolvida** nas respostas.

---

## 🔐 Autenticação

### Login de Usuário

**POST** `/login`

#### Request Body

```json
{
  "email": "deolastro@gmail.com",
  "senha": "123456"
}
```

#### Response 200 OK

```json
{
  "id": 1,
  "nome": "Deodato Sebastião Luzayadio",
  "email": "deolastro@gmail.com",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## ⚙️ Estrutura de Diretórios (Backend)

```
backend/
 ├─ src/
 │   ├─ main/java/iade/pt/backend/controllers/
 │   │   ├─ ProdutoController.java
 │   │   ├─ UploadController.java
 │   │   └─ UsuarioController.java
 │   ├─ main/java/iade/pt/backend/models/
 │   │   ├─ Produto.java
 │   │   └─ Usuario.java
 │   ├─ main/java/iade/pt/backend/repositories/
 │   │   ├─ ProdutoRepository.java
 │   │   └─ UsuarioRepository.java
 │   └─ main/resources/application.properties
 ├─ uploads/
 └─ pom.xml
```

---

## 🧾 Códigos de Status HTTP

| Código                      | Significado                | Exemplo                |
| --------------------------- | -------------------------- | ---------------------- |
| `200 OK`                    | Requisição bem-sucedida    | Produto listado        |
| `201 Created`               | Recurso criado com sucesso | Produto publicado      |
| `400 Bad Request`           | Erro de validação          | Usuário inexistente    |
| `404 Not Found`             | Recurso não encontrado     | Produto inexistente    |
| `500 Internal Server Error` | Erro interno do servidor   | Falha ao salvar imagem |

---

## 🧠 Tecnologias Utilizadas

| Camada             | Tecnologia                         |
| ------------------ | ---------------------------------- |
| Backend            | Spring Boot (Java 17)              |
| Banco de Dados     | MySQL                              |
| ORM                | JPA / Hibernate                    |
| Frontend           | Android (Kotlin + Jetpack Compose) |
| Cliente HTTP       | Retrofit                           |
| Upload de Arquivos | Multipart via `Spring MVC`         |
| Serialização JSON  | Gson                               |

---

## 📖 Swagger (Documentação Automática)

Para gerar uma interface interativa da API:

### Adicione no `pom.xml`

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

Depois de executar o projeto, aceda a:

```
http://localhost:8080/swagger-ui.html
```

---

