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
