/*Creacion de la tabla medida*/

CREATE TABLE `bd_aguilera`.`da_medida` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_pedido` INT NOT NULL,
  `persona` VARCHAR(45) NULL,
  `sexo` VARCHAR(1) NULL,
  `talla_camisa` INT NULL,
  `talla_pantalon` INT NULL,
  `pecho` DECIMAL(8,2) NULL,
  `torax` DECIMAL(8,2) NULL,
  `cintura` DECIMAL(8,2) NULL,
  `contorno_cadera` DECIMAL(8,2) NULL,
  `hombro` DECIMAL(8,2) NULL,
  `encuentro` DECIMAL(8,2) NULL,
  `cuello` DECIMAL(8,2) NULL,
  `ancho_espalda` DECIMAL(8,2) NULL,
  `muneca` DECIMAL(8,2) NULL,
  `altura_cadera` DECIMAL(8,2) NULL,
  `tiro` DECIMAL(8,2) NULL,
  `rodilla` DECIMAL(8,2) NULL,
  `pierna` DECIMAL(8,2) NULL,
  `codo` DECIMAL(8,2) NULL,
  `entre_pierna` DECIMAL(8,2) NULL,
  `brazo` DECIMAL(8,2) NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));
  
ALTER TABLE `bd_aguilera`.`da_medida` 
ADD INDEX `fk_medida_pedido_idx` (`id_pedido` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_medida` 
ADD CONSTRAINT `fk_medida_pedido`
  FOREIGN KEY (`id_pedido`)
  REFERENCES `bd_aguilera`.`da_pedido` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `bd_aguilera`.`da_medida` 
ADD COLUMN `cantidad` INT NULL AFTER `talla_pantalon`;