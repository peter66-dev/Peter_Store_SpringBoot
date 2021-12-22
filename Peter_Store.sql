CREATE DATABASE  IF NOT EXISTS `peterstore` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `peterstore`;
-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: peterstore
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Kinh dị'),(2,'Ngôn tình'),(3,'Lãng mạn'),(4,'Khoa học'),(5,'Nghệ thuật'),(6,'Cổ tích'),(7,'IT technology'),(8,'Trí tuệ'),(9,'Sức khỏe');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `gender` bit(1) NOT NULL DEFAULT b'1',
  `age` int NOT NULL,
  `points` int DEFAULT '0',
  `role_id` char(10) NOT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (12,'vanphuong@gmail.com','1','Võ Văn Phương','Bùi Quang Là TPHCM',_binary '',20,1,'Admin',_binary ''),(13,'trieumy@gmail.com','1','Triều My','Đình Quới An',_binary '\0',21,14,'User',_binary ''),(15,'kimkieu@gmail.com','1','Kim Kiều','Gò Vấp',_binary '\0',26,10,'User',_binary '');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `order_detail_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `order_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '0',
  `subtotal` double NOT NULL,
  PRIMARY KEY (`order_detail_id`),
  KEY `fk_order_details_products1_idx` (`product_id`),
  KEY `fk_order_details_orders1_idx` (`order_id`),
  CONSTRAINT `fk_order_details_orders1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `fk_order_details_products1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (1,1,11,1,40000),(2,1,12,1,40000),(3,1,13,1,40000),(4,1,14,1,40000),(5,3,15,2,100000),(6,2,16,1,120000),(7,1,17,10,400000),(8,4,18,5,3000000),(9,5,19,2,240000),(10,6,20,2,280000),(11,14,21,2,240000),(12,63,22,1,120000),(13,40,23,2,100000),(14,1,24,2,100000),(15,1,25,2,100000),(16,7,26,2,100000),(17,8,26,2,140000),(18,12,27,2,100000),(19,77,28,2,100000),(20,75,29,2,100000),(21,1,30,2,100000),(22,1,31,2,100000),(23,1,32,8,400000);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `order_date` datetime NOT NULL,
  `discount` float NOT NULL,
  `total` double NOT NULL,
  `status` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`order_id`),
  KEY `fk_orders_customers_idx` (`customer_id`),
  CONSTRAINT `fk_orders_customers` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (11,13,'2021-12-19 14:20:02',0.09,57844.83,_binary ''),(12,13,'2021-12-19 14:22:36',0.13,57844.83,_binary ''),(13,15,'2021-12-19 14:36:14',0.11,57844.83,_binary ''),(14,15,'2021-12-20 02:10:49',0.14,57844.83,_binary ''),(15,13,'2021-12-20 02:12:09',0.29,71472.33,_binary ''),(16,13,'2021-12-20 02:17:21',0.02,57844.83,_binary ''),(17,13,'2021-12-20 04:22:10',0.33,266538.29,_binary ''),(18,15,'2021-12-20 07:12:38',0.04,57844.83,_binary ''),(19,15,'2021-12-20 07:16:34',0.04,230650.14,_binary ''),(20,15,'2021-12-20 07:16:46',0.32,190087.27,_binary ''),(21,15,'2021-12-20 07:17:04',0.2,192685.78,_binary ''),(22,15,'2021-12-20 07:18:11',0.25,57844.83,_binary ''),(23,13,'2021-12-20 07:19:19',0.42,57844.83,_binary ''),(24,13,'2021-12-20 07:19:43',0.1,90063.23,_binary ''),(25,13,'2021-12-20 07:20:02',0.41,58735.39,_binary ''),(26,13,'2021-12-21 04:22:06',0.43,136851.99,_binary ''),(27,15,'2021-12-21 04:54:06',0.2,79540.12,_binary ''),(28,13,'2021-12-21 04:55:25',0.09,91000,_binary ''),(29,13,'2021-12-22 03:47:31',0.06,94000,_binary ''),(30,13,'2021-12-22 03:48:48',0.08,92000,_binary ''),(31,13,'2021-12-22 03:56:14',0.1,90000,_binary ''),(32,13,'2021-12-22 03:57:21',0.07,372000,_binary ''),(33,13,'2021-12-22 04:16:32',0.07,50000,_binary '\0');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `product_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `quantity_in_stock` int NOT NULL,
  `import_price` double NOT NULL,
  `export_price` double NOT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `fk_products_categories1_idx` (`category_id`),
  CONSTRAINT `fk_products_categories1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,1,'Ác mộng kinh hoàng',10,35000,50000,_binary ''),(2,1,'Tấm vải đỏ',20,43000,120000,_binary ''),(3,1,'Địa ngục tầng thứ 19',10,33000,50000,_binary ''),(4,1,'Người tìm xác',20,120000,600000,_binary ''),(5,1,'Hồ sơ bí ẩn',60,90000,120000,_binary ''),(6,1,'Tơ đồng rỏ máu',70,72000,140000,_binary ''),(7,1,'Kỳ án ánh trăng',10,40000,50000,_binary ''),(8,1,'Cẩm tú kỳ bào',80,44000,70000,_binary ''),(9,1,'Quán trọ hoang thôn',42,42000,60000,_binary ''),(10,2,'CÔ PHƯƠNG BẤT TỰ THƯỞNG',12,23000,40000,_binary ''),(11,2,'ĐẶC CÔNG HOÀNG PHI SỞ KIỀU',21,43000,120000,_binary ''),(12,2,'TAM SINH TAM THẾ THẬP LÝ ĐÀO HOA',10,33000,50000,_binary ''),(13,3,'HẬU CUNG NHƯ Ý',25,120000,600000,_binary ''),(14,3,'TRẠCH THIÊN KÝ',60,90000,120000,_binary ''),(15,3,'LONG CHÂU TRUYỀN KỲ',72,72000,140000,_binary ''),(16,3,'THƯỢNG CỔ TÌNH CA',12,40000,50000,_binary ''),(17,3,'CON ĐƯỜNG ĐƯA TIỄN ĐẦY HOA',82,44000,70000,_binary ''),(18,4,'Người truyền ký ức',12,23000,40000,_binary ''),(19,4,'Cỗ máy thời gian',21,43000,120000,_binary ''),(20,4,'Series Animorphs Người Hóa Thú',12,33000,50000,_binary ''),(21,4,'Tam thể',25,120000,600000,_binary ''),(22,4,'George và vụ nổ Big Bang',62,90000,120000,_binary ''),(23,4,'All you need is kill',72,72000,140000,_binary ''),(24,4,'Nhà hàng ở tận cùng vũ trụ',12,40000,50000,_binary ''),(25,4,'451 độ F.',82,44000,70000,_binary ''),(26,5,'The Square',12,23000,40000,_binary ''),(27,5,'The Killing of a Sacred Deer',21,43000,120000,_binary ''),(28,5,'Loveless',12,33000,50000,_binary ''),(29,5,'On Body and Soul',25,120000,600000,_binary ''),(30,5,'Three Billboards Outside Ebbing, Missouri',62,90000,120000,_binary ''),(31,5,'Call Me by Your Name',72,72000,140000,_binary ''),(32,5,'Mudbound',12,40000,50000,_binary ''),(33,5,'Lady Bird',82,44000,70000,_binary ''),(34,6,'The Red Shoes',12,23000,40000,_binary ''),(35,6,'Lọ lem (2015)',21,43000,120000,_binary ''),(36,6,'The Princess Bride (1987)',12,33000,50000,_binary ''),(37,6,'Beauty and the Beast (2017)',25,120000,600000,_binary ''),(38,6,'The Wizard of Oz (1939)',62,100000,120000,_binary ''),(39,6,'The Brothers Grimm (2005)',72,72000,140000,_binary ''),(40,6,'Mirror Mirror (2012)',10,40000,50000,_binary ''),(41,6,'Snow White and the Huntsman (2012)',12,40000,50000,_binary ''),(42,6,'Enchanted (2007)',12,40000,50000,_binary ''),(43,6,'Into the Woods (2014)',82,44000,70000,_binary ''),(44,7,'The Pragmatic Programmer',12,23000,40000,_binary ''),(54,7,'The Clean Coder 21',28,43000,120000,_binary ''),(55,7,'Code Complete',12,33000,50000,_binary ''),(56,7,'The Mythical Man-month',25,120000,600000,_binary ''),(57,7,'Peopleware',62,90000,120000,_binary ''),(58,7,'Advanced Linux Programming',72,72000,140000,_binary ''),(59,7,'C# Yellow Book – Rob Miles',12,40000,50000,_binary ''),(60,7,'C++ GUI Programming With Qt 3',12,40000,50000,_binary ''),(61,7,'Ruby on Rails 4.0 Guide – Stefan Wintermeyer',12,40000,50000,_binary ''),(62,7,'Core HTML5 Canvas',82,44000,70000,_binary ''),(63,8,'Luật Trí Não',27,43000,120000,_binary ''),(64,8,'IQ Trong Nghệ Thuật Thuyết Phục',12,33000,50000,_binary ''),(65,8,'1000 Câu Đố Phát Triển Trí Tuệ',25,120000,600000,_binary ''),(66,8,'Bí Mật Của Một Trí Nhớ Siêu Phàm',62,90000,120000,_binary ''),(67,8,'Sử Dụng Trí Tuệ Của Bạn',72,72000,140000,_binary ''),(68,8,'Tối Ưu Hóa Trí Thông Minh',12,40000,50000,_binary ''),(69,8,'Lối Tư Duy Của Người Thông Minh',12,40000,50000,_binary ''),(70,9,'Nhân tố Enzyme',28,43000,120000,_binary ''),(71,9,'Làm sạch mạch và máu',12,33000,50000,_binary ''),(72,9,'Dinh dưỡng',25,120000,600000,_binary ''),(73,9,'Bill Henderson',62,90000,120000,_binary ''),(74,9,'Thực phẩm khéo dùng nên thuốc',72,72000,140000,_binary ''),(75,9,'Suối nguồn tươi trẻ',12,40000,50000,_binary ''),(76,9,'Yoga cho sức khỏe vững bền',12,40000,50000,_binary ''),(77,9,'Sức Khỏe Trong Tay Bạn',10,40000,50000,_binary ''),(79,1,'Truyện 3 giờ sáng',12,140000,200000,_binary ''),(80,2,'Đôi tình nhân',3,90000,150000,_binary '');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'peterstore'
--

--
-- Dumping routines for database 'peterstore'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-22 11:42:09
