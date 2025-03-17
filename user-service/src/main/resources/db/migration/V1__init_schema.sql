CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     firstname VARCHAR(100),
    lastname VARCHAR(100),
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    role VARCHAR(15) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(100)
    );

CREATE INDEX IF NOT EXISTS idx_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_phone ON users (phone);

CREATE TABLE IF NOT EXISTS user_profiles (
                                             id BIGSERIAL PRIMARY KEY,
                                             bio VARCHAR(500),
    avatar_url VARCHAR(255),
    language VARCHAR(100) NOT NULL,
    currency VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    user_id BIGINT UNIQUE NOT NULL,
    CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS tokens (
                                      id BIGSERIAL PRIMARY KEY,
                                      token VARCHAR(255) UNIQUE NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_token ON tokens (token);
CREATE INDEX IF NOT EXISTS idx_user_id ON tokens (user_id);

CREATE TABLE IF NOT EXISTS verification_tokens (
                                                   id BIGSERIAL PRIMARY KEY,
                                                   token VARCHAR(255) UNIQUE NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT UNIQUE NOT NULL,
    CONSTRAINT fk_verification_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_verification_token ON verification_tokens (token);
CREATE INDEX IF NOT EXISTS idx_verification_user_id ON verification_tokens (user_id);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
                                                     id BIGSERIAL PRIMARY KEY,
                                                     token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT UNIQUE NOT NULL,
    CONSTRAINT fk_password_reset_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_password_reset_user_id ON password_reset_tokens (user_id);