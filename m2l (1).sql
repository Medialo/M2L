-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  mer. 08 avr. 2020 à 11:39
-- Version du serveur :  5.7.17
-- Version de PHP :  7.1.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `m2l`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `login` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `nom` varchar(30) NOT NULL,
  `role` varchar(30) NOT NULL,
  `mail` varchar(100) NOT NULL,
  `mdp` varchar(128) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`id`, `login`, `prenom`, `nom`, `role`, `mail`, `mdp`) VALUES
(1, 'pbrelet', 'Paul', 'Brelet', 'Administrateur', 'paul.brelet@outllok.com', 'mdp'),
(2, 'eallais', 'Eddy', 'Allais', 'Organisateur', 'eddy.allais@gmail.com', 'mdp2'),
(3, 'tculorier', 'Tristan', 'Culorier', 'Utilisateur', 'jsp', 'mdp3');

-- --------------------------------------------------------

--
-- Structure de la table `appartient`
--

CREATE TABLE `appartient` (
  `personne_id_personne` int(11) NOT NULL,
  `equipe_idequipe` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `appartient`
--

INSERT INTO `appartient` (`personne_id_personne`, `equipe_idequipe`) VALUES
(1, 1),
(4, 2),
(6, 1);

-- --------------------------------------------------------

--
-- Structure de la table `candidat`
--

CREATE TABLE `candidat` (
  `personne_id_personne` int(11) NOT NULL,
  `competition_id_competition` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `candidat`
--

INSERT INTO `candidat` (`personne_id_personne`, `competition_id_competition`) VALUES
(1, 36),
(3, 36),
(6, 36);

-- --------------------------------------------------------

--
-- Structure de la table `competition`
--

CREATE TABLE `competition` (
  `id_competition` int(11) NOT NULL,
  `nom` varchar(45) NOT NULL,
  `date` date NOT NULL,
  `datefin` date NOT NULL,
  `date_cloture_inscr` date NOT NULL,
  `individuel` tinyint(1) NOT NULL,
  `id_sport` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `competition`
--

INSERT INTO `competition` (`id_competition`, `nom`, `date`, `datefin`, `date_cloture_inscr`, `individuel`, `id_sport`) VALUES
(2, 'SuperCourse', '2020-03-22', '2020-03-22', '2020-03-22', 0, -1),
(36, 'SuperCourse', '2020-04-18', '2020-04-20', '2020-04-10', 1, 7),
(37, 'tetete', '2020-04-08', '2020-04-08', '2020-04-08', 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `equipe`
--

CREATE TABLE `equipe` (
  `idequipe` int(11) NOT NULL,
  `nom` varchar(45) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `equipe`
--

INSERT INTO `equipe` (`idequipe`, `nom`) VALUES
(1, 'bre'),
(2, 'allais');

-- --------------------------------------------------------

--
-- Structure de la table `equipecandidat`
--

CREATE TABLE `equipecandidat` (
  `idcompetition` int(11) NOT NULL,
  `idequipe` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `equipecandidat`
--

INSERT INTO `equipecandidat` (`idcompetition`, `idequipe`) VALUES
(2, 1);

-- --------------------------------------------------------

--
-- Structure de la table `personne`
--

CREATE TABLE `personne` (
  `id_personne` int(11) NOT NULL,
  `nom` varchar(45) NOT NULL,
  `prenom` varchar(45) NOT NULL,
  `mail` varchar(75) DEFAULT NULL,
  `adresse` varchar(100) NOT NULL,
  `num` varchar(30) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `personne`
--

INSERT INTO `personne` (`id_personne`, `nom`, `prenom`, `mail`, `adresse`, `num`) VALUES
(1, 'Paul', 'Brelet', 'paul.brelet@outllok.com', 'les', '0123456789'),
(3, 'Culorier', 'tristan', 'le mail de tristant', 'adresse', '06.01.02.03.05'),
(4, 'Allais', 'Eddy', 'Mail de eddy', 'Adresse de eddy', '06.43.50.51.62'),
(6, 'Chauvin', 'Karen', 'mail', 'adresse', '123456789');

-- --------------------------------------------------------

--
-- Structure de la table `sport`
--

CREATE TABLE `sport` (
  `idsport` int(11) NOT NULL,
  `nomSport` varchar(30) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `sport`
--

INSERT INTO `sport` (`idsport`, `nomSport`) VALUES
(1, 'Tennis'),
(2, 'Marche'),
(3, 'Plongeon'),
(5, 'Ping Pong'),
(6, 'Course à pied'),
(7, 'Escalade');

-- --------------------------------------------------------

--
-- Structure de la table `sportequipe`
--

CREATE TABLE `sportequipe` (
  `equipe_idequipe` int(11) NOT NULL,
  `sport_idsport` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `sportequipe`
--

INSERT INTO `sportequipe` (`equipe_idequipe`, `sport_idsport`) VALUES
(1, 3),
(1, 5),
(1, 6),
(2, 7),
(9, 2);

-- --------------------------------------------------------

--
-- Structure de la table `sportpers`
--

CREATE TABLE `sportpers` (
  `personne_id_personne` int(11) NOT NULL,
  `sport_idsport` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `sportpers`
--

INSERT INTO `sportpers` (`personne_id_personne`, `sport_idsport`) VALUES
(1, 6),
(3, 6),
(3, 7),
(5, 1),
(6, 3);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `appartient`
--
ALTER TABLE `appartient`
  ADD PRIMARY KEY (`personne_id_personne`,`equipe_idequipe`),
  ADD KEY `fk_table1_equipe1` (`equipe_idequipe`);

--
-- Index pour la table `candidat`
--
ALTER TABLE `candidat`
  ADD PRIMARY KEY (`personne_id_personne`,`competition_id_competition`) USING BTREE,
  ADD KEY `fk_candidat_competition1` (`competition_id_competition`);

--
-- Index pour la table `competition`
--
ALTER TABLE `competition`
  ADD PRIMARY KEY (`id_competition`),
  ADD KEY `fk_sportcomp` (`id_sport`);

--
-- Index pour la table `equipe`
--
ALTER TABLE `equipe`
  ADD PRIMARY KEY (`idequipe`);

--
-- Index pour la table `equipecandidat`
--
ALTER TABLE `equipecandidat`
  ADD PRIMARY KEY (`idcompetition`,`idequipe`),
  ADD KEY `idequipe` (`idequipe`);

--
-- Index pour la table `personne`
--
ALTER TABLE `personne`
  ADD PRIMARY KEY (`id_personne`);

--
-- Index pour la table `sport`
--
ALTER TABLE `sport`
  ADD PRIMARY KEY (`idsport`);

--
-- Index pour la table `sportequipe`
--
ALTER TABLE `sportequipe`
  ADD PRIMARY KEY (`equipe_idequipe`,`sport_idsport`),
  ADD KEY `fk_sportequipe_sport1` (`sport_idsport`);

--
-- Index pour la table `sportpers`
--
ALTER TABLE `sportpers`
  ADD PRIMARY KEY (`personne_id_personne`,`sport_idsport`),
  ADD KEY `fk_sportpers_sport1` (`sport_idsport`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT pour la table `competition`
--
ALTER TABLE `competition`
  MODIFY `id_competition` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;
--
-- AUTO_INCREMENT pour la table `equipe`
--
ALTER TABLE `equipe`
  MODIFY `idequipe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT pour la table `personne`
--
ALTER TABLE `personne`
  MODIFY `id_personne` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT pour la table `sport`
--
ALTER TABLE `sport`
  MODIFY `idsport` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
