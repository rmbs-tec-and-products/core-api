ALTER TABLE usuario
    ADD COLUMN email VARCHAR(150) NULL AFTER login;

UPDATE usuario
SET email = CONCAT(login, '@sysodonto.local')
WHERE codigo > 0
  AND (email IS NULL OR email = '');

ALTER TABLE usuario
    MODIFY COLUMN email VARCHAR(150) NOT NULL;

ALTER TABLE usuario
    ADD CONSTRAINT uk_usuario_email UNIQUE (email);
