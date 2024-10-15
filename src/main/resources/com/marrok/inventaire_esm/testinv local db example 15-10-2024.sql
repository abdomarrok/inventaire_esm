-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 15, 2024 at 11:09 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `testinv`
--

-- --------------------------------------------------------

--
-- Table structure for table `article`
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
-- Dumping data for table `article`
--

INSERT INTO `article` (`id`, `name`, `unite`, `description`, `quantity`, `remarque`, `id_category`) VALUES
(239, 'LECTURE VIDEO JVC', 'piece', '', 2, '', 3),
(240, 'TERMINAL POUR CAMERA DE SURVEILLANCE', 'piece', 'M', 0, 'NULL', 2),
(241, 'APPREIL TELEPHONIQUE ENTC', 'piece', 'A', 0, 'NULL', 2),
(243, 'ARMOIRE POUR CLEF EN BOIS', 'piece', 'E', 5, 'NULL', 2),
(244, 'BUREAU METALIQUE', 'piece', 'E', 0, 'NULL', 2),
(246, 'BUREAU DE SERVICE', 'piece', 'E', 0, 'NULL', 2),
(248, 'ARMOIRE 01 ELEMENT EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(249, 'APPAREI TELEPHONIQUE', 'piece', 'E', 0, 'NULL', 2),
(250, 'PORTE-MONTEAU METALIQUE', 'piece', 'E', 0, 'NULL', 2),
(251, 'FONTAINE FRAICHE', 'piece', 'E', 0, 'NULL', 2),
(252, 'VIDEO DE SURVEILLANCE SANYO', 'piece', 'E', 0, 'NULL', 2),
(253, 'ECRAN DE SURVEILLANCE', 'piece', 'E', 0, 'NULL', 2),
(254, 'ARMOIRE EN BOIS DE 06 PORTES', 'piece', 'E', 0, 'NULL', 2),
(255, 'ARMOIRE MURALE EN BOIS POUR CLEFS', 'piece', 'E', 0, 'NULL', 2),
(256, 'LAMPE RECHARGEABLE', 'piece', 'E', 0, 'NULL', 2),
(257, 'CHAISE VISITEUR', 'piece', 'E', 0, 'NULL', 2),
(258, 'MICRO-ORDINATEUR MULTI-MEDIA', 'piece', 'E', 0, 'NULL', 2),
(259, 'IMPRIMANTE LQ 2070', 'piece', 'E', 0, 'NULL', 2),
(260, 'TALKABOUT', 'piece', 'E', 0, 'NULL', 2),
(261, 'BUREAU EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(262, 'BIBLIOTHEQUE 02 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(263, 'PORTE-MONTEAU EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(264, 'CHAISE AVEC ACCOUDOIR EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(265, 'ARMOIRE BASSE', 'piece', 'E', 0, 'NULL', 2),
(266, 'TABLE INFORMATIQUE', 'piece', 'E', 0, 'NULL', 2),
(267, 'MICRO MULTIMEDIA (montage)', 'piece', 'E', 0, 'NULL', 2),
(268, 'IMPRIMANTE EPSON LQ 2070', 'piece', 'E', 0, 'NULL', 2),
(269, 'BUREAU EN BOIS TROIS TIROIRES', 'piece', 'E', 0, 'NULL', 2),
(270, 'CHAISE EN BOIS AVEC ACCOIDOIR', 'piece', 'E', 0, 'NULL', 2),
(271, 'CHAISE EN BOIS POUR ELEVE', 'piece', 'E', 0, 'NULL', 2),
(272, 'ELEMENT A ETAGER', 'piece', 'E', 0, 'NULL', 2),
(273, 'BAIN D’HUILE 12 ELEMENTS PHILIPS', 'piece', 'E', 0, 'NULL', 2),
(274, 'MICRO-ORDINATEUR ACER', 'piece', 'E', 0, 'NULL', 2),
(275, 'COFFRE-FORT', 'piece', 'E', 0, 'NULL', 2),
(276, 'BUREAU DE SERVICE AVEC RETOUR', 'piece', 'E', 0, 'NULL', 2),
(277, 'BIBLIOTHEQUE TROIS ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(278, 'SALLON 05 PLACES 03 PCS.', 'piece', 'E', 0, 'NULL', 2),
(279, 'CHAISE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(280, 'TABLE BASSE SCULPTEE', 'piece', 'E', 0, 'NULL', 2),
(281, 'CLIMATISEUR ENIEM', 'piece', 'E', 0, 'NULL', 2),
(282, 'FAUTEUIL PDG', 'piece', 'E', 0, 'NULL', 2),
(283, 'CAISSON EN METAL 10 CAISSES', 'piece', 'E', 0, 'NULL', 2),
(284, 'COFFRE FORT 60CM', 'piece', 'E', 0, 'NULL', 2),
(285, 'ARMOIRE EN METAL POUR CLEFS', 'piece', 'E', 0, 'NULL', 2),
(286, 'TABLE POUR PHOTOCOPIEUSE', 'piece', 'E', 0, 'NULL', 2),
(287, 'PHOTOCOPIEUSE MITA 1455', 'piece', 'E', 0, 'NULL', 2),
(288, 'DECHICTEUR DE PAPIER PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(289, 'APPAREIL TELEPHONIQUE ALCATEL', 'piece', 'E', 0, 'NULL', 2),
(290, 'FANION', 'piece', 'E', 0, 'NULL', 2),
(291, 'MICRO-ORDINATEUR IBM. P04', 'piece', 'E', 0, 'NULL', 2),
(292, 'BUREAU EN BOIS DE SERVICE', 'piece', 'E', 0, 'NULL', 2),
(293, 'ARMOIRE BASSE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(294, 'ELEMENT A ETAGERE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(295, 'CHAISE EN BOIS AVEC ACCOUDOIR', 'piece', 'E', 0, 'NULL', 2),
(296, 'APPAREIL TELEPHONIQUE ENTC', 'piece', 'E', 0, 'NULL', 2),
(297, 'ARMOIRE FORTE', 'piece', 'E', 0, 'NULL', 2),
(298, 'ARMOIRE EN METAL', 'piece', 'E', 0, 'NULL', 2),
(299, 'CHAISE DALLE', 'piece', 'E', 0, 'NULL', 2),
(300, 'MICRO-ORDINATEUR MULTIMEDIA ZENITH', 'piece', 'E', 0, 'NULL', 2),
(301, 'CHAISE SIMPLE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(302, 'CALCULATRICE OLIVETTI', 'piece', 'E', 0, 'NULL', 2),
(303, 'BAC A FICHE EN METAL', 'piece', 'E', 0, 'NULL', 2),
(304, 'CALCULATRICE MODEL 1006 IBICO', 'piece', 'E', 0, 'NULL', 2),
(305, 'BIBLIOTHEQUE 01 ELEMENT', 'piece', 'E', 0, 'NULL', 2),
(306, 'CAISSON DE 05 CAISSES EN METAL', 'piece', 'E', 0, 'NULL', 2),
(307, 'MICRO-ORDINATEUR MULTIMEDIA', 'piece', 'E', 0, 'NULL', 2),
(308, 'BAIN D’HUILE 12 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(309, 'PORTE-MONTEAU EN METAL', 'piece', 'E', 0, 'NULL', 2),
(310, 'MACHINE A CALCULER CITIZEN CX123A', 'piece', 'E', 0, 'NULL', 2),
(311, 'MACHINE A CALCULER FLAMINGO', 'piece', 'E', 0, 'NULL', 2),
(312, 'ONDULEUR', 'piece', 'E', 0, 'NULL', 2),
(313, 'APC', 'piece', 'E', 0, 'NULL', 2),
(314, 'BUREAU ROND EN METAL', 'piece', 'E', 0, 'NULL', 2),
(315, 'CAISSON 10 ETAGERES', 'piece', 'E', 0, 'NULL', 2),
(316, 'BIBLIOTHEQUE 01 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(317, 'APPAREIL TELEPHONIQUE', 'piece', 'E', 0, 'NULL', 2),
(318, 'ARMOIRE FORTE EN METAL', 'piece', 'E', 0, 'NULL', 2),
(319, 'TABLE TELEPHONIQUE', 'piece', 'E', 0, 'NULL', 2),
(320, 'CALCULATRICE', 'piece', 'E', 0, 'NULL', 2),
(321, 'MICRO-ORDINATEUR MULTIMEDIA IBM. P04', 'piece', 'E', 0, 'NULL', 2),
(322, 'IMPRIMANTE COULEUR LEXMARC Z23', 'piece', 'E', 0, 'NULL', 2),
(323, 'IMPRIMANTE EPSOM LQ2070', 'piece', 'E', 0, 'NULL', 2),
(324, 'CAISSON DE 10 CAISSES EN METAL', 'piece', 'E', 0, 'NULL', 2),
(325, 'ETAUX', 'piece', 'E', 0, 'NULL', 2),
(326, 'CHAISE VISITEUR CHROMEE', 'piece', 'E', 0, 'NULL', 2),
(327, 'PHOTOCOPIEUSE MITA 1560', 'piece', 'E', 0, 'NULL', 2),
(328, 'FAUTEUILLE 06 PLACES', 'piece', 'E', 0, 'NULL', 2),
(329, 'MICRO-ORDINATEUR M/LG', 'piece', 'E', 0, 'NULL', 2),
(330, 'ELEMENT EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(331, 'ARMOIRE EN BOIS A ELEMENT', 'piece', 'E', 0, 'NULL', 2),
(332, 'CHAISE POUR MICRO.', 'piece', 'E', 0, 'NULL', 2),
(333, 'CLIMATISEUR CONDOR 9 BTU', 'piece', 'E', 0, 'NULL', 2),
(334, 'RALLONGE A FUSIBLE', 'piece', 'E', 0, 'NULL', 2),
(335, 'MICRO-ORDINATEUR', 'piece', 'E', 0, 'NULL', 2),
(336, 'PRISE APC', 'piece', 'E', 0, 'NULL', 2),
(337, 'MACHINE A ECRIR OLIVETTI EN ARABE', 'piece', 'E', 0, 'NULL', 2),
(338, 'TABLEAU D’AFFICHAGE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(339, 'CHAISE ACCOUDOIR', 'piece', 'E', 0, 'NULL', 2),
(340, 'VENTILLATEUR PLAFONNIER', 'piece', 'E', 0, 'NULL', 2),
(341, 'COMMODE POUR DOCUMENTS 02 PORTES', 'piece', 'E', 0, 'NULL', 2),
(342, 'SALLON DE 05 PLACES', 'piece', 'E', 0, 'NULL', 2),
(343, 'IMPRIMANTE CANON L LBP 1120', 'piece', 'E', 0, 'NULL', 2),
(344, 'TABLE BASSE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(345, 'CLASSEUR EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(346, 'BUREAU EN BOIS AVEC RETOUR', 'piece', 'E', 0, 'NULL', 2),
(347, 'BIBLIOTHEQUE 03 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(348, 'FAUTEUIL P.D.G.', 'piece', 'E', 0, 'NULL', 2),
(349, 'SALON 02 PCS', 'piece', 'E', 0, 'NULL', 2),
(350, 'BUREAU SOUS DIRECTEUR AVEC RETOUR FIXE', 'piece', 'E', 0, 'NULL', 2),
(351, 'SIEGE DE FAUTEUIL', 'piece', 'E', 0, 'NULL', 2),
(352, 'SALON 03 PCS. DE 05 PLACES', 'piece', 'E', 0, 'NULL', 2),
(353, 'CLIMATISEUR COOLINE', 'piece', 'E', 0, 'NULL', 2),
(354, 'MICRO-ORDINATEUR MULTIMEDIA HP.', 'piece', 'E', 0, 'NULL', 2),
(355, 'DRAPEAU SUR SOCLE', 'piece', 'E', 0, 'NULL', 2),
(356, 'IMPRIMANTE LEXMARQ EN COULEUR', 'piece', 'E', 0, 'NULL', 2),
(357, 'PRISE DE COURANT ELECTRIQUE', 'piece', 'E', 0, 'NULL', 2),
(358, 'HUB POUR RESEAU INTRANET', 'piece', 'E', 0, 'NULL', 2),
(359, 'DESTRICTEUR DE PAPIER', 'piece', 'E', 0, 'NULL', 2),
(360, 'CHAISE DALLE ROULANTE', 'piece', 'E', 0, 'NULL', 2),
(361, 'TABLE INFORMATIQUE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(362, 'MODEM POUR RESEAU INTERNET', 'piece', 'E', 0, 'NULL', 2),
(363, 'ECRAN TELEVISEUR POUR CAMERA EXT.SONY', 'piece', 'E', 0, 'NULL', 2),
(366, 'PORTRAIT DU PRESIDENT DE LA R.A.D.P', 'piece', 'E', 0, 'NULL', 2),
(367, 'BIBLIOTHEQUE 01 ELEMENT VITREE', 'piece', 'E', 0, 'NULL', 2),
(368, 'APPAREIL TELEPHONIQUE OPUS', 'piece', 'E', 0, 'NULL', 2),
(369, 'MICRO-ORDINATEUR MULTIMEDIA IBM.P.04', 'piece', 'E', 0, 'NULL', 2),
(370, 'IMPRIMANTE LAZER CANNON', 'piece', 'E', 0, 'NULL', 2),
(371, 'ONDULEUR MGE', 'piece', 'E', 0, 'NULL', 2),
(372, 'GROUPPEUR DE 06 DOSSIERS', 'piece', 'E', 0, 'NULL', 2),
(373, 'CHAISE SECRETAIRE', 'piece', 'E', 0, 'NULL', 2),
(374, 'BAIN D’HUILE', 'piece', 'E', 0, 'NULL', 2),
(375, 'PHOTOCOPIEUSE PANASONIC 1510', 'piece', 'E', 0, 'NULL', 2),
(376, 'ARMOIRE BASSE 02 P', 'piece', 'E', 0, 'NULL', 2),
(377, 'TABLE BASSE', 'piece', 'E', 0, 'NULL', 2),
(378, 'CHAISE P.D.G.', 'piece', 'E', 0, 'NULL', 2),
(379, 'SALON 05 PLACES', 'piece', 'E', 0, 'NULL', 2),
(380, 'DECHIQUETEUR DE PAPIER', 'piece', 'E', 0, 'NULL', 2),
(381, 'MICRO-ORDINATEUR MULTIMEDIA IBM. P.04', 'piece', 'E', 0, 'NULL', 2),
(382, 'IMPRIMANTE EPSON LQ2070', 'piece', 'E', 0, 'NULL', 2),
(383, 'GROUPPEUR DE DOSSIER', 'piece', 'E', 0, 'NULL', 2),
(384, 'BUREAU EN BOIS DE 06 TIROIRS', 'piece', 'E', 0, 'NULL', 2),
(385, 'MICRO-ORDINATEUR ZENITH MULTIMEDIA', 'piece', 'E', 0, 'NULL', 2),
(386, 'CHAISE ROULANTE DALLE', 'piece', 'E', 0, 'NULL', 2),
(387, 'M-ORDINATEUR MULTIMEDIAIBMP04+P03', 'piece', 'E', 0, 'NULL', 2),
(388, 'M-ORDINATEUR MULTIMEDIAIBMP04+P04', 'piece', 'E', 0, 'NULL', 2),
(389, 'M-ORDINATEUR MULTIMEDIAIBMP04+P05', 'piece', 'E', 0, 'NULL', 2),
(390, 'IMPRIMANTE CANON LBP2900', 'piece', 'E', 0, 'NULL', 2),
(391, 'MACHINE A CALCULER OLIVETTI', 'piece', 'E', 0, 'NULL', 2),
(392, 'MACHINE A ECRIRE OLIVETTI FRANÇAIS', 'piece', 'E', 0, 'NULL', 2),
(393, 'MACHINE A ECRIRE OLIVETTI ARABE', 'piece', 'E', 0, 'NULL', 2),
(394, 'TABLE BASSE ETAGERES', 'piece', 'E', 0, 'NULL', 2),
(395, 'BUREAU DE SERVICE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(396, 'BIBLIOTHEQUE 03 ELEMENT GM', 'piece', 'E', 0, 'NULL', 2),
(397, 'BIBLIOTHEQUE 02 ELEMENTS PM', 'piece', 'E', 0, 'NULL', 2),
(398, 'APPAREIL TELEPHONIQUE PANASONIQUE', 'piece', 'E', 0, 'NULL', 2),
(399, 'IMPRIMANTE CANON', 'piece', 'E', 0, 'NULL', 2),
(400, 'CAISSON EN BOIS DE 04 TIROIRS', 'piece', 'E', 0, 'NULL', 2),
(401, 'PORTE MONTEAU EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(402, 'URNE DE VOTE', 'piece', 'E', 0, 'NULL', 2),
(403, 'BAIN D’HUILE PHILIPS 12 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(404, 'MASSICOT IDEAL 3905', 'piece', 'E', 0, 'NULL', 2),
(405, 'MASSICOT PM SANS STAND', 'piece', 'E', 0, 'NULL', 2),
(406, 'RELIEUSE IBECO A.G.', 'piece', 'E', 0, 'NULL', 2),
(407, 'PHOTOCOPIEUSE MITA 6500', 'piece', 'E', 0, 'NULL', 2),
(408, 'PERFORATEUR', 'piece', 'E', 0, 'NULL', 2),
(409, 'CAISSON 02 PORTES', 'piece', 'E', 0, 'NULL', 2),
(410, 'BUREAU EN METAL', 'piece', 'E', 0, 'NULL', 2),
(411, 'PHOTOCOPIEUSE RICOH 551', 'piece', 'E', 0, 'NULL', 2),
(412, 'DUPLO RISO RP 3105EP', 'piece', 'E', 0, 'NULL', 2),
(413, 'DUPLO DP 21S', 'piece', 'E', 0, 'NULL', 2),
(414, 'AGRAFEUSE NOVUS B/54/3', 'piece', 'E', 0, 'NULL', 2),
(415, 'AGRAFEUSE KW TRIO', 'piece', 'E', 0, 'NULL', 2),
(416, 'PHOTOCOPIEUSE OCE 3045', 'piece', 'E', 0, 'NULL', 2),
(417, 'STABILISATEUR 3000VBA VSV+P', 'piece', 'E', 0, 'NULL', 2),
(418, 'CLIMATISEUR PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(419, 'TABLE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(420, 'APPAREIL TELEPHPONIC', 'piece', 'E', 0, 'NULL', 2),
(421, 'FAUTEUILE', 'piece', 'E', 0, 'NULL', 2),
(422, 'STORES CALIFORNIENS', 'piece', 'E', 0, 'NULL', 2),
(423, 'CADRE', 'piece', 'E', 0, 'NULL', 2),
(424, 'TABLE DE CHEVET', 'piece', 'E', 0, 'NULL', 2),
(425, 'APPAREI TELEPHONIQUE ENTC', 'piece', 'E', 0, 'NULL', 2),
(426, 'PORTE-MONTEAU', 'piece', 'E', 0, 'NULL', 2),
(427, 'TABLE DE REUNION', 'piece', 'E', 0, 'NULL', 2),
(428, 'TABLE POUR ELEVE', 'piece', 'E', 0, 'NULL', 2),
(429, 'ASPIRATEUR', 'piece', 'E', 0, 'NULL', 2),
(430, 'PORTE DRAPEAU', 'piece', 'E', 0, 'NULL', 2),
(431, 'VASE POUR FLEUR', 'piece', 'E', 0, 'NULL', 2),
(432, 'ARMOIRE VITREE', 'piece', 'E', 0, 'NULL', 2),
(433, 'REFRIGERATEUR 01 PORTE', 'piece', 'E', 0, 'NULL', 2),
(434, 'TABLE D’EXAMEN', 'piece', 'NULL', 0, 'NULL', 2),
(435, 'PARAVENT', 'piece', 'E', 0, 'NULL', 2),
(436, 'ESCABEAU DEUX MARCHE', 'piece', 'E', 0, 'NULL', 2),
(437, 'BALANCE MEDICALE', 'piece', 'E', 0, 'NULL', 2),
(438, 'MEGATOSCOPE', 'piece', 'E', 0, 'NULL', 2),
(439, 'BOITE PHARMACIE', 'piece', 'E', 0, 'NULL', 2),
(440, 'TENSIOMETRE', 'piece', 'E', 0, 'NULL', 2),
(441, 'TENSIOMETRE ELECTRONIC', 'piece', 'E', 0, 'NULL', 2),
(442, 'GLACIAIRE G.M.', 'piece', 'E', 0, 'NULL', 2),
(443, 'OPHTALMOSCOPE', 'piece', 'E', 0, 'NULL', 2),
(444, 'TABOURET', 'piece', 'E', 0, 'NULL', 2),
(445, 'ARMOIR A TIROIRS', 'piece', 'E', 0, 'NULL', 2),
(446, 'FAUTEUILLE', 'piece', 'I', 0, 'NULL', 2),
(447, 'TOISE', 'piece', 'E', 0, 'NULL', 2),
(448, 'ARMOIR EN METAL', 'piece', 'E', 0, 'NULL', 2),
(449, 'CHARIOT DE SOIN', 'piece', 'E', 0, 'NULL', 2),
(450, 'TABLE POUR EXAMEN', 'piece', 'E', 0, 'NULL', 2),
(451, 'TABLE DE TRAVAIL AVEC TIROIR', 'piece', 'E', 0, 'NULL', 2),
(452, 'TABLE DE CHEVET EN METAL', 'piece', 'E', 0, 'NULL', 2),
(453, 'LIT', 'piece', 'E', 0, 'NULL', 2),
(454, 'ESCABEAU', 'piece', 'E', 0, 'NULL', 2),
(455, 'POUPINELLE', 'piece', 'E', 0, 'NULL', 2),
(456, 'POTENCE A SERUM', 'piece', 'E', 0, 'NULL', 2),
(457, 'CHAISE ROULANTE', 'piece', 'E', 0, 'NULL', 2),
(458, 'BANC', 'piece', 'E', 0, 'NULL', 2),
(459, 'RAYONNAGE EN METAL', 'piece', 'E', 0, 'NULL', 2),
(460, 'MICROPHONE SANS FIL CHIAYO', 'piece', 'E', 0, 'NULL', 2),
(461, 'MICROPHONE DO94', 'piece', 'E', 0, 'NULL', 2),
(462, 'MICROPHONE CRAVATE COMPLET', 'piece', 'E', 0, 'NULL', 2),
(463, 'MEGA PROJECTEUR LARA 1200', 'piece', 'E', 0, 'NULL', 2),
(464, 'DOUBLE LECTEUR DE CASSETTE SONY', 'piece', 'E', 0, 'NULL', 2),
(465, 'CAMERA VODEO PANASONIC M9000', 'piece', 'E', 0, 'NULL', 2),
(466, 'BAFFE GM NORMAL', 'piece', 'E', 0, 'NULL', 2),
(467, 'APPAREIL PHOTO. CANON', 'piece', 'E', 0, 'NULL', 2),
(468, 'AMPLIFICATEUR DE PUISSANCE 2x150 W', 'piece', 'E', 0, 'NULL', 2),
(469, 'VIDEO CASSETTE SONY', 'piece', 'E', 0, 'NULL', 2),
(470, 'TREPIED POUR C.V. M9000', 'piece', 'E', 0, 'NULL', 2),
(471, 'TREPIED AP.PH. CANON', 'piece', 'E', 0, 'NULL', 2),
(472, 'TABLE DE MIXAGE BST 8 PISTES', 'piece', 'E', 0, 'NULL', 2),
(473, 'RETROPROJECTEUR 914', 'piece', 'E', 0, 'NULL', 2),
(474, 'LECTEUR HANATIONAL', 'piece', 'E', 0, 'NULL', 2),
(475, 'VALISE POUR CAMERA', 'piece', 'E', 0, 'NULL', 2),
(476, 'CAMERA VIDEO 9500', 'piece', 'E', 0, 'NULL', 2),
(477, 'MICROPHONE UNI DERECTIONEL', 'piece', 'E', 0, 'NULL', 2),
(478, 'MICROPHONE UNI DIRECTIONEL', 'piece', 'E', 0, 'NULL', 2),
(479, 'VIDEO CASSETTE PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(480, 'CHAINE STEREO 05 C.D. PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(481, 'VASE POUR TIRAGE AU SORT', 'piece', 'E', 0, 'NULL', 2),
(482, 'MARTEAU+SUPPORT+BOIS', 'piece', 'E', 0, 'NULL', 2),
(483, 'VALISE EN BOIS POUR TIRAGE', 'piece', 'E', 0, 'NULL', 2),
(484, 'BUREAU EN BOIS 04 TIROIRS', 'piece', 'E', 0, 'NULL', 2),
(485, 'MINI-CHAINE STEREO PHILIPS', 'piece', 'E', 0, 'NULL', 2),
(486, 'AMPLIFICATEUR PHONIC POD740', 'piece', 'E', 0, 'NULL', 2),
(487, 'CHAISE ACCOUDOIRE', 'piece', 'E', 0, 'NULL', 2),
(488, 'RAYONNAGE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(489, 'BIBLIOTHEQUE UN ELEMENT', 'piece', 'E', 0, 'NULL', 2),
(490, 'MACHINE A CALCULIE', 'piece', 'E', 0, 'NULL', 2),
(491, 'TELEVISION', 'piece', 'E', 0, 'NULL', 2),
(492, 'COFRE FORT', 'piece', 'E', 0, 'NULL', 2),
(493, 'ECRAN DE PROJECTION', 'piece', 'E', 0, 'NULL', 2),
(494, 'PERCHE POUR BAFFE', 'piece', 'E', 0, 'NULL', 2),
(495, 'SALON 03 PCS. 05 PLACES', 'piece', 'E', 0, 'NULL', 2),
(496, 'ARMOIRE BASSE 02 PORTES', 'piece', 'E', 0, 'NULL', 2),
(497, 'CHAISE EN BOIS ACCOUDOIR', 'piece', 'E', 0, 'NULL', 2),
(498, 'TELEPHONE', 'piece', 'E', 0, 'NULL', 2),
(499, 'MICRO-ORDINATEUR LG', 'piece', 'E', 0, 'NULL', 2),
(500, 'IMPRIMANTE H.P 1220C', 'piece', 'E', 0, 'NULL', 2),
(501, 'CHAISE POUR ELEVE', 'piece', 'E', 0, 'NULL', 2),
(502, 'MICRO-ORDINATEUR LG(oeuvres sociales)', 'piece', 'E', 0, 'NULL', 2),
(503, 'SALLON EN VELOUR DE 05 PLACES/ 03 P', 'piece', 'E', 0, 'NULL', 2),
(504, 'CHAISE DALLE AVEC ACCOUDOIR', 'piece', 'E', 0, 'NULL', 2),
(505, 'CHAISE DALLE SIMPLE', 'piece', 'E', 0, 'NULL', 2),
(506, 'CADRE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(507, 'GRAVEUR DE CD', 'piece', 'E', 0, 'NULL', 2),
(508, 'IMPRIMANTE LASER CANNON', 'piece', 'E', 0, 'NULL', 2),
(509, 'IMPRIMANTE LEXMARK COULEUR', 'piece', 'E', 0, 'NULL', 2),
(510, 'MICRO-ORDINATEUR PORTABLE', 'piece', 'E', 0, 'NULL', 2),
(511, 'SCANNER BENQ', 'piece', 'E', 0, 'NULL', 2),
(512, 'TABLE BASSE (RETOUR DU BUREAU)', 'piece', 'E', 0, 'NULL', 2),
(513, 'MICRO-ORDINATEUR MULTIMEDIA ACER P.03', 'piece', 'E', 0, 'NULL', 2),
(514, 'APPAREIL TELEPHONE', 'piece', 'E', 0, 'NULL', 2),
(515, 'IMPRIMANTE EPSON LQ 570', 'piece', 'E', 0, 'NULL', 2),
(516, 'SCANNER EPSON 1260', 'piece', 'E', 0, 'NULL', 2),
(517, 'BIBLIOTHEQUE A ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(518, 'IMPRIMANTE HP1220', 'piece', 'E', 0, 'NULL', 2),
(519, 'CAISSON 03 TIROIRS', 'piece', 'E', 0, 'NULL', 2),
(520, 'PHOTOCOPIEUR MITA 1460', 'piece', 'E', 0, 'NULL', 2),
(521, 'FAUTEUILE PDG', 'piece', 'E', 0, 'NULL', 2),
(522, 'TABLE DE CHVET', 'piece', 'E', 0, 'NULL', 2),
(523, 'SALLON', 'piece', 'E', 0, 'NULL', 2),
(524, 'CHAISE SIMPLE', 'piece', 'E', 0, 'NULL', 2),
(525, 'PORTRAIT DU PRESIDENT', 'piece', 'E', 0, 'NULL', 2),
(526, 'DESTRUCTEUR DE PAPIER', 'piece', 'E', 0, 'NULL', 2),
(527, 'PHOTOCOPIEUSE PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(528, 'ARMOIR BASSE DEUX PORTES', 'piece', 'E', 0, 'NULL', 2),
(529, 'MICRO-ORDINATEUR IBM', 'piece', 'NULL', 0, 'NULL', 2),
(530, 'CLASEUR EN BOIS 03 TIROIRES', 'piece', 'E', 0, 'NULL', 2),
(531, 'PRESENTOIRE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(532, 'TABLE DE LECTURE', 'piece', 'E', 0, 'NULL', 2),
(533, 'ELEMENT POUR SEPARATION EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(534, 'IMPRIMANTE CANNON', 'piece', 'E', 0, 'NULL', 2),
(535, 'MICRO-ORDINATEUR MULTIMEDIA LG', 'piece', 'E', 0, 'NULL', 2),
(536, 'SIEGE DE BUREAU', 'piece', 'E', 0, 'NULL', 2),
(537, 'TABLE TELEPHONIQUE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(538, 'DESTRUCTEUR DE PAPIER PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(539, 'SALON 03 PIECES 05 PLACES', 'piece', 'E', 0, 'NULL', 2),
(540, 'BIBLIOTHEQUE BERBERE', 'piece', 'E', 0, 'NULL', 2),
(541, 'IMPRIMANTE CANNON 2900 LBP', 'piece', 'E', 0, 'NULL', 2),
(542, 'BIBLIOTHEQUE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(543, 'ARMOIRE EN BOIS 02 PORTES', 'piece', 'E', 0, 'NULL', 2),
(544, 'SCANNER MUSTEK', 'piece', 'E', 0, 'NULL', 2),
(545, 'CLIMATISEUR CONDOR 09 BTU', 'piece', 'E', 0, 'NULL', 2),
(546, 'MICRO-ORDINATEUR ML', 'piece', 'E', 0, 'NULL', 2),
(547, 'SALON POUR 05 PLACES', 'piece', 'E', 0, 'NULL', 2),
(548, 'CALCULATRICE CITIZEN', 'piece', 'E', 0, 'NULL', 2),
(549, 'MACHINE A ECRIRE OLIVETTI ELECTRIQUE', 'piece', 'E', 0, 'NULL', 2),
(550, 'BIBLIOTHEQUE 04 ELEMENT', 'piece', 'E', 0, 'NULL', 2),
(551, 'TABLE DE TELEPHONE', 'piece', 'E', 0, 'NULL', 2),
(552, 'APPAREIL TELEPHONIQUE PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(553, 'MICRO-ORDINATEUR IBM. P.04 MULTIMEDIA', 'piece', 'E', 0, 'NULL', 2),
(554, 'IMPRIMANTE EN COULEUR LEXMARQ', 'piece', 'E', 0, 'NULL', 2),
(555, 'CALCULATRICE COMPTABLE', 'piece', 'E', 0, 'NULL', 2),
(556, 'IMPRIMANTE CANNON LBP 1120', 'piece', 'E', 0, 'NULL', 2),
(557, 'REFRIGERATEUR AVEC CACHE-REFRIGERATEUR', 'piece', 'E', 0, 'NULL', 2),
(558, 'BUREAU P.D.G.', 'piece', 'E', 0, 'NULL', 2),
(559, 'RETOUR DE BUREAU', 'piece', 'E', 0, 'NULL', 2),
(560, 'BIBLIOTHEQUE', 'piece', 'E', 0, 'NULL', 2),
(561, 'MICRO-ORDINATEUR H.P MULTIMEDIA', 'piece', 'E', 0, 'NULL', 2),
(562, 'IMPRIMANTE EN COULEUR LASER 4L H.P', 'piece', 'E', 0, 'NULL', 2),
(563, 'ONDULEUR APC', 'piece', 'E', 0, 'NULL', 2),
(564, 'DRAPEAU PRESIDENTIEL', 'piece', 'E', 0, 'NULL', 2),
(565, 'FAX SAGEM', 'piece', 'E', 0, 'NULL', 2),
(566, 'APPAREIL TELEPHONIQUE STANDARD', 'piece', 'NULL', 0, 'NULL', 2),
(567, 'HORLOGE PIERRE CARDIN', 'piece', 'E', 0, 'NULL', 2),
(568, 'PORTRAIT DU PRESIDENT (RADP)', 'piece', 'E', 0, 'NULL', 2),
(569, 'POT DE FLEURE ARTIFICIELLE', 'piece', 'E', 0, 'NULL', 2),
(570, 'ECRAN POUR CAMERA EXT.', 'piece', 'E', 0, 'NULL', 2),
(571, 'MIROIR GRAND MODEL', 'piece', 'E', 0, 'NULL', 2),
(572, 'VEILLEUSE GRAND MODEL', 'piece', 'E', 0, 'NULL', 2),
(573, 'BUREAU EN BOIS 06 TIROIRS', 'piece', 'E', 0, 'NULL', 2),
(574, 'MICRO-ORDINATEUR MULTIMEDIA IBM P.04', 'piece', 'E', 0, 'NULL', 2),
(575, 'TABLE POUR PHOTOCOPIEUSE 02 PORTES', 'piece', 'E', 0, 'NULL', 2),
(576, 'FAX PANASONIC KX-FL 512', 'piece', 'E', 0, 'NULL', 2),
(577, 'MODEM POUR INTERNET 56K', 'piece', 'E', 0, 'NULL', 2),
(578, 'CAISSON DE 03 TIROIRS', 'piece', 'E', 0, 'NULL', 2),
(579, 'PHOTOCOPIEUSE MITA KN 1510', 'piece', 'E', 0, 'NULL', 2),
(580, 'IMPRIMANTE LASER LEXM', 'piece', 'E', 0, 'NULL', 2),
(581, 'SCANNER', 'piece', 'E', 0, 'NULL', 2),
(582, 'BIBLIOTHEQUE VITREE', 'piece', 'E', 0, 'NULL', 2),
(583, 'TABLE DE CUISINE', 'piece', 'E', 0, 'NULL', 2),
(584, 'T.V PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(585, 'ANTENNE PORTATIVE', 'piece', 'E', 0, 'NULL', 2),
(586, 'COFFRE FORT', 'piece', 'E', 0, 'NULL', 2),
(587, 'PRESSE POUR PLOMBAGE', 'piece', 'E', 0, 'NULL', 2),
(588, 'TABLE DE REUNION OVALE', 'piece', 'E', 0, 'NULL', 2),
(589, 'CHAISE DE REUNION', 'piece', 'E', 0, 'NULL', 2),
(590, 'COMMODE BERBERE BASSE', 'piece', 'E', 0, 'NULL', 2),
(591, 'REFRIGERATEUR ENIEM', 'piece', 'E', 0, 'NULL', 2),
(592, 'PRESSE A CAFE ELECTRIQUE PHILIPS', 'piece', 'E', 0, 'NULL', 2),
(593, 'MICRO-ORDINATEUR ACER P. 03', 'piece', 'E', 0, 'NULL', 2),
(594, 'MICRO-ORDINATEUR ACER P. 04', 'piece', 'E', 0, 'NULL', 2),
(595, 'BAIN D’HUILE 09 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(596, 'MICRO-ORDINATEUR IBM P.04 MULTIMEDIA', 'piece', 'E', 0, 'NULL', 2),
(597, 'IMPRIMANTE COULEUR LEXMARK', 'piece', 'E', 0, 'NULL', 2),
(598, 'IMPRIMANTE H.P. COULEUR 930', 'piece', 'E', 0, 'NULL', 2),
(599, 'CHAISE POUR ELEVE EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(600, 'BAIN D’HUILE 9 ELEMENTS PHILIPS', 'piece', 'E', 0, 'NULL', 2),
(601, 'BUREAU INFORMATIQUE', 'piece', 'E', 0, 'NULL', 2),
(602, 'ELEMENT A ETAGERE', 'piece', 'E', 0, 'NULL', 2),
(603, 'CHAISE AVEC ACCOUDOIRE', 'piece', 'E', 0, 'NULL', 2),
(604, 'APPAREIL TELEPHONIQUE BEETEL', 'piece', 'E', 0, 'NULL', 2),
(605, 'ESCABOT', 'piece', 'E', 0, 'NULL', 2),
(606, 'CLASSEUR A BAC', 'piece', 'E', 0, 'NULL', 2),
(607, 'PRESENTOIR EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(608, 'CHARIOT POUR LIVRE', 'piece', 'E', 0, 'NULL', 2),
(609, 'APPAREIL TELEPHONNIQUE', 'piece', 'E', 0, 'NULL', 2),
(610, 'MONT DOCUMENT 1000KG', 'piece', 'E', 0, 'NULL', 2),
(611, 'ARMOIRE 06 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(612, 'TABLE', 'piece', 'E', 0, 'NULL', 2),
(613, 'MICRO-ORDINATEUR NEC', 'piece', 'E', 0, 'NULL', 2),
(614, 'CLASSEUR EN BOIS A ETAGERE', 'piece', 'E', 0, 'NULL', 2),
(615, 'CLIMATISEUR PORTABLE MONOBLOC', 'piece', 'E', 0, 'NULL', 2),
(616, 'TELEPHONNE', 'piece', 'E', 0, 'NULL', 2),
(617, 'INPREMENETE HP 1100', 'piece', 'E', 0, 'NULL', 2),
(618, 'ONDULEUR TIPPLITE', 'piece', 'E', 0, 'NULL', 2),
(619, 'BUREAU EN BOIS AVEC RETOURE', 'piece', 'E', 0, 'NULL', 2),
(620, 'MICRO-ORDINATEUR MONTAGE', 'piece', 'E', 0, 'NULL', 2),
(621, 'ARMOIRE DE CLASSEMENT', 'piece', 'E', 0, 'NULL', 2),
(622, 'BIBLIOTHEQUE 03 ELEMENT', 'piece', 'E', 0, 'NULL', 2),
(623, 'CLASSEUR METALLIQUE', 'piece', 'E', 0, 'NULL', 2),
(624, 'CLASSEUR A TIROIRS EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(625, 'PHOTOCOPIEUSE XEROX', 'piece', 'E', 0, 'NULL', 2),
(626, 'PRESENTOIR EN ALUMINIUM', 'piece', 'E', 0, 'NULL', 2),
(627, 'ARMOIR POUR INFORMATIQUE', 'piece', 'E', 0, 'NULL', 2),
(628, 'RAYONNAGE METALLIQUE', 'piece', 'E', 0, 'NULL', 2),
(629, 'ARMOIR EN BOIS 06 ELEMENTS', 'piece', 'E', 0, 'NULL', 2),
(630, 'CHAISE A ACCOUDOIRE', 'piece', 'E', 0, 'NULL', 2),
(631, 'BAIN D\'HUILE', 'piece', 'E', 0, 'NULL', 2),
(632, 'CALCULATRICE COMPTABLE CITIZEN 123A', 'piece', 'E', 0, 'NULL', 2),
(633, 'PHOTOCOPIEUSE OCE3045', 'piece', 'E', 0, 'NULL', 2),
(634, 'STABILISATEUR', 'piece', 'E', 0, 'NULL', 2),
(635, 'RALLONGE', 'piece', 'E', 0, 'NULL', 2),
(636, 'BUREAU AVEC RETOUR', 'piece', 'E', 0, 'NULL', 2),
(637, 'MICRO-ORDINATEUR HP', 'piece', 'E', 0, 'NULL', 2),
(638, 'IMPRIMANTE CANON 2900', 'piece', 'E', 0, 'NULL', 2),
(639, 'IMPRIMANTE LEXMARK', 'piece', 'E', 0, 'NULL', 2),
(640, 'ARMOIRE DE CLASSEMENT EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(641, 'VENTILATEUR PLAFONNIER', 'piece', 'E', 0, 'NULL', 2),
(642, 'MICRO-ORDINATEUR H.P', 'piece', 'E', 0, 'NULL', 2),
(643, 'SERVEUR H.P', 'piece', 'E', 0, 'NULL', 2),
(644, 'CHAISE ROTATIVE DALLE', 'piece', 'E', 0, 'NULL', 2),
(645, 'IMPREMENTE HP', 'piece', 'E', 0, 'NULL', 2),
(646, 'IMPREMENTE LEXMARK', 'piece', 'E', 0, 'NULL', 2),
(648, 'ARMOIRE EN BOIS DEUX PORTES', 'piece', 'E', 0, 'NULL', 2),
(649, 'ARMOIRE A ETAGERE', 'piece', 'E', 0, 'NULL', 2),
(650, 'APPAREIL TELEPHONNE', 'piece', 'E', 0, 'NULL', 2),
(651, 'TABLE DE TRI', 'piece', 'E', 0, 'NULL', 2),
(652, 'FANNION', 'piece', 'E', 0, 'NULL', 2),
(653, 'TABLE DE CLASSE', 'piece', 'E', 0, 'NULL', 2),
(654, 'TABLEAU BLANC', 'piece', 'E', 0, 'NULL', 2),
(655, 'ARMOIRE DEUX PORTES', 'piece', 'E', 0, 'NULL', 2),
(656, 'ARMOIRE A ETAGERES', 'piece', 'E', 0, 'NULL', 2),
(657, 'BIBLIOTHREQUE CLASSEMENT', 'piece', 'E', 0, 'NULL', 2),
(658, 'APPAREIL TELEPHONNE INATEL', 'piece', 'E', 0, 'NULL', 2),
(659, 'TABLEAU D’AFFICHAGE', 'piece', 'E', 0, 'NULL', 2),
(660, 'MODEM INTERNET', 'piece', 'E', 0, 'NULL', 2),
(661, 'SIEGE RABATABLE POUR AMPHIE', 'piece', 'E', 0, 'NULL', 2),
(662, 'AMPLIFICATEUR PHILIPS', 'piece', 'E', 0, 'NULL', 2),
(663, 'ARMOIRE DE CLIMATISATION LG', 'piece', 'E', 0, 'NULL', 2),
(664, 'MICROPHONNE', 'piece', 'E', 0, 'NULL', 2),
(665, 'BAFFES', 'piece', 'E', 0, 'NULL', 2),
(666, 'PORTRAIT', 'piece', 'E', 0, 'NULL', 2),
(667, 'TABLEAU DE CLASSE BLANC', 'piece', 'E', 0, 'NULL', 2),
(668, 'SUPPORT DE CONFERENCE', 'piece', 'E', 0, 'NULL', 2),
(669, 'TABLE DE CONFERENCE', 'piece', 'E', 0, 'NULL', 2),
(670, 'RETROPROJECTEUR MURAL', 'piece', 'E', 0, 'NULL', 2),
(671, 'DATA CHOW HITACHI', 'piece', 'E', 0, 'NULL', 2),
(672, 'DRAPEAU', 'piece', 'E', 0, 'NULL', 2),
(673, 'BAFFE ALLU', 'piece', 'E', 0, 'NULL', 2),
(674, 'ARMOIR DE CLIMATISATION PANASONIC', 'piece', 'E', 0, 'NULL', 2),
(675, 'SIEGE RABATABLE FIXES', 'piece', 'E', 0, 'NULL', 2),
(676, 'TABLE D’AMPHIE FIXEE', 'piece', 'E', 0, 'NULL', 2),
(677, 'RETROPROJECTEUR', 'piece', 'E', 0, 'NULL', 2),
(678, 'BUREAU DE PROF.', 'piece', 'E', 0, 'NULL', 2),
(679, 'CHAISE EN BIOS', 'piece', 'E', 0, 'NULL', 2),
(680, 'CHAISE', 'piece', 'E', 0, 'NULL', 2),
(681, 'CHAISE EN METAL', 'piece', 'E', 0, 'NULL', 2),
(682, 'ARMOIR 02 ELEMENTS VITREE', 'piece', 'E', 0, 'NULL', 2),
(683, 'CLIMATISEUR SAMSUNG 18000 BTU', 'piece', 'E', 0, 'NULL', 2),
(684, 'TABLE D’ELEVE', 'piece', 'E', 0, 'NULL', 2),
(685, 'TABLE D\'ELEVE', 'piece', 'E', 0, 'NULL', 2),
(686, 'CHAISE POUR PROF.', 'piece', 'NULL', 0, 'NULL', 2),
(687, 'CHAISE EN BOIS AVEC ACCOUDOIRE', 'piece', 'E', 0, 'NULL', 2),
(688, 'FONTAINE FRECHE', 'piece', 'E', 0, 'NULL', 2),
(689, 'TABLEAU D\'AFFICHAGE VITRE', 'piece', 'E', 0, 'NULL', 2),
(690, 'PRESENTOIRE VITRE', 'piece', 'E', 0, 'NULL', 2),
(691, 'TABLEAU', 'piece', 'E', 0, 'NULL', 2),
(692, 'CORBEILLE METALIQUE', 'piece', 'E', 0, 'NULL', 2),
(693, 'CORBEILLE METALLIQUE', 'piece', 'E', 0, 'NULL', 2),
(694, 'FAUTEUIL EN SKAI', 'piece', 'E', 0, 'NULL', 2),
(695, 'PANNEAU D\'AFFICHAGE', 'piece', 'E', 0, 'NULL', 2),
(696, 'VASE EN PIERRE', 'piece', 'E', 0, 'NULL', 2),
(697, 'BUTE DE TELEPHONE', 'piece', 'E', 0, 'NULL', 2),
(698, 'VENTILATEUR PLAFON', 'piece', 'E', 0, 'NULL', 2),
(699, 'NETTOYEUR', 'piece', 'E', 0, 'NULL', 2),
(700, 'PRIS APC', 'piece', 'E', 0, 'NULL', 2),
(701, 'CHAISEEN BOIS', 'piece', 'E', 0, 'NULL', 2),
(702, 'SURFACE DE PROJECTION', 'piece', 'E', 0, 'NULL', 2),
(703, 'CLIMATISEUR CONDOR', 'piece', 'E', 0, 'NULL', 2),
(704, 'PRESENTOIRE', 'piece', 'E', 0, 'NULL', 2),
(705, 'CHAISE POUR MICRO', 'piece', 'E', 0, 'NULL', 2),
(706, 'ARMOIRE DE CLASSEMENT A ETAGERES', 'piece', 'E', 0, 'NULL', 2),
(707, 'PRISE STABILISATEUR', 'piece', 'E', 0, 'NULL', 2),
(708, 'SURFACE DE PROJECTION MURALE', 'piece', 'E', 0, 'NULL', 2),
(709, 'TABLEAU D\'AFFICHACE', 'piece', 'E', 0, 'NULL', 2),
(710, 'PORTE-MONTEAU METALLIQUE', 'piece', 'E', 0, 'NULL', 2),
(711, 'STABILISATEUR DE COURANT ELECTRIQUE', 'piece', 'E', 0, 'NULL', 2),
(712, 'PORTE-CEINTRE EN METAL', 'piece', 'E', 0, 'NULL', 2),
(713, 'TABLEAU POUR RETROPROJECTION', 'piece', 'E', 0, 'NULL', 2),
(714, 'DATA CHOW SANYO', 'piece', 'E', 0, 'NULL', 2),
(715, 'MICRO ORDINATEUR POUR RETRO.', 'piece', 'E', 0, 'NULL', 2),
(716, 'MULTI-PRISE', 'piece', 'E', 0, 'NULL', 2),
(717, 'TREPIE POUR DATASHOW', 'piece', 'E', 0, 'NULL', 2),
(718, 'AMPLIFICATEUR.PHILIPS', 'piece', 'E', 0, 'NULL', 2),
(719, 'CHAISE RABATABLE FIXES', 'piece', 'E', 0, 'NULL', 3),
(720, 'TABLE DE CLASSE RABATABLE FIX', 'piece', 'E', 0, 'NULL', 2),
(721, 'ESTRADE', 'piece', 'NULL', 0, 'NULL', 2),
(722, 'CLIMATISEUR HAIER', 'piece', 'E', 0, 'NULL', 2),
(723, 'BAFFE', 'piece', 'E', 0, 'NULL', 2),
(724, 'MICROPHONNE A AMPLI. PHILIPS', 'piece', 'NULL', 0, 'NULL', 2),
(725, 'TABLE DE SIEGE RABATABLE', 'piece', 'E', 0, 'NULL', 2),
(726, 'IMPRIMANTE EPSON 561+', 'piece', 'E', 0, 'NULL', 2),
(727, 'VASE EN PLASTIQUE', 'piece', 'E', 0, 'NULL', 2),
(728, 'CORBEILLE', 'piece', 'E', 0, 'NULL', 2),
(729, 'LIT EN BOIS', 'piece', 'E', 0, 'NULL', 2),
(730, 'CHAISE EN BOIS POUR REFECTOIRE', 'piece', 'E', 0, 'NULL', 2),
(731, 'PORTE MONTEAU BOIS', 'piece', 'E', 0, 'NULL', 2),
(732, 'IMPRIMANTE HP LAZER', 'piece', 'E', 0, 'NULL', 1),
(733, 'T.V 47 CM ENIE', 'piece', 'E', 0, 'NULL', 2),
(734, 'TABLE T.V', 'piece', 'E', 0, 'NULL', 2),
(736, 'IMPRIMANTE HP LASER JET P1102', 'piece', 'E', 0, 'NULL', 2),
(737, 'IMPRIMANTE LEXMARK E 250 D', 'piece', 'E', 0, 'NULL', 2),
(738, 'BANKNOTE COUNTER', 'piece', 'E', 0, 'NULL', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bon_entree`
--

CREATE TABLE `bon_entree` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_fournisseur` int(10) UNSIGNED NOT NULL,
  `date` datetime NOT NULL,
  `TVA` int(11) NOT NULL DEFAULT 19
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_entree`
--

INSERT INTO `bon_entree` (`id`, `id_fournisseur`, `date`, `TVA`) VALUES
(1, 5, '2024-10-15 22:34:58', 19);

-- --------------------------------------------------------

--
-- Table structure for table `bon_sortie`
--

CREATE TABLE `bon_sortie` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_employeur` int(10) UNSIGNED NOT NULL,
  `id_service` int(10) UNSIGNED NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_sortie`
--

INSERT INTO `bon_sortie` (`id`, `id_employeur`, `id_service`, `date`) VALUES
(1, 1, 1, '2024-10-15 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(10) UNSIGNED NOT NULL,
  `name_cat` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name_cat`) VALUES
(1, 'othre'),
(2, 'meuble'),
(3, 'tech');

-- --------------------------------------------------------

--
-- Table structure for table `employeur`
--

CREATE TABLE `employeur` (
  `id` int(10) UNSIGNED NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employeur`
--

INSERT INTO `employeur` (`id`, `firstname`, `lastname`, `title`) VALUES
(1, 'خديجة', 'بن زموري', 'ing'),
(2, 'يخلف', 'مصطفى', 'ing'),
(3, 'منصور', 'وردة', 'ing'),
(4, 'قلال', 'عبد الرزاق', 'sec'),
(5, 'بن شهرة ', 'انور', 'ing'),
(8, 'جوادي', 'عدلان كريم', NULL),
(9, 'كحلال', 'جلول', NULL),
(12, 'أومجكان', 'سميرة', NULL),
(13, 'راشد', 'بن زاوي', NULL),
(14, 'قاستل', 'نور الدين', NULL),
(15, 'بوقاسي', 'سامية', NULL),
(16, 'تومي', 'محجوبة', NULL),
(17, 'كاتب', 'نسيم', NULL),
(18, 'راحو', 'أمال', NULL),
(19, 'مروش', 'يونس', NULL),
(20, 'شهاب', 'عصام', NULL),
(21, 'كسراوي', 'مليكة', NULL),
(22, 'حشماوي', 'محمد', NULL),
(23, 'بن تواتي', 'شهرزاد', NULL),
(24, 'شعبان', 'سمير', NULL),
(26, 'بولشلوش', 'وردة', NULL),
(27, 'عباس', 'عبد اللطيف', NULL),
(28, 'عمراوي', 'عبد الحميد', NULL),
(29, 'يحياوي', 'حسينة', NULL),
(30, 'بلمادي', 'إيمان', NULL),
(31, 'بوطالبي', 'عبد الله', NULL),
(32, 'جودي', 'فوزية', NULL),
(33, 'قعلول', 'فتحية', NULL),
(34, 'بركان', 'ليلى', NULL),
(35, 'نايت يحيى', 'بلال', NULL),
(36, 'بوطين', 'سلمى', NULL),
(37, 'عمار', 'يوسف دليلة', NULL),
(38, 'عيش', 'محمد', NULL),
(39, 'كيتوس', 'إيدير', NULL),
(40, 'بوناب', 'عبد الناصر', NULL),
(41, 'ساسي', 'نواري', NULL),
(42, 'رعاش', 'سامية', NULL),
(43, 'أوزية', 'كنزة', NULL),
(44, 'قاسي', 'سهيلة', NULL),
(45, 'دباغ', 'وردة', NULL),
(46, 'شارف', 'خوجة سمير', NULL),
(47, 'قارة', 'علي ذهبيـة', NULL),
(48, 'بن عمارة', 'زكية', NULL),
(49, 'حسيان', 'زهية', NULL),
(50, 'هومة', 'سمية', NULL),
(51, 'شعشوع', 'أنيسة', NULL),
(52, 'أوزناني', 'فزيلة', NULL),
(53, 'رزيقات', 'سجية', NULL),
(54, 'ميهوب', 'صليحة', NULL),
(55, 'هدروق', 'عبد الكريم', NULL),
(56, 'بولحواش', 'مراد', NULL),
(57, 'هدروق', 'منور', NULL),
(58, 'صالح', 'محمد', NULL),
(59, 'شجاري', 'أحمد', NULL),
(60, 'قلال', 'عمر', NULL),
(61, 'كرامدي', 'عبد القادر', NULL),
(62, 'حمود', 'سليم', NULL),
(63, 'حمايدية', 'محمد', NULL),
(64, 'كودري', 'امبارك', NULL),
(65, 'روابحية', 'السعيد', NULL),
(66, 'بوخشبة', 'رشيد', NULL),
(67, 'رابحي', 'سيد علي', NULL),
(68, 'بن زين', 'هندة', NULL),
(69, 'ستيحي', 'نور الدين', NULL),
(70, 'بن سالم', 'توفيق', NULL),
(71, 'ربيعي', 'رابح', NULL),
(72, 'حميدي', 'محمد', NULL),
(73, 'بقـال', 'محمد الصغير', NULL),
(74, 'بلكسة', 'محمد', NULL),
(75, 'طروش', 'نورالدين', NULL),
(76, 'رزوق', 'بوعلام', NULL),
(77, 'بركاني', 'عبد القادر', NULL),
(78, 'لكحل', 'حميد', NULL),
(79, 'عيادي', 'عبد الرزاق', NULL),
(80, 'سدراتي', 'عبد الغني', NULL),
(81, 'حوش', 'رمضان', NULL),
(82, 'بولقرون', 'ياسين', NULL),
(83, 'هني', 'خالد', NULL),
(84, 'عوان', 'عبد الله', NULL),
(85, 'توازي', 'رضوان', NULL),
(86, 'كشرود', 'فاروق', NULL),
(87, 'منديلي', 'رضوان', NULL),
(88, 'بن ناصر', 'مولود', NULL),
(89, 'حدبي', 'عبد القادر', NULL),
(90, 'قتــال', 'حســـان', NULL),
(91, 'فوغالي', 'نسيم', NULL),
(92, 'دليو', 'فيصل', NULL),
(93, 'براهيتي', 'مصطفى', NULL),
(94, 'عبدي', 'عمر', NULL),
(95, 'دبوب', 'حمزة', NULL),
(96, 'مخلوف', 'كمال', NULL),
(97, 'زواق', 'قدور', NULL),
(98, 'شولي', 'عياش', NULL),
(99, 'آمبارك', 'محمد عدلان', NULL),
(100, 'عمراني', 'علي', NULL),
(101, 'عثوم', 'زبيدة', NULL),
(102, 'مريبعي', 'حمزة', NULL),
(103, 'فدول', 'كريم', NULL),
(104, 'شهر الدين', 'يوسف', NULL),
(105, 'عزوزي', 'عبد الرحيم', NULL),
(106, 'شنايت', 'حسين', NULL),
(107, 'لطرش', 'محمد', NULL),
(108, 'سراجية', 'حفصة', NULL),
(109, 'راربي', 'فضيلة', NULL),
(110, 'بوطواطو', 'رابح', NULL),
(111, 'حوري', 'نصيرة', NULL),
(112, 'جبري', 'مسعودة', NULL),
(113, 'مسعودان', 'حفيظة', NULL),
(114, 'مريجة', 'فاطمة الزهراء', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `entree`
--

CREATE TABLE `entree` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_article` int(10) UNSIGNED NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` double NOT NULL,
  `id_be` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `entree`
--

INSERT INTO `entree` (`id`, `id_article`, `quantity`, `unit_price`, `id_be`) VALUES
(1, 296, 15, 10, 1);

-- --------------------------------------------------------

--
-- Table structure for table `fournisseur`
--

CREATE TABLE `fournisseur` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `RC` varchar(255) NOT NULL,
  `NIF` varchar(255) NOT NULL,
  `AI` varchar(255) NOT NULL,
  `NIS` varchar(255) NOT NULL,
  `TEL` varchar(255) NOT NULL,
  `FAX` varchar(255) NOT NULL,
  `ADDRESS` varchar(255) NOT NULL,
  `EMAIL` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fournisseur`
--

INSERT INTO `fournisseur` (`id`, `name`, `RC`, `NIF`, `AI`, `NIS`, `TEL`, `FAX`, `ADDRESS`, `EMAIL`) VALUES
(5, 'Supplier A', 'RC123', 'NIF456', 'AI789', 'NIS012', '1234567890', '0987654321', '123 Street', 1234567890123456);

-- --------------------------------------------------------

--
-- Table structure for table `inventaire_item`
--

CREATE TABLE `inventaire_item` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_article` int(10) UNSIGNED NOT NULL,
  `id_localisation` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `time` datetime DEFAULT NULL,
  `num_inventaire` varchar(255) DEFAULT NULL,
  `id_employer` int(10) UNSIGNED DEFAULT NULL,
  `status` varchar(255) DEFAULT 'active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventaire_item`
--

INSERT INTO `inventaire_item` (`id`, `id_article`, `id_localisation`, `user_id`, `time`, `num_inventaire`, `id_employer`, `status`) VALUES
(6, 252, 8, 9, '2024-10-01 03:21:11', '45775454', 21, 'MOYEN'),
(8, 291, 8, 9, '2024-10-01 03:20:44', '554545455', 31, 'BON ETAT'),
(9, 240, 7, 9, '2024-10-01 03:21:16', '87547854', 29, 'MAUVAIS'),
(11, 240, 8, 9, '2024-10-25 00:00:00', '54754542', 28, 'MOYEN'),
(53, 300, 5, 1, '2024-01-01 08:00:00', 'INV-001', 1, 'active'),
(54, 301, 7, 4, '2024-01-02 09:00:00', 'INV-002', 2, 'inactive'),
(55, 302, 8, 1, '2024-01-03 10:00:00', 'INV-003', 3, 'active'),
(57, 304, 7, 1, '2024-01-05 12:00:00', 'INV-005', 5, 'inactive'),
(58, 305, 8, 4, '2024-01-06 13:00:00', 'INV-006', 1, 'active'),
(59, 306, 5, 1, '2024-01-07 14:00:00', 'INV-007', 2, 'inactive'),
(60, 307, 7, 4, '2024-01-08 15:00:00', 'INV-008', 3, 'active'),
(61, 308, 8, 1, '2024-01-09 16:00:00', 'INV-009', 4, 'active'),
(62, 309, 5, 4, '2024-01-10 17:00:00', 'INV-010', 5, 'inactive'),
(63, 310, 7, 1, '2024-01-11 18:00:00', 'INV-011', 1, 'active'),
(64, 311, 8, 4, '2024-01-12 19:00:00', 'INV-012', 2, 'inactive'),
(65, 312, 5, 1, '2024-01-13 20:00:00', 'INV-013', 3, 'active'),
(66, 313, 7, 4, '2024-01-14 21:00:00', 'INV-014', 4, 'active'),
(67, 314, 8, 1, '2024-01-15 22:00:00', 'INV-015', 5, 'inactive'),
(68, 315, 5, 4, '2024-01-16 23:00:00', 'INV-016', 1, 'active'),
(69, 316, 7, 1, '2024-01-17 08:00:00', 'INV-017', 2, 'inactive'),
(70, 317, 8, 4, '2024-01-18 09:00:00', 'INV-018', 3, 'active'),
(71, 318, 5, 1, '2024-01-19 10:00:00', 'INV-019', 4, 'active'),
(72, 319, 7, 4, '2024-01-20 11:00:00', 'INV-020', 5, 'inactive'),
(73, 320, 8, 1, '2024-01-21 12:00:00', 'INV-021', 1, 'active'),
(74, 321, 5, 4, '2024-01-22 13:00:00', 'INV-022', 2, 'active'),
(75, 322, 7, 1, '2024-01-23 14:00:00', 'INV-023', 3, 'inactive'),
(76, 323, 8, 4, '2024-01-24 15:00:00', 'INV-024', 4, 'active'),
(77, 324, 5, 1, '2024-01-25 16:00:00', 'INV-025', 5, 'active'),
(78, 325, 7, 4, '2024-01-26 17:00:00', 'INV-026', 1, 'inactive'),
(79, 326, 8, 1, '2024-01-27 18:00:00', 'INV-027', 2, 'active'),
(80, 327, 5, 4, '2024-01-28 19:00:00', 'INV-028', 3, 'active'),
(81, 328, 7, 1, '2024-01-29 20:00:00', 'INV-029', 4, 'inactive'),
(82, 329, 8, 4, '2024-01-30 21:00:00', 'INV-030', 5, 'active'),
(83, 330, 5, 1, '2024-01-31 22:00:00', 'INV-031', 1, 'active'),
(84, 331, 7, 4, '2024-02-01 23:00:00', 'INV-032', 2, 'inactive'),
(85, 332, 8, 1, '2024-02-02 08:00:00', 'INV-033', 3, 'active'),
(86, 333, 5, 4, '2024-02-03 09:00:00', 'INV-034', 4, 'inactive'),
(87, 334, 7, 1, '2024-02-04 10:00:00', 'INV-035', 5, 'active'),
(88, 335, 8, 4, '2024-02-05 11:00:00', 'INV-036', 1, 'inactive'),
(90, 337, 7, 4, '2024-02-07 13:00:00', 'INV-038', 3, 'active'),
(91, 338, 8, 9, '2024-10-02 05:44:19', 'INV-039', 4, 'VER ANNEXE-HARRACHE'),
(92, 339, 5, 9, '2024-10-02 05:42:57', 'INV-040', 5, 'MOYEN'),
(93, 258, 11, 9, '2024-10-25 00:00:00', '65265203', 2, 'BON ETAT'),
(94, 240, 8, 9, '2024-10-23 00:00:00', '854215', 28, 'MOYEN'),
(95, 732, 8, 9, '2024-10-31 00:00:00', '8520653', 3, 'MAUVAIS');

-- --------------------------------------------------------

--
-- Table structure for table `localisation`
--

CREATE TABLE `localisation` (
  `id` int(10) UNSIGNED NOT NULL,
  `loc_name` varchar(255) NOT NULL,
  `id_service` int(10) UNSIGNED DEFAULT NULL,
  `floor` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `localisation`
--

INSERT INTO `localisation` (`id`, `loc_name`, `id_service`, `floor`) VALUES
(5, 'الفندق', 1, 1),
(7, 'مكتب 1', 2, 4),
(8, 'المكتب الرئيسي', 1, 4),
(11, 'قاعة الاجتماعات', 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE `service` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`id`, `name`) VALUES
(1, 'الموارد البشرية'),
(2, 'المالية'),
(3, 'وسائل عامة ولاعلام الالي'),
(4, 'الادارة');

-- --------------------------------------------------------

--
-- Table structure for table `sortie`
--

CREATE TABLE `sortie` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_article` int(10) UNSIGNED NOT NULL,
  `quantity` int(11) NOT NULL,
  `id_bs` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sortie`
--

INSERT INTO `sortie` (`id`, `id_article`, `quantity`, `id_bs`) VALUES
(4, 239, 50, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(10) UNSIGNED NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `role`) VALUES
(1, '', '', ''),
(2, 'user1', 'password1', 'Admin'),
(4, 'z', 'z', 'User'),
(5, 'e', 'e', 'Editor'),
(9, 'i', 'i', 'Admin'),
(10, 'A', 'A', 'Admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `article`
--
ALTER TABLE `article`
  ADD PRIMARY KEY (`id`),
  ADD KEY `article_id_category_foreign` (`id_category`);

--
-- Indexes for table `bon_entree`
--
ALTER TABLE `bon_entree`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bon_entree_id_fournisseur_index` (`id_fournisseur`);

--
-- Indexes for table `bon_sortie`
--
ALTER TABLE `bon_sortie`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bon_sortie_id_employeur_index` (`id_employeur`),
  ADD KEY `bon_sortie_id_service_index` (`id_service`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `employeur`
--
ALTER TABLE `employeur`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `entree`
--
ALTER TABLE `entree`
  ADD PRIMARY KEY (`id`),
  ADD KEY `entree_id_article_index` (`id_article`),
  ADD KEY `entree_bon_entree_relation` (`id_be`);

--
-- Indexes for table `fournisseur`
--
ALTER TABLE `fournisseur`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `inventaire_item`
--
ALTER TABLE `inventaire_item`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_num_inventaire` (`num_inventaire`),
  ADD KEY `inventaire_item_id_article_foreign` (`id_article`),
  ADD KEY `inventaire_item_id_localisation_foreign` (`id_localisation`),
  ADD KEY `inventaire_item_user_id_foreign` (`user_id`),
  ADD KEY `inventaire_item_id_employer_foreign` (`id_employer`);

--
-- Indexes for table `localisation`
--
ALTER TABLE `localisation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `localisation_id_service_foreign` (`id_service`);

--
-- Indexes for table `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sortie`
--
ALTER TABLE `sortie`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sortie_id_article_index` (`id_article`),
  ADD KEY `id_bs` (`id_bs`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `article`
--
ALTER TABLE `article`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=739;

--
-- AUTO_INCREMENT for table `bon_entree`
--
ALTER TABLE `bon_entree`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `bon_sortie`
--
ALTER TABLE `bon_sortie`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `employeur`
--
ALTER TABLE `employeur`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT for table `entree`
--
ALTER TABLE `entree`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `fournisseur`
--
ALTER TABLE `fournisseur`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `inventaire_item`
--
ALTER TABLE `inventaire_item`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=96;

--
-- AUTO_INCREMENT for table `localisation`
--
ALTER TABLE `localisation`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `service`
--
ALTER TABLE `service`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `sortie`
--
ALTER TABLE `sortie`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `article`
--
ALTER TABLE `article`
  ADD CONSTRAINT `article_id_category_foreign` FOREIGN KEY (`id_category`) REFERENCES `category` (`id`);

--
-- Constraints for table `bon_entree`
--
ALTER TABLE `bon_entree`
  ADD CONSTRAINT `fournisseur_bon_entree_relation` FOREIGN KEY (`id_fournisseur`) REFERENCES `fournisseur` (`id`);

--
-- Constraints for table `bon_sortie`
--
ALTER TABLE `bon_sortie`
  ADD CONSTRAINT `bon_sortie_employeur_relation` FOREIGN KEY (`id_employeur`) REFERENCES `employeur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `bon_sortie_service_relation` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `entree`
--
ALTER TABLE `entree`
  ADD CONSTRAINT `entree_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `entree_bon_entree_relation` FOREIGN KEY (`id_be`) REFERENCES `bon_entree` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `inventaire_item`
--
ALTER TABLE `inventaire_item`
  ADD CONSTRAINT `inventaire_item_id_article_foreign` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`),
  ADD CONSTRAINT `inventaire_item_id_employer_foreign` FOREIGN KEY (`id_employer`) REFERENCES `employeur` (`id`),
  ADD CONSTRAINT `inventaire_item_id_localisation_foreign` FOREIGN KEY (`id_localisation`) REFERENCES `localisation` (`id`),
  ADD CONSTRAINT `inventaire_item_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `localisation`
--
ALTER TABLE `localisation`
  ADD CONSTRAINT `localisation_id_service_foreign` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`);

--
-- Constraints for table `sortie`
--
ALTER TABLE `sortie`
  ADD CONSTRAINT `sortie_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `sortie_bon_sortie_relation` FOREIGN KEY (`id_bs`) REFERENCES `bon_sortie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
