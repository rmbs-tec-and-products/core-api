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
        ON UPDATE RESTRICT
);

CREATE INDEX idx_prontuario_arquivo_paciente_data
    ON prontuario_arquivo (
        paciente_codigo,
        criado_em
    );
