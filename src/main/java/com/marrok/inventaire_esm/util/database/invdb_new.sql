-- Database backup
-- Generated on 2024-11-27T05:29:19.887611900

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name_cat` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` VALUES(1, 'الالبسة');
INSERT INTO `category` VALUES(2, 'مواد التنظيف');
INSERT INTO `category` VALUES(3, 'مواد الصيانة');
INSERT INTO `category` VALUES(5, 'ادوات مكتبية');
INSERT INTO `category` VALUES(6, 'المطبخ');

--
-- Table structure for table `fournisseur`
--

CREATE TABLE `fournisseur` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `RC` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `NIF` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `AI` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `NIS` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `TEL` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `FAX` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `ADDRESS` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `EMAIL` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `RIB` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fournisseur`
--

INSERT INTO `fournisseur` VALUES(14, 'Ahmed Bouaicha', '42/00-2661195A 19', '172260300002449', '', '797226030000243', '0665838597', '', 'Cité chaig, Chaiba, W.TIPAZA', '', '003004370001450300 10 agence : Badr kolea');

--
-- Table structure for table `employeur`
--

CREATE TABLE `employeur` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `lastname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employeur`
--

INSERT INTO `employeur` VALUES(1, 'خديجة', 'بن زموري', 'مهندسة دولة  في الاعلام الالي');
INSERT INTO `employeur` VALUES(2, 'يخلف', 'مصطفى', 'مهندس دولة  في الاعلام الالي');
INSERT INTO `employeur` VALUES(3, 'منصور', 'وردة', 'مهندسة دولة  في الاعلام الالي');
INSERT INTO `employeur` VALUES(4, 'قلال', 'عبد الرزاق', 'مهندس دولة  في الاعلام الالي');
INSERT INTO `employeur` VALUES(5, 'بن شهرة ', 'انور', 'مهندس دولة  في الاعلام الالي');
INSERT INTO `employeur` VALUES(8, 'جوادي', 'عدلان كريم', 'الأمين العام');
INSERT INTO `employeur` VALUES(9, 'كحلال', 'جلول', 'مدير التداريب');
INSERT INTO `employeur` VALUES(12, 'أومجكان', 'سميرة', 'طبيبة عامة');
INSERT INTO `employeur` VALUES(13, 'راشد', 'بن زاوي', 'رئيس مصلحة المستخدمين');
INSERT INTO `employeur` VALUES(14, 'قاستل', 'نور الدين', '');
INSERT INTO `employeur` VALUES(15, 'بوقاسي', 'سامية', 'رئيسة مصلحة المكتبة ');
INSERT INTO `employeur` VALUES(16, 'تومي', 'محجوبة', '');
INSERT INTO `employeur` VALUES(17, 'كاتب', 'نسيم', '');
INSERT INTO `employeur` VALUES(18, 'راحو', 'أمال', '');
INSERT INTO `employeur` VALUES(19, 'مروش', 'يونس', '');
INSERT INTO `employeur` VALUES(20, 'شهاب', 'عصام', '');
INSERT INTO `employeur` VALUES(21, 'كسراوي', 'مليكة', '');
INSERT INTO `employeur` VALUES(22, 'حشماوي', 'محمد', '');
INSERT INTO `employeur` VALUES(23, 'بن تواتي', 'شهرزاد', '');
INSERT INTO `employeur` VALUES(24, 'شعبان', 'سمير', '');
INSERT INTO `employeur` VALUES(26, 'بولشلوش', 'وردة', '');
INSERT INTO `employeur` VALUES(27, 'عباس', 'عبد اللطيف', '');
INSERT INTO `employeur` VALUES(28, 'عمراوي', 'عبد الحميد', '');
INSERT INTO `employeur` VALUES(29, 'يحياوي', 'حسينة', '');
INSERT INTO `employeur` VALUES(30, 'بلمادي', 'إيمان', '');
INSERT INTO `employeur` VALUES(31, 'بوطالبي', 'عبد الله', '');
INSERT INTO `employeur` VALUES(32, 'جودي', 'فوزية', '');
INSERT INTO `employeur` VALUES(33, 'قعلول', 'فتحية', '');
INSERT INTO `employeur` VALUES(34, 'بركان', 'ليلى', '');
INSERT INTO `employeur` VALUES(35, 'نايت يحيى', 'بلال', '');
INSERT INTO `employeur` VALUES(36, 'بوطين', 'سلمى', '');
INSERT INTO `employeur` VALUES(37, 'عمار', 'يوسف دليلة', '');
INSERT INTO `employeur` VALUES(38, 'عيش', 'محمد', '');
INSERT INTO `employeur` VALUES(39, 'كيتوس', 'إيدير', '');
INSERT INTO `employeur` VALUES(40, 'بوناب', 'عبد الناصر', '');
INSERT INTO `employeur` VALUES(41, 'ساسي', 'نواري', '');
INSERT INTO `employeur` VALUES(42, 'رعاش', 'سامية', '');
INSERT INTO `employeur` VALUES(43, 'أوزية', 'كنزة', '');
INSERT INTO `employeur` VALUES(44, 'قاسي', 'سهيلة', '');
INSERT INTO `employeur` VALUES(45, 'دباغ', 'وردة', '');
INSERT INTO `employeur` VALUES(46, 'شارف', 'خوجة سمير', '');
INSERT INTO `employeur` VALUES(47, 'قارة', 'علي ذهبيـة', '');
INSERT INTO `employeur` VALUES(48, 'بن عمارة', 'زكية', '');
INSERT INTO `employeur` VALUES(49, 'حسيان', 'زهية', '');
INSERT INTO `employeur` VALUES(50, 'هومة', 'سمية', '');
INSERT INTO `employeur` VALUES(51, 'شعشوع', 'أنيسة', '');
INSERT INTO `employeur` VALUES(52, 'أوزناني', 'فزيلة', '');
INSERT INTO `employeur` VALUES(53, 'رزيقات', 'سجية', '');
INSERT INTO `employeur` VALUES(54, 'ميهوب', 'صليحة', '');
INSERT INTO `employeur` VALUES(55, 'هدروق', 'عبد الكريم', '');
INSERT INTO `employeur` VALUES(56, 'بولحواش', 'مراد', '');
INSERT INTO `employeur` VALUES(57, 'هدروق', 'منور', '');
INSERT INTO `employeur` VALUES(58, 'صالح', 'محمد', '');
INSERT INTO `employeur` VALUES(59, 'شجاري', 'أحمد', '');
INSERT INTO `employeur` VALUES(60, 'قلال', 'عمر', '');
INSERT INTO `employeur` VALUES(61, 'كرامدي', 'عبد القادر', '');
INSERT INTO `employeur` VALUES(62, 'حمود', 'سليم', '');
INSERT INTO `employeur` VALUES(63, 'حمايدية', 'محمد', '');
INSERT INTO `employeur` VALUES(64, 'كودري', 'امبارك', '');
INSERT INTO `employeur` VALUES(65, 'روابحية', 'السعيد', '');
INSERT INTO `employeur` VALUES(66, 'بوخشبة', 'رشيد', '');
INSERT INTO `employeur` VALUES(67, 'رابحي', 'سيد علي', '');
INSERT INTO `employeur` VALUES(68, 'بن زين', 'هندة', '');
INSERT INTO `employeur` VALUES(69, 'ستيحي', 'نور الدين', '');
INSERT INTO `employeur` VALUES(70, 'بن سالم', 'توفيق', '');
INSERT INTO `employeur` VALUES(71, 'ربيعي', 'رابح', '');
INSERT INTO `employeur` VALUES(72, 'حميدي', 'محمد', '');
INSERT INTO `employeur` VALUES(73, 'بقـال', 'محمد الصغير', '');
INSERT INTO `employeur` VALUES(74, 'بلكسة', 'محمد', '');
INSERT INTO `employeur` VALUES(75, 'طروش', 'نورالدين', '');
INSERT INTO `employeur` VALUES(76, 'رزوق', 'بوعلام', '');
INSERT INTO `employeur` VALUES(77, 'بركاني', 'عبد القادر', '');
INSERT INTO `employeur` VALUES(78, 'لكحل', 'حميد', '');
INSERT INTO `employeur` VALUES(79, 'عيادي', 'عبد الرزاق', '');
INSERT INTO `employeur` VALUES(80, 'سدراتي', 'عبد الغني', '');
INSERT INTO `employeur` VALUES(81, 'حوش', 'رمضان', '');
INSERT INTO `employeur` VALUES(82, 'بولقرون', 'ياسين', '');
INSERT INTO `employeur` VALUES(83, 'هني', 'خالد', '');
INSERT INTO `employeur` VALUES(84, 'عوان', 'عبد الله', '');
INSERT INTO `employeur` VALUES(85, 'توازي', 'رضوان', '');
INSERT INTO `employeur` VALUES(86, 'كشرود', 'فاروق', '');
INSERT INTO `employeur` VALUES(87, 'منديلي', 'رضوان', '');
INSERT INTO `employeur` VALUES(88, 'بن ناصر', 'مولود', '');
INSERT INTO `employeur` VALUES(89, 'حدبي', 'عبد القادر', '');
INSERT INTO `employeur` VALUES(90, 'قتــال', 'حســـان', '');
INSERT INTO `employeur` VALUES(91, 'فوغالي', 'نسيم', '');
INSERT INTO `employeur` VALUES(92, 'دليو', 'فيصل', '');
INSERT INTO `employeur` VALUES(93, 'براهيتي', 'مصطفى', '');
INSERT INTO `employeur` VALUES(94, 'عبدي', 'عمر', '');
INSERT INTO `employeur` VALUES(95, 'دبوب', 'حمزة', '');
INSERT INTO `employeur` VALUES(96, 'مخلوف', 'كمال', '');
INSERT INTO `employeur` VALUES(97, 'زواق', 'قدور', '');
INSERT INTO `employeur` VALUES(98, 'شولي', 'عياش', '');
INSERT INTO `employeur` VALUES(99, 'آمبارك', 'محمد عدلان', '');
INSERT INTO `employeur` VALUES(100, 'عمراني', 'علي', '');
INSERT INTO `employeur` VALUES(101, 'عثوم', 'زبيدة', '');
INSERT INTO `employeur` VALUES(102, 'مريبعي', 'حمزة', '');
INSERT INTO `employeur` VALUES(103, 'فدول', 'كريم', '');
INSERT INTO `employeur` VALUES(104, 'شهر الدين', 'يوسف', '');
INSERT INTO `employeur` VALUES(105, 'عزوزي', 'عبد الرحيم', '');
INSERT INTO `employeur` VALUES(106, 'شنايت', 'حسين', '');
INSERT INTO `employeur` VALUES(107, 'لطرش', 'محمد', '');
INSERT INTO `employeur` VALUES(108, 'سراجية', 'حفصة', '');
INSERT INTO `employeur` VALUES(109, 'راربي', 'فضيلة', '');
INSERT INTO `employeur` VALUES(110, 'بوطواطو', 'رابح', '');
INSERT INTO `employeur` VALUES(111, 'حوري', 'نصيرة', '');
INSERT INTO `employeur` VALUES(112, 'جبري', 'مسعودة', '');
INSERT INTO `employeur` VALUES(113, 'مسعودان', 'حفيظة', '');
INSERT INTO `employeur` VALUES(114, 'مريجة', 'فاطمة الزهراء', '');
INSERT INTO `employeur` VALUES(115, 'فرحاوي', 'بوعلام', 'مدير التكوين القاعدي');
INSERT INTO `employeur` VALUES(116, 'ولد محمد', 'مريم', 'مديرة التكوين المستمر');
INSERT INTO `employeur` VALUES(118, 'وابل', 'ياسمين', ' ');
INSERT INTO `employeur` VALUES(119, 'زروخي ', 'أكرم', ' ');
INSERT INTO `employeur` VALUES(120, 'مصطفى', 'صبرينة', ' ');
INSERT INTO `employeur` VALUES(121, 'حمتاني', 'سهام', ' ');
INSERT INTO `employeur` VALUES(122, 'كوريد', 'جميلة', ' ');
INSERT INTO `employeur` VALUES(123, 'بن سعادة', 'زهية', ' ');
INSERT INTO `employeur` VALUES(124, 'بداني', 'سارة', ' ');
INSERT INTO `employeur` VALUES(125, 'عباس', 'سعيد', ' ');
INSERT INTO `employeur` VALUES(126, 'جوباري', 'نعيمة', ' ');
INSERT INTO `employeur` VALUES(127, 'جدو', 'سمية', ' ');
INSERT INTO `employeur` VALUES(128, 'بن نجمة', 'بلقاسم', ' ');
INSERT INTO `employeur` VALUES(129, 'خالدي ', 'عبد الرحمان', ' ');
INSERT INTO `employeur` VALUES(130, 'قادم', 'مريم', ' ');
INSERT INTO `employeur` VALUES(131, 'جلالي', 'ليلى', ' ');
INSERT INTO `employeur` VALUES(132, 'مسلمي', 'بلال', ' ');
INSERT INTO `employeur` VALUES(133, 'جوهر', 'عائشة', ' ');
INSERT INTO `employeur` VALUES(134, 'قرباي', 'لويزة', 'تقني في الاعلام الالي');
INSERT INTO `employeur` VALUES(135, 'جوهر', 'عائشة', ' ');
INSERT INTO `employeur` VALUES(136, 'بوكراتم', 'ايمان', ' ');
INSERT INTO `employeur` VALUES(137, 'مجدوب', 'أم الخير', ' ');
INSERT INTO `employeur` VALUES(138, 'قادم', 'زينب', ' ');
INSERT INTO `employeur` VALUES(139, 'أمغار', 'محند', ' ');
INSERT INTO `employeur` VALUES(140, 'فرصادو', 'محمد', ' ');
INSERT INTO `employeur` VALUES(141, 'عايدي', 'نور الدين', ' ');
INSERT INTO `employeur` VALUES(142, 'دراجي', 'وردة', ' ');
INSERT INTO `employeur` VALUES(143, 'سعدون', 'خديجة', ' ');
INSERT INTO `employeur` VALUES(144, 'سيفي', 'شريفة', ' ');
INSERT INTO `employeur` VALUES(145, 'عسول', 'سارة شهرزاد', ' ');
INSERT INTO `employeur` VALUES(147, 'عبدون', 'ذكرى رانيا', ' ');
INSERT INTO `employeur` VALUES(148, 'مروك', 'عبد الرحمان', 'مهندس دولة  في الاعلام الالي');

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES(2, 'user1', 'password1', 'Admin');
INSERT INTO `user` VALUES(13, 'nacira', '1234', 'Editor');
INSERT INTO `user` VALUES(14, 'hamoud', '12345', 'Manager');

--
-- Table structure for table `service`
--

CREATE TABLE `service` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `chef_service_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `chef_service_employer_relation` (`chef_service_id`),
  CONSTRAINT `chef_service_employer_relation` FOREIGN KEY (`chef_service_id`) REFERENCES `employeur` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `service`
