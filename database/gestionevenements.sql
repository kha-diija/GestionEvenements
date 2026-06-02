-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 08 juil. 2025 à 02:28
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestionevenements`
--
CREATE DATABASE IF NOT EXISTS `gestionevenements` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `gestionevenements`;

-- --------------------------------------------------------

--
-- Structure de la table `confirmations`
--

DROP TABLE IF EXISTS `confirmations`;
CREATE TABLE IF NOT EXISTS `confirmations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inscription_id` int DEFAULT NULL,
  `date_envoi` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `inscription_id` (`inscription_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `evenements`
--

DROP TABLE IF EXISTS `evenements`;
CREATE TABLE IF NOT EXISTS `evenements` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `description` text,
  `date` datetime NOT NULL,
  `lieu` varchar(100) DEFAULT NULL,
  `capacite` int NOT NULL,
  `prix` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `evenements`
--

INSERT INTO `evenements` (`id`, `nom`, `description`, `date`, `lieu`, `capacite`, `prix`) VALUES
(2, 'Hackathon', 'Compétition de programmation', '2025-06-15 09:00:00', 'khouribga', 50, 0.00),
(10, 'Formation en développement mobile', 'développer des applications mobiles pour Android et iOS.', '2025-06-02 00:00:00', 'rabat', 45, 0.00),
(11, 'Concours de codage', 'Un concours où les participants résolvant des problèmes complexes de programmation.', '2025-06-25 00:00:00', 'Casablanca', 150, 0.00),
(12, 'Atelier sur le Big Data et l\'analyse de données', 'Un atelier pour apprendre à travailler avec de grandes quantités de données .', '2025-08-10 00:00:00', 'Agadir', 75, 0.00),
(13, 'Forum sur la blockchain', 'Un forum pour discuter des applications de la blockchain.', '2025-11-12 00:00:00', 'Marrakech', 100, 0.00),
(15, 'ensak', 'rien', '2025-05-31 00:00:00', 'ensa khouribga', 100, 0.00);

-- --------------------------------------------------------

--
-- Structure de la table `inscriptions`
--

DROP TABLE IF EXISTS `inscriptions`;
CREATE TABLE IF NOT EXISTS `inscriptions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `utilisateur_id` int DEFAULT NULL,
  `evenement_id` int DEFAULT NULL,
  `date_inscription` datetime DEFAULT CURRENT_TIMESTAMP,
  `statut` enum('active','annulée') DEFAULT 'active',
  PRIMARY KEY (`id`),
  KEY `utilisateur_id` (`utilisateur_id`),
  KEY `evenement_id` (`evenement_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `inscriptions`
--

INSERT INTO `inscriptions` (`id`, `utilisateur_id`, `evenement_id`, `date_inscription`, `statut`) VALUES
(46, 1, 2, '2025-04-24 22:35:16', 'active'),
(49, 1, 10, '2025-04-26 08:07:54', 'active'),
(63, 117, 11, '2025-04-30 07:49:44', 'active'),
(64, 118, 11, '2025-04-30 07:50:55', 'active'),
(66, 117, 10, '2025-04-30 07:52:40', 'active'),
(67, 119, 10, '2025-04-30 07:53:00', 'active'),
(69, 117, 12, '2025-05-14 00:55:27', 'active'),
(71, 117, 2, '2025-05-14 12:24:47', 'active');

-- --------------------------------------------------------

--
-- Structure de la table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_role` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `roles`
--

INSERT INTO `roles` (`id`, `nom_role`) VALUES
(1, 'Administrateur'),
(2, 'Utilisateur');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateurs`
--

DROP TABLE IF EXISTS `utilisateurs`;
CREATE TABLE IF NOT EXISTS `utilisateurs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `role_id` int DEFAULT NULL,
  `date_inscription` date DEFAULT NULL,
  `evenement_prefere` varchar(255) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `utilisateurs`
--

INSERT INTO `utilisateurs` (`id`, `nom`, `email`, `mot_de_passe`, `role_id`, `date_inscription`, `evenement_prefere`, `date_naissance`) VALUES
(1, 'chaimae', 'chaimae8@gmail.com', '1234', 1, '2022-10-03', 'AI', '2000-09-19'),
(117, 'khalid', 'khalid@gmail.com', '1234', 2, '2025-04-19', 'Hackathon', '2013-04-11'),
(118, 'salma', 'salma@gmail.com', '1234', 2, '2025-04-20', 'Atelier de développement logiciel', '2015-04-18'),
(119, 'hassnae', 'hassnae@gmail.com', '1234', 2, '2025-04-22', 'Séminaire sur la cybersécurité', '2025-04-05'),
(126, 'ikram', 'ikram@gmail.com', '1234', 1, '2025-04-24', 'Formation en développement mobile', '2012-04-06'),
(127, 'ENSAK', 'ensak@gmail.com', '1234', 2, '2025-04-26', 'événement........', '2007-07-03'),
(133, 'khadija', 'khadijakartouch8@gmail.com', 'azer', 2, '2025-04-30', 'web', '2012-04-28'),
(134, 'amina', 'amina@gmail.com', '1234', 2, '2025-05-14', 'rien', '2011-05-13'),
(135, 'sami', 'sami@gmail.com', '1234', 2, '2025-05-14', 'developement', '2013-05-16');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `confirmations`
--
ALTER TABLE `confirmations`
  ADD CONSTRAINT `confirmations_ibfk_1` FOREIGN KEY (`inscription_id`) REFERENCES `inscriptions` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `inscriptions`
--
ALTER TABLE `inscriptions`
  ADD CONSTRAINT `inscriptions_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateurs` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `inscriptions_ibfk_2` FOREIGN KEY (`evenement_id`) REFERENCES `evenements` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `utilisateurs`
--
ALTER TABLE `utilisateurs`
  ADD CONSTRAINT `utilisateurs_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
