DROP TRIGGER IF EXISTS `bd_aguilera`.`da_pedido_producto_BEFORE_INSERT`;

DELIMITER $$
USE `bd_aguilera`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `da_pedido_producto_BEFORE_INSERT` BEFORE INSERT ON `da_pedido_producto` FOR EACH ROW BEGIN
	DECLARE stockAnterior INT;
	
    SELECT stock INTO stockAnterior FROM da_producto 
    WHERE estado IS NULL AND id = NEW.id_producto FOR UPDATE;
    
    UPDATE da_producto set stock = stockAnterior - NEW.cantidad WHERE id = NEW.id_producto;
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `bd_aguilera`.`da_pedido_producto_BEFORE_UPDATE`;

DELIMITER $$
USE `bd_aguilera`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `da_pedido_producto_BEFORE_UPDATE` BEFORE UPDATE ON `da_pedido_producto` FOR EACH ROW BEGIN
	DECLARE stockAnterior INT;
	
    IF NEW.estado = 'X' THEN
		SELECT stock INTO stockAnterior FROM da_producto
		WHERE estado IS NULL AND id = NEW.id_producto FOR UPDATE;
    
		UPDATE da_producto set stock = stockAnterior + OLD.cantidad  WHERE id = NEW.id_producto;
	END IF;
END$$
DELIMITER ;

CREATE TABLE `bd_aguilera`.`da_nota_cabecera` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_pedido` INT NOT NULL,
  `id_operario` INT NOT NULL,
  `fecha_entrega` DATE NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));

 ALTER TABLE `bd_aguilera`.`da_nota_cabecera` 
ADD INDEX `fk_nota_cabecera_pedido_idx` (`id_pedido` ASC) VISIBLE,
ADD INDEX `fk_nota_cabecera_operario_idx` (`id_operario` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_nota_cabecera` 
ADD CONSTRAINT `fk_nota_cabecera_pedido`
  FOREIGN KEY (`id_pedido`)
  REFERENCES `bd_aguilera`.`da_pedido` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_nota_cabecera_operario`
  FOREIGN KEY (`id_operario`)
  REFERENCES `bd_aguilera`.`da_usuario` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `bd_aguilera`.`da_nota_detalle` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_nota_cabecera` INT NOT NULL,
  `id_disenio` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `estado` VARCHAR(1) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `bd_aguilera`.`da_nota_detalle` 
