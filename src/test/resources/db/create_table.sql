DROP DATABASE IF EXISTS tests;
CREATE DATABASE tests;

\connect "tests";

CREATE TABLE public."users" (
	"email" varchar(50) NOT NULL,
	"full_name" varchar(50) NOT NULL,
	"city" varchar(50) NOT NULL,
	"age" integer NOT NULL,
	CONSTRAINT "pk_table" PRIMARY KEY ("email")
);
CREATE INDEX "ix_users_email" ON public."users" USING btree ("email");

INSERT INTO public."users" ("email", "full_name", "city", "age") VALUES
    ('mario@rossi.it', 'Mario Rossi', 'Firenze', 35),
    ('luigi@verdi.it', 'Luigi Verdi', 'Lucca', 28),
    ('paolo@bianchi.it', 'Paolo Bianchi', 'Firenze', 6),
    ('matteo@renzi.it', 'Matteo Renzi', 'Firenze', 45),
    ('marco@verdi.it', 'Marco Verdi', 'Milano', 13),
    ('vittorio@gialli.it', 'Vittorio Gialli', 'Milano', 64);
