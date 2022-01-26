ALTER TABLE `bd_aguilera`.`da_producto` 
ADD COLUMN `tipo_producto` VARCHAR(1) NULL AFTER `descripcion`;

CREATE TABLE `bd_aguilera`.`da_parametro` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(100) NULL,
  `valor` DECIMAL(8,2) NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));
  
INSERT INTO `bd_aguilera`.`da_parametro` (`nombre`, `descripcion`, `valor`) VALUES ('recargo', 'valor de recargo para los pedidos', '3');

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Parametros', 'Mantenedor de parámetros', 'z-icon-list', '/administrador/parametroLista.zul', '2');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '3' WHERE (`id` = '5');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '4' WHERE (`id` = '6');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '5' WHERE (`id` = '7');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '6' WHERE (`id` = '8');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '7' WHERE (`id` = '9');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '8' WHERE (`id` = '10');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '9' WHERE (`id` = '11');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '10' WHERE (`id` = '12');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '11' WHERE (`id` = '13');
UPDATE `bd_aguilera`.`da_opcion` SET `orden` = '12' WHERE (`id` = '14');

INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('15', '1');

