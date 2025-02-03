-- MySQL dump 10.13  Distrib 8.0.40, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: foody
-- ------------------------------------------------------
-- Server version	5.5.5-10.3.27-MariaDB-0+deb10u1

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`id`, `created_at`, `deleted_at`, `city`, `civic_number`, `postal_code`, `province`, `street`) VALUES (1,'2025-02-02 22:39:39.000000',NULL,'Roma','12','00185','RM','Via Garibaldi');
INSERT INTO `addresses` (`id`, `created_at`, `deleted_at`, `city`, `civic_number`, `postal_code`, `province`, `street`) VALUES (2,'2025-02-02 22:39:39.000000',NULL,'Milano','5','20121','MI','Corso Vittorio Emanuele');
INSERT INTO `addresses` (`id`, `created_at`, `deleted_at`, `city`, `civic_number`, `postal_code`, `province`, `street`) VALUES (3,'2025-02-02 22:39:39.000000',NULL,'Napoli','3','80134','NA','Piazza Dante');
INSERT INTO `addresses` (`id`, `created_at`, `deleted_at`, `city`, `civic_number`, `postal_code`, `province`, `street`) VALUES (4,'2025-02-02 22:39:39.000000',NULL,'Torino','8','10141','TO','Via delle Alpi');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (1,'2025-02-02 22:39:43.000000',NULL,'2025-11-01',2,'ACTIVE',2,1,1);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (2,'2025-02-02 22:39:43.000000',NULL,'2025-11-01',3,'ACTIVE',3,1,2);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (3,'2025-02-02 22:39:43.000000',NULL,'2025-11-01',4,'ACTIVE',3,1,4);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (4,'2025-02-02 22:39:43.000000',NULL,'2025-11-01',2,'ACTIVE',2,1,5);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (5,'2025-02-02 22:39:43.000000',NULL,'2025-11-01',3,'ACTIVE',2,1,6);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (6,'2025-02-02 22:39:43.000000',NULL,'2024-10-22',3,'ACTIVE',4,1,1);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (7,'2025-02-02 22:39:43.000000',NULL,'2024-10-06',2,'ACTIVE',4,2,21);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (8,'2025-02-02 22:39:43.000000',NULL,'2024-10-07',4,'ACTIVE',4,2,29);
INSERT INTO `bookings` (`id`, `created_at`, `deleted_at`, `date`, `seats`, `status`, `customer_id`, `restaurant_id`, `sitting_time_id`) VALUES (9,'2025-02-02 22:39:43.000000',NULL,'2024-10-08',2,'ACTIVE',4,2,37);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dishes`
--

INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (1,'2025-02-02 22:39:42.000000',NULL,'Pasta con guanciale, uova, pecorino e pepe','Carbonara',NULL,12.00,1);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (2,'2025-02-02 22:39:42.000000',NULL,'Pasta con guanciale, pomodoro e pecorino','Amatriciana',NULL,11.50,1);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (3,'2025-02-02 22:39:42.000000',NULL,'Assortimento di nigiri, sashimi e roll','Sushi Misto',NULL,25.00,1);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (4,'2025-02-02 22:39:42.000000',NULL,'Brodo ricco con noodles, maiale e uovo','Ramen di Maiale',NULL,15.00,1);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (5,'2025-02-02 22:39:42.000000',NULL,'Pizza con pomodoro, mozzarella e basilico','Pizza Margherita',NULL,8.50,1);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (6,'2025-02-02 22:39:42.000000',NULL,'Pasta fatta in casa con frutti di mare freschi','Scialatielli ai Frutti di Mare',NULL,18.00,1);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (7,'2025-02-02 22:39:42.000000',NULL,'Specialità piemontese a base di acciughe e aglio','Bagna Cauda',NULL,10.00,2);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (8,'2025-02-02 22:39:42.000000',NULL,'Carne di manzo cotta lentamente nel vino Barolo','Brasato al Barolo',NULL,22.00,2);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (9,'2025-02-02 22:39:42.000000',NULL,'Taglio di carne toscano servito al sangue','Bistecca alla Fiorentina',NULL,35.00,2);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (10,'2025-02-02 22:39:42.000000',NULL,'Zuppa di pomodoro tradizionale toscana','Pappa al Pomodoro',NULL,9.50,2);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (11,'2025-02-02 22:39:42.000000',NULL,'Risotto cremoso con nero di seppia','Risotto al Nero di Seppia',NULL,16.00,2);
INSERT INTO `dishes` (`id`, `created_at`, `deleted_at`, `description`, `name`, `photo_url`, `price`, `restaurant_id`) VALUES (12,'2025-02-02 22:39:42.000000',NULL,'Sardine marinate con cipolle e aceto','Sarde in Saor',NULL,12.00,2);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_dish`
--

INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (2,1,1);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (3,1,5);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (2,1,8);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (1,2,1);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (2,2,3);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (3,3,2);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (1,3,4);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (1,4,2);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (1,4,4);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (2,5,3);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (1,5,5);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (2,6,6);
INSERT INTO `order_dish` (`quantity`, `dish_id`, `order_id`) VALUES (1,7,7);

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (1,'2025-02-02 22:39:44.000000',NULL,2,'PREPARING','TBL001',1);
INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (2,'2025-02-02 22:39:44.000000',NULL,3,'PREPARING','TBL002',1);
INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (3,'2025-02-02 22:39:44.000000',NULL,3,'PREPARING','TBL003',1);
INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (4,'2025-02-02 22:39:44.000000',NULL,2,'PREPARING','TBL004',1);
INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (5,'2025-02-02 22:39:44.000000',NULL,2,'PREPARING','TBL005',1);
INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (6,'2025-02-02 22:39:44.000000',NULL,2,'COMPLETED','TBL006',2);
INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (7,'2025-02-02 22:39:44.000000',NULL,3,'COMPLETED','TBL007',2);
INSERT INTO `orders` (`id`, `created_at`, `deleted_at`, `buyer_id`, `status`, `table_code`, `restaurant_id`) VALUES (8,'2025-02-02 22:39:44.000000',NULL,4,'COMPLETED','TBL008',1);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant_category`
--

INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (1,1);
INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (1,7);
INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (2,4);
INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (2,9);
INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (3,1);
INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (3,8);
INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (4,1);
INSERT INTO `restaurant_category` (`restaurant_id`, `category_id`) VALUES (4,5);

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurants`
--

