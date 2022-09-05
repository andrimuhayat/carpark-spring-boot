-- CREATE DATABASE IF NOT EXISTS postgres;

CREATE TABLE IF NOT EXISTS car_parks (
                       id SERIAL PRIMARY KEY,
                       car_park character varying(255) UNIQUE,
                       address VARCHAR,
                       x_coord double precision,
                       y_coord double precision,
                       car_park_type character varying(50),
                       type_of_parking_system character varying(50),
                       short_term_parking character varying(50),
                       free_parking character varying(50),
                       night_parking character varying(10),
                       car_park_decks int,
                       gantry_height character varying(10),
                       car_park_basement character varying(10),
                       created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                       updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT car_park_unique UNIQUE (car_park)
    );
GRANT ALL PRIVILEGES ON DATABASE postgres to "postgres";