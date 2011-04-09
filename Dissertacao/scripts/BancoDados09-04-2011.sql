-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 09, 2011 at 09:43 PM
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
  `dataInicio` date NOT NULL,
  `dataFinal` date NOT NULL,
  `horas` int(11) NOT NULL,
  `fk_idMembro` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idMembro` (`fk_idMembro`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Dumping data for table `agendas`
--

INSERT INTO `agendas` (`id`, `dataInicio`, `dataFinal`, `horas`, `fk_idMembro`) VALUES
(1, '2007-05-08', '2007-07-30', 4, 1),
(2, '2007-05-28', '2007-09-28', 8, 2),
(3, '2007-05-08', '2007-08-31', 6, 3),
(4, '2007-09-03', '2007-10-31', 8, 3),
(5, '2007-05-08', '2007-10-31', 6, 4),
(6, '2007-05-08', '2007-09-14', 8, 5),
(7, '2007-05-08', '2007-09-14', 8, 6),
(8, '2007-05-08', '2007-08-31', 8, 7),
(9, '2007-05-08', '2007-08-31', 8, 8),
(10, '2007-05-08', '2007-08-31', 8, 9),
(11, '2007-05-08', '2007-10-31', 8, 10),
(12, '2007-05-08', '2007-11-30', 8, 11),
(13, '2007-05-08', '2007-10-31', 8, 12),
(14, '2007-05-08', '2007-10-31', 8, 13),
(15, '2007-05-08', '2007-10-31', 8, 14);

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
  `estado` enum('bloqueada','disponivel','alocada','terminada') NOT NULL,
  `orcamento` double DEFAULT NULL,
  `fk_idMembro` int(11) DEFAULT NULL,
  `idProjeto` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idMembro` (`fk_idMembro`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `atividades`
--

INSERT INTO `atividades` (`id`, `nome`, `tipo`, `dataInicial`, `dataEntrega`, `duracao`, `estado`, `orcamento`, `fk_idMembro`, `idProjeto`) VALUES
(1, 'P1T1', 'Análise de Requisitos', '2007-05-28', '2007-06-20', NULL, 'disponivel', 25000, NULL, 1),
(2, 'P1T2', 'Projeto de Software', '2007-06-21', '2007-07-13', NULL, 'disponivel', 12000, NULL, 1),
(3, 'P1T3', 'Projeto de Software', '2007-06-21', '2007-07-06', NULL, 'disponivel', 10000, NULL, 1),
(4, 'P1T4', 'Programação', '2007-07-16', '2007-08-17', NULL, 'disponivel', 24000, NULL, 1),
(5, 'P1T5', 'Programação', '2007-07-09', '2007-08-17', NULL, 'disponivel', 15000, NULL, 1),
(6, 'P1T6', 'Testes', '2007-06-21', '2007-08-17', NULL, 'disponivel', 25000, NULL, 1),
(7, 'P1T7', 'Testes', '2007-08-20', '2007-10-12', NULL, 'disponivel', 32000, NULL, 1),
(8, 'P2T1', 'Análise de Requisitos', '2007-06-12', '2007-06-27', NULL, 'disponivel', 6500, NULL, 2),
(9, 'P2T2', 'Projeto de Software', '2007-06-28', '2007-07-13', NULL, 'disponivel', 4000, NULL, 2),
(10, 'P2T3', 'Programação', '2007-07-16', '2007-07-27', NULL, 'disponivel', 7000, NULL, 2),
(11, 'P2T4', 'Testes', '2007-06-28', '2007-07-27', NULL, 'disponivel', 7000, NULL, 2),
(12, 'P2T5', 'Testes', '2007-07-30', '2007-08-10', NULL, 'disponivel', 8000, NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `atividades_predecessoras`
--

CREATE TABLE IF NOT EXISTS `atividades_predecessoras` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_idAtividade` int(11) NOT NULL,
  `fk_idAtividadePredecessora` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idAtividade` (`fk_idAtividade`,`fk_idAtividadePredecessora`),
  KEY `fk_idAtividadePredecessora` (`fk_idAtividadePredecessora`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `atividades_predecessoras`
--

INSERT INTO `atividades_predecessoras` (`id`, `fk_idAtividade`, `fk_idAtividadePredecessora`) VALUES
(1, 2, 1),
(2, 3, 1),
(3, 4, 2),
(4, 5, 3),
(5, 6, 1),
(6, 7, 4),
(7, 7, 5),
(8, 7, 6),
(9, 9, 8),
(10, 10, 9),
(11, 11, 8),
(12, 12, 10),
(13, 12, 11);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=52 ;

--
-- Dumping data for table `habilidades`
--

INSERT INTO `habilidades` (`id`, `fk_idMembro`, `fk_idHabilidade`, `fk_idAtividade`, `nivel`, `xp`) VALUES
(1, 1, 1, 0, 4, 390),
(2, 1, 2, 0, 3, 235),
(3, 2, 1, 0, 3, 500),
(4, 3, 1, 0, 4, 600),
(5, 3, 3, 0, 4, 600),
(6, 3, 4, 0, 4, 150),
(7, 3, 5, 0, 4, 150),
(8, 4, 1, 0, 4, 500),
(9, 4, 3, 0, 4, 600),
(10, 4, 4, 0, 4, 150),
(11, 4, 5, 0, 4, 150),
(12, 5, 3, 0, 3, 600),
(13, 5, 4, 0, 3, 117),
(14, 5, 5, 0, 4, 116),
(15, 5, 8, 0, 3, 116),
(16, 6, 3, 0, 3, 500),
(17, 6, 4, 0, 4, 125),
(18, 6, 5, 0, 4, 125),
(19, 7, 3, 0, 2, 400),
(20, 7, 4, 0, 4, 125),
(21, 7, 5, 0, 3, 125),
(22, 8, 3, 0, 2, 400),
(23, 8, 4, 0, 4, 100),
(24, 8, 5, 0, 3, 100),
(25, 9, 3, 0, 2, 400),
(26, 9, 4, 0, 3, 125),
(27, 9, 5, 0, 3, 125),
(28, 10, 6, 0, 4, 500),
(29, 10, 7, 0, 4, 400),
(30, 11, 6, 0, 4, 350),
(31, 11, 7, 0, 4, 300),
(32, 12, 6, 0, 3, 350),
(33, 12, 7, 0, 3, 300),
(34, 13, 6, 0, 3, 350),
(35, 13, 7, 0, 3, 300),
(36, 14, 7, 0, 3, 300),
(37, 0, 1, 1, 3, 0),
(38, 0, 3, 2, 4, 0),
(39, 0, 3, 3, 3, 0),
(40, 0, 4, 4, 4, 0),
(41, 0, 5, 4, 2, 0),
(42, 0, 4, 5, 3, 0),
(43, 0, 5, 5, 2, 0),
(44, 0, 6, 6, 4, 0),
(45, 0, 7, 7, 4, 0),
(46, 0, 1, 8, 3, 0),
(47, 0, 3, 9, 2, 0),
(48, 0, 4, 10, 3, 0),
(49, 0, 5, 10, 2, 0),
(50, 0, 6, 11, 3, 0),
(51, 0, 7, 12, 3, 0);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `membros`
--

INSERT INTO `membros` (`id`, `nome`, `salario`) VALUES
(1, 'jh', 60),
(2, 'hls', 45),
(3, 'cy', 60),
(4, 'wht', 50),
(5, 'wzg', 50),
(6, 'yjj', 45),
(7, 'dj', 20),
(8, 'fc', 20),
(9, 'zrm', 20),
(10, 'hcy', 40),
(11, 'lt', 45),
(12, 'yf', 40),
(13, 'cp', 40),
(14, 'lq', 20);

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
(1, 'Análise de Requisitos', 'Análise Orientada a Objetos'),
(2, 'Análise de Requisitos', 'Análise estruturada'),
(3, 'Projeto de Software', 'Projeto orientado a objeto'),
(4, 'Programação', 'Linguagem Java'),
(5, 'Programação', 'IDE Eclipse'),
(6, 'Testes', 'Escrever casos de teste'),
(7, 'Testes', 'Executar casos de teste'),
(8, 'Programação', 'Linguagem C#');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agendas`
--
ALTER TABLE `agendas`
  ADD CONSTRAINT `agendas_ibfk_1` FOREIGN KEY (`fk_idMembro`) REFERENCES `membros` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `atividades`
--
ALTER TABLE `atividades`
  ADD CONSTRAINT `atividades_ibfk_1` FOREIGN KEY (`fk_idMembro`) REFERENCES `membros` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `atividades_predecessoras`
--
ALTER TABLE `atividades_predecessoras`
  ADD CONSTRAINT `atividades_predecessoras_ibfk_2` FOREIGN KEY (`fk_idAtividadePredecessora`) REFERENCES `atividades` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `atividades_predecessoras_ibfk_1` FOREIGN KEY (`fk_idAtividade`) REFERENCES `atividades` (`id`) ON UPDATE CASCADE;