INSERT INTO `restaurants` (`id`, `created_at`, `deleted_at`, `approved`, `description`, `name`, `phone_number`, `photo_url`, `seats`, `address_id`, `restaurateur_id`) VALUES (1,'2025-02-02 22:39:40.000000',NULL,_binary '','Specialità romane e atmosfera familiare','Trattoria da Mario','+393331234573',NULL,40,1,7);
INSERT INTO `restaurants` (`id`, `created_at`, `deleted_at`, `approved`, `description`, `name`, `phone_number`, `photo_url`, `seats`, `address_id`, `restaurateur_id`) VALUES (2,'2025-02-02 22:39:40.000000',NULL,_binary '','Autentica cucina giapponese con un tocco moderno','Sushi Milano','+393331234574',NULL,30,2,8);
INSERT INTO `restaurants` (`id`, `created_at`, `deleted_at`, `approved`, `description`, `name`, `phone_number`, `photo_url`, `seats`, `address_id`, `restaurateur_id`) VALUES (3,'2025-02-02 22:39:40.000000',NULL,_binary '','Cucina napoletana tradizionale','Osteria Partenopea','+393331234575',NULL,50,3,9);
INSERT INTO `restaurants` (`id`, `created_at`, `deleted_at`, `approved`, `description`, `name`, `phone_number`, `photo_url`, `seats`, `address_id`, `restaurateur_id`) VALUES (4,'2025-02-02 22:39:40.000000',NULL,_binary '','Piatti tipici piemontesi e vini locali','Ristorante delle Alpi','+393331234576',NULL,45,4,10);

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (1,'2025-02-02 22:39:43.000000',NULL,'Un piatto davvero straordinario!',5,'Eccellente',2,1,1);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (2,'2025-02-02 22:39:43.000000',NULL,'Esperienza piacevole e piatti ben curati.',4,'Molto buono',2,2,2);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (3,'2025-02-02 22:39:43.000000',NULL,'Un buon rapporto qualità-prezzo.',3,'Soddisfacente',3,3,3);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (4,'2025-02-02 22:39:43.000000',NULL,'Servizio rapido, ma piatti migliorabili.',3,'Non male',3,4,1);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (5,'2025-02-02 22:39:43.000000',NULL,'Atmosfera e cibo perfetti!',5,'Indimenticabile',2,NULL,2);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (6,'2025-02-02 22:39:43.000000',NULL,'Un ristorante da consigliare.',4,'Piacevole esperienza',3,NULL,2);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (7,'2025-02-02 22:39:43.000000',NULL,'Camerieri gentili e cucina eccellente.',5,'Servizio ottimo',4,NULL,1);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (8,'2025-02-02 22:39:43.000000',NULL,'I Scialatielli erano incredibili!',5,'Eccellente',2,6,1);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (9,'2025-02-02 22:39:43.000000',NULL,'La Bagna Cauda mi ha sorpreso.',4,'Un sapore unico',3,7,2);
INSERT INTO `reviews` (`id`, `created_at`, `deleted_at`, `description`, `rating`, `title`, `customer_id`, `dish_id`, `restaurant_id`) VALUES (10,'2025-02-02 22:39:43.000000',NULL,'Una Carbonara eseguita alla perfezione.',5,'Buonissima',4,1,1);

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
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitting_times`
--

INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (1,'2025-02-02 22:39:42.000000',NULL,'12:30:00.000000','12:00:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (2,'2025-02-02 22:39:42.000000',NULL,'13:00:00.000000','12:30:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (3,'2025-02-02 22:39:42.000000',NULL,'13:30:00.000000','13:00:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (4,'2025-02-02 22:39:42.000000',NULL,'14:00:00.000000','13:30:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (5,'2025-02-02 22:39:42.000000',NULL,'19:30:00.000000','19:00:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (6,'2025-02-02 22:39:42.000000',NULL,'20:00:00.000000','19:30:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (7,'2025-02-02 22:39:42.000000',NULL,'20:30:00.000000','20:00:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (8,'2025-02-02 22:39:42.000000',NULL,'21:00:00.000000','20:30:00.000000',1);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (9,'2025-02-02 22:39:42.000000',NULL,'12:30:00.000000','12:00:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (10,'2025-02-02 22:39:42.000000',NULL,'13:00:00.000000','12:30:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (11,'2025-02-02 22:39:42.000000',NULL,'13:30:00.000000','13:00:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (12,'2025-02-02 22:39:42.000000',NULL,'14:00:00.000000','13:30:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (13,'2025-02-02 22:39:42.000000',NULL,'19:30:00.000000','19:00:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (14,'2025-02-02 22:39:42.000000',NULL,'20:00:00.000000','19:30:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (15,'2025-02-02 22:39:42.000000',NULL,'20:30:00.000000','20:00:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (16,'2025-02-02 22:39:42.000000',NULL,'21:00:00.000000','20:30:00.000000',2);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (17,'2025-02-02 22:39:42.000000',NULL,'19:30:00.000000','19:00:00.000000',3);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (18,'2025-02-02 22:39:42.000000',NULL,'20:00:00.000000','19:30:00.000000',3);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (19,'2025-02-02 22:39:42.000000',NULL,'20:30:00.000000','20:00:00.000000',3);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (20,'2025-02-02 22:39:42.000000',NULL,'21:00:00.000000','20:30:00.000000',3);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (21,'2025-02-02 22:39:42.000000',NULL,'12:00:00.000000','12:30:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (22,'2025-02-02 22:39:42.000000',NULL,'12:30:00.000000','13:00:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (23,'2025-02-02 22:39:42.000000',NULL,'13:00:00.000000','13:30:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (24,'2025-02-02 22:39:42.000000',NULL,'13:30:00.000000','14:00:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (25,'2025-02-02 22:39:42.000000',NULL,'19:00:00.000000','19:30:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (26,'2025-02-02 22:39:42.000000',NULL,'19:30:00.000000','20:00:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (27,'2025-02-02 22:39:42.000000',NULL,'20:00:00.000000','20:30:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (28,'2025-02-02 22:39:42.000000',NULL,'20:30:00.000000','21:00:00.000000',4);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (29,'2025-02-02 22:39:42.000000',NULL,'12:00:00.000000','12:30:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (30,'2025-02-02 22:39:42.000000',NULL,'12:30:00.000000','13:00:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (31,'2025-02-02 22:39:42.000000',NULL,'13:00:00.000000','13:30:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (32,'2025-02-02 22:39:42.000000',NULL,'13:30:00.000000','14:00:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (33,'2025-02-02 22:39:42.000000',NULL,'19:00:00.000000','19:30:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (34,'2025-02-02 22:39:42.000000',NULL,'19:30:00.000000','20:00:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (35,'2025-02-02 22:39:42.000000',NULL,'20:00:00.000000','20:30:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (36,'2025-02-02 22:39:42.000000',NULL,'20:30:00.000000','21:00:00.000000',5);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (37,'2025-02-02 22:39:42.000000',NULL,'19:00:00.000000','19:30:00.000000',6);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (38,'2025-02-02 22:39:42.000000',NULL,'19:30:00.000000','20:00:00.000000',6);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (39,'2025-02-02 22:39:42.000000',NULL,'20:00:00.000000','20:30:00.000000',6);
INSERT INTO `sitting_times` (`id`, `created_at`, `deleted_at`, `end`, `start`, `week_day_info_id`) VALUES (40,'2025-02-02 22:39:42.000000',NULL,'20:30:00.000000','21:00:00.000000',6);

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('ADMIN',1,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1980-01-01','admin@example.com',NULL,'Admin','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+123456789','User',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('CUSTOMER',2,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1990-05-15','mario.rossi@example.com',NULL,'Mario','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234568','Rossi',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('CUSTOMER',3,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1987-09-22','luigi.verdi@example.com',NULL,'Luigi','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234569','Verdi',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('CUSTOMER',4,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1995-03-08','anna.bianchi@example.com',NULL,'Anna','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234570','Bianchi',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('CUSTOMER',5,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1993-10-05','carla.neri@example.com',NULL,'Carla','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234585','Neri',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('CUSTOMER',6,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1991-12-15','giovanni.rossi@example.com',NULL,'Giovanni','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234586','Rossi',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('RESTAURATEUR',7,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1980-12-10','paolo.galli@example.com',NULL,'Paolo','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234571','Galli',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('RESTAURATEUR',8,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1978-11-18','giulia.neri@example.com',NULL,'Giulia','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234572','Neri',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('RESTAURATEUR',9,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1985-04-22','andrea.marchi@example.com',NULL,'Andrea','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234581','Marchi',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('RESTAURATEUR',10,'2025-02-02 22:39:39.000000',NULL,_binary '',NULL,'1990-08-19','silvia.valli@example.com',NULL,'Silvia','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234582','Valli',NULL,NULL,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('WAITER',11,'2025-02-02 22:39:40.000000',NULL,_binary '',NULL,'1992-06-14','giovanni.ferri@example.com',NULL,'Giovanni','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234577','Ferri',NULL,1,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('WAITER',12,'2025-02-02 22:39:40.000000',NULL,_binary '',NULL,'1990-04-25','marco.bianchi@example.com',NULL,'Marco','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234588','Bianchi',NULL,1,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('COOK',13,'2025-02-02 22:39:40.000000',NULL,_binary '',NULL,'1988-03-20','fabio.carra@example.com',NULL,'Fabio','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234579','Carra',NULL,1,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('COOK',14,'2025-02-02 22:39:40.000000',NULL,_binary '',NULL,'1985-09-14','giorgio.verdi@example.com',NULL,'Giorgio','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234590','Verdi',NULL,1,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('WAITER',15,'2025-02-02 22:39:41.000000',NULL,_binary '',NULL,'1994-01-12','marta.longo@example.com',NULL,'Marta','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234578','Longo',NULL,2,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('WAITER',16,'2025-02-02 22:39:41.000000',NULL,_binary '',NULL,'1991-11-30','luca.rossi@example.com',NULL,'Luca','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234589','Rossi',NULL,2,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('COOK',17,'2025-02-02 22:39:41.000000',NULL,_binary '',NULL,'1990-07-25','alessandra.marchi@example.com',NULL,'Alessandra','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234580','Marchi',NULL,2,NULL);
INSERT INTO `users` (`role`, `id`, `created_at`, `deleted_at`, `active`, `avatar_url`, `birth_date`, `email`, `firebase_custom_token`, `name`, `password`, `phone_number`, `surname`, `buyer_id`, `employer_restaurant_id`, `credit_card_id`) VALUES ('COOK',18,'2025-02-02 22:39:41.000000',NULL,_binary '',NULL,'1989-02-18','simone.neri@example.com',NULL,'Simone','$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe','+393331234591','Neri',NULL,2,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `week_day_infos`
--

INSERT INTO `week_day_infos` (`id`, `created_at`, `deleted_at`, `end_dinner`, `end_launch`, `sitting_time_step`, `start_dinner`, `start_launch`, `week_day`, `restaurant_id`) VALUES (1,'2025-02-02 22:39:41.000000',NULL,'21:00:00.000000','14:00:00.000000','THIRTY','19:00:00.000000','12:00:00.000000',1,1);
INSERT INTO `week_day_infos` (`id`, `created_at`, `deleted_at`, `end_dinner`, `end_launch`, `sitting_time_step`, `start_dinner`, `start_launch`, `week_day`, `restaurant_id`) VALUES (2,'2025-02-02 22:39:41.000000',NULL,'21:00:00.000000','14:00:00.000000','THIRTY','19:00:00.000000','12:00:00.000000',2,1);
INSERT INTO `week_day_infos` (`id`, `created_at`, `deleted_at`, `end_dinner`, `end_launch`, `sitting_time_step`, `start_dinner`, `start_launch`, `week_day`, `restaurant_id`) VALUES (3,'2025-02-02 22:39:41.000000',NULL,'21:00:00.000000',NULL,'THIRTY','19:00:00.000000',NULL,3,1);
INSERT INTO `week_day_infos` (`id`, `created_at`, `deleted_at`, `end_dinner`, `end_launch`, `sitting_time_step`, `start_dinner`, `start_launch`, `week_day`, `restaurant_id`) VALUES (4,'2025-02-02 22:39:41.000000',NULL,'21:00:00.000000','14:00:00.000000','THIRTY','19:00:00.000000','12:00:00.000000',1,2);
INSERT INTO `week_day_infos` (`id`, `created_at`, `deleted_at`, `end_dinner`, `end_launch`, `sitting_time_step`, `start_dinner`, `start_launch`, `week_day`, `restaurant_id`) VALUES (5,'2025-02-02 22:39:41.000000',NULL,'21:00:00.000000','14:00:00.000000','THIRTY','19:00:00.000000','12:00:00.000000',2,2);
INSERT INTO `week_day_infos` (`id`, `created_at`, `deleted_at`, `end_dinner`, `end_launch`, `sitting_time_step`, `start_dinner`, `start_launch`, `week_day`, `restaurant_id`) VALUES (6,'2025-02-02 22:39:41.000000',NULL,'21:00:00.000000',NULL,'THIRTY','19:00:00.000000',NULL,3,2);
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-02 22:44:10
