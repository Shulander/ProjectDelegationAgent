-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 26, 2011 at 11:05 PM
-- Server version: 5.5.8
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `mestrado`
--

-- --------------------------------------------------------

--
-- Table structure for table `agenda`
--

CREATE TABLE IF NOT EXISTS `agenda` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `hora` time NOT NULL,
  `fk_idAtividade` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idAtividade` (`fk_idAtividade`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `agenda`
--


-- --------------------------------------------------------

--
-- Table structure for table `atividade`
--

CREATE TABLE IF NOT EXISTS `atividade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) NOT NULL,
  `tipo` varchar(200) NOT NULL,
  `dataInicial` date NOT NULL,
  `dataEntrega` date NOT NULL,
  `duracao` double DEFAULT NULL,
  `estado` enum('bloqueada','disponivel','alocada') NOT NULL,
  `orcamento` double DEFAULT NULL,
  `fk_idMembro` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idMembro` (`fk_idMembro`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `atividade`
--

INSERT INTO `atividade` (`id`, `nome`, `tipo`, `dataInicial`, `dataEntrega`, `duracao`, `estado`, `orcamento`, `fk_idMembro`) VALUES
(1, 'Levantar requisitos', 'Análise de requisitos', '2011-03-28', '2011-03-31', NULL, 'disponivel', NULL, NULL),
(2, 'Programar', 'Implementação', '2011-04-04', '2011-03-11', NULL, 'disponivel', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `habilidade`
--

CREATE TABLE IF NOT EXISTS `habilidade` (
  `fk_idMembro` int(11) NOT NULL,
  `fk_idHabilidades` int(11) NOT NULL,
  `nivel` enum('Junior','Pleno','Senior','Master') NOT NULL,
  `xp` int(11) NOT NULL,
  PRIMARY KEY (`fk_idMembro`,`fk_idHabilidades`),
  KEY `fk_idHabilidades` (`fk_idHabilidades`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `habilidade`
--

INSERT INTO `habilidade` (`fk_idMembro`, `fk_idHabilidades`, `nivel`, `xp`) VALUES
(1, 6, 'Senior', 0),
(1, 7, 'Senior', 0),
(1, 8, 'Master', 0),
(2, 1, 'Senior', 0),
(2, 4, 'Master', 0);

-- --------------------------------------------------------

--
-- Table structure for table `habilidades`
--

CREATE TABLE IF NOT EXISTS `habilidades` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `area` varchar(100) NOT NULL,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `habilidades`
--

INSERT INTO `habilidades` (`id`, `area`, `nome`) VALUES
(1, 'Linguagem de Programação', 'Java'),
(2, 'Linguagem de Programação', 'C++'),
(3, 'Linguagem de Programação', 'C'),
(4, 'Linguagem de Programação', 'PHP'),
(5, 'Linguagem de Programação', '.NET'),
(6, 'Modelagem', 'UML'),
(7, 'Social', 'Relacionamento com clientes'),
(8, 'Social', 'Gestão de time');

-- --------------------------------------------------------

--
-- Table structure for table `habilidades_atividade`
--

CREATE TABLE IF NOT EXISTS `habilidades_atividade` (
  `fk_idAtividade` int(11) NOT NULL,
  `fk_idHabilidades` int(11) NOT NULL,
  `nivel` enum('Junior','Pleno','Senior','Master') NOT NULL,
  PRIMARY KEY (`fk_idAtividade`,`fk_idHabilidades`),
  KEY `fk_idHabilidades` (`fk_idHabilidades`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `habilidades_atividade`
--

INSERT INTO `habilidades_atividade` (`fk_idAtividade`, `fk_idHabilidades`, `nivel`) VALUES
(1, 6, 'Senior'),
(1, 7, 'Pleno');

-- --------------------------------------------------------

--
-- Table structure for table `membro`
--

CREATE TABLE IF NOT EXISTS `membro` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `salario` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`),
  UNIQUE KEY `nome_2` (`nome`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `membro`
--

INSERT INTO `membro` (`id`, `nome`, `salario`) VALUES
(1, 'liane', 2000),
(2, 'henrique', 4000);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agenda`
--
ALTER TABLE `agenda`
  ADD CONSTRAINT `agenda_ibfk_1` FOREIGN KEY (`fk_idAtividade`) REFERENCES `atividade` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `atividade`
--
ALTER TABLE `atividade`
  ADD CONSTRAINT `atividade_ibfk_1` FOREIGN KEY (`fk_idMembro`) REFERENCES `membro` (`id`) ON UPDATE CASCADE;
