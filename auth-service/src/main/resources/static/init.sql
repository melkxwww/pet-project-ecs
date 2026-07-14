CREATE TABLE founders
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL
);

CREATE TABLE employees
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    company_code  VARCHAR(60)         NOT NULL,
    UNIQUE (email, company_code)
);