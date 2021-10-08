DROP TABLE IF EXISTS advertisements;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS body_types;
DROP TABLE IF EXISTS models;
DROP TABLE IF EXISTS brands;
DROP TABLE IF EXISTS transmissions;
DROP TABLE IF EXISTS production_years;
DROP TABLE IF EXISTS sort_types;
DROP TABLE IF EXISTS period_filters;
DROP TABLE IF EXISTS status_filters;

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

CREATE TABLE IF NOT EXISTS models (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    brand_id INT NOT NULL REFERENCES brands(id)
);

CREATE TABLE IF NOT EXISTS transmissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS advertisements (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    sold BOOLEAN NOT NULL,
    photo BOOLEAN NOT NULL,
    mileage INT NOT NULL,
    production_year INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    model_id INT NOT NULL REFERENCES models(id),
    body_type_id INT NOT NULL REFERENCES body_types(id),
    transmission_id INT NOT NULL REFERENCES transmissions(id),
    author_id INT NOT NULL REFERENCES users(id)
);

-- Данная таблица необходима для пользовательского выбора годов
CREATE TABLE IF NOT EXISTS production_years (
    id SERIAL PRIMARY KEY,
    year INT NOT NULL UNIQUE
);

-- Данная таблица необходима для пользовательского выбора сортировки
CREATE TABLE IF NOT EXISTS sort_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Данная таблица необходима для пользовательского выбора интервала
CREATE TABLE IF NOT EXISTS period_filters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Данная таблица необходима для пользовательского выбора статуса объявления
CREATE TABLE IF NOT EXISTS status_filters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO body_types(name)
VALUES ('Седан'), ('Универсал'), ('Хэтчбэк'),
       ('Купе'), ('Минивэн'), ('Лифтбэк'),
       ('Кабриолет'), ('Родстер'), ('Пикап'),
       ('Фургон'), ('Микроавтобус');

INSERT INTO brands(name)
VALUES ('LADA (ВАЗ)'), ('Audi'), ('BMW'),
       ('Chevrolet'), ('Citroen'), ('Ford'),
       ('Honda'), ('Hyundai'), ('Infiniti'),
       ('Jeep'), ('Kia'), ('Lexus'),
       ('MINI'), ('Mazda'), ('Mercedes-Benz'),
       ('Mitsubishi'), ('Nissan'), ('Opel'),
       ('Peugeot'), ('Porsche'), ('Renault'),
       ('Skoda'), ('Subaru'), ('Toyota'),
       ('Volkswagen');

INSERT INTO models(name, brand_id)
VALUES ('2114', 1), ('Granta', 1), ('Kalina', 1), ('Priora', 1), ('Vesta', 1),
       ('A4', 2), ('A5', 2), ('A6', 2), ('Q5', 2), ('Q7', 2),
       ('3 серии', 3), ('5 серии', 3), ('7 серии', 3), ('X5', 3), ('X6', 3),
       ('Aveo', 4), ('Cruze', 4), ('Lacetti', 4), ('Lanos', 4), ('Niva', 4),
       ('Berlingo', 5), ('C3', 5), ('C4', 5), ('C5', 5),
       ('Fiesta', 6), ('Focus', 6), ('Fusion', 6), ('Kuga', 6), ('Mondeo', 6),
       ('Accord', 7), ('Civic', 7),
       ('Solaris', 8), ('Creata', 8), ('Elantra', 8),
       ('FX', 9), ('QX70', 9), ('QX80', 9),
       ('Grand Cherokee', 10), ('Cherokee', 10), ('Wrangler', 10),
       ('Ceed', 11), ('Cerato', 11), ('K5', 11), ('Optima', 11), ('Rio', 11), ('Sportage', 11),
       ('RX', 12), ('LX', 12), ('IS', 12),
       ('Countryman', 13), ('Hatch', 13),
       ('3', 14), ('6', 14), ('CX-5', 14), ('CX-7', 14),
       ('C-Класс', 15), ('E-Класс', 15), ('S-Класс', 15), ('G-Класс', 15),
       ('ASX', 16), ('Lancer', 16), ('Outlander', 16), ('Pajero', 16),
       ('Almera', 17), ('Juke', 17), ('Murano', 17), ('Qashqai', 17),
       ('Astra', 18), ('Corsa', 18), ('Vectra', 18),
       ('308', 19), ('406', 19), ('206', 19), ('408', 19),
       ('911', 20), ('Cayenne', 20), ('Panamera', 20),
       ('Duster', 21), ('Kaptur', 21), ('Logan', 21),
       ('Octavia', 22), ('Rapid', 22), ('Yeti', 22),
       ('Forester', 23), ('Legacy', 23),('Impreza', 23), ('Outback', 23),
       ('Corolla', 24), ('Camry', 24), ('Land Cruiser', 24), ('RAV4', 24),
       ('Golf', 25), ('Jetta', 25), ('Passat', 25), ('Polo', 25), ('Tiguan', 25), ('Touareg', 25);

INSERT INTO transmissions(name)
VALUES ('Автомат'), ('Механическая'), ('Робот'), ('Вариатор');

/* Генерация годов выпуска */
INSERT INTO production_years(year)
SELECT y.year
FROM (
         WITH x AS (SELECT n FROM (VALUES (0), (1), (2), (3), (4), (5), (6), (7), (8), (9)) v(n))
         SELECT ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS year
         FROM x ones,
              x tens,
              x hundreds,
              x thousands
         ORDER BY 1
     ) AS y
WHERE y.year >= 1960 AND y.year < 2022;

INSERT INTO sort_types(name)
VALUES ('По дате размещения'), ('По возрастанию цены'), ('По убыванию цены'),
       ('По возрастанию пробега'), ('По убыванию пробега');

INSERT INTO period_filters(name)
VALUES ('За всё время'), ('За сутки'), ('За неделю'), ('За месяц');

INSERT INTO status_filters(name)
VALUES ('Любой'), ('Продан'), ('Продается');