package com.aguilera.util;

public class Constantes {
	//Roles del sistema
	public final static String ADMINISTRADOR = "ROLE_ADMINISTRADOR";
	public final static String CLIENTE = "ROLE_CLIENTE";
	public final static String DISENIADOR = "ROLE_DISENIADOR";
	//Tipos de Cita
	public final static String TIPO_CITA_ENTREVISTA = "E";
	public final static String TIPO_CITA_TOMA_MEDIDAS = "T";
	//Estados de la Cita
	public final static String ESTADO_CITA_PENDIENTE = "P";
	public final static String ESTADO_CITA_EJECUTADA = "E";
	public final static String ESTADO_CITA_PERDIDA = "L";
	//Estados de pedido
	public final static String ESTADO_PEDIDO_CREADO = "C";
	public final static String ESTADO_PEDIDO_DISENIO = "D";
	public final static String ESTADO_PEDIDO_DEVUELTO = "R";
	public final static String ESTADO_PEDIDO_APROBADO = "A";
	public final static String ESTADO_PEDIDO_FINALIZADO = "F";
	public final static String ESTADO_PEDIDO_FABRICADO = "B";
	public final static String ESTADO_PEDIDO_PAGADO = "P";
	//Estados de pedido_disenio
	public final static String ESTADO_PEDIDO_D_CREADO = "C";
	public final static String ESTADO_PEDIDO_D_DEVUELTO = "R";
	public final static String ESTADO_PEDIDO_D_APROBADO = "A";
	//Parametros de recargo
	//public final static float TARIFA_RECARGO = 3;
	public final static int MAX_CAMBIOS = 2;
	//IVA
	public final static float IVA = Float.parseFloat("0.14");
	//Tipos de archivos
	public final static String FORMATO_PDF = "pdf";
	public final static String FORMATO_EXCEL = "xls";
	//Tipos de producto
	public final static String TIPO_PRODUCTO_TELAS = "T";
	public final static String TIPO_PRODUCTO_HILOS = "H";
	//Parametros globales
	public final static String PARAMETRO_RECARGO = "recargo";
}
