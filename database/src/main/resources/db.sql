DROP DATABASE IF EXISTS console_bank;

CREATE DATABASE console_bank;

CREATE TABLE users
(
    user_id         BIGSERIAL PRIMARY KEY,
    full_name       VARCHAR(60) NOT NULL,
    passport_number VARCHAR(60) UNIQUE NOT NULL,
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
    transaction_id   BIGSERIAL PRIMARY KEY,
    from_account_id  BIGINT REFERENCES account (account_id),
    to_account_id    BIGINT REFERENCES account (account_id),
    amount           DECIMAL(15, 2)   NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    created_at       TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users(full_name, passport_number, password)
VALUES ('Yuriy Mihailovich Pinchuk', '308767HB134', '123' ),
       ('Olga Vasilevna Sugak', '308767HB136', '123' ),
       ('Yuliya Viktorovna Dolgacheva', '308767HB137', '123' ),
       ('Petr Sergeevich Eroshenko', '308767HB138', '123' ),
       ('Viktor Gennadevich Stolypin', '308767HB139', '123' ),
       ('Yurii Valerevich Pinchuk ', '308767HB120', '123' ),
       ('Evgenii Petrovich Bazylev', '308767HB126', '123' ),
       ('Yurii Sergeevich Ivanov', '308767HB143', '123' ),
       ('Petrova Anna Yurevna', '308767HB156', '123' ),
       ('Timofei Aleksandrovich Kot', '308767HB165', '123' ),
       ('Mariya Ivanovna Romanova', '308767HB178', '123' ),
       ('Olga Mikhailovna Prus', '308767HB198', '123' ),
       ('Nika Andreevna Rusakova', '308767HB153', '123' ),
       ('Arsenii Nikolaevich Stolypin', '308767HB169', '123' ),
       ('Anna Ivanovna PlyuShchina', '308767HB111', '123' ),
       ('Marina Grigorevna Petrova', '308767HB121', '123' ),
       ('Andrei Aleksandrovich Gaponenko', '308767HB109', '123' ),
       ('Polina Sergeevna Pukh', '308767HB345', '123' ),
       ('Tatyana Aleksandrovna Khvesyuk', '308767HB456', '123' ),
       ('Oleg Timofeevich Burak', '308767HB246', '123' ),
       ('Olga Timofeevna Burak', '308767CB246', '123' );

INSERT INTO bank(name)
VALUES ('CleverBank'),
       ('AlfaBank'),
       ('TinkoffBank'),
       ('BelBank'),
       ('PriorBank');

INSERT INTO account(account_number, balance, owner_id, bank_id)
VALUES ('123456789A', '100.70', 1, 1 ),
       ('12345F789A', '1100.10', 1, 1 ),
       ('12345678FA', '1300.00', 1, 2 ),
       ('1R3456789A', '4100.20', 2, 1 ),
       ('123456T89A', '76100.70', 2, 1 ),
       ('123456W89A', '65100.90', 2, 5 ),
       ('123E56789A', '4100.57', 3, 1 ),
       ('12345Y789A', '450.32', 3, 1 ),
       ('12345678HA', '5670.55', 3, 3 ),
       ('12345GF89A', '4320.16', 4, 1 ),
       ('1234DF789A', '99880.67', 4, 1 ),
       ('12RT56789A', '345100.93', 4, 4 ),
       ('12345N789A', '200.67', 5, 1 ),
       ('123456789S', '768.01', 5, 1 ),
       ('123456780A', '11500.06', 5, 2 ),
       ('ED3456789A', '2100.90', 6, 1 ),
       ('V23456789A', '56100.27', 6, 1 ),
       ('FGH456789A', '78500.16', 6, 4 ),
       ('1234JLP89A', '4679.94', 7, 1 ),
       ('123DVU789A', '1300.64', 7, 1 ),
       ('123456QWDA', '1230.91', 7, 5 ),
       ('1FMN56789A', '555.66', 8, 1 ),
       ('123456CT9A', '5577.99', 8, 1 ),
       ('1234AZ789A', '77990.67', 8, 2 ),
       ('12DVU6789A', '1111.31', 9, 1 ),
       ('12345QAL9A', '10020.24', 9, 1 ),
       ('1W345R789A', '9003.00', 9, 2 ),
       ('123R5678TA', '8899.15', 10, 1 ),
       ('T2345678TA', '767.67', 10, 1 ),
       ('WFH456789A', '8885.56', 10, 3 ),
       ('GKIF56789A', '436457.20', 11, 1 ),
       ('1234DEGJ9A', '200.10', 12, 1 ),
       ('12DBUD789A', '6555.90', 13, 5 ),
       ('123456FH9A', '11111.89', 14, 1 ),
       ('12ER56789A', '2457.87', 15, 1 ),
       ('12345RGBHA', '3090.32', 16, 5),
       ('12QWET789A', '5055.45', 17, 1 ),
       ('1234BBV89A', '40300.25', 18, 1 ),
       ('12CCC6789A', '90099.55', 19, 4 ),
       ('12345GGG9A', '18567.10', 20, 1 );
