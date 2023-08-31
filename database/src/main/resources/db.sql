DROP DATABASE IF EXISTS console_bank;
CREATE DATABASE console_bank;

CREATE TABLE users
(
    user_id         BIGSERIAL PRIMARY KEY,
    full_name       VARCHAR(60) NOT NULL,
    passport_number VARCHAR(60) NOT NULL,
    password        VARCHAR(60) NOT NULL
);

CREATE TABLE bank
(
    bank_id SERIAL PRIMARY KEY,
    name    VARCHAR(60) NOT NULL
);

CREATE TABLE account
(
    account_id     BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(60) UNIQUE NOT NULL,
    balance        DECIMAL(15, 2)     NOT NULL,
    owner_id       BIGINT REFERENCES users (user_id),
    bank_id        INT REFERENCES bank (bank_id)
);

CREATE TABLE transaction
(
    transaction_id  BIGSERIAL PRIMARY KEY,
    from_account_id BIGINT REFERENCES account (account_id),
    to_account_id   BIGINT REFERENCES account (account_id),
    amount          DECIMAL(15, 2) NOT NULL,
    created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);