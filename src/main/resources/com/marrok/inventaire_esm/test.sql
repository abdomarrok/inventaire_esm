-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 04 juil. 2024 à 11:44
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `test`
--

-- --------------------------------------------------------

--
-- Structure de la table `article`
--

CREATE TABLE `article` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `unite` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `remarque` varchar(255) DEFAULT NULL,
  `id_category` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id`, `name`, `unite`, `description`, `quantity`, `remarque`, `id_category`) VALUES
(1, 'pc ', 'piece', 'i7 11eme', 50, 'dell', 3),
(7, 'keyboard', 'piece', 'لاشي', 152, 'لاشي', 3),
(8, 'table', 'piece', 'small table', 46, '/', 2),
(9, 'printer', 'piece', 'canon', 40, '5000', 3),
(10, 'tonor', 'piece', '6000', 10, 'canon', 1),
(11, 'chair', 'piece', 'foldable', 10, 'gamer', 2);

-- --------------------------------------------------------

--
-- Structure de la table `category`
--

CREATE TABLE `category` (
  `id` int(10) UNSIGNED NOT NULL,
  `name_cat` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `category`
--

INSERT INTO `category` (`id`, `name_cat`) VALUES
(1, 'othre'),
(2, 'meuble'),
(3, 'tech');

-- --------------------------------------------------------

--
-- Structure de la table `employeur`
--

CREATE TABLE `employeur` (
  `id` int(10) UNSIGNED NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `employeur`
--

INSERT INTO `employeur` (`id`, `firstname`, `lastname`, `title`) VALUES
(1, 'خديحة', 'بن زموري', 'ing'),
(2, 'يخلف', 'مصطفى', 'ing'),
(3, 'منصور', 'وردة', 'ing'),
(4, 'قلال', 'عبد الرزاق', 'sec'),
(5, 'بن شهرة ', 'انور', 'ing'),
(6, 'louiza', 'guerbai', 'ts info');

-- --------------------------------------------------------

--
-- Structure de la table `inventaire_item`
--

CREATE TABLE `inventaire_item` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_article` int(10) UNSIGNED NOT NULL,
  `id_localisation` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `time` datetime DEFAULT NULL,
  `num_inventaire` varchar(255) DEFAULT NULL,
  `id_employer` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `inventaire_item`
--

INSERT INTO `inventaire_item` (`id`, `id_article`, `id_localisation`, `user_id`, `time`, `num_inventaire`, `id_employer`) VALUES
(13, 9, 11, 1, '2024-07-03 11:19:29', '7410852', 2),
(14, 11, 7, 1, '2024-07-03 11:20:53', '8527410', 4),
(15, 11, 5, 1, '2024-07-03 10:19:38', '8547845', 3),
(16, 7, 8, 1, '2024-07-03 10:46:40', '8745210', 2),
(17, 8, 9, 1, '2024-07-03 10:47:57', '7854210', 3),
(18, 8, 8, 1, '2024-07-03 10:50:56', '8754210', 3),
(19, 7, 8, 1, '2024-07-03 11:16:15', '417854787', 2),
(20, 8, 9, 1, '2024-07-03 11:16:39', '85421', 1),
(21, 1, 7, 1, '2024-07-03 11:16:54', '8644564', 1),
(22, 7, 11, 1, '2024-07-03 11:44:55', '868446564', 5),
(23, 9, 8, 1, '2024-07-03 13:27:44', '0000254', 3),
(24, 7, 9, 1, '2024-07-03 13:48:07', '7410852', 5),
(25, 8, 8, 1, '2024-07-04 10:34:21', '854210', 2);

-- --------------------------------------------------------

--
-- Structure de la table `localisation`
--

CREATE TABLE `localisation` (
  `id` int(10) UNSIGNED NOT NULL,
  `loc_name` varchar(255) NOT NULL,
  `id_service` int(10) UNSIGNED DEFAULT NULL,
  `floor` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `localisation`
--

INSERT INTO `localisation` (`id`, `loc_name`, `id_service`, `floor`) VALUES
(5, 'الفندق', 1, 1),
(7, 'مكتب 1', 2, 4),
(8, 'المكتب الرئيسي', 1, 4),
(9, 'مكتب 5', 1, 4),
(10, 'مكتب 1', 3, 2),
(11, 'قاعة الاجتماعات', 3, 1);

-- --------------------------------------------------------

--
-- Structure de la table `service`
--

CREATE TABLE `service` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `service`
--

INSERT INTO `service` (`id`, `name`) VALUES
(1, 'الموارد البشرية'),
(2, 'المالية'),
(3, 'وسائل عامة ولاعلام الالي'),
(4, 'الادارة');

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(10) UNSIGNED NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `role`) VALUES
(1, '', '', ''),
(2, 'user1', 'password1', 'admin'),
(3, 'u', 'p', '');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `article`
--
ALTER TABLE `article`
  ADD PRIMARY KEY (`id`),
  ADD KEY `article_id_category_foreign` (`id_category`);

--
-- Index pour la table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `employeur`
--
ALTER TABLE `employeur`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `inventaire_item`
--
ALTER TABLE `inventaire_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `inventaire_item_id_article_foreign` (`id_article`),
  ADD KEY `inventaire_item_id_localisation_foreign` (`id_localisation`),
  ADD KEY `inventaire_item_user_id_foreign` (`user_id`),
  ADD KEY `inventaire_item_id_employer_foreign` (`id_employer`);

--
-- Index pour la table `localisation`
--
ALTER TABLE `localisation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `localisation_id_service_foreign` (`id_service`);

--
-- Index pour la table `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `article`
--
ALTER TABLE `article`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT pour la table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `employeur`
--
ALTER TABLE `employeur`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `inventaire_item`
--
ALTER TABLE `inventaire_item`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT pour la table `localisation`
--
ALTER TABLE `localisation`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT pour la table `service`
--
ALTER TABLE `service`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `article`
--
ALTER TABLE `article`
  ADD CONSTRAINT `article_id_category_foreign` FOREIGN KEY (`id_category`) REFERENCES `category` (`id`);

--
-- Contraintes pour la table `inventaire_item`
--
ALTER TABLE `inventaire_item`
  ADD CONSTRAINT `inventaire_item_id_article_foreign` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`),
  ADD CONSTRAINT `inventaire_item_id_employer_foreign` FOREIGN KEY (`id_employer`) REFERENCES `employeur` (`id`),
  ADD CONSTRAINT `inventaire_item_id_localisation_foreign` FOREIGN KEY (`id_localisation`) REFERENCES `localisation` (`id`),
  ADD CONSTRAINT `inventaire_item_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `localisation`
--
ALTER TABLE `localisation`
  ADD CONSTRAINT `localisation_id_service_foreign` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
