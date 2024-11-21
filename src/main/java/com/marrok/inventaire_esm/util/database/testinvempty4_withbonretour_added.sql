-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 21, 2024 at 01:39 PM
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
-- Database: `testinvempty2`
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
  `remarque` varchar(255) DEFAULT NULL,
  `id_category` int(10) UNSIGNED DEFAULT NULL,
  `last_edited` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `article`
--

INSERT INTO `article` (`id`, `name`, `unite`, `description`, `remarque`, `id_category`, `last_edited`) VALUES
(1, 'Tonor', 'piece', 'lpp64', '', 3, '2024-11-20 01:41:16');

-- --------------------------------------------------------

--
-- Table structure for table `bon_entree`
--

CREATE TABLE `bon_entree` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_fournisseur` int(10) UNSIGNED NOT NULL,
  `date` datetime NOT NULL,
  `TVA` int(11) NOT NULL DEFAULT 19,
  `document_num` varchar(255) NOT NULL,
  `last_edited` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_entree`
--

INSERT INTO `bon_entree` (`id`, `id_fournisseur`, `date`, `TVA`, `document_num`, `last_edited`) VALUES
(47, 5, '2024-11-20 00:00:00', 19, '4587', '2024-11-20 01:45:40');

-- --------------------------------------------------------

--
-- Table structure for table `bon_retour`
--

CREATE TABLE `bon_retour` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_employeur` int(10) UNSIGNED NOT NULL,
  `id_service` int(10) UNSIGNED NOT NULL,
  `date` datetime NOT NULL,
  `return_reason` varchar(255) DEFAULT NULL,
  `last_edited` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_retour`
--

INSERT INTO `bon_retour` (`id`, `id_employeur`, `id_service`, `date`, `return_reason`, `last_edited`) VALUES
(1, 5, 1, '2024-11-21 13:18:03', 'reason', '2024-11-21 04:18:21');

-- --------------------------------------------------------

--
-- Table structure for table `bon_sortie`
--

CREATE TABLE `bon_sortie` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_employeur` int(10) UNSIGNED NOT NULL,
  `id_service` int(10) UNSIGNED NOT NULL,
  `date` datetime NOT NULL,
  `last_edited` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_sortie`
--

