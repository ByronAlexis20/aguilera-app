CREATE DATABASE  IF NOT EXISTS `bd_aguilera`;
USE `bd_aguilera`;

DROP TABLE IF EXISTS `da_archivo`;
CREATE TABLE `da_archivo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `formato` varchar(45) NOT NULL,
  `mime_type` varchar(25) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `da_disenio`;
CREATE TABLE `da_disenio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(60) NOT NULL,
  `precio` decimal(8,2) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `da_disenio_archivo`;
CREATE TABLE `da_disenio_archivo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_disenio` int(11) NOT NULL,
  `id_archivo` int(11) NOT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_disenio_archivo_disenio_idx` (`id_disenio`),
  KEY `fk_disenio_archivo_archivo_idx` (`id_archivo`),
  CONSTRAINT `fk_disenio_archivo_archivo` FOREIGN KEY (`id_archivo`) REFERENCES `da_archivo` (`id`),
  CONSTRAINT `fk_disenio_archivo_disenio` FOREIGN KEY (`id_disenio`) REFERENCES `da_disenio` (`id`)
);

DROP TABLE IF EXISTS `da_opcion`;
CREATE TABLE `da_opcion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_opcion_padre` int(11) DEFAULT NULL,
  `titulo` varchar(45) NOT NULL,
  `descripcion` varchar(60) DEFAULT NULL,
  `imagen` varchar(45) DEFAULT NULL,
  `formulario` varchar(60) DEFAULT NULL,
  `orden` int(11) DEFAULT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_opcion_opcion_idx` (`id_opcion_padre`),
  CONSTRAINT `fk_opcion_opcion` FOREIGN KEY (`id_opcion_padre`) REFERENCES `da_opcion` (`id`)
);

DROP TABLE IF EXISTS `da_persona`;
CREATE TABLE `da_persona` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `identificacion` varchar(10) NOT NULL,
  `nombres` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `telefono` varchar(10) DEFAULT NULL,
  `direccion` varchar(60) DEFAULT NULL,
  `correo` varchar(45) DEFAULT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `da_privilegio`;
CREATE TABLE `da_privilegio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(45) NOT NULL,
  `descripcion` varchar(60) DEFAULT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `da_usuario`;
CREATE TABLE `da_usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_persona` int(11) DEFAULT NULL,
  `usuario` varchar(45) NOT NULL,
  `clave` varchar(45) NOT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_usuario_persona_idx` (`id_persona`),
  CONSTRAINT `fk_usuario_persona` FOREIGN KEY (`id_persona`) REFERENCES `da_persona` (`id`)
);

DROP TABLE IF EXISTS `da_usuario_privilegio`;
CREATE TABLE `da_usuario_privilegio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `id_privilegio` int(11) NOT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_usuario_privilegio_usuario_idx` (`id_usuario`),
  KEY `fk_usuario_privilegio_privilegio_idx` (`id_privilegio`),
  CONSTRAINT `fk_usuario_privilegio_privilegio` FOREIGN KEY (`id_privilegio`) REFERENCES `da_privilegio` (`id`),
  CONSTRAINT `fk_usuario_privilegio_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `da_usuario` (`id`)
);

DROP TABLE IF EXISTS `da_opcion_privilegio`;
CREATE TABLE `da_opcion_privilegio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_opcion` int(11) NOT NULL,
  `id_privilegio` int(11) NOT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_opcion_privilegio_opcion_idx` (`id_opcion`),
  KEY `fk_opcion_privilegio_pivilegio_idx` (`id_privilegio`),
  CONSTRAINT `fk_opcion_privilegio_opcion` FOREIGN KEY (`id_opcion`) REFERENCES `da_opcion` (`id`),
  CONSTRAINT `fk_opcion_privilegio_pivilegio` FOREIGN KEY (`id_privilegio`) REFERENCES `da_privilegio` (`id`)
);