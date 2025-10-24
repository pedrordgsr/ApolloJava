-- ===============================
-- 🧍 TABELA: pessoa
-- ===============================
INSERT INTO pessoa (
    id_pessoa, bairro, categoria, cep, cidade, cpfcnpj, data_cadastro, email,
    endereco, ie, nome, status, telefone, tipo_pessoa, uf
) VALUES
      (1, 'Centro', 'CLIENTE', '87507100', 'Cascavel', '12345678900', NOW(), 'joao@gmail.com',
       'Rua das Flores, 123', NULL, 'João Silva', 'ATIVO', '4440028922', 'FISICA', 'PR'),
      (2, 'Centro', 'FORNECEDOR', '87507100', 'Cascavel', '11222333000188', NOW(), 'fornecealimentos@gmail.com',
       'Av. Brasil, 45', '123456789', 'Fornece Alimentos LTDA', 'ATIVO', '4440028922', 'JURIDICA', 'PR'),
      (3, 'São Cristóvão', 'FUNCIONARIO', '87507100', 'Cascavel', '98765432100', NOW(), 'maria@mercadinho.com',
       'Rua Projetada, 78', NULL, 'Maria Souza', 'ATIVO', '4440028922', 'FISICA', 'PR'),
      (4, 'Santa Cruz', 'CLIENTE', '87507100', 'Cascavel', '11122233344', NOW(), 'ana@gmail.com',
       'Rua Paraná, 55', NULL, 'Ana Costa', 'ATIVO', '4440028922', 'FISICA', 'PR')

-- ===============================
-- 👩 TABELA: cliente
-- ===============================
INSERT INTO cliente (genero, id_pessoa)
VALUES
    ('MASCULINO', 1),
    ('FEMININO', 4);

-- ===============================
-- 🏢 TABELA: fornecedor
-- ===============================
INSERT INTO fornecedor (tipo_fornecedor, id_pessoa)
VALUES
    ('Alimentos e Bebidas', 2);

-- ===============================
-- 👷 TABELA: funcionario
-- ===============================
INSERT INTO funcionario (cargo, data_admissao, data_demissao, salario, id_pessoa)
VALUES
    ('Caixa', '2023-01-15', NULL, 1800.00, 3);

-- ===============================
-- 🧂 TABELA: produto
-- ===============================
INSERT INTO produto (id, descricao, nome, preco_custo, preco_venda, qntd_estoque, status)
VALUES
    (1, 'Arroz branco tipo 1 5kg', 'Arroz Tio João 5kg', 18.00, 25.00, 100, 'ATIVO'),
    (2, 'Feijão carioca 1kg', 'Feijão Camil 1kg', 6.50, 9.00, 150, 'ATIVO'),
    (3, 'Refrigerante 2L', 'Coca-Cola 2L', 6.00, 9.50, 80, 'ATIVO'),
    (4, 'Sabão em pó 1kg', 'Omo Lavagem Perfeita 1kg', 9.00, 13.50, 60, 'ATIVO');

-- ===============================
-- 🧾 TABELA: pedido
-- ===============================
INSERT INTO pedido (
    id_pedido, data_emissao, forma_pagamento, status, tipo,
    total_custo, total_venda, vencimento, id_funcionario, id_pessoa
) VALUES
      (1, NOW(), 'Dinheiro', 'FATURADO', 'VENDA', 30.50, 43.50, NULL, 3, 1),
      (2, NOW(), 'Cartão de Crédito', 'ORCAMENTO', 'VENDA', 12.00, 18.50, NULL, 3, 4),
      (3, NOW(), 'Boleto', 'FATURADO', 'COMPRA', 1200.00, 0, NOW() + INTERVAL '15 day', 3, 2);

-- ===============================
-- 📦 TABELA: pedido_produto
-- ===============================
INSERT INTO pedido_produto (id, preco_custoun, preco_vendaun, qntd, pedido_id, produto_id)
VALUES
    (1, 18.00, 25.00, 1, 1, 1),
    (2, 6.50, 9.00, 1, 1, 2),
    (3, 6.00, 9.50, 1, 2, 3),
    (4, 9.00, 13.50, 1, 2, 4),
    (5, 18.00, 0.00, 50, 3, 1),
    (6, 6.50, 0.00, 100, 3, 2),
    (7, 6.00, 0.00, 80, 3, 3);
