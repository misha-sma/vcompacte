--users
CREATE TABLE IF NOT EXISTS users(id_user bigserial PRIMARY KEY, surname varchar(256), name varchar(256), birthday date, phone bigint, email varchar(256), password varchar(256), is_deleted boolean DEFAULT 'f', registration_date timestamp without time zone DEFAULT NOW());

INSERT INTO users(surname, name, birthday, phone, email, password) VALUES ('Пупкин', 'Василий', '1980-05-01', 79211234567, 'vpupkin', '$2a$10$1RS/LcSkvdtXlFYNEbVUQeEUE.YC/te3cQCmWWclsKyTX3/VrDKnq');
INSERT INTO users(surname, name, birthday, phone, email, password) VALUES ('Иванов', 'Михаил', '1983-12-31', 79211234560, 'misha', '$2a$10$Khv/H/62TUmXdvcU81l27ecA9Gwy7TY.X3O1eJBzsQto4ckLM8DFe');

--roles
CREATE TABLE IF NOT EXISTS roles(id_role int PRIMARY KEY, role_name varchar(20));
INSERT INTO roles(id_role, role_name) VALUES (1, 'ROLE_USER');

--users_roles
CREATE TABLE IF NOT EXISTS users_roles(id_user_role bigserial PRIMARY KEY, id_user bigint, id_role int);

ALTER TABLE users_roles ADD CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id_user);
ALTER TABLE users_roles ADD CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES roles(id_role);

INSERT INTO users_roles(id_user, id_role) VALUES (1, 1);
INSERT INTO users_roles(id_user, id_role) VALUES (2, 1);

--persistent_logins
CREATE TABLE IF NOT EXISTS persistent_logins(username varchar(256) NOT NULL, series varchar(64) NOT NULL PRIMARY KEY, token varchar(64) NOT NULL, last_used timestamp without time zone NOT NULL);
