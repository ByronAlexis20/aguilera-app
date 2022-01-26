ALTER TABLE `bd_aguilera`.`da_cita` 
ADD COLUMN `hora_fin` TIME NULL AFTER `hora_inicio`,
CHANGE COLUMN `hora` `hora_inicio` TIME NOT NULL ;

ALTER TABLE `bd_aguilera`.`da_pedido` 
ADD COLUMN `correcciones` INT NOT NULL AFTER `detalle`,
ADD COLUMN `recargo` DECIMAL(8,2) NOT NULL AFTER `correcciones`;

ALTER TABLE `bd_aguilera`.`da_pedido_disenio` 
ADD COLUMN `cantidad` INT NOT NULL AFTER `id_disenio`;

ALTER TABLE `bd_aguilera`.`da_cabecera` 
DROP FOREIGN KEY `fk_cabecera_usuario`;
ALTER TABLE `bd_aguilera`.`da_cabecera` 
CHANGE COLUMN `id_cliente` `id_pedido` INT(11) NOT NULL ,
ADD INDEX `fk_cabecera_usuario_idx` (`id_pedido` ASC) VISIBLE,
DROP INDEX `fk_cabecera_usuario_idx` ;
;
ALTER TABLE `bd_aguilera`.`da_cabecera` 
ADD CONSTRAINT `fk_cabecera_usuario`
  FOREIGN KEY (`id_pedido`)
  REFERENCES `bd_aguilera`.`da_pedido` (`id`);

ALTER TABLE `bd_aguilera`.`da_cabecera` 
ADD COLUMN `recargo` DECIMAL(8,2) NOT NULL AFTER `subtotal`;

ALTER TABLE `bd_aguilera`.`da_detalle` 
DROP FOREIGN KEY `fk_detalle_pedido`;
ALTER TABLE `bd_aguilera`.`da_detalle` 
CHANGE COLUMN `id_pedido` `id_disenio` INT(11) NOT NULL ,
ADD INDEX `fk_detalle_disenio_idx` (`id_disenio` ASC) VISIBLE,
DROP INDEX `fk_detalle_pedido_idx` ;
;
ALTER TABLE `bd_aguilera`.`da_detalle` 
ADD CONSTRAINT `fk_detalle_disenio`
  FOREIGN KEY (`id_disenio`)
  REFERENCES `bd_aguilera`.`da_disenio` (`id`);

CREATE TABLE `bd_aguilera`.`da_producto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(100) NULL,
  `stock` INT NOT NULL,
  `precio` DECIMAL(8,2) NOT NULL,
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
  
INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Inventario', 'Gestor de inventario', 'z-icon-tags', '/administrador/productoLista.zul', '10');
INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('13', '1');

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Reportes', 'Gestor de reportes', 'z-icon-print', '/administrador/reporteEdita.zul', '11');
INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('14', '1');

SET GLOBAL lc_time_names = 'es_ES';

USE `bd_aguilera`;
DROP procedure IF EXISTS `calcular_comparativo`;

DELIMITER $$
USE `bd_aguilera`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `calcular_comparativo`(fecha_desde DATE, fecha_hasta DATE)
BEGIN
	DECLARE total_ventas DECIMAL(8,2);
    DECLARE total_compras DECIMAL(8,2);

	DROP TEMPORARY TABLE IF EXISTS comparativo;

	CREATE TEMPORARY TABLE comparativo(
		etiqueta VARCHAR(45),
		valor DECIMAL(10,2)
	);
    
    SELECT SUM(c.total) INTO total_ventas
	FROM da_cabecera AS c
	WHERE 	c.estado IS NULL AND
			c.fecha_venta >= fecha_desde AND
			c.fecha_venta <= fecha_hasta;
            
	SELECT SUM(c.total) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				c.fecha_compra >= fecha_desde AND
				c.fecha_compra <= fecha_hasta;
                
	INSERT INTO comparativo(etiqueta,valor) VALUES ("VENTAS", total_ventas);
    INSERT INTO comparativo(etiqueta,valor) VALUES ("COMPRAS", total_compras);
    
    SELECT * FROM comparativo;
END$$

DELIMITER ;