--

INSERT INTO `service` VALUES(1, 'الوسائل العامة والاعلام الالي', 28);
INSERT INTO `service` VALUES(2, 'مصلحة المستخدمين و التكوين', 13);
INSERT INTO `service` VALUES(3, 'مصلحة الميزانية و المحاسبة', 22);
INSERT INTO `service` VALUES(4, 'مصلحة الإيواء و الإطعام و النشاطات', 37);
INSERT INTO `service` VALUES(7, 'مديرية التكوين القاعدي', 115);
INSERT INTO `service` VALUES(8, 'مديرية التكوين المستمر', 116);
INSERT INTO `service` VALUES(9, 'مديرية التداريب', 9);
INSERT INTO `service` VALUES(10, 'مصلحة المكتبة و الوثائق و الأرشيف', 15);

--
-- Table structure for table `localisation`
--

CREATE TABLE `localisation` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `loc_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `id_service` int unsigned DEFAULT NULL,
  `floor` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `localisation_id_service_foreign` (`id_service`),
  CONSTRAINT `localisation_id_service_foreign` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `localisation`
--


--
-- Table structure for table `article`
--

CREATE TABLE `article` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `unite` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remarque` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_category` int unsigned DEFAULT NULL,
  `last_edited` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `article_id_category_foreign` (`id_category`),
  CONSTRAINT `article_id_category_foreign` FOREIGN KEY (`id_category`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `article`
--

INSERT INTO `article` VALUES(2, 'vachette porte', 'piece', '', '', 2, '2024-11-27 12:43:22');
INSERT INTO `article` VALUES(3, 'Coude PVC 200', 'piece', '', '', 3, '2024-11-27 14:20:17');
INSERT INTO `article` VALUES(4, 'Bidon peinture blanc huxe 25 kg', 'kg', '', '', 3, '2024-11-27 14:19:46');
INSERT INTO `article` VALUES(5, 'Sac poubelle', 'piece', '', '', 2, '2024-11-27 14:19:57');
INSERT INTO `article` VALUES(6, 'Cuillère soupe inox luxe', 'piece', '', '', 1, '2024-11-27 09:50:54');
INSERT INTO `article` VALUES(7, 'Tuyau 63 ohd (50m)', 'm', '', '', 3, '2024-11-27 14:21:53');
INSERT INTO `article` VALUES(8, 'Manchon phd 63', 'piece', '', '', 1, '2024-11-27 09:54:20');
INSERT INTO `article` VALUES(9, 'Coude phd 63', 'piece', '', '', 3, '2024-11-27 14:20:26');
INSERT INTO `article` VALUES(10, 'Douille Ceramique', 'piece', '', '', 1, '2024-11-27 10:25:13');
INSERT INTO `article` VALUES(11, 'Dijoncteur GM380V100AMP3 VS', 'piece', '', '', 3, '2024-11-27 10:25:48');
INSERT INTO `article` VALUES(12, 'Lampe LED 9W', 'piece', '', '', 3, '2024-11-27 10:26:31');
INSERT INTO `article` VALUES(13, 'Mitigene lavabo', 'piece', '', '', 2, '2024-11-27 10:27:26');
INSERT INTO `article` VALUES(14, 'Flexible douche 1.60', 'piece', '', '', 1, '2024-11-27 10:29:28');
INSERT INTO `article` VALUES(15, 'Mélangeur de donche', 'piece', '', '', 1, '2024-11-27 10:29:53');
INSERT INTO `article` VALUES(16, 'Gants en Cuire', 'piece', '', '', 2, '2024-11-27 10:37:10');
INSERT INTO `article` VALUES(17, 'Ballet contenier gm', 'piece', '', '', 1, '2024-11-27 10:38:51');
INSERT INTO `article` VALUES(18, 'Sac de platre 40kg', 'kg', '', '', 1, '2024-11-27 10:39:11');
INSERT INTO `article` VALUES(19, 'Rallonge 6/8', 'piece', '', '', 3, '2024-11-27 10:39:31');
INSERT INTO `article` VALUES(20, 'Baguette de soudure en boite', 'piece', '', '', 3, '2024-11-27 10:40:24');
INSERT INTO `article` VALUES(21, 'Lampe led 50w', 'piece', '', '', 1, '2024-11-27 10:40:58');
INSERT INTO `article` VALUES(22, 'Serrure Encastre a canaux', 'piece', '', '', 2, '2024-11-27 10:41:32');
INSERT INTO `article` VALUES(23, 'Serrure Encastre condanation', 'piece', '', '', 2, '2024-11-27 10:41:59');
INSERT INTO `article` VALUES(24, 'Passe de porte condanation', 'piece', '', '', 1, '2024-11-27 10:42:21');
INSERT INTO `article` VALUES(25, 'Boite Agrafes 23/17', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(26, 'Boite Agrafes 23/15', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(27, 'Boite Agrafes 23/19', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(28, 'Boite de coins de lettre dorée', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(29, 'Boite de marqueur fluorescent', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(30, 'Boite de punaise en couleur', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(31, 'Boite de trombone en couleur', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(32, 'Boite trombone', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(33, 'Calculatrice simple', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(34, 'Ciseau', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(35, 'Classeur 100 pochettes (200 vues)', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(36, 'Classeur 80 pochettes (160 vues)', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(37, 'Correcteur blanc stylo', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(38, 'Crayon noir (boite de 12 pieceés)', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(39, 'Cutter 9cm', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(40, 'Dateur en arabe', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(41, 'Dateur en français', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(42, 'Enveloppe armée soufflet', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(43, 'Enveloppe bulle blanche f26', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(44, 'Enveloppe bulle blanche f16', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(45, 'Enveloppe bulle blanche f18', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(46, 'Enveloppe pochette blanche f16', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(47, 'Enveloppe pochette blanche f18', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(48, 'Enveloppe pochette blanche f26', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(49, 'Gomme blanche', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(50, 'Pochette transparente', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(51, 'Post-lt (75mm×75mm)', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(52, 'Rame couverture bristol blanc', 'piece', NULL, NULL, 5, '2024-11-27 13:44:08');
INSERT INTO `article` VALUES(53, 'Rame couverture transparente', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(54, 'Rame papier A4 500 feuilles, 80gr. 1er choix', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(55, 'Registre 2 mains', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(56, 'Registre 3 mains', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(57, 'Registre 4 mains', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(58, 'Registre 5 mains', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(59, 'Spirale de reliure N° 08 (boite)', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(60, 'Spirale de reliure N° 10 (boite)', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(61, 'Baguette de serrage pm', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(62, 'Baguette de serrage mm', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(63, 'Baguette de serrage gm', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(64, 'Boite Stabilo bleu', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(65, 'Boite Stabilo noir', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(66, 'Stylo à bille bleu', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(67, 'Stylo à bille noir', 'piece', NULL, NULL, NULL, '2024-11-27 10:57:04');
INSERT INTO `article` VALUES(68, 'Stylo à bille rouge', 'piece', NULL, NULL, 5, '2024-11-27 14:23:12');

--
-- Table structure for table `bon_entree`
--

CREATE TABLE `bon_entree` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `id_fournisseur` int unsigned NOT NULL,
  `date` datetime NOT NULL,
  `TVA` int NOT NULL DEFAULT '19',
  `document_num` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `last_edited` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `bon_entree_id_fournisseur_index` (`id_fournisseur`),
  CONSTRAINT `fournisseur_bon_entree_relation` FOREIGN KEY (`id_fournisseur`) REFERENCES `fournisseur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_entree`
--

INSERT INTO `bon_entree` VALUES(49, 14, '2024-11-27 00:00:00', 19, '16/2024', '2024-11-27 11:52:07');

--
-- Table structure for table `bon_sortie`
--

CREATE TABLE `bon_sortie` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `id_employeur` int unsigned NOT NULL,
  `id_service` int unsigned NOT NULL,
  `date` datetime NOT NULL,
  `last_edited` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `bon_sortie_id_employeur_index` (`id_employeur`),
  KEY `bon_sortie_id_service_index` (`id_service`),
  CONSTRAINT `bon_sortie_employeur_relation` FOREIGN KEY (`id_employeur`) REFERENCES `employeur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bon_sortie_service_relation` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_sortie`
