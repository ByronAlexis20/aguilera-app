ALTER TABLE `bd_aguilera`.`da_pedido` 
CHANGE COLUMN `observacion` `observacion` VARCHAR(200) NULL DEFAULT NULL ;

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Pedidos', 'Gestor de pedidos - Diseniador', 'z-icon-list', '/diseniador/pedidoLista.zul', '6');

INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('9', '3');
