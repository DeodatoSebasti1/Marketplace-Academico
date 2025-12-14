
---

# Documentação REST API – Mercado Académico

API REST para o sistema **Mercado Académico**, responsável pela gestão de utilizadores, produtos, categorias, favoritos, propostas, compras, uploads e pagamentos.

**Versão:** 1.0.0
**Última atualização:** Dezembro 2025

---

## Índice

1. Introdução
2. Autenticação
3. Utilizadores
4. Produtos
5. Categorias
6. Favoritos
7. Propostas
8. Compras
9. Upload de Imagens
10. Pagamentos
11. Códigos de Resposta

---

## 1. Introdução

A API Mercado Académico segue o estilo arquitetural REST e comunica através de JSON.
Alguns endpoints requerem autenticação via **JWT (JSON Web Token)**.

### Base URL (Android Emulator)

```
http://10.0.2.2:8080/api
```

---

## 2. Autenticação

### 2.1 Login

**Endpoint**

```
POST /login
```

**Headers**

```
Content-Type: application/json
```

**Body**

```json
{
  "email": "utilizador@email.com",
  "senhaHash": "password"
}
```

**Resposta (200 OK)**

```json
{
  "id": 2,
  "nome": "Utilizador",
  "email": "utilizador@email.com",
  "token": "jwt-token"
}
```

---

## 3. Utilizadores

### 3.1 Registar Utilizador

**Endpoint**

```
POST /usuarios/cadastrar
```

**Body**

```json
{
  "nome": "Nome",
  "email": "email@email.com",
  "telefone": "912345678",
  "senhaHash": "password"
}
```

---

### 3.2 Obter Perfil Autenticado

**Endpoint**

```
GET /usuarios/perfil
```

**Headers**

```
Authorization: Bearer <token>
```

**Resposta (200 OK)**

```json
{
  "id": 2,
  "nome": "Utilizador",
  "email": "email@email.com",
  "telefone": "912345678",
  "fotoPerfil": "url_imagem"
}
```

---

### 3.3 Editar Perfil

**Endpoint**

```
PUT /usuarios/editar
```

**Headers**

```
Authorization: Bearer <token>
```

**Body**

```json
{
  "nome": "Novo Nome",
  "telefone": "987654321",
  "fotoPerfil": "url_imagem"
}
```

---

## 4. Produtos

### 4.1 Criar Produto

**Endpoint**

```
POST /produtos
```

**Body**

```json
{
  "usuario": { "id": 2 },
  "nome": "Livro",
  "titulo": "Livro de Matemática",
  "descricao": "Livro em bom estado",
  "preco": 10.0,
  "categoria": { "idCategoria": 1 },
  "imagens": "img1.jpg,img2.jpg"
}
```

---

### 4.2 Listar Produtos

**Endpoint**

```
GET /produtos
```

**Resposta (200 OK)**

```json
[
  {
    "idProduto": 1,
    "titulo": "Livro de Matemática",
    "preco": 10.0,
    "status": "DISPONIVEL"
  }
]
```

---

### 4.3 Obter Produto por ID

**Endpoint**

```
GET /produtos/{id}
```

---

### 4.4 Atualizar Produto

**Endpoint**

```
PUT /produtos/{id}
```

**Body**

```json
{
  "titulo": "Novo título",
  "descricao": "Nova descrição",
  "preco": 12.0,
  "categoria": { "idCategoria": 2 }
}
```

---

### 4.5 Eliminar Produto

**Endpoint**

```
DELETE /produtos/{id}
```

---

## 5. Categorias

### 5.1 Listar Categorias

**Endpoint**

```
GET /categorias
```

**Resposta (200 OK)**

```json
[
  { "idCategoria": 1, "nome": "Livros" },
  { "idCategoria": 2, "nome": "Tecnologia" }
]
```

---

## 6. Favoritos

### 6.1 Adicionar Favorito

**Endpoint**

```
POST /favoritos/{userId}/{produtoId}
```

---

### 6.2 Remover Favorito

**Endpoint**

```
DELETE /favoritos/{userId}/{produtoId}
```

---

## 7. Propostas

### 7.1 Enviar Proposta

**Endpoint**

```
POST /propostas/enviar
```

**Parâmetros**

```
produtoId
compradorId
valor
```

---

### 7.2 Listar Propostas como Comprador

**Endpoint**

```
GET /propostas/comprador/{id}
```

---

### 7.3 Listar Propostas como Vendedor

**Endpoint**

```
GET /propostas/vendedor/{id}
```

---

### 7.4 Aceitar Proposta

**Endpoint**

```
PUT /propostas/{id}/aceitar
```

---

### 7.5 Recusar Proposta

**Endpoint**

```
PUT /propostas/{id}/recusar
```

---

### 7.6 Contraproposta

**Endpoint**

```
PUT /propostas/{id}/contrapropor
```

---

## 8. Compras

### 8.1 Minhas Compras

**Endpoint**

```
GET /compras
```

**Headers**

```
Authorization: Bearer <token>
```

---

### 8.2 Minhas Vendas

**Endpoint**

```
GET /compras/vendas
```

**Headers**

```
Authorization: Bearer <token>
```

---

## 9. Upload de Imagens

### 9.1 Upload

**Endpoint**

```
POST /uploads
```

**Form Data**

```
file: imagem
```

**Resposta**

```json
{
  "imageUrl": "http://localhost:8080/uploads/imagem.png"
}
```

---

## 10. Pagamentos

### 10.1 Criar Pagamento (Stripe)

**Endpoint**

```
POST /pagamento/stripe/create
```

**Body**

```json
{
  "amount": 1000
}
```

**Resposta**

```json
{
  "clientSecret": "stripe_secret"
}
```

---

## 11. Códigos de Resposta

| Código | Significado     |
| ------ | --------------- |
| 200    | OK              |
| 201    | Criado          |
| 400    | Pedido inválido |
| 401    | Não autorizado  |
| 404    | Não encontrado  |
| 500    | Erro interno    |

---

Esta documentação descreve os principais endpoints da API Mercado Académico.
A autenticação é realizada através de JWT e alguns endpoints requerem permissões específicas.

---


