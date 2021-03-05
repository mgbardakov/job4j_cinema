DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS viewer;

CREATE TABLE viewer (
    id SERIAL PRIMARY KEY,
    name TEXT,
    phone_number TEXT,
    UNIQUE (name, phone_number)
);
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    row int,
    number int,
    viewer_id int references viewer(id),
    UNIQUE (row, number)
);