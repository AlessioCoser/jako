DROP DATABASE IF EXISTS tests;
CREATE DATABASE tests;

\connect "tests";

CREATE TABLE public."users" (
	"email" varchar(50) NOT NULL,
	"name" varchar(50) NOT NULL,
	"city" varchar(50) NOT NULL,
	"age" integer NOT NULL,
	CONSTRAINT "pk_table" PRIMARY KEY ("email")
);
CREATE INDEX "ix_users_email" ON public."users" USING btree ("email");

CREATE TABLE public."pets" (
    "name" varchar(50) NOT NULL,
    "type" varchar(50) NOT NULL,
    "owner" varchar(50) NOT NULL,
    "age" integer NOT NULL,
    CONSTRAINT "pk_pets_table" PRIMARY KEY ("name", "owner"),
    CONSTRAINT fk_pets FOREIGN KEY(owner) REFERENCES users(email)
);
CREATE INDEX "ix_pets_name_and_owner" ON public."pets" USING btree ("name", "owner");

INSERT INTO public."users" ("email", "name", "city", "age") VALUES
    ('mario@rossi.it', 'Mario Rossi', 'Firenze', 35),
    ('luigi@verdi.it', 'Luigi Verdi', 'Lucca', 28),
    ('paolo@bianchi.it', 'Paolo Bianchi', 'Firenze', 6),
    ('matteo@renzi.it', 'Matteo Renzi', 'Firenze', 45),
    ('marco@verdi.it', 'Marco Verdi', 'Milano', 13),
    ('vittorio@gialli.it', 'Vittorio Gialli', 'Milano', 64);

INSERT INTO public."pets" ("name", "type", "owner", "age") VALUES
    ('Pluto', 'Dog', 'luigi@verdi.it', 2),
    ('Fido', 'Dog', 'matteo@renzi.it', 3),
    ('Argo', 'Cat', 'vittorio@gialli.it', 1),
    ('Bruto', 'Mouse', 'luigi@verdi.it', 6),
    ('Fufi', 'Horse', 'vittorio@gialli.it', 4);

CREATE TABLE public."types" (
   "id" serial primary key,
   "string" varchar(50),
   "boolean" boolean,
   "short" smallint
--    "int" integer,
--    "long" bigint,
--    "float" real,
--    "double" double precision,
--    "bytes" bytea,
--    "date" date,
--    "time" time(3),
--    "time_no_zone" time(4) without time zone,
--    "timestamp" timestamp,
--    "timestamp_no_zone" timestamp without time zone
);

INSERT INTO public."types"
    ("string",
     "boolean",
     "short"
--      "int",
--      "long",
--      "float",
--      "double",
--      "bytes",
--      "date",
--      "time",
--      "time_no_zone",
--      "timestamp",
--      "timestamp_no_zone"
)
VALUES
    ('str',
     true,
     1
--      2,
--      3,
--      3.4,
--      5.6,
--      E'\xC744'::bytea,
--      '1980-01-01',
--      '01:02.123',
--      '09:08.1234',
--      '2013-03-21 09:10:59.897666 +02:00',
--      '2013-03-21 09:10:59.897666'
),
(null, null, null)
-- ,
--     (null, null, null, null, null, null, null, null, null, null, null, null, null, null);