--


--
-- Table structure for table `bon_retour`
--

CREATE TABLE `bon_retour` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `id_employeur` int unsigned NOT NULL,
  `id_service` int unsigned NOT NULL,
  `date` datetime NOT NULL,
  `return_reason` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `last_edited` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `bon_retour_id_employeur_index` (`id_employeur`),
  KEY `bon_retour_id_service_index` (`id_service`),
  CONSTRAINT `bon_retour_employeur_relation` FOREIGN KEY (`id_employeur`) REFERENCES `employeur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bon_retour_service_relation` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bon_retour`
--


--
-- Table structure for table `entree`
--

CREATE TABLE `entree` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `id_article` int unsigned NOT NULL,
  `quantity` int unsigned NOT NULL,
  `unit_price` double unsigned NOT NULL,
  `id_be` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `entree_id_article_index` (`id_article`),
  KEY `entree_bon_entree_relation` (`id_be`),
  CONSTRAINT `entree_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `entree_bon_entree_relation` FOREIGN KEY (`id_be`) REFERENCES `bon_entree` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `entree`
--

INSERT INTO `entree` VALUES(3, 2, 2, 1500.0, 49);
INSERT INTO `entree` VALUES(4, 3, 1, 900.0, 49);
INSERT INTO `entree` VALUES(5, 4, 1, 2200.0, 49);
INSERT INTO `entree` VALUES(6, 5, 1, 500.0, 49);
INSERT INTO `entree` VALUES(7, 6, 504, 38.0, 49);
INSERT INTO `entree` VALUES(8, 7, 50, 450.0, 49);
INSERT INTO `entree` VALUES(9, 8, 1, 800.0, 49);
INSERT INTO `entree` VALUES(10, 9, 2, 725.0, 49);
INSERT INTO `entree` VALUES(11, 10, 100, 70.0, 49);
INSERT INTO `entree` VALUES(12, 11, 3, 6200.0, 49);
INSERT INTO `entree` VALUES(13, 12, 200, 300.0, 49);
INSERT INTO `entree` VALUES(14, 13, 15, 3800.0, 49);
INSERT INTO `entree` VALUES(15, 14, 50, 850.0, 49);
INSERT INTO `entree` VALUES(16, 15, 15, 850.0, 49);
INSERT INTO `entree` VALUES(17, 16, 50, 450.0, 49);
INSERT INTO `entree` VALUES(18, 17, 3, 800.0, 49);
INSERT INTO `entree` VALUES(19, 18, 4, 450.0, 49);
INSERT INTO `entree` VALUES(20, 19, 1, 1500.0, 49);
INSERT INTO `entree` VALUES(21, 20, 1, 3100.0, 49);
INSERT INTO `entree` VALUES(22, 21, 100, 650.0, 49);
INSERT INTO `entree` VALUES(23, 22, 50, 850.0, 49);
INSERT INTO `entree` VALUES(24, 23, 20, 400.0, 49);
INSERT INTO `entree` VALUES(25, 24, 100, 400.0, 49);

