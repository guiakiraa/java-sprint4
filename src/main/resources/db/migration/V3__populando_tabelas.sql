-- ENDEREÇOS
INSERT INTO ENDERECO (numero, cep, bairro, cidade, estado, complemento, logradouro)
VALUES
(101, '01001-000', 'Sé', 'São Paulo', 'SP', 'Edifício Central', 'Praça da Sé'),
(789, '20040-030', 'Centro', 'Rio de Janeiro', 'RJ', NULL, 'Av. Rio Branco'),
(321, '30130-005', 'Savassi', 'Belo Horizonte', 'MG', 'Loja 5', 'Rua Antônio de Albuquerque'),
(654, '90010-150', 'Centro Histórico', 'Porto Alegre', 'RS', 'Conjunto 2B', 'Rua da Praia'),
(987, '71000-000', 'Asa Sul', 'Brasília', 'DF', 'Bloco D', 'SQS 105'),
(1234, '80020-300', 'Centro', 'Curitiba', 'PR', 'Sala 10', 'Rua XV de Novembro'),
(567, '40020-000', 'Comércio', 'Salvador', 'BA', NULL, 'Av. Contorno'),
(890, '60115-170', 'Meireles', 'Fortaleza', 'CE', 'Andar 3', 'Av. Beira Mar'),
(112, '50010-240', 'Santo Antônio', 'Recife', 'PE', 'Sala 401', 'Rua do Sol'),
(335, '13013-010', 'Centro', 'Campinas', 'SP', 'Fundos', 'Rua Treze de Maio');

-- FILIAIS
INSERT INTO FILIAL (fk_endereco, nome)
VALUES
(1, 'Filial Sé'),
(2, 'Filial Centro RJ'),
(3, 'Filial Savassi'),
(4, 'Filial Porto Alegre'),
(5, 'Filial Brasília Sul'),
(6, 'Filial Curitiba Centro'),
(7, 'Filial Salvador Comércio'),
(8, 'Filial Fortaleza Beira Mar'),
(9, 'Filial Recife Antigo'),
(10, 'Filial Campinas Centro');

-- FUNCIONÁRIOS
INSERT INTO FUNCIONARIO (fk_filial, nome)
VALUES
(1, 'Ana Souza'),
(1, 'Bruno Costa'),
(2, 'Carla Lima'),
(3, 'Daniel Pereira'),
(4, 'Eduarda Santos'),
(5, 'Fernando Rodrigues'),
(6, 'Gabriela Fernandes'),
(7, 'Hugo Almeida'),
(8, 'Isabela Gomes'),
(9, 'Julio Martins');

-- MOTOS
INSERT INTO MOTO (ano, fk_filial, placa, modelo, tipo_combustivel)
VALUES
(2024, 1, 'GHI4J56', 'MOTTU_SPORT', 'Gasolina'),
(2023, 1, 'KLM7N89', 'MOTTU_POP', 'Eletrico'),
(2022, 2, 'OPQ1R23', 'MOTTU_E', 'Gasolina'),
(2024, 3, 'STU4V56', 'MOTTU_SPORT', 'Gasolina'),
(2023, 4, 'WXY7Z89', 'MOTTU_POP', 'Eletrico'),
(2021, 5, 'BCD1E23', 'MOTTU_E', 'Gasolina'),
(2024, 6, 'FGH4I56', 'MOTTU_SPORT', 'Gasolina'),
(2022, 7, 'JKL7M89', 'MOTTU_POP', 'Eletrico'),
(2023, 8, 'NAO0P12', 'MOTTU_E', 'Gasolina'),
(2024, 9, 'QRS3T45', 'MOTTU_SPORT', 'Gasolina');

-- LOCALIZAÇÕES
INSERT INTO LOCALIZACAO (pontox, pontoy, data_hora, fk_moto, fonte)
VALUES
(-46.633309, -23.550520, '2025-05-19 09:00:00', 1, 'GPS'),
(-46.640000, -23.560000, '2025-05-19 09:05:00', 1, 'GPS'),
(-43.172897, -22.906847, '2025-05-19 09:10:00', 3, 'Visao_Computacional'),
(-43.180000, -22.910000, '2025-05-19 09:15:00', 3, 'Sensor'),
(-47.882432, -15.794229, '2025-05-19 09:20:00', 5, 'GPS'),
(-47.890000, -15.800000, '2025-05-19 09:25:00', 5, 'Visao_Computacional'),
(-51.220000, -30.030000, '2025-05-19 09:30:00', 4, 'Sensor'),
(-51.230000, -30.040000, '2025-05-19 09:35:00', 4, 'GPS'),
(-38.520000, -3.730000, '2025-05-19 09:40:00', 8, 'Visao_Computacional'),
(-38.530000, -3.740000, '2025-05-19 09:45:00', 8, 'Sensor');

