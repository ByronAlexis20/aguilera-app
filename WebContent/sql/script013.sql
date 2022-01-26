ALTER TABLE `bd_aguilera`.`da_disenio` 
ADD COLUMN `fecha_registro` DATETIME NULL AFTER `descripcion`,
ADD COLUMN `diseniador` VARCHAR(90) NULL AFTER `fecha_registro`;

ALTER TABLE `bd_aguilera`.`da_compra` 
ADD COLUMN `numero_factura` VARCHAR(45) NULL AFTER `id_producto`,
ADD COLUMN `ruc_proveedor` VARCHAR(13) NULL AFTER `numero_factura`,
ADD COLUMN `nombre_proveedor` VARCHAR(100) NULL AFTER `ruc_proveedor`;