INSERT INTO `bon_sortie` (`id`, `id_employeur`, `id_service`, `date`, `last_edited`) VALUES
(1, 2, 1, '2024-11-20 00:00:00', '2024-11-20 01:44:09');

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
(1, 'خديجة', 'بن زموري', ''),
(2, 'يخلف', 'مصطفى', ''),
(3, 'منصور', 'وردة', ''),
(4, 'قلال', 'عبد الرزاق', ''),
(5, 'بن شهرة ', 'انور', ''),
(8, 'جوادي', 'عدلان كريم', ''),
(9, 'كحلال', 'جلول', ''),
(12, 'أومجكان', 'سميرة', ''),
(13, 'راشد', 'بن زاوي', ''),
(14, 'قاستل', 'نور الدين', ''),
(15, 'بوقاسي', 'سامية', ''),
(16, 'تومي', 'محجوبة', ''),
(17, 'كاتب', 'نسيم', ''),
(18, 'راحو', 'أمال', ''),
(19, 'مروش', 'يونس', ''),
(20, 'شهاب', 'عصام', ''),
(21, 'كسراوي', 'مليكة', ''),
(22, 'حشماوي', 'محمد', ''),
(23, 'بن تواتي', 'شهرزاد', ''),
(24, 'شعبان', 'سمير', ''),
(26, 'بولشلوش', 'وردة', ''),
(27, 'عباس', 'عبد اللطيف', ''),
(28, 'عمراوي', 'عبد الحميد', ''),
(29, 'يحياوي', 'حسينة', ''),
(30, 'بلمادي', 'إيمان', ''),
(31, 'بوطالبي', 'عبد الله', ''),
(32, 'جودي', 'فوزية', ''),
(33, 'قعلول', 'فتحية', ''),
(34, 'بركان', 'ليلى', ''),
(35, 'نايت يحيى', 'بلال', ''),
(36, 'بوطين', 'سلمى', ''),
(37, 'عمار', 'يوسف دليلة', ''),
(38, 'عيش', 'محمد', ''),
(39, 'كيتوس', 'إيدير', ''),
(40, 'بوناب', 'عبد الناصر', ''),
(41, 'ساسي', 'نواري', ''),
(42, 'رعاش', 'سامية', ''),
(43, 'أوزية', 'كنزة', ''),
(44, 'قاسي', 'سهيلة', ''),
(45, 'دباغ', 'وردة', ''),
(46, 'شارف', 'خوجة سمير', ''),
(47, 'قارة', 'علي ذهبيـة', ''),
(48, 'بن عمارة', 'زكية', ''),
(49, 'حسيان', 'زهية', ''),
(50, 'هومة', 'سمية', ''),
(51, 'شعشوع', 'أنيسة', ''),
(52, 'أوزناني', 'فزيلة', ''),
(53, 'رزيقات', 'سجية', ''),
(54, 'ميهوب', 'صليحة', ''),
(55, 'هدروق', 'عبد الكريم', ''),
(56, 'بولحواش', 'مراد', ''),
(57, 'هدروق', 'منور', ''),
(58, 'صالح', 'محمد', ''),
(59, 'شجاري', 'أحمد', ''),
(60, 'قلال', 'عمر', ''),
(61, 'كرامدي', 'عبد القادر', ''),
(62, 'حمود', 'سليم', ''),
(63, 'حمايدية', 'محمد', ''),
(64, 'كودري', 'امبارك', ''),
(65, 'روابحية', 'السعيد', ''),
(66, 'بوخشبة', 'رشيد', ''),
(67, 'رابحي', 'سيد علي', ''),
(68, 'بن زين', 'هندة', ''),
(69, 'ستيحي', 'نور الدين', ''),
(70, 'بن سالم', 'توفيق', ''),
(71, 'ربيعي', 'رابح', ''),
(72, 'حميدي', 'محمد', ''),
(73, 'بقـال', 'محمد الصغير', ''),
(74, 'بلكسة', 'محمد', ''),
(75, 'طروش', 'نورالدين', ''),
(76, 'رزوق', 'بوعلام', ''),
(77, 'بركاني', 'عبد القادر', ''),
(78, 'لكحل', 'حميد', ''),
(79, 'عيادي', 'عبد الرزاق', ''),
(80, 'سدراتي', 'عبد الغني', ''),
(81, 'حوش', 'رمضان', ''),
(82, 'بولقرون', 'ياسين', ''),
(83, 'هني', 'خالد', ''),
(84, 'عوان', 'عبد الله', ''),
(85, 'توازي', 'رضوان', ''),
(86, 'كشرود', 'فاروق', ''),
(87, 'منديلي', 'رضوان', ''),
(88, 'بن ناصر', 'مولود', ''),
(89, 'حدبي', 'عبد القادر', ''),
(90, 'قتــال', 'حســـان', ''),
(91, 'فوغالي', 'نسيم', ''),
(92, 'دليو', 'فيصل', ''),
(93, 'براهيتي', 'مصطفى', ''),
(94, 'عبدي', 'عمر', ''),
(95, 'دبوب', 'حمزة', ''),
(96, 'مخلوف', 'كمال', ''),
(97, 'زواق', 'قدور', ''),
(98, 'شولي', 'عياش', ''),
(99, 'آمبارك', 'محمد عدلان', ''),
(100, 'عمراني', 'علي', ''),
(101, 'عثوم', 'زبيدة', ''),
(102, 'مريبعي', 'حمزة', ''),
(103, 'فدول', 'كريم', ''),
(104, 'شهر الدين', 'يوسف', ''),
(105, 'عزوزي', 'عبد الرحيم', ''),
(106, 'شنايت', 'حسين', ''),
(107, 'لطرش', 'محمد', ''),
(108, 'سراجية', 'حفصة', ''),
(109, 'راربي', 'فضيلة', ''),
(110, 'بوطواطو', 'رابح', ''),
(111, 'حوري', 'نصيرة', ''),
(112, 'جبري', 'مسعودة', ''),
(113, 'مسعودان', 'حفيظة', ''),
(114, 'مريجة', 'فاطمة الزهراء', '');

-- --------------------------------------------------------

--
-- Table structure for table `entree`
--

CREATE TABLE `entree` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_article` int(10) UNSIGNED NOT NULL,
  `quantity` int(10) UNSIGNED NOT NULL,
  `unit_price` double UNSIGNED NOT NULL,
  `id_be` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `entree`
--

INSERT INTO `entree` (`id`, `id_article`, `quantity`, `unit_price`, `id_be`) VALUES
(1, 1, 10, 4000, 47);

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
  `EMAIL` varchar(255) NOT NULL,
  `RIB` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fournisseur`
--

INSERT INTO `fournisseur` (`id`, `name`, `RC`, `NIF`, `AI`, `NIS`, `TEL`, `FAX`, `ADDRESS`, `EMAIL`, `RIB`) VALUES
(5, 'Supplier A', 'RC123', 'NIF456', 'AI789', 'NIS012', '1234567890', '0987654321', '123 Street', '12345@67890123.456', '45435');

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
  `status` varchar(255) DEFAULT 'active',
  `last_edited` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Table structure for table `retour`
--

