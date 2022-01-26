INSERT INTO `bd_aguilera`.`da_privilegio` (`codigo`, `descripcion`) VALUES ('ROLE_ADMINISTRADOR', 'Administrador');
INSERT INTO `bd_aguilera`.`da_privilegio` (`codigo`, `descripcion`) VALUES ('ROLE_GERENTE', 'Gerente');
INSERT INTO `bd_aguilera`.`da_privilegio` (`codigo`, `descripcion`) VALUES ('ROLE_DISENIADOR', 'Diseñador');
INSERT INTO `bd_aguilera`.`da_privilegio` (`codigo`, `descripcion`) VALUES ('ROLE_OPERARIO', 'Operario');
INSERT INTO `bd_aguilera`.`da_privilegio` (`codigo`, `descripcion`) VALUES ('ROLE_CLIENTE', 'Cliente');

INSERT INTO `bd_aguilera`.`da_usuario` (`usuario`, `clave`) VALUES ('admin', 'd033e22ae348aeb5660fc2140aec35850c4da997');

INSERT INTO `bd_aguilera`.`da_usuario_privilegio` (`id_usuario`, `id_privilegio`) VALUES ('1', '1');

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `orden`) VALUES ('Seguridad', 'Opciones de Seguridad', 'z-icon-cogs', '1');
INSERT INTO `bd_aguilera`.`da_opcion` (`id_opcion_padre`, `titulo`, `descripcion`, `imagen`, `orden`) VALUES ('1', 'Usuarios', 'Mantenedor de Usuarios', 'z-icon-users', '1');

INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('1', '1');
INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('2', '1');

INSERT INTO `bd_aguilera`.`da_opcion` (`id_opcion_padre`, `titulo`, `descripcion`, `imagen`, `orden`) VALUES ('1', 'Privilegios', 'Mantenedor de Privilegios', 'z.icon-list', '2');

INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('3', '1');

UPDATE `bd_aguilera`.`da_opcion` SET `formulario` = '/administrador/usuarioLista.zul' WHERE (`id` = '2');
UPDATE `bd_aguilera`.`da_opcion` SET `imagen` = 'z-icon-list', `formulario` = '/administrador/privilegioLista.zul' WHERE (`id` = '3');
INSERT INTO `bd_aguilera`.`da_opcion` (`id_opcion_padre`, `titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('1', 'Opciones', 'Mantenedor de Opciones', 'z-icon-list', '/administrador/opcionLista.zul', '3');

INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('4', '1');

UPDATE `bd_aguilera`.`da_opcion` SET `estado` = 'X' WHERE (`id` = '4');

UPDATE `bd_aguilera`.`da_opcion_privilegio` SET `estado` = 'X' WHERE (`id` = '4');

INSERT INTO `bd_aguilera`.`da_opcion` (`titulo`, `descripcion`, `imagen`, `formulario`, `orden`) VALUES ('Diseños', 'Mantenedor de Diseños', '', '/diseniador/disenioLista.zul', '2');

INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('5', '1');
INSERT INTO `bd_aguilera`.`da_opcion_privilegio` (`id_opcion`, `id_privilegio`) VALUES ('5', '3');

