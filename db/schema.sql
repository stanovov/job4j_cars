DROP TABLE IF EXISTS advertisements;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS body_types;
DROP TABLE IF EXISTS brands;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(25)
);

CREATE TABLE IF NOT EXISTS body_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS brands (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS advertisements (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    sold BOOLEAN NOT NULL,
    brand_id INT NOT NULL REFERENCES brands(id),
    body_type_id INT NOT NULL REFERENCES body_types(id),
    author_id INT NOT NULL REFERENCES users(id)
);

INSERT INTO body_types(name)
VALUES ('Седан'), ('Универсал'), ('Хэтчбэк'),
       ('Купе'), ('Минивэн'), ('Лифтбэк'),
       ('Кабриолет'), ('Родстер'), ('Пикап'),
       ('Фургон'), ('Микроавтобус');

INSERT INTO brands(name)
VALUES ('LADA (ВАЗ)'), ('Audi'), ('BMW'),
       ('Chery'), ('Chevrolet'), ('Citroen'),
       ('Daewoo'), ('Ford'), ('Haval'),
       ('Honda'), ('Hyundai'), ('Infiniti'),
       ('Jeep'), ('Kia'), ('Land Rover'),
       ('Lexus'), ('MINI'), ('Mazda'),
       ('Mercedes-Benz'), ('Mitsubishi'), ('Nissan'),
       ('Open'), ('Peugeot'), ('Porsche'),
       ('Renault'), ('Skoda'), ('SsangYong'),
       ('Subaru'), ('Suzuki'), ('Toyota'),
       ('Volkswagen'), ('Volvo'), ('ГАЗ'),
       ('УАЗ'), ('УАЗ');