CREATE TABLE `retour` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_article` int(10) UNSIGNED NOT NULL,
  `quantity` int(10) UNSIGNED NOT NULL,
  `id_br` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `retour`
--

INSERT INTO `retour` (`id`, `id_article`, `quantity`, `id_br`) VALUES
(1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE `service` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `chef_service_id` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`id`, `name`, `chef_service_id`) VALUES
(1, 'الوسائل العامة والاعلام الالي', 28);

-- --------------------------------------------------------

--
-- Table structure for table `sortie`
--

CREATE TABLE `sortie` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_article` int(10) UNSIGNED NOT NULL,
  `quantity` int(10) UNSIGNED NOT NULL,
  `id_bs` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sortie`
--

INSERT INTO `sortie` (`id`, `id_article`, `quantity`, `id_bs`) VALUES
(27, 1, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `stock_adjustment`
--

CREATE TABLE `stock_adjustment` (
  `id` int(10) UNSIGNED NOT NULL,
  `article_id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `adjustment_date` datetime NOT NULL,
  `quantity` int(10) UNSIGNED NOT NULL,
  `adjustment_type` enum('increase','decrease') NOT NULL,
  `remarks` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stock_adjustment`
--

INSERT INTO `stock_adjustment` (`id`, `article_id`, `user_id`, `adjustment_date`, `quantity`, `adjustment_type`, `remarks`) VALUES
(11, 1, 2, '2024-11-20 00:00:00', 16, 'increase', 'promiere fois'),
(12, 1, 2, '2024-11-20 00:00:00', 1, 'increase', 'خطا'),
(13, 1, 2, '2024-11-20 00:00:00', 1, 'decrease', 'ارجاع من طرف فلان');

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
(2, 'user1', 'password1', 'Admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `article`
--
ALTER TABLE `article`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `article_id_category_foreign` (`id_category`);

--
-- Indexes for table `bon_entree`
--
ALTER TABLE `bon_entree`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bon_entree_id_fournisseur_index` (`id_fournisseur`);

--
-- Indexes for table `bon_retour`
--
ALTER TABLE `bon_retour`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bon_retour_id_employeur_index` (`id_employeur`),
  ADD KEY `bon_retour_id_service_index` (`id_service`);

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
-- Indexes for table `retour`
--
ALTER TABLE `retour`
  ADD PRIMARY KEY (`id`),
  ADD KEY `retour_id_article_index` (`id_article`),
  ADD KEY `retour_bon_retour_relation` (`id_br`);

--
-- Indexes for table `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`id`),
  ADD KEY `chef_service_employer_relation` (`chef_service_id`);

--
-- Indexes for table `sortie`
--
ALTER TABLE `sortie`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sortie_id_article_index` (`id_article`),
  ADD KEY `id_bs` (`id_bs`);

--
-- Indexes for table `stock_adjustment`
--
ALTER TABLE `stock_adjustment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `stock_adjustment_id_article_foreign` (`article_id`),
  ADD KEY `stock_adjustment_id_user_foreign` (`user_id`);

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
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `bon_entree`
--
ALTER TABLE `bon_entree`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT for table `bon_retour`
--
ALTER TABLE `bon_retour`
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
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `inventaire_item`
--
ALTER TABLE `inventaire_item`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=105;

--
-- AUTO_INCREMENT for table `localisation`
--
ALTER TABLE `localisation`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `retour`
--
ALTER TABLE `retour`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `service`
--
ALTER TABLE `service`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `sortie`
--
ALTER TABLE `sortie`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `stock_adjustment`
--
ALTER TABLE `stock_adjustment`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

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
-- Constraints for table `bon_retour`
--
ALTER TABLE `bon_retour`
  ADD CONSTRAINT `bon_retour_employeur_relation` FOREIGN KEY (`id_employeur`) REFERENCES `employeur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `bon_retour_service_relation` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `entree_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON UPDATE CASCADE,
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
-- Constraints for table `retour`
--
ALTER TABLE `retour`
  ADD CONSTRAINT `retour_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `retour_bon_retour_relation` FOREIGN KEY (`id_br`) REFERENCES `bon_retour` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `service`
--
ALTER TABLE `service`
  ADD CONSTRAINT `chef_service_employer_relation` FOREIGN KEY (`chef_service_id`) REFERENCES `employeur` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `sortie`
--
ALTER TABLE `sortie`
  ADD CONSTRAINT `sortie_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `sortie_bon_sortie_relation` FOREIGN KEY (`id_bs`) REFERENCES `bon_sortie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `stock_adjustment`
--
ALTER TABLE `stock_adjustment`
  ADD CONSTRAINT `stock_adjustment_id_article_foreign` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  ADD CONSTRAINT `stock_adjustment_id_user_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
