-- MySQL dump 10.13  Distrib 8.0.40, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: foody
-- ------------------------------------------------------
-- Server version	11.4.2-MariaDB-ubu2404

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `addresses`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `city` varchar(20) NOT NULL,
  `civic_number` varchar(10) NOT NULL,
  `postal_code` varchar(5) NOT NULL,
  `province` varchar(2) NOT NULL,
  `street` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--


--
-- Table structure for table `bookings`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `date` date NOT NULL,
  `seats` int(11) NOT NULL CHECK (`seats` >= 1),
  `status` enum('ACTIVE','CANCELLED') NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `restaurant_id` bigint(20) NOT NULL,
  `sitting_time_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKib6gjgj2e9binkktxmm175bmm` (`customer_id`),
  KEY `FK3pv7kriicofgxo9l3kgpjbi9i` (`restaurant_id`),
  KEY `FKm5p1e5etmqkexcpbs02ldvi4k` (`sitting_time_id`),
  CONSTRAINT `FK3pv7kriicofgxo9l3kgpjbi9i` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`),
  CONSTRAINT `FKib6gjgj2e9binkktxmm175bmm` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKm5p1e5etmqkexcpbs02ldvi4k` FOREIGN KEY (`sitting_time_id`) REFERENCES `sitting_times` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--


--
-- Table structure for table `categories`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES (5,'Americana');
INSERT INTO `categories` (`id`, `name`) VALUES (2,'Cinese');
INSERT INTO `categories` (`id`, `name`) VALUES (6,'Francese');
INSERT INTO `categories` (`id`, `name`) VALUES (9,'Fusion');
INSERT INTO `categories` (`id`, `name`) VALUES (4,'Giapponese');
INSERT INTO `categories` (`id`, `name`) VALUES (1,'Italiana');
INSERT INTO `categories` (`id`, `name`) VALUES (3,'Messicana');
INSERT INTO `categories` (`id`, `name`) VALUES (8,'Vegan');
INSERT INTO `categories` (`id`, `name`) VALUES (7,'Vegetariana');

--
-- Table structure for table `credit_cards`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_cards` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `token` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_cards`
--


--
-- Table structure for table `dishes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dishes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `description` text NOT NULL,
  `name` varchar(100) NOT NULL,
  `photo_url` varchar(255) DEFAULT NULL,
  `price` decimal(8,2) NOT NULL,
  `restaurant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpslsa9mci7gsfhwukb3mx7s6n` (`restaurant_id`),
  CONSTRAINT `FKpslsa9mci7gsfhwukb3mx7s6n` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dishes`
--


--
-- Table structure for table `order_dish`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_dish` (
  `quantity` int(11) NOT NULL,
  `dish_id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  PRIMARY KEY (`dish_id`,`order_id`),
  KEY `FK1fevhe8ke4l3uebaotqn5ae77` (`order_id`),
  CONSTRAINT `FK1fevhe8ke4l3uebaotqn5ae77` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKsxcogiw9xscinh77ixpor5apo` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_dish`
--


--
-- Table structure for table `orders`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `buyer_id` bigint(20) DEFAULT NULL,
  `status` enum('COMPLETED','CREATED','PAID','PREPARING') NOT NULL,
  `table_code` varchar(10) NOT NULL,
  `restaurant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2m9qulf12xm537bku3jnrrbup` (`restaurant_id`),
  CONSTRAINT `FK2m9qulf12xm537bku3jnrrbup` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--


--
-- Table structure for table `restaurant_category`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant_category` (
  `restaurant_id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL,
  KEY `FK2o35t5gqrlskvh0bmcy0fcad` (`category_id`),
  KEY `FKmr72crorsl7cmhwse8taff6fr` (`restaurant_id`),
  CONSTRAINT `FK2o35t5gqrlskvh0bmcy0fcad` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `FKmr72crorsl7cmhwse8taff6fr` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant_category`
--


--
-- Table structure for table `restaurants`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurants` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `approved` bit(1) NOT NULL,
  `description` text NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone_number` varchar(16) NOT NULL,
  `photo_url` varchar(255) DEFAULT NULL,
  `seats` int(11) NOT NULL CHECK (`seats` >= 0),
  `address_id` bigint(20) NOT NULL,
  `restaurateur_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqmof4bn52u3t3qtpqlt80ypej` (`address_id`),
  UNIQUE KEY `UKauv9pkq4bym6ume5c9so042uc` (`restaurateur_id`),
  CONSTRAINT `FK2x1hkv2g4x7juh3xh3klds0cn` FOREIGN KEY (`restaurateur_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKf77xr396uxbl0crr0pq0qj2q7` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurants`
