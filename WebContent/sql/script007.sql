ALTER TABLE `bd_aguilera`.`da_pedido_disenio` 
ADD COLUMN `estado_disenio` VARCHAR(1) NOT NULL AFTER `id_disenio`,
ADD COLUMN `critica` VARCHAR(200) NULL AFTER `estado_disenio`;

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Pedidos', 'Gestor de pedidos - Cliente', 'z-icon-list', '/cliente/pedidoLista.zul', '7');
INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('10', '5');

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Pedidos', 'Gestor de pedidos - Operario', 'z-icon-list', '/operario/pedidoLista.zul', '8');
INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('11', '4');

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Pedidos', 'Gestor de pedidos - Adminstrador', 'z-icon-list', '/administrador/pedidoLista.zul', '9');
INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('12', '1');

UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-paint-brush' WHERE (`id` = '5');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-calendar' WHERE (`id` = '6');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-calendar' WHERE (`id` = '7');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-lock' WHERE (`id` = '3');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-shopping-cart' WHERE (`id` = '9');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-shopping-cart' WHERE (`id` = '10');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-shopping-cart' WHERE (`id` = '11');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-shopping-cart' WHERE (`id` = '12');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-paint-brush' WHERE (`id` = '8');
