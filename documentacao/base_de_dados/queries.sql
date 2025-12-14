-- QUERIES.SQL
USE banco_marketplace;

-- 1. Listar todos os produtos com o nome do vendedor
SELECT p.nome AS produto, u.nome AS vendedor, p.preco
FROM produtos p
JOIN usuarios u ON p.id_usuario = u.id_usuario;

-- 2. Mostrar as propostas de cada produto
SELECT pr.id_proposta, u.nome AS comprador, p.nome AS produto, pr.valor, pr.status
FROM propostas pr
JOIN usuarios u ON pr.id_usuario = u.id_usuario
JOIN produtos p ON pr.id_produto = p.id_produto;

-- 3. Listar mensagens associadas a uma proposta
SELECT m.id_mensagem, u.nome AS remetente, m.conteudo, m.enviado_em
FROM mensagens m
JOIN usuarios u ON m.id_usuario = u.id_usuario
WHERE m.id_proposta = 1;

-- 4. Contar quantos produtos cada utilizador tem
SELECT u.nome, COUNT(p.id_produto) AS total_produtos
FROM usuarios u
LEFT JOIN produtos p ON u.id_usuario = p.id_usuario
GROUP BY u.nome;

-- 5. Mostrar produtos favoritos de cada utilizador
SELECT u.nome AS utilizador, p.nome AS produto_favorito
FROM produtos_favoritos f
JOIN usuarios u ON f.id_usuario = u.id_usuario
JOIN produtos p ON f.id_produto = p.id_produto;
-- Fim do arquivo QUERIES.SQL