--


--
-- Table structure for table `reviews`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `rating` int(11) NOT NULL CHECK (`rating` <= 5 and `rating` >= 1),
  `title` varchar(100) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  `dish_id` bigint(20) DEFAULT NULL,
  `restaurant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkquncb1glvrldaui8v52xfd5q` (`customer_id`),
  KEY `FK4d7oru9qv44238vvop3j6rwx0` (`dish_id`),
  KEY `FKsu8ow2q842enesfbqphjrfi5g` (`restaurant_id`),
  CONSTRAINT `FK4d7oru9qv44238vvop3j6rwx0` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`),
  CONSTRAINT `FKkquncb1glvrldaui8v52xfd5q` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsu8ow2q842enesfbqphjrfi5g` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--


--
-- Table structure for table `sitting_times`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sitting_times` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `end` time(6) NOT NULL,
  `start` time(6) NOT NULL,
  `week_day_info_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn3iohhq787tavlil19pnqq0v9` (`week_day_info_id`),
  CONSTRAINT `FKn3iohhq787tavlil19pnqq0v9` FOREIGN KEY (`week_day_info_id`) REFERENCES `week_day_infos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitting_times`
--


--
-- Table structure for table `users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `role` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `birth_date` date NOT NULL,
  `email` varchar(320) NOT NULL,
  `firebase_custom_token` text DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone_number` varchar(16) DEFAULT NULL,
  `surname` varchar(30) NOT NULL,
  `buyer_id` bigint(20) DEFAULT NULL,
  `employer_restaurant_id` bigint(20) DEFAULT NULL,
  `credit_card_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK6loi1uou6eo0uy59wdvdh9hn6` (`credit_card_id`),
  KEY `FK4kirlqeh805q7w69615ugm8jb` (`employer_restaurant_id`),
  CONSTRAINT `FK4kirlqeh805q7w69615ugm8jb` FOREIGN KEY (`employer_restaurant_id`) REFERENCES `restaurants` (`id`),
  CONSTRAINT `FK79uy6vvmr5rq8bmavnmjnsau5` FOREIGN KEY (`credit_card_id`) REFERENCES `credit_cards` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('ADMIN',1,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1980-01-01','admin@example.com','eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTczODYwMTQ0MiwiaWF0IjoxNzM4NTk3ODQyLCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay1xdDl3bUBmb29keS05ZDA3Mi5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInN1YiI6ImZpcmViYXNlLWFkbWluc2RrLXF0OXdtQGZvb2R5LTlkMDcyLmlhbS5nc2VydmljZWFjY291bnQuY29tIiwidWlkIjoiYWRtaW5AZXhhbXBsZS5jb20ifQ.C4PtUTgUMg8OYMRAmiJ8UWR3-x-xuArXVSQoMrVLy5_oPOfPMtgUUqdI1iDYG9aul5l0Op4HOKAY1HxZCMJwbKpe2s2ZUpQdDGKcKWoFpnbjXqaCEwrmg3V4wfOUDmdmonqjQJEqP4Bsj2f95Bt6njuWPLjzPlplmRUIim_8IfOuWhyZ4JhpjYm3L0UEq_PpcGT-KstIaLgOwi9YgexFeQIH6P07nMgqjHm64vqQ0XEbPJJbM6SLrJRNn1mmK5LWzrhsdSshaOpxs9keNz1lDZ2LnFJMc_dpTNuBmMzroXAn9_WGOZUdSSSD_x9MgINuh6MnvUK-jMN5KzC49eQ8mA','Admin','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+123456789','User',NULL,NULL,NULL);

--
-- Table structure for table `week_day_infos`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `week_day_infos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `end_dinner` time(6) DEFAULT NULL,
  `end_launch` time(6) DEFAULT NULL,
  `sitting_time_step` enum('FIFTEEN','SIXTY','THIRTY') NOT NULL,
  `start_dinner` time(6) DEFAULT NULL,
  `start_launch` time(6) DEFAULT NULL,
  `week_day` int(11) NOT NULL CHECK (`week_day` >= 1 and `week_day` <= 7),
  `restaurant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `week_day_restaurant_id_unique` (`week_day`,`restaurant_id`),
  KEY `FKjfpq69u5wso31uqquu86dlmld` (`restaurant_id`),
  CONSTRAINT `FKjfpq69u5wso31uqquu86dlmld` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `week_day_infos`
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-03 17:12:00
