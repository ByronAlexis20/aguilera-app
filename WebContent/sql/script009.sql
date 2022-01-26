USE `bd_aguilera`;
DROP procedure IF EXISTS `calcular_comparativo_mensual`;

DELIMITER $$
USE `bd_aguilera`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `calcular_comparativo_mensual`(anio INT)
BEGIN
	DECLARE total_ventas DECIMAL(8,2);
    DECLARE total_compras DECIMAL(8,2);
    DECLARE mes INT;
    DECLARE mes_texto VARCHAR(45);

	DROP TEMPORARY TABLE IF EXISTS comparativo_mensual;

	CREATE TEMPORARY TABLE comparativo_mensual(
		mes VARCHAR(45),
		etiqueta VARCHAR(45),
		valor DECIMAL(10,2)
	);
    
    /*ENERO*/
    SET mes = 1;
    SET mes_texto = "ENERO";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*FEBRERO*/
    SET mes = 2;
    SET mes_texto = "FEBRERO";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*MARZO*/
    SET mes = 3;
    SET mes_texto = "MARZO";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*ABRIL*/
    SET mes = 4;
    SET mes_texto = "ABRIL";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*MAYO*/
    SET mes = 5;
    SET mes_texto = "MAYO";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*JUNIO*/
    SET mes = 6;
    SET mes_texto = "JUNIO";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*JULIO*/
    SET mes = 7;
    SET mes_texto = "JULIO";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*AGOSTO*/
    SET mes = 8;
    SET mes_texto = "AGOSTO";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*SEPTIEMBRE*/
    SET mes = 9;
    SET mes_texto = "SEPTIEMBRE";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*OCTUBRE*/
    SET mes = 10;
    SET mes_texto = "OCTUBRE";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*NOVIEMBRE*/
    SET mes = 11;
    SET mes_texto = "NOVIEMBRE";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    /*DICIEMBRE*/
    SET mes = 12;
    SET mes_texto = "DICIEMBRE";
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_ventas
		FROM da_cabecera AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_venta) = mes AND
				YEAR(c.fecha_venta) = anio;
            
	SELECT IF(SUM(c.total) IS NULL, 0, SUM(c.total)) INTO total_compras
		FROM da_compra AS c
		WHERE 	c.estado IS NULL AND
				MONTH(c.fecha_compra) = mes AND
				YEAR(c.fecha_compra) = anio;
                
	INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"VENTAS", total_ventas);
    INSERT INTO comparativo_mensual(mes,etiqueta,valor) VALUES (mes_texto,"COMPRAS", total_compras);
    
    SELECT * FROM comparativo_mensual;
END$$

DELIMITER ;