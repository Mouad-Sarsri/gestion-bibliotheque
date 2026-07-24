CREATE DATABASE IF NOT EXISTS bibliotheque;

use bibliotheque;

CREATE TABLE `auteur` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `nationalite` varchar(30) NOT NULL,
  `date_naissance` date DEFAULT NULL
);


CREATE TABLE `categorie` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nom` varchar(20) NOT NULL
);

CREATE TABLE `editeur` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nom` varchar(50) NOT NULL
);


CREATE TABLE `livre` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `titre` varchar(100) NOT NULL,
  `annee_publication` date DEFAULT NULL,
  `nbre_exemplaire` int(11) NOT NULL
);


CREATE TABLE `livre_auteur` (
  `id_livre` int(11) NOT NULL,
  `id_auteur` int(11) NOT NULL,
  PRIMARY KEY (`id_livre`, `id_auteur`),
  KEY `id_auteur` (`id_auteur`),
  CONSTRAINT `livre_auteur_ibfk_1` FOREIGN KEY (`id_livre`) REFERENCES `livre` (`id`) ON DELETE CASCADE,
  CONSTRAINT `livre_auteur_ibfk_2` FOREIGN KEY (`id_auteur`) REFERENCES `auteur` (`id`) ON DELETE CASCADE

);


CREATE TABLE `livre_categorie` (
  `id_livre` int(11) NOT NULL,
  `id_categorie` int(11) NOT NULL,
  PRIMARY KEY (`id_livre`, `id_categorie`),
  KEY `id_categorie` (`id_categorie`),
  CONSTRAINT `livre_categorie_ibfk_1` FOREIGN KEY (`id_livre`) REFERENCES `livre` (`id`) ON DELETE CASCADE,
  CONSTRAINT `livre_categorie_ibfk_2` FOREIGN KEY (`id_categorie`) REFERENCES `categorie` (`id`) ON DELETE CASCADE

);


CREATE TABLE `livre_editeur` (
  `id_livre` int(11) NOT NULL,
  `id_editeur` int(11) NOT NULL,
  PRIMARY KEY (`id_livre`, `id_editeur`),
  KEY `id_editeur` (`id_editeur`),
  CONSTRAINT `livre_editeur_ibfk_1` FOREIGN KEY (`id_livre`) REFERENCES `livre` (`id`) ON DELETE CASCADE,
  CONSTRAINT `livre_editeur_ibfk_2` FOREIGN KEY (`id_editeur`) REFERENCES `editeur` (`id`) ON DELETE CASCADE

);

INSERT INTO `auteur` (`nom`, `prenom`, `nationalite`, `date_naissance`) VALUES
('Hugo', 'Victor', 'Française', '1802-02-26'),
('Orwell', 'George', 'Britannique', '1903-06-25'),
('Camus', 'Albert', 'Française', '1913-11-07'),
('Tolkien', 'J.R.R.', 'Britannique', '1892-01-03'),
('Christie', 'Agatha', 'Britannique', '1890-09-15'),
('Verne', 'Jules', 'Française', '1828-02-08'),
('Zola', 'Émile', 'Française', '1840-04-02'),
('Asimov', 'Isaac', 'Américaine', '1920-01-02'),
('Austen', 'Jane', 'Britannique', '1775-12-16'),
('Dumas', 'Alexandre', 'Française', '1802-07-24'),
('Saint-Exupéry', 'Antoine', 'Française', '1900-06-29'),
('King', 'Stephen', 'Américaine', '1947-09-21');


INSERT INTO `categorie` (`nom`) VALUES
('Roman'),
('Science-fiction'),
('Philosophie'),
('Dystopie'),
('Policier'),
('Aventure'),
('Fantastique'),
('Jeunesse');


INSERT INTO `editeur` (`nom`) VALUES
('Gallimard'),
('Le Livre de Poche'),
('Secker & Warburg'),
('Flammarion'),
('Hachette'),
('Actes Sud');



