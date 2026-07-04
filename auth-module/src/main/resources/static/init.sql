-- PROPS: UUID version = 7
-- COMPANY MANAGEMENT & BILLING

CREATE TABLE founders
(
    id            INTEGER PRIMARY KEY,
    public_id     UUID UNIQUE         NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(60)         NOT NULL,
    created_at    TIMESTAMP           NOT NULL,
    is_active     BOOLEAN             NOT NULL DEFAULT FALSE
);

CREATE TABLE companies
(
    id           INTEGER PRIMARY KEY,
    public_id    UUID UNIQUE                      NOT NULL,
    founder_id   INTEGER REFERENCES founders (id) NOT NULL,
    name         VARCHAR(150)                     NOT NULL,
    company_code VARCHAR(30) UNIQUE               NOT NULL,
    is_active    BOOLEAN                          NOT NULL DEFAULT FALSE
);

CREATE TABLE sub_companies
(
    id          BIGINT PRIMARY KEY,
    public_id   UUID UNIQUE                       NOT NULL,
    company_id  INTEGER REFERENCES companies (id) NOT NULL,
    name        VARCHAR(150)                      NOT NULL,
    address     VARCHAR(500),
    description TEXT
);

CREATE TABLE modules
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE feature_flags
(
    id             BIGINT PRIMARY KEY,
    module_id      INTEGER REFERENCES modules (id)      NOT NULL,
    sub_company_id BIGINT REFERENCES sub_companies (id) NOT NULL,
    is_enabled     BOOLEAN                              NOT NULL DEFAULT FALSE,
    UNIQUE (module_id, sub_company_id)
);

-- COMPANY INFRASTRUCTURE

CREATE TABLE employees
(
    id            BIGINT PRIMARY KEY,
    public_id     UUID UNIQUE  NOT NULL,
    company_id    UUID         NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(60)  NOT NULL,
    UNIQUE (company_id, email)
);

CREATE TABLE roles
(
    id         BIGINT PRIMARY KEY,
    company_id UUID         NOT NULL,
    name       VARCHAR(100) NOT NULL,
    UNIQUE (company_id, name)
);

CREATE TABLE permissions
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(60) UNIQUE NOT NULL
);

CREATE TABLE roles_permissions
(
    role_id       BIGINT REFERENCES roles (id),
    permission_id INTEGER REFERENCES permissions (id),
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE employee_sub_company_roles
(
    employee_id    BIGINT NOT NULL,
    role_id        BIGINT NOT NULL,
    sub_company_id UUID   NOT NULL,
    company_id     UUID   NOT NULL,

    FOREIGN KEY (employee_id, company_id)
        REFERENCES employees (id, company_id),

    FOREIGN KEY (role_id, company_id)
        REFERENCES roles (id, company_id),

    PRIMARY KEY (employee_id, sub_company_id, company_id)
);