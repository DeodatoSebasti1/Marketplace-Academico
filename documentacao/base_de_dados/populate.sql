-- POPULATE.SQL
USE banco_marketplace;

INSERT INTO usuarios (nome, email, senha_hash) VALUES
('Deodato Sebastião Luzayadio', 'deolastro@gmail.com', 'deodato123'),
('Maria Almeida', 'maria@example.com', 'maria123'),
('João Silva', 'joao@example.com', 'joao123');

INSERT INTO categorias (nome) VALUES
('Tecnologia'),
('Serviços Académicos'),
('Material Escolar'),
('Livros');

INSERT INTO produtos (id_usuario, nome, descricao, preco) VALUES
(1, 'Aulas de Programação', 'Explicações particulares de Kotlin e Java', 25.00),
(2, 'Venda de Portátil Lenovo', 'Lenovo IdeaPad 3 usado, bom estado', 300.00),
(3, 'Livro de Cálculo I', 'Livro usado em boas condições', 15.00);

INSERT INTO produtos_categorias (id_produto, id_categoria) VALUES
(1, 2),
(2, 1),
(3, 4);

INSERT INTO propostas (id_produto, id_usuario, valor, status) VALUES
(2, 1, 280.00, 'pendente'),
(3, 2, 10.00, 'aceita');

INSERT INTO mensagens (id_proposta, id_usuario, conteudo) VALUES
(1, 1, 'Posso ver o portátil antes de comprar?'),
(2, 2, 'Envio amanhã de manhã.');

INSERT INTO produtos_favoritos (id_usuario, id_produto) VALUES
(1, 3),
(2, 1),
(3, 2);
-- Fim do arquivo POPULATE.SQL