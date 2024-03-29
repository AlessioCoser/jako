
CREATE TABLE `customers` (
                                `name` varchar(50) NOT NULL,
                                `age` integer NOT NULL,
                                CONSTRAINT `pk_customers` PRIMARY KEY (`name`)
);

CREATE TABLE `users` (
                                `email` varchar(50) NOT NULL,
                                `name` varchar(50) NOT NULL,
                                `city` varchar(50),
                                `age` integer NOT NULL,
                                CONSTRAINT `pk_users` PRIMARY KEY (`email`)
);

CREATE TABLE `pets` (
                               `name` varchar(50) NOT NULL,
                               `type` varchar(50) NOT NULL,
                               `owner` varchar(50) NOT NULL,
                               `age` integer NOT NULL,
                               CONSTRAINT `pk_pets` PRIMARY KEY (`name`, `owner`),
                               CONSTRAINT fk_pets FOREIGN KEY(owner) REFERENCES users(email)
);

INSERT INTO `users` (`email`, `name`, `city`, `age`) VALUES
                                                                ('mario@rossi.it', 'Mario Rossi', 'Firenze', 35),
                                                                ('luigi@verdi.it', 'Luigi Verdi', 'Lucca', 28),
                                                                ('paolo@bianchi.it', 'Paolo Bianchi', 'Firenze', 6),
                                                                ('matteo@renzi.it', 'Matteo Renzi', 'Firenze', 45),
                                                                ('marco@verdi.it', 'Marco Verdi', 'Milano', 13),
                                                                ('vittorio@gialli.it', 'Vittorio Gialli', 'Milano', 64),
                                                                ('cavallino@cavallini.it', 'Cavallino Cavallini', 'Roma', 2),
                                                                ('null@city.it', 'Null City', NULL, 88);

INSERT INTO `pets` (`name`, `type`, `owner`, `age`) VALUES
                                                               ('Pluto', 'Dog', 'luigi@verdi.it', 2),
                                                               ('Fido', 'Dog', 'matteo@renzi.it', 3),
                                                               ('Argo', 'Cat', 'vittorio@gialli.it', 1),
                                                               ('Bruto', 'Mouse', 'luigi@verdi.it', 6),
                                                               ('Fufi', 'Horse', 'vittorio@gialli.it', 4);

CREATE TABLE `types` (
                                `id` serial primary key,
                                `string` varchar(50),
                                `boolean` boolean,
                                `short` smallint,
                                `int` integer,
                                `long` bigint,
                                `float` real,
                                `double` double precision,
                                `date` date,
                                `local_date` date,
                                `time` time,
                                `timestamp` timestamp,
                                `timestamp_no_zone` timestamp,
                                `bytes` longblob
);

INSERT INTO `types`
(`string`,
 `boolean`,
 `short`,
 `int`,
 `long`,
 `float`,
 `double`,
 `date`,
 `local_date`,
 `time`,
 `timestamp`,
 `timestamp_no_zone`,
 `bytes`
)
VALUES
    ('str',
     true,
     1,
     999,
     3,
     3.4,
     5.6,
     '1980-01-01',
     '1980-01-01',
     '01:02:03',
     '2013-03-21 10:10:59.897666',
     '2013-03-21 10:10:59.897666',
     '\\000'
    ),
    (null, null, null, null, null, null, null, null, null, null, null, null, null);

CREATE TABLE `pets_deletable` (
       `name` varchar(50) NOT NULL,
       `type` varchar(50) NOT NULL,
       `age` integer NOT NULL,
       CONSTRAINT `pk_pets_deletable` PRIMARY KEY (`name`)
);
INSERT INTO `pets_deletable` (`name`, `type`, `age`) VALUES
                         ('Pluto', 'Dog', 2),
                         ('Fido', 'Dog', 3);