INSERT INTO `livre` (`titre`, `annee_publication`, `nbre_exemplaire`) VALUES
('Les Misérables', '1862-01-01', 5),                     -- 1
('1984', '1949-01-01', 8),                                -- 2
('L''Étranger', '1942-01-01', 4),                         -- 3
('Le Hobbit', '1937-01-01', 6),                           -- 4
('Le Seigneur des Anneaux', '1954-01-01', 3),             -- 5
('Le Crime de l''Orient-Express', '1934-01-01', 7),       -- 6
('Dix Petits Nègres', '1939-01-01', 5),                   -- 7
('Vingt Mille Lieues sous les mers', '1870-01-01', 4),    -- 8
('Le Tour du monde en 80 jours', '1872-01-01', 6),        -- 9
('Germinal', '1885-01-01', 3),                            -- 10
('Nana', '1880-01-01', 2),                                -- 11
('Fondation', '1951-01-01', 5),                           -- 12
('Les Robots', '1950-01-01', 4),                          -- 13
('Orgueil et Préjugés', '1813-01-01', 6),                 -- 14
('Raison et Sentiments', '1811-01-01', 3),                -- 15
('Les Trois Mousquetaires', '1844-01-01', 5),             -- 16
('Le Comte de Monte-Cristo', '1844-01-01', 4),            -- 17
('Le Petit Prince', '1943-01-01', 10),                    -- 18
('Vol de nuit', '1931-01-01', 3),                         -- 19
('Ça', '1986-01-01', 5),                                  -- 20
('Shining', '1977-01-01', 4),                             -- 21
('La Peste', '1947-01-01', 6),                            -- 22
('La Chute', '1956-01-01', 2),                            -- 23
('Le Silmarillion', '1977-01-01', 3),                     -- 24
('Bel-Ami', '1885-01-01', 4);                             -- 25

INSERT INTO `livre_categorie` (`id_livre`, `id_categorie`) VALUES
(1, 1),
(2, 2), (2, 4),
(3, 1), (3, 3),
(4, 6), (4, 7),
(5, 6), (5, 7),
(6, 5),
(7, 5),
(8, 2), (8, 6),
(9, 6),
(10, 1),
(11, 1),
(12, 2),
(13, 2),
(14, 1),
(15, 1),
(16, 6),
(17, 6),
(18, 8), (18, 1),
(19, 1),
(20, 7),
(21, 7),
(22, 1), (22, 3),
(23, 3),
(24, 7);
-- Livre 25 (Bel-Ami) volontairement laissé SANS catégorie.



INSERT INTO `livre_editeur` (`id_livre`, `id_editeur`) VALUES
(1, 1), (2, 3), (3, 1), (4, 4), (5, 4), (6, 5), (7, 5),
(8, 4), (9, 4), (10, 1), (11, 1), (12, 6), (13, 6),
(14, 2), (15, 2), (16, 5), (17, 5), (18, 1), (19, 1),
(20, 6), (21, 6), (22, 1), (23, 1), (24, 4);
-- Livre 25 (Bel-Ami) volontairement laissé SANS éditeur.


INSERT INTO `livre_auteur` (`id_livre`, `id_auteur`) VALUES
(1, 1),   -- Les Misérables - Hugo
(2, 2),   -- 1984 - Orwell
(3, 3),   -- L'Étranger - Camus
(4, 4),   -- Le Hobbit - Tolkien
(5, 4),   -- Seigneur des Anneaux - Tolkien
(6, 5),   -- Crime Orient-Express - Christie
(7, 5),   -- Dix Petits Nègres - Christie
(8, 6),   -- 20000 lieues - Verne
(9, 6),   -- Tour du monde - Verne
(10, 7),  -- Germinal - Zola
(11, 7),  -- Nana - Zola
(12, 8),  -- Fondation - Asimov
(13, 8),  -- Les Robots - Asimov
(14, 9),  -- Orgueil et Préjugés - Austen
(15, 9),  -- Raison et Sentiments - Austen
(16, 10), -- Trois Mousquetaires - Dumas
(17, 10), -- Comte de Monte-Cristo - Dumas
(18, 11), -- Petit Prince - Saint-Exupéry
(19, 11), -- Vol de nuit - Saint-Exupéry
(20, 12), -- Ça - King
(21, 12), -- Shining - King
(22, 3),  -- La Peste - Camus
(23, 3),  -- La Chute - Camus
(24, 4);  -- Silmarillion - Tolkien
-- Livre 25 (Bel-Ami) volontairement laissé SANS auteur pour tester
-- le cas "aucun auteur associé" dans l'affichage.

