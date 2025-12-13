-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: wassine_schema
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Current Database: `wassine_schema`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `wassine_schema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `wassine_schema`;

--
-- Table structure for table `accept`
--

DROP TABLE IF EXISTS `accept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accept` (
  `service_id` int NOT NULL,
  `traveler_id` int NOT NULL,
  `acceptance_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`service_id`,`traveler_id`),
  KEY `traveler_id` (`traveler_id`),
  CONSTRAINT `accept_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`),
  CONSTRAINT `accept_ibfk_2` FOREIGN KEY (`traveler_id`) REFERENCES `traveler` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(300) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contract`
--

DROP TABLE IF EXISTS `contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract` (
  `contract_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_id` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `transport_fee` decimal(10,2) NOT NULL,
  `pickup_date` datetime DEFAULT NULL,
  `delivery_date` datetime DEFAULT NULL,
  `signed_date` datetime DEFAULT NULL,
  `terms_text` text,
  `payment_method` enum('Cash','Card','Wallet') NOT NULL,
  `payment` enum('Pending','Paid','Refunded') NOT NULL DEFAULT 'Pending',
  `status` enum('Active','Completed','Cancelled','Disputed') NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`contract_id`),
  KEY `vehicle_id` (`vehicle_id`),
  CONSTRAINT `contract_ibfk_1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicle_id`),
  CONSTRAINT `chk_completed_has_delivery` CHECK (((`status` <> _utf8mb4'Completed') or (`delivery_date` is not null))),
  CONSTRAINT `chk_delivery_after_pickup` CHECK (((`pickup_date` is null) or (`delivery_date` is null) or (`delivery_date` >= `pickup_date`))),
  CONSTRAINT `chk_fee_pos` CHECK ((`transport_fee` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=303 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conversation`
--

DROP TABLE IF EXISTS `conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversation` (
  `conversation_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `sender_id` int NOT NULL,
  `traveler_id` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_message_at` datetime DEFAULT NULL,
  `status` enum('open','closed','archived') DEFAULT 'open',
  PRIMARY KEY (`conversation_id`),
  UNIQUE KEY `service_id` (`service_id`,`sender_id`,`traveler_id`),
  KEY `sender_id` (`sender_id`),
  KEY `traveler_id` (`traveler_id`),
  KEY `idx_conv_last_activity` (`last_message_at`),
  CONSTRAINT `conversation_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`),
  CONSTRAINT `conversation_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `sender` (`user_id`),
  CONSTRAINT `conversation_ibfk_3` FOREIGN KEY (`traveler_id`) REFERENCES `traveler` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=403 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `make_contract`
--

DROP TABLE IF EXISTS `make_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `make_contract` (
  `service_id` int NOT NULL,
  `sender_id` int NOT NULL,
  `traveler_id` int NOT NULL,
  `admin_id` int NOT NULL,
  `contract_id` int NOT NULL,
  PRIMARY KEY (`service_id`,`sender_id`,`traveler_id`,`admin_id`,`contract_id`),
  KEY `sender_id` (`sender_id`),
  KEY `traveler_id` (`traveler_id`),
  KEY `admin_id` (`admin_id`),
  KEY `idx_mc_contract` (`contract_id`),
  CONSTRAINT `make_contract_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`),
  CONSTRAINT `make_contract_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `sender` (`user_id`),
  CONSTRAINT `make_contract_ibfk_3` FOREIGN KEY (`traveler_id`) REFERENCES `traveler` (`user_id`),
  CONSTRAINT `make_contract_ibfk_4` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`admin_id`),
  CONSTRAINT `make_contract_ibfk_5` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `conversation_id` int NOT NULL,
  `author_user_id` int NOT NULL,
  `body` text NOT NULL,
  `sent_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`),
  KEY `idx_msg_conv_time` (`conversation_id`,`sent_at`),
  KEY `idx_msg_author` (`author_user_id`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`conversation_id`),
  CONSTRAINT `message_ibfk_2` FOREIGN KEY (`author_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate`
--

DROP TABLE IF EXISTS `rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rate` (
  `sender_id` int NOT NULL,
  `contract_id` int NOT NULL,
  `score` int NOT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `date_given` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sender_id`,`contract_id`),
  KEY `contract_id` (`contract_id`),
  CONSTRAINT `rate_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `sender` (`user_id`),
  CONSTRAINT `rate_ibfk_2` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`contract_id`),
  CONSTRAINT `chk_score_range` CHECK ((`score` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sender`
--

