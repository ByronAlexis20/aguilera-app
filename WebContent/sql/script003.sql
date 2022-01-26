/*Creación de las nuevas tablas*/
CREATE TABLE `bd_aguilera`.`da_cita` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `fecha` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `tipo` VARCHAR(1) NOT NULL,
  `estado_cita` VARCHAR(1) NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));
  
  ALTER TABLE `bd_aguilera`.`da_cita` 
ADD INDEX `fk_cita_usuario_idx` (`id_cliente` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_cita` 
ADD CONSTRAINT `fk_cita_usuario`
  FOREIGN KEY (`id_cliente`)
  REFERENCES `bd_aguilera`.`da_usuario` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `bd_aguilera`.`da_pedido` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `observacion` VARCHAR(100) NULL,
  `detalle` VARCHAR(200) NULL,
  `estado_pedido` VARCHAR(1) NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));
 
ALTER TABLE `bd_aguilera`.`da_pedido` 
ADD INDEX `fk_pedido_cliente_idx` (`id_cliente` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_pedido` 
ADD CONSTRAINT `fk_pedido_cliente`
  FOREIGN KEY (`id_cliente`)
  REFERENCES `bd_aguilera`.`da_usuario` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
CREATE TABLE `bd_aguilera`.`da_pedido_disenio` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_pedido` INT NOT NULL,
  `id_disenio` INT NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `bd_aguilera`.`da_pedido_disenio` 
ADD INDEX `fk_pedido_disenio_pedido_idx` (`id_pedido` ASC) VISIBLE,
ADD INDEX `fk_pedido_disenio_disenio_idx` (`id_disenio` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_pedido_disenio` 
ADD CONSTRAINT `fk_pedido_disenio_pedido`
  FOREIGN KEY (`id_pedido`)
  REFERENCES `bd_aguilera`.`da_pedido` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_pedido_disenio_disenio`
  FOREIGN KEY (`id_disenio`)
  REFERENCES `bd_aguilera`.`da_disenio` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `bd_aguilera`.`da_cabecera` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `fecha_venta` DATE NOT NULL,
  `subtotal` DECIMAL(8,2) NOT NULL,
  `iva` DECIMAL(8,2) NOT NULL,
  `total` DECIMAL(8,2) NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `bd_aguilera`.`da_cabecera` 
ADD INDEX `fk_cabecera_usuario_idx` (`id_cliente` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_cabecera` 
ADD CONSTRAINT `fk_cabecera_usuario`
  FOREIGN KEY (`id_cliente`)
  REFERENCES `bd_aguilera`.`da_usuario` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `bd_aguilera`.`da_detalle` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_cabecera` INT NOT NULL,
  `id_pedido` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `valor_unitario` DECIMAL(8,2) NOT NULL,
  `subtotal` DECIMAL(8,2) NOT NULL,
  `iva` DECIMAL(8,2) NOT NULL,
  `total` DECIMAL(8,2) NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `bd_aguilera`.`da_detalle` 
ADD INDEX `fk_detalle_cabecera_idx` (`id_cabecera` ASC) VISIBLE,
ADD INDEX `fk_detalle_pedido_idx` (`id_pedido` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_detalle` 
ADD CONSTRAINT `fk_detalle_cabecera`
  FOREIGN KEY (`id_cabecera`)
  REFERENCES `bd_aguilera`.`da_cabecera` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_detalle_pedido`
  FOREIGN KEY (`id_pedido`)
  REFERENCES `bd_aguilera`.`da_pedido` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
