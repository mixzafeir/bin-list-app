CREATE TABLE IF NOT EXISTS clearing_cost (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    card_issuing_country VARCHAR(2) NOT NULL UNIQUE,
    clearing_cost_value DECIMAL(15, 3) NOT NULL,
    created_date TIMESTAMP NULL,
    modified_date TIMESTAMP NULL,
    created_by CHAR(36) NULL,
    modified_by CHAR(36) NULL
);

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    username VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS revinfo (
    rev INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    revtstmp BIGINT NULL
);

CREATE TABLE IF NOT EXISTS clearing_cost_aud (
    card_issuing_country VARCHAR(2) NULL,
    clearing_cost_value DECIMAL(15, 3) NULL,
    rev INT NOT NULL,
    revtype INTEGER NULL,
    id VARCHAR(36) NOT NULL,
    PRIMARY KEY (rev, id),
    FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users_aud (
    id VARCHAR(36) NOT NULL,
    username VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    rev INT NOT NULL,
    revtype INTEGER NULL,
    PRIMARY KEY (rev, id),
    FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE
);
