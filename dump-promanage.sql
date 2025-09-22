-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: promanage
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `equipe_membros`
--

DROP TABLE IF EXISTS `equipe_membros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipe_membros` (
  `id_equipe` int NOT NULL,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id_equipe`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `equipe_membros_ibfk_1` FOREIGN KEY (`id_equipe`) REFERENCES `equipes` (`id_equipe`),
  CONSTRAINT `equipe_membros_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipe_membros`
--

LOCK TABLES `equipe_membros` WRITE;
/*!40000 ALTER TABLE `equipe_membros` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipe_membros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipes`
--

DROP TABLE IF EXISTS `equipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipes` (
  `id_equipe` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `descricao` text,
  `gerente_responsavel_id` int DEFAULT NULL,
  PRIMARY KEY (`id_equipe`),
  KEY `fk_equipe_gerente` (`gerente_responsavel_id`),
  CONSTRAINT `fk_equipe_gerente` FOREIGN KEY (`gerente_responsavel_id`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipes`
--

LOCK TABLES `equipes` WRITE;
/*!40000 ALTER TABLE `equipes` DISABLE KEYS */;
INSERT INTO `equipes` VALUES (1,'Engenharia DEV','Equipe de desenvolvimento de sistemas',2),(8,'Equipe de QA','Equipe responsavel por realizar os testes de qualidade',2);
/*!40000 ALTER TABLE `equipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projeto_equipes`
--

DROP TABLE IF EXISTS `projeto_equipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projeto_equipes` (
  `id_projeto` int NOT NULL,
  `id_equipe` int NOT NULL,
  PRIMARY KEY (`id_projeto`,`id_equipe`),
  KEY `id_equipe` (`id_equipe`),
  CONSTRAINT `projeto_equipes_ibfk_1` FOREIGN KEY (`id_projeto`) REFERENCES `projetos` (`id_projeto`),
  CONSTRAINT `projeto_equipes_ibfk_2` FOREIGN KEY (`id_equipe`) REFERENCES `equipes` (`id_equipe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projeto_equipes`
--

LOCK TABLES `projeto_equipes` WRITE;
/*!40000 ALTER TABLE `projeto_equipes` DISABLE KEYS */;
/*!40000 ALTER TABLE `projeto_equipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projetos`
--

DROP TABLE IF EXISTS `projetos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projetos` (
  `id_projeto` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `descricao` text,
  `data_inicio` date NOT NULL,
  `data_termino_prevista` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `gerente_responsavel_id` int DEFAULT NULL,
  `equipe_responsavel_id` int DEFAULT NULL,
  `data_termino` date DEFAULT NULL,
  PRIMARY KEY (`id_projeto`),
  KEY `gerente_responsavel_id` (`gerente_responsavel_id`),
  CONSTRAINT `projetos_ibfk_1` FOREIGN KEY (`gerente_responsavel_id`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projetos`
--

LOCK TABLES `projetos` WRITE;
/*!40000 ALTER TABLE `projetos` DISABLE KEYS */;
INSERT INTO `projetos` VALUES (15,'Realizar testes de sistema','','2025-09-22','2025-09-23','Pendente',2,8,NULL),(16,'Realizar implementação X o sistema','','2025-10-10','2025-11-10','Pendente',2,1,NULL);
/*!40000 ALTER TABLE `projetos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarefas`
--

DROP TABLE IF EXISTS `tarefas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarefas` (
  `id_tarefa` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) NOT NULL,
  `descricao` text,
  `projeto_id` int DEFAULT NULL,
  `responsavel_id` int DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `data_inicio_prevista` date DEFAULT NULL,
  `data_fim_prevista` date DEFAULT NULL,
  `data_inicio_real` date DEFAULT NULL,
  `data_fim_real` date DEFAULT NULL,
  PRIMARY KEY (`id_tarefa`),
  KEY `projeto_id` (`projeto_id`),
  KEY `responsavel_id` (`responsavel_id`),
  CONSTRAINT `tarefas_ibfk_1` FOREIGN KEY (`projeto_id`) REFERENCES `projetos` (`id_projeto`),
  CONSTRAINT `tarefas_ibfk_2` FOREIGN KEY (`responsavel_id`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarefas`
--

LOCK TABLES `tarefas` WRITE;
/*!40000 ALTER TABLE `tarefas` DISABLE KEYS */;
INSERT INTO `tarefas` VALUES (6,'Realizar teste de tela de login','Testar a tela de login',15,2,'A Fazer','2025-09-22','2025-09-23',NULL,NULL),(7,'Implementar X','',16,2,'A Fazer','2025-10-10','2025-11-10',NULL,NULL);
/*!40000 ALTER TABLE `tarefas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nome_completo` varchar(255) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `email` varchar(255) NOT NULL,
  `cargo` varchar(100) DEFAULT NULL,
  `login` varchar(50) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `perfil` enum('ADMINISTRADOR','GERENTE','COLABORADOR') NOT NULL,
  `equipe_id` int DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (2,'Raphael Perella','51212988892','raphaelperella@hotmail.com','Desenvolvedor','raphael.perella','$2a$10$ZU97THDIfdq/vj040BnRpeyWe2gbo3E2YKJb8mtmhKjseIJS.Yela','ADMINISTRADOR',NULL),(10,'Natasha Cury','37412545888','natasha@mail.com','Estagiário','natasha.cury','$2a$10$HHBDbGFupIk28d2oifqZl.PipUWPTa9U3sS/Ls2bHOnuv33SGGDZG','COLABORADOR',8),(11,'Teste','00000000000','Teste@mail.com','Desenvolvedor','Teste','$2a$10$zetnH0BGC69PgpahvA8Y5.tIbE3l6BoIIOdq4AQXLAIaUhnLMgtEm','ADMINISTRADOR',1);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'promanage'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-21 23:14:57
