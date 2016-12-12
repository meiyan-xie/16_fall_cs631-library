-- MySQL dump 10.13  Distrib 5.7.16, for Linux (x86_64)
--
-- Host: localhost    Database: citylib
-- ------------------------------------------------------
-- Server version	5.7.16-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `AUTHOR`
--

DROP TABLE IF EXISTS `AUTHOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUTHOR` (
  `AUTHORID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ANAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`AUTHORID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BOOK`
--

DROP TABLE IF EXISTS `BOOK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BOOK` (
  `DOCID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ISBN` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DOCID`),
  CONSTRAINT `fk_book_document` FOREIGN KEY (`DOCID`) REFERENCES `DOCUMENT` (`DOCID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BORROWS`
--

DROP TABLE IF EXISTS `BORROWS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BORROWS` (
  `BORNUMBER` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `READERID` int(11) unsigned NOT NULL,
  `DOCID` int(11) unsigned NOT NULL,
  `COPYNO` int(11) unsigned NOT NULL,
  `LIBID` int(11) unsigned NOT NULL,
  `BDTIME` datetime DEFAULT NULL,
  `RDTIME` datetime DEFAULT NULL,
  PRIMARY KEY (`BORNUMBER`),
  KEY `fk_borrows_reader` (`READERID`),
  KEY `fk_borrows_copy` (`DOCID`,`COPYNO`,`LIBID`),
  CONSTRAINT `fk_borrows_copy` FOREIGN KEY (`DOCID`, `COPYNO`, `LIBID`) REFERENCES `COPY` (`DOCID`, `COPYNO`, `LIBID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_borrows_reader` FOREIGN KEY (`READERID`) REFERENCES `READER` (`READERID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BRANCH`
--

DROP TABLE IF EXISTS `BRANCH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BRANCH` (
  `LIBID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `LNAME` varchar(255) DEFAULT NULL,
  `LLOCATION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`LIBID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CHIEF_EDITOR`
--

DROP TABLE IF EXISTS `CHIEF_EDITOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHIEF_EDITOR` (
  `EDITOR_ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ENAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`EDITOR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COPY`
--

DROP TABLE IF EXISTS `COPY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COPY` (
  `DOCID` int(11) unsigned NOT NULL,
  `COPYNO` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `LIBID` int(11) unsigned NOT NULL,
  `POSITION` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`COPYNO`,`DOCID`,`LIBID`),
  KEY `fk_copy_doc` (`DOCID`),
  CONSTRAINT `fk_copy_doc` FOREIGN KEY (`DOCID`) REFERENCES `DOCUMENT` (`DOCID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOCUMENT`
--

DROP TABLE IF EXISTS `DOCUMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DOCUMENT` (
  `DOCID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(255) DEFAULT NULL,
  `PDATE` datetime DEFAULT NULL,
  `PUBLISHERID` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`DOCID`),
  KEY `fk_book_publisher` (`PUBLISHERID`),
  CONSTRAINT `fk_book_publisher` FOREIGN KEY (`PUBLISHERID`) REFERENCES `PUBLISHER` (`PUBLISHERID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INV_EDITOR`
--

DROP TABLE IF EXISTS `INV_EDITOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INV_EDITOR` (
  `DOCID` int(11) unsigned NOT NULL,
  `ISSUE_NO` int(11) unsigned NOT NULL,
  `IENAME` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`DOCID`,`ISSUE_NO`,`IENAME`),
  KEY `fk_ie_ji` (`ISSUE_NO`,`DOCID`),
  CONSTRAINT `fk_ie_ji` FOREIGN KEY (`ISSUE_NO`, `DOCID`) REFERENCES `JOURNAL_ISSUE` (`ISSUE_NO`, `DOCID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOURNAL_ISSUE`
--

DROP TABLE IF EXISTS `JOURNAL_ISSUE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JOURNAL_ISSUE` (
  `DOCID` int(11) unsigned NOT NULL,
  `ISSUE_NO` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `SCOPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ISSUE_NO`,`DOCID`),
  KEY `fk_ji_jv_doc` (`DOCID`),
  CONSTRAINT `fk_ji_jv_doc` FOREIGN KEY (`DOCID`) REFERENCES `JOURNAL_VOLUME` (`DOCID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOURNAL_VOLUME`
--

DROP TABLE IF EXISTS `JOURNAL_VOLUME`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JOURNAL_VOLUME` (
  `DOCID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `JVOLUME` varchar(255) DEFAULT NULL,
  `EDITOR_ID` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`DOCID`),
  KEY `fk_jv_editor` (`EDITOR_ID`),
  CONSTRAINT `fk_jv_document` FOREIGN KEY (`DOCID`) REFERENCES `DOCUMENT` (`DOCID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jv_editor` FOREIGN KEY (`EDITOR_ID`) REFERENCES `CHIEF_EDITOR` (`EDITOR_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROCEEDINGS`
--

DROP TABLE IF EXISTS `PROCEEDINGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROCEEDINGS` (
  `DOCID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `CDATE` datetime DEFAULT NULL,
  `CLOCATION` varchar(255) DEFAULT NULL,
  `CEDITOR` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DOCID`),
  CONSTRAINT `fk_proceedings_document` FOREIGN KEY (`DOCID`) REFERENCES `DOCUMENT` (`DOCID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PUBLISHER`
--

DROP TABLE IF EXISTS `PUBLISHER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PUBLISHER` (
  `PUBLISHERID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `PUBNAME` varchar(255) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`PUBLISHERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `READER`
--

DROP TABLE IF EXISTS `READER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `READER` (
  `READERID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `RTYPE` varchar(255) DEFAULT NULL,
  `RNAME` varchar(255) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`READERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RESERVES`
--

DROP TABLE IF EXISTS `RESERVES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESERVES` (
  `RESNUMBER` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `READERID` int(11) unsigned NOT NULL,
  `DOCID` int(11) unsigned NOT NULL,
  `COPYNO` int(11) unsigned NOT NULL,
  `LIBID` int(11) unsigned NOT NULL,
  `DTIME` datetime DEFAULT NULL,
  PRIMARY KEY (`RESNUMBER`),
  KEY `fk_reserve_reader` (`READERID`),
  KEY `fk_reserve_copy` (`DOCID`,`COPYNO`,`LIBID`),
  CONSTRAINT `fk_reserve_copy` FOREIGN KEY (`DOCID`, `COPYNO`, `LIBID`) REFERENCES `COPY` (`DOCID`, `COPYNO`, `LIBID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reserve_reader` FOREIGN KEY (`READERID`) REFERENCES `READER` (`READERID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WRITES`
--

DROP TABLE IF EXISTS `WRITES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WRITES` (
  `AURHORID` int(11) unsigned NOT NULL,
  `DOCID` int(11) unsigned NOT NULL,
  PRIMARY KEY (`AURHORID`,`DOCID`),
  KEY `fk_writes_document` (`DOCID`),
  CONSTRAINT `fk_writes_author` FOREIGN KEY (`AURHORID`) REFERENCES `AUTHOR` (`AUTHORID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_writes_document` FOREIGN KEY (`DOCID`) REFERENCES `DOCUMENT` (`DOCID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-12  0:36:16
