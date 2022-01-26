CREATE TABLE `bd_aguilera`.`da_compra` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_producto` INT NOT NULL,
  `fecha_compra` DATE NOT NULL,
  `cantidad` INT NOT NULL,
  `precio_unitario` DECIMAL(8,2) NOT NULL,
  `subtotal` DECIMAL(8,2) NOT NULL,
  `iva` DECIMAL(8,2) NOT NULL,
  `total` DECIMAL(8,2) NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `bd_aguilera`.`da_compra` 
ADD INDEX `fk_compra_producto_idx` (`id_producto` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_compra` 
ADD CONSTRAINT `fk_compra_producto`
  FOREIGN KEY (`id_producto`)
  REFERENCES `bd_aguilera`.`da_producto` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `bd_aguilera`.`da_categoria` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(100) NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));
  
CREATE TABLE `bd_aguilera`.`da_disenio_categoria` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_disenio` INT NOT NULL,
  `id_categoria` INT NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));
  
ALTER TABLE `bd_aguilera`.`da_disenio_categoria` 
ADD INDEX `fk_disenio_categoria_disenio_idx` (`id_disenio` ASC) VISIBLE,
ADD INDEX `fk_disenio_categoria_categoria_idx` (`id_categoria` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_disenio_categoria` 
ADD CONSTRAINT `fk_disenio_categoria_disenio`
  FOREIGN KEY (`id_disenio`)
  REFERENCES `bd_aguilera`.`da_disenio` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_disenio_categoria_categoria`
  FOREIGN KEY (`id_categoria`)
  REFERENCES `bd_aguilera`.`da_categoria` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `bd_aguilera`.`da_pedido_producto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_pedido` INT NOT NULL,
  `id_producto` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `bd_aguilera`.`da_pedido_producto` 
ADD INDEX `fk_pedido_producto_pedido_idx` (`id_pedido` ASC) VISIBLE,
ADD INDEX `fk_pedido_producto_producto_idx` (`id_producto` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_pedido_producto` 
ADD CONSTRAINT `fk_pedido_producto_pedido`
  FOREIGN KEY (`id_pedido`)
  REFERENCES `bd_aguilera`.`da_pedido` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_pedido_producto_producto`
  FOREIGN KEY (`id_producto`)
  REFERENCES `bd_aguilera`.`da_producto` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
© 2022 GitHub, Inc.
Terms