DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS viewer;

CREATE TABLE viewer (
    id SERIAL PRIMARY KEY,
    name TEXT,
    phone_number TEXT,
    UNIQUE (name, phone_number)
);
CREATE TABLE place (
    id SERIAL PRIMARY KEY,
    row int,
    number int,
    available BOOLEAN,
    viewer_id int references viewer(id)
);
DO
$do$
BEGIN
   FOR c_row IN 1..3 LOOP
   	  FOR c_number IN 1..3 LOOP
	  	INSERT INTO place (row, number, available) values (c_row, c_number, TRUE);
	  END LOOP;
   END LOOP;
END
$do$;