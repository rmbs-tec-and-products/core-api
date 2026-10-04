CREATE DATABASE IF NOT EXISTS sysodonto
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sysodonto;

CREATE TABLE fornecedor (
    codigo INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(25) NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE paciente (
    codigo INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(300) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    celular VARCHAR(15) NOT NULL,
    cpf VARCHAR(15) NULL,
    datanascimento DATETIME(6) NULL,
    sexo VARCHAR(10) NULL,
    endereco VARCHAR(200) NULL,
    numero VARCHAR(20) NULL,
    cep VARCHAR(10) NULL,
    cidade VARCHAR(100) NULL,
    estado VARCHAR(2) NULL,
    bairro VARCHAR(50) NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE dentista (
    codigo INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE procedimento (
    codigo INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(200) NOT NULL,
    valor DECIMAL(19,4) NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE caixa (
    codigo INT NOT NULL AUTO_INCREMENT,
    dtabertura DATETIME(6) NOT NULL,
    dtfechamento DATETIME(6) NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE configuracao (
    id INT NOT NULL AUTO_INCREMENT,
    caminho VARCHAR(100) NOT NULL,
    tempo INT NULL,
    data DATETIME(6) NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE conta (
    codigo INT NOT NULL AUTO_INCREMENT,
    tipo INT NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    formapagamento VARCHAR(100) NOT NULL,
    valor DECIMAL(19,4) NOT NULL,
    dtpagamento DATETIME(6) NULL,
    dtvencimento DATETIME(6) NOT NULL,
    observacao TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    recorrente BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (codigo),
    INDEX idx_conta_vencimento (dtvencimento),
    INDEX idx_conta_tipo_status (tipo, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuario (
    codigo INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(80) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha_hash VARCHAR(100) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME(6) NOT NULL,
    atualizado_em DATETIME(6) NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT uk_usuario_login UNIQUE (login),
    CONSTRAINT uk_usuario_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE produto (
    codigo INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    quantidade INT NOT NULL,
    unidade VARCHAR(100) NOT NULL,
    embalagem INT NULL,
    qtdembalagem INT NULL,
    valor DECIMAL(19,4) NULL,
    ultimovalor DECIMAL(19,4) NULL,
    for_codigo INT NULL,
    quantidademin INT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT fk_produto_fornecedor
        FOREIGN KEY (for_codigo)
        REFERENCES fornecedor (codigo)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE anamnese (
    pac_codigo INT NOT NULL,
    algumtratamento BOOLEAN NOT NULL,
    hospitalizado BOOLEAN NOT NULL,
    motivo TEXT NOT NULL,
    alergiamedicamentoanestesico BOOLEAN NOT NULL,
    qualcirurgia TEXT NOT NULL,
    transfusaosangue BOOLEAN NOT NULL,
    bebidaalcoolica BOOLEAN NOT NULL,
    problemacardiaco BOOLEAN NOT NULL,
    febrereumatica BOOLEAN NOT NULL,
    especialidade TEXT NOT NULL,
    quandohospitalizado VARCHAR(100) NOT NULL,
    tomamedicamento BOOLEAN NOT NULL,
    alergia TEXT NOT NULL,
    sangramuito BOOLEAN NOT NULL,
    fumante BOOLEAN NOT NULL,
    desmaiotontura BOOLEAN NULL,
    hiv BOOLEAN NOT NULL,
    tuberculose BOOLEAN NOT NULL,
    nomemedico VARCHAR(100) NOT NULL,
    tempo VARCHAR(100) NOT NULL,
    medicamento VARCHAR(100) NOT NULL,
    cirurgia BOOLEAN NOT NULL,
    cirurgiabucal BOOLEAN NOT NULL,
    cigarrodia INT NOT NULL,
    respiratorio BOOLEAN NOT NULL,
    hepatite BOOLEAN NOT NULL,
    demoracicatrizar BOOLEAN NOT NULL,
    depressao BOOLEAN NOT NULL,
    diabete BOOLEAN NOT NULL,
    hipertensao BOOLEAN NOT NULL,
    reumatismo BOOLEAN NOT NULL,
    renal BOOLEAN NOT NULL,
    problemanervoso BOOLEAN NOT NULL,
    anemia BOOLEAN NOT NULL,
    epilepsia BOOLEAN NULL,
    PRIMARY KEY (pac_codigo),
    CONSTRAINT fk_anamnese_paciente
        FOREIGN KEY (pac_codigo)
        REFERENCES paciente (codigo)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE agenda (
    codigo INT NOT NULL AUTO_INCREMENT,
    data DATETIME(6) NOT NULL,
    pac_codigo INT NOT NULL,
    den_codigo INT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT fk_agenda_paciente
        FOREIGN KEY (pac_codigo)
        REFERENCES paciente (codigo)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    CONSTRAINT fk_agenda_dentista
        FOREIGN KEY (den_codigo)
        REFERENCES dentista (codigo)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE odontograma (
    codigo INT NOT NULL AUTO_INCREMENT,
    tipo INT NOT NULL,
    pac_codigo INT NULL,
    valor DECIMAL(19,4) NULL,
    data DATETIME(6) NOT NULL,
    status INT NULL,
    nome VARCHAR(100) NULL,
    telefone VARCHAR(15) NULL,
    odontopediatria BOOLEAN NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT fk_odontograma_paciente
        FOREIGN KEY (pac_codigo)
        REFERENCES paciente (codigo)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE procedimento_produto (
    pro_codigo INT NOT NULL,
    pro_codigopro INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (pro_codigo, pro_codigopro),
    CONSTRAINT fk_procedimento_produto_procedimento
        FOREIGN KEY (pro_codigo)
        REFERENCES procedimento (codigo)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT fk_procedimento_produto_produto
        FOREIGN KEY (pro_codigopro)
        REFERENCES produto (codigo)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE odontograma_dente (
    odo_codigo INT NOT NULL,
    dente INT NOT NULL,
    PRIMARY KEY (odo_codigo, dente),
    CONSTRAINT fk_odontograma_dente_odontograma
        FOREIGN KEY (odo_codigo)
        REFERENCES odontograma (codigo)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE odontograma_procedimento (
    codigo INT NOT NULL AUTO_INCREMENT,
    odo_codigo INT NOT NULL,
    pro_codigo INT NOT NULL,
    dente INT NOT NULL,
    status INT NOT NULL,
    valor DECIMAL(19,4) NOT NULL,
    face VARCHAR(200) NULL,
    observacao TEXT NULL,
    data DATETIME(6) NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT fk_odontograma_procedimento_odontograma
        FOREIGN KEY (odo_codigo)
        REFERENCES odontograma (codigo)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT fk_odontograma_procedimento_procedimento
        FOREIGN KEY (pro_codigo)
        REFERENCES procedimento (codigo)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE caixa_historico (
    codigo INT NOT NULL AUTO_INCREMENT,
    tipo VARCHAR(100) NOT NULL,
    origem VARCHAR(30) NOT NULL DEFAULT 'MANUAL',
    origem_codigo INT NULL,
    cai_codigo INT NOT NULL,
    observacao TEXT NOT NULL,
    descricao VARCHAR(100) NULL,
    data DATETIME(6) NOT NULL,
    valor DECIMAL(19,4) NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT fk_caixa_historico_caixa
        FOREIGN KEY (cai_codigo)
        REFERENCES caixa (codigo)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT uk_caixa_historico_origem_codigo
        UNIQUE (origem, origem_codigo),
    INDEX idx_caixa_historico_origem (origem, origem_codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE prontuario_arquivo (
    codigo INT NOT NULL AUTO_INCREMENT,
    paciente_codigo INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NULL,
    nome_original VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    tamanho BIGINT NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    criado_em DATETIME(6) NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT uk_prontuario_arquivo_storage_key
        UNIQUE (storage_key),
    CONSTRAINT fk_prontuario_arquivo_paciente
        FOREIGN KEY (paciente_codigo)
        REFERENCES paciente (codigo)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    INDEX idx_prontuario_arquivo_paciente_data (
        paciente_codigo,
        criado_em
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