ADD INDEX `fk_nota_detalle_nota_cabecera_idx` (`id_nota_cabecera` ASC) VISIBLE,
ADD INDEX `fk_nota_detalle_disenio_idx` (`id_disenio` ASC) VISIBLE;
;
ALTER TABLE `bd_aguilera`.`da_nota_detalle` 
ADD CONSTRAINT `fk_nota_detalle_nota_cabecera`
  FOREIGN KEY (`id_nota_cabecera`)
  REFERENCES `bd_aguilera`.`da_nota_cabecera` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_nota_detalle_disenio`
  FOREIGN KEY (`id_disenio`)
  REFERENCES `bd_aguilera`.`da_disenio` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

USE `bd_aguilera`;
DROP procedure IF EXISTS `calcular_comparativo_tallas_sexo`;

DELIMITER $$
USE `bd_aguilera`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `calcular_comparativo_tallas_sexo`(desde DATE, hasta DATE)
BEGIN
	DECLARE finished INTEGER DEFAULT 0;
	DECLARE cantidad_m INT;
    DECLARE cantidad_f INT;
    DECLARE talla_cursor INT;
    DECLARE curTalla CURSOR FOR
		SELECT m.talla_camisa as talla FROM da_medida m 
        INNER JOIN da_pedido p	ON p.id = m.id_pedido 
        INNER JOIN da_cabecera c ON c.id_pedido = p.id 
        WHERE m.estado IS NULL AND p.estado IS NULL AND c.estado IS NULL AND 
        c.fecha_venta BETWEEN desde AND hasta
		GROUP BY talla ORDER BY talla;
    
   -- declare NOT FOUND handler
	DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
   
   DROP TEMPORARY TABLE IF EXISTS comparativo_talla;

	CREATE TEMPORARY TABLE comparativo_talla(
		talla INT,
		sexo VARCHAR(45),
		valor DECIMAL(10,2)
	);
    
    OPEN curTalla;
	bucle : LOOP
		FETCH curTalla INTO talla_cursor;
        
        IF finished = 1 THEN
			LEAVE bucle;
        END IF;
        
        SELECT IF(SUM(m.cantidad) IS NULL,0,SUM(m.cantidad)) INTO cantidad_m
		FROM da_medida m 
		INNER JOIN da_pedido p	ON p.id = m.id_pedido 
		INNER JOIN da_cabecera c ON c.id_pedido = p.id 
		WHERE 	m.estado IS NULL 
				AND p.estado IS NULL
				AND c.estado IS NULL 
				AND c.fecha_venta BETWEEN desde AND hasta
				AND m.talla_camisa = talla_cursor
				AND m.sexo = 'M';
                
		SELECT IF(SUM(m.cantidad) IS NULL,0,SUM(m.cantidad)) INTO cantidad_f
		FROM da_medida m 
		INNER JOIN da_pedido p	ON p.id = m.id_pedido 
		INNER JOIN da_cabecera c ON c.id_pedido = p.id 
		WHERE 	m.estado IS NULL 
				AND p.estado IS NULL
				AND c.estado IS NULL 
				AND c.fecha_venta BETWEEN desde AND hasta
				AND m.talla_camisa = talla_cursor
				AND m.sexo = 'F';
                
		INSERT INTO comparativo_talla(talla,sexo,valor) VALUES (talla_cursor,"MASCULINO", cantidad_m);
		INSERT INTO comparativo_talla(talla,sexo,valor) VALUES (talla_cursor,"FEMENINO", cantidad_f);
        
    END LOOP bucle;
    CLOSE curTalla;
    
    SELECT * FROM comparativo_talla; 
END$$

DELIMITER ;

SET GLOBAL log_bin_trust_function_creators = 1;

USE `bd_aguilera`;
DROP function IF EXISTS `get_categorias`;

DELIMITER $$
USE `bd_aguilera`$$
CREATE FUNCTION `get_categorias` (id_producto INT)
RETURNS VARCHAR(200)
BEGIN
	DECLARE finished INTEGER DEFAULT 0;
    DECLARE categoriaU VARCHAR(45);
	DECLARE categorias VARCHAR(200);
    DECLARE curCategorias CURSOR FOR
		SELECT c.nombre
		FROM da_categoria c
		INNER JOIN da_disenio_categoria dc
			ON dc.id_categoria = c.id
		WHERE 	c.estado IS NULL
				AND dc.estado IS NULL
				AND dc.id_disenio = id_producto;
	
		-- declare NOT FOUND handler
	DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
        
	SET categorias = "";
    
	OPEN curCategorias;
	bucle : LOOP
		FETCH curCategorias INTO categoriaU;
        
        IF finished = 1 THEN
			LEAVE bucle;
        END IF;
        
        SET categorias = CONCAT(categorias, " ", categoriaU);
        
	END LOOP bucle;
    CLOSE curCategorias;
RETURN categorias;
END$$

DELIMITER ;

USE `bd_aguilera`;
DROP procedure IF EXISTS `calcular_producto_mas_vendido`;

DELIMITER $$
USE `bd_aguilera`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `calcular_producto_mas_vendido`(desde DATE, hasta DATE)
BEGIN
	DECLARE finished INTEGER DEFAULT 0;
    DECLARE id_producto INT;
    DECLARE nombre_producto VARCHAR(60);
    DECLARE cantidad_producto INT;
    DECLARE categorias_producto VARCHAR(200);
    DECLARE curProducto CURSOR FOR
		SELECT  d.id, d.nombre, SUM(dt.cantidad) as cantidad
		FROM da_disenio d
		INNER JOIN da_detalle dt ON dt.id_disenio = d.id
		INNER JOIN da_cabecera c ON c.id = dt.id_cabecera
		WHERE d.estado IS NULL AND dt.estado IS NULL
			AND c.estado IS NULL AND c.fecha_venta BETWEEN desde AND hasta
		GROUP BY d.id
		ORDER BY cantidad;
	
    -- declare NOT FOUND handler
	DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
	
    DROP TEMPORARY TABLE IF EXISTS producto_mas_vendido;

	CREATE TEMPORARY TABLE producto_mas_vendido(
		producto VARCHAR(60),
		categorias VARCHAR(200),
		cantidad INT
	);
    
    OPEN curProducto;
	bucle : LOOP
		FETCH curProducto INTO id_producto, nombre_producto, cantidad_producto;
        
        IF finished = 1 THEN
			LEAVE bucle;
        END IF;
		
        select bd_aguilera.get_categorias(id_producto) INTO categorias_producto;
        
        INSERT INTO producto_mas_vendido(producto,categorias,cantidad) VALUES (nombre_producto,categorias_producto, cantidad_producto);
        
	END LOOP bucle;
    CLOSE curProducto; 
    
    SELECT * FROM producto_mas_vendido; 
END$$

DELIMITER ;

