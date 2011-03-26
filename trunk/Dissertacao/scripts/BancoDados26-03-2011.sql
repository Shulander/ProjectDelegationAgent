-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 27, 2011 at 12:49 AM
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
-- Table structure for table `agendas`
--

CREATE TABLE IF NOT EXISTS `agendas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `hora` time NOT NULL,
  `fk_idAtividade` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idAtividade` (`fk_idAtividade`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `agendas`
--


-- --------------------------------------------------------

--
-- Table structure for table `atividades`
--

CREATE TABLE IF NOT EXISTS `atividades` (
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
-- Dumping data for table `atividades`
--

INSERT INTO `atividades` (`id`, `nome`, `tipo`, `dataInicial`, `dataEntrega`, `duracao`, `estado`, `orcamento`, `fk_idMembro`) VALUES
(1, 'Levantar requisitos', 'Análise de requisitos', '2011-03-28', '2011-03-31', NULL, 'disponivel', NULL, NULL),
(2, 'Programar', 'Implementação', '2011-04-04', '2011-03-11', NULL, 'disponivel', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `habilidades`
--

CREATE TABLE IF NOT EXISTS `habilidades` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_idMembro` int(11) NOT NULL,
  `fk_idHabilidade` int(11) NOT NULL,
  `fk_idAtividade` int(11) NOT NULL,
  `nivel` int(11) NOT NULL,
  `xp` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idMembro` (`fk_idMembro`,`fk_idHabilidade`,`fk_idAtividade`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `habilidades`
--

INSERT INTO `habilidades` (`id`, `fk_idMembro`, `fk_idHabilidade`, `fk_idAtividade`, `nivel`, `xp`) VALUES
(1, 1, 6, 0, 3, 0),
(2, 1, 7, 0, 4, 0),
(3, 2, 4, 0, 4, 0),
(4, 0, 6, 1, 3, 0),
(5, 0, 7, 1, 3, 0),
(6, 0, 4, 2, 3, 0);

-- --------------------------------------------------------

--
-- Table structure for table `membros`
--

CREATE TABLE IF NOT EXISTS `membros` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `salario` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`),
  UNIQUE KEY `nome_2` (`nome`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `membros`
--

INSERT INTO `membros` (`id`, `nome`, `salario`) VALUES
(1, 'liane', 2000),
(2, 'henrique', 4000);

-- --------------------------------------------------------

--
-- Table structure for table `tipos_habilidade`
--

CREATE TABLE IF NOT EXISTS `tipos_habilidade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `area` varchar(100) NOT NULL,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `tipos_habilidade`
--

INSERT INTO `tipos_habilidade` (`id`, `area`, `nome`) VALUES
(1, 'Linguagem de Programação', 'Java'),
(2, 'Linguagem de Programação', 'C++'),
(3, 'Linguagem de Programação', 'C'),
(4, 'Linguagem de Programação', 'PHP'),
(5, 'Linguagem de Programação', '.NET'),
(6, 'Modelagem', 'UML'),
(7, 'Social', 'Relacionamento com clientes'),
(8, 'Social', 'Gestão de time');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agendas`
--
ALTER TABLE `agendas`
  ADD CONSTRAINT `agendas_ibfk_1` FOREIGN KEY (`fk_idAtividade`) REFERENCES `atividades` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `atividades`
--
ALTER TABLE `atividades`
  ADD CONSTRAINT `atividades_ibfk_1` FOREIGN KEY (`fk_idMembro`) REFERENCES `membros` (`id`) ON UPDATE CASCADE;