--
-- Table structure for table `sortie`
--

CREATE TABLE `sortie` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `id_article` int unsigned NOT NULL,
  `quantity` int unsigned NOT NULL,
  `id_bs` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sortie_id_article_index` (`id_article`),
  KEY `id_bs` (`id_bs`),
  CONSTRAINT `sortie_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sortie_bon_sortie_relation` FOREIGN KEY (`id_bs`) REFERENCES `bon_sortie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sortie`
--


--
-- Table structure for table `retour`
--

CREATE TABLE `retour` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `id_article` int unsigned NOT NULL,
  `quantity` int unsigned NOT NULL,
  `id_br` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `retour_id_article_index` (`id_article`),
  KEY `retour_bon_retour_relation` (`id_br`),
  CONSTRAINT `retour_article_relation` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `retour_bon_retour_relation` FOREIGN KEY (`id_br`) REFERENCES `bon_retour` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `retour`
--


--
-- Table structure for table `stock_adjustment`
--

CREATE TABLE `stock_adjustment` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `article_id` int unsigned NOT NULL,
  `user_id` int unsigned NOT NULL,
  `adjustment_date` datetime NOT NULL,
  `quantity` int unsigned NOT NULL,
  `adjustment_type` enum('increase','decrease') COLLATE utf8mb4_general_ci NOT NULL,
  `remarks` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `stock_adjustment_id_article_foreign` (`article_id`),
  KEY `stock_adjustment_id_user_foreign` (`user_id`),
  CONSTRAINT `stock_adjustment_id_article_foreign` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `stock_adjustment_id_user_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stock_adjustment`
--


--
-- Table structure for table `inventaire_item`
--

CREATE TABLE `inventaire_item` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `id_article` int unsigned NOT NULL,
  `id_localisation` int unsigned NOT NULL,
  `user_id` int unsigned NOT NULL,
  `time` datetime DEFAULT NULL,
  `num_inventaire` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_employer` int unsigned DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_general_ci DEFAULT 'active',
  `last_edited` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_num_inventaire` (`num_inventaire`),
  KEY `inventaire_item_id_article_foreign` (`id_article`),
  KEY `inventaire_item_id_localisation_foreign` (`id_localisation`),
  KEY `inventaire_item_user_id_foreign` (`user_id`),
  KEY `inventaire_item_id_employer_foreign` (`id_employer`),
  CONSTRAINT `inventaire_item_id_article_foreign` FOREIGN KEY (`id_article`) REFERENCES `article` (`id`),
  CONSTRAINT `inventaire_item_id_employer_foreign` FOREIGN KEY (`id_employer`) REFERENCES `employeur` (`id`),
  CONSTRAINT `inventaire_item_id_localisation_foreign` FOREIGN KEY (`id_localisation`) REFERENCES `localisation` (`id`),
  CONSTRAINT `inventaire_item_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventaire_item`
--


