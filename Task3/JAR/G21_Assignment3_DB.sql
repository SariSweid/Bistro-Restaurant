-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: restaurant_main
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `BillId` int NOT NULL AUTO_INCREMENT,
  `reservationId` int DEFAULT NULL,
  `Amount` double DEFAULT NULL,
  `issuedAt` datetime DEFAULT NULL,
  `paid` tinyint DEFAULT NULL,
  PRIMARY KEY (`BillId`),
  KEY `reservationID_idx` (`reservationId`),
  CONSTRAINT `reservationId` FOREIGN KEY (`reservationId`) REFERENCES `reservation` (`reservationID`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservationID` int NOT NULL AUTO_INCREMENT,
  `reservationDate` date DEFAULT NULL,
  `reservationTime` time DEFAULT NULL,
  `numOfGuests` int DEFAULT NULL,
  `confirmationCode` int DEFAULT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED','SEATED','COMPLETED','NOT_SHOWED','WAITLIST') DEFAULT 'PENDING',
  `customerID` int DEFAULT NULL,
  `TableId` int DEFAULT NULL,
  `BillId` int DEFAULT NULL,
  `reservationPlacedDate` date DEFAULT NULL,
  `reservationPlacedTime` time DEFAULT NULL,
  `actualarrivaltime` time DEFAULT NULL,
  `departuretiime` time DEFAULT NULL,
  `isNotified` tinyint DEFAULT '1',
  `paymentReminderSent` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`reservationID`),
  KEY `customerID_idx` (`customerID`),
  KEY `TableId_idx` (`TableId`),
  CONSTRAINT `fk_reservation_table` FOREIGN KEY (`TableId`) REFERENCES `table` (`TableId`),
  CONSTRAINT `fk_reservation_user` FOREIGN KEY (`customerID`) REFERENCES `user` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=539 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `restaurantsettings`
--

DROP TABLE IF EXISTS `restaurantsettings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurantsettings` (
  `Day` enum('SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY') NOT NULL,
  `MaxTables` int DEFAULT NULL,
  `OpeningHours` time DEFAULT NULL,
  `ClosingHours` time DEFAULT NULL,
  PRIMARY KEY (`Day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specialdates`
--

DROP TABLE IF EXISTS `specialdates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialdates` (
  `special_date` date NOT NULL,
  `OpeningHours` time DEFAULT NULL,
  `ClosingHours` time DEFAULT NULL,
  `description` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`special_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table`
--

DROP TABLE IF EXISTS `table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table` (
  `TableId` int NOT NULL,
  `Capacity` int DEFAULT NULL,
  `IsAvailable` tinyint DEFAULT NULL,
  PRIMARY KEY (`TableId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `UserId` int NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `UserName` varchar(45) DEFAULT NULL,
  `MemberShipCode` int DEFAULT NULL,
  `Role` enum('GUEST','SUBSCRIBER','SUPERVISOR','MANAGER') NOT NULL,
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waitinglist`
--

DROP TABLE IF EXISTS `waitinglist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waitinglist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userID` int DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  `numOfGuests` int DEFAULT NULL,
  `confirmationCode` int DEFAULT NULL,
  `WaitDate` date DEFAULT NULL,
  `WaitTime` time DEFAULT NULL,
  `enterdate` date DEFAULT NULL,
  `entertime` time DEFAULT NULL,
  `exitReason` enum('SEATED','CANCELLED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UserID` (`userID`),
  CONSTRAINT `UserID` FOREIGN KEY (`userID`) REFERENCES `user` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-17 20:07:15
