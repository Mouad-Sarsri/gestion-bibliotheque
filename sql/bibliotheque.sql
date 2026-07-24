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

