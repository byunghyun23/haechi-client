CREATE DATABASE `haechi` /*!40100 DEFAULT CHARACTER SET latin1 */;

DROP TABLE IF EXISTS `haechi`.`contracts`;
CREATE TABLE  `haechi`.`contracts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` text,
  `ip` varchar(64) DEFAULT NULL,
  `createdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `haechi`.`issues`;
CREATE TABLE  `haechi`.`issues` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contractid` int(11) NOT NULL,
  `vulnerabilityid` int(11) NOT NULL,
  `line` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `haechi`.`news`;
CREATE TABLE  `haechi`.`news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL,
  `content` varchar(256) DEFAULT NULL,
  `date` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `haechi`.`vulnerability`;
CREATE TABLE  `haechi`.`vulnerability` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL,
  `content` varchar(256) DEFAULT NULL,
  `recommendation` varchar(128) DEFAULT NULL,
  `example` text,
  `links` varchar(64) DEFAULT NULL,
  `implement` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;