DROP TABLE IF EXISTS `sender`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sender` (
  `user_id` int NOT NULL,
  `verified` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `sender_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(120) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `parcel_weight_kg` decimal(6,2) NOT NULL,
  `category` enum('Documents','Electronics','Clothes','Other') NOT NULL,
  `pickup_address` varchar(250) NOT NULL,
  `dropoff_address` varchar(250) NOT NULL,
  `pickup_time_from` datetime NOT NULL,
  `pickup_time_until` datetime NOT NULL,
  `delivery_deadline` datetime NOT NULL,
  `insurance_requested` tinyint(1) DEFAULT '0',
  `offer_price` decimal(10,2) NOT NULL,
  `status` enum('Active','Suspended','Pending','Taken Down','Contracted','Cancelled') NOT NULL DEFAULT 'Pending',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`service_id`),
  KEY `idx_service_status` (`status`),
  KEY `idx_service_category` (`category`),
  KEY `idx_service_pickup_window` (`pickup_time_from`,`pickup_time_until`),
  KEY `idx_service_deadline` (`delivery_deadline`),
  CONSTRAINT `chk_deadline_after_pickup` CHECK ((`delivery_deadline` >= `pickup_time_until`)),
  CONSTRAINT `chk_price_nonnegative` CHECK ((`offer_price` >= 0)),
  CONSTRAINT `chk_time_window` CHECK ((`pickup_time_from` <= `pickup_time_until`)),
  CONSTRAINT `chk_weight_positive` CHECK ((`parcel_weight_kg` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_approval`
--

DROP TABLE IF EXISTS `service_approval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_approval` (
  `admin_id` int NOT NULL,
  `service_id` int NOT NULL,
  `sender_id` int NOT NULL,
  `approved_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`service_id`,`sender_id`,`admin_id`),
  KEY `idx_sa_sender` (`sender_id`),
  KEY `idx_sa_admin` (`admin_id`),
  CONSTRAINT `service_approval_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`admin_id`),
  CONSTRAINT `service_approval_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`),
  CONSTRAINT `service_approval_ibfk_3` FOREIGN KEY (`sender_id`) REFERENCES `sender` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_image`
--

DROP TABLE IF EXISTS `service_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_image` (
  `image_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `image_url` varchar(500) NOT NULL,
  `caption` varchar(200) DEFAULT NULL,
  `is_primary` tinyint(1) DEFAULT '0',
  `sort_order` int DEFAULT '0',
  `uploaded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`image_id`),
  KEY `idx_service_image_service` (`service_id`,`is_primary`,`sort_order`),
  CONSTRAINT `service_image_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `star`
--

DROP TABLE IF EXISTS `star`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `star` (
  `service_id` int NOT NULL,
  `traveler_id` int NOT NULL,
  `star_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`service_id`,`traveler_id`),
  KEY `traveler_id` (`traveler_id`),
  CONSTRAINT `star_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`),
  CONSTRAINT `star_ibfk_2` FOREIGN KEY (`traveler_id`) REFERENCES `traveler` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `traveler`
--

DROP TABLE IF EXISTS `traveler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traveler` (
  `user_id` int NOT NULL,
  `verified` tinyint(1) DEFAULT NULL,
  `bio` varchar(300) DEFAULT NULL,
  `licence_ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `traveler_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `status` enum('SUSPENDED','ACTIVE','BANNED') DEFAULT NULL,
  `country_code` varchar(6) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `country` varchar(20) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `street` varchar(40) DEFAULT NULL,
  `building` varchar(40) DEFAULT NULL,
  `profile_image_url` varchar(500) DEFAULT NULL,
  `id_image_url` varchar(500) DEFAULT NULL,
  `date_created` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `vehicle_id` int NOT NULL AUTO_INCREMENT,
  `traveler_id` int NOT NULL,
  `vehicle_type` enum('Car','Plane','Boat','Other') NOT NULL,
  `make` varchar(60) DEFAULT NULL,
  `model` varchar(60) DEFAULT NULL,
  `vehicle_number` varchar(60) DEFAULT NULL,
  `capacity_weight` decimal(8,2) DEFAULT NULL,
  `status` enum('Available','Under Maintenance','Inactive','Unavailable') DEFAULT 'Available',
  PRIMARY KEY (`vehicle_id`),
  KEY `idx_vehicle_traveler` (`traveler_id`),
  CONSTRAINT `vehicle_ibfk_1` FOREIGN KEY (`traveler_id`) REFERENCES `traveler` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vehicle_image`
--

DROP TABLE IF EXISTS `vehicle_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_image` (
  `image_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_id` int NOT NULL,
  `image_url` varchar(500) NOT NULL,
  `caption` varchar(200) DEFAULT NULL,
  `is_primary` tinyint(1) DEFAULT '0',
  `sort_order` int DEFAULT '0',
  `uploaded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`image_id`),
  KEY `idx_vehicle_image_vehicle` (`vehicle_id`,`is_primary`,`sort_order`),
  CONSTRAINT `vehicle_image_ibfk_1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-13 21:12:40
