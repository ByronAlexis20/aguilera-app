package com.aguilera.control.administrador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.util.Constantes;
import com.aguilera.util.ReporteUtil;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;

public class ReporteEdita {
	
	private final static String REPORTE_PEDIDOS = "/administrador/reportes/reporte_pedidos.jasper";
	private final static String REPORTE_INVENTARIO = "/administrador/reportes/reporte_inventario.jasper";
	private final static String REPORTE_VENTAS_MESES = "/administrador/reportes/reporte_ventas_meses.jasper";
	private final static String REPORTE_VENTAS_ANIOS = "/administrador/reportes/reporte_ventas_anios.jasper";
	private final static String REPORTE_VENTAS_COMPRAS_FECHAS = "/administrador/reportes/reporte_ventas_compras.jasper";
	private final static String REPORTE_VENTAS_COMPRAS_MESES = "/administrador/reportes/reporte_ventas_compras_meses.jasper";
	private final static String REPORTE_PRODUCTOS_CATEGORIAS = "/administrador/reportes/reporte_productos_categoria.jasper";
	private final static String REPORTE_PRODUCTOS_SEXO = "/administrador/reportes/reporte_productos_sexo.jasper";
	
	//------------------------Reporte cambios por pedido
	private final static String P_FECHA_DESDE_R1 = "__FECHA_DESDE__";
	private final static String P_FECHA_HASTA_R1 = "__FECHA_HASTA__";
	
	@Getter @Setter private Date fechaDesdeRep1;
	@Getter @Setter private Date fechaHastaRep1;
	
	//------------------------Reporte ventas
	private final static String P_ANIO_R2 = "__ANIO__";
	private final static String P_ANIO_DESDE_R2 = "__ANIO_DESDE__";
	private final static String P_ANIO_HASTA_R2 = "__ANIO_HASTA__";
	
	@Getter @Setter private int anioRep2;
	@Getter @Setter private int anioDesdeRep2;
	@Getter @Setter private int anioHastaRep2;
	
	//------------------------Reporte inventario
	private final static String P_TIPO_PRODUCTO_R3 = "__TIPO_PRODUCTO__";
	
	@Getter @Setter private String tipoProductoRep3;
	
	//------------------------Reporte ventas VS compras
	private final static String P_FECHA_DESDE_R4 = "__FECHA_DESDE__";
	private final static String P_FECHA_HASTA_R4 = "__FECHA_HASTA__";
	private final static String P_ANIO_R5 = "__ANIO__";
	
	@Getter @Setter private Date fechaDesdeRep4;
	@Getter @Setter private Date fechaHastaRep4;
	@Getter @Setter private int anioRep5;
	
	//------------------------Reporte Productos por categoria
	private final static String P_FECHA_DESDE_R5 = "__FECHA_DESDE__";
	private final static String P_FECHA_HASTA_R5 = "__FECHA_HASTA__";
	
	@Getter @Setter private Date fechaDesdeRep5;
	@Getter @Setter private Date fechaHastaRep5;
	
	//------------------------Reporte Productos por sexo
	private final static String P_FECHA_DESDE_R6 = "__FECHA_DESDE__";
	private final static String P_FECHA_HASTA_R6 = "__FECHA_HASTA__";
	
	@Getter @Setter private Date fechaDesdeRep6;
	@Getter @Setter private Date fechaHastaRep6;
	
	private PedidoDAO pedidoDAO = new PedidoDAO();
	
	@Getter @Setter private String rutaReporte;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		fechaDesdeRep1 = null;
		fechaHastaRep1 = null;
		tipoProductoRep3 = null;
		fechaDesdeRep4 = null;
		fechaHastaRep4 = null;
		fechaDesdeRep5 = null;
		fechaHastaRep5 = null;
		fechaDesdeRep6 = null;
		fechaHastaRep6 = null;
	}
	
	
	@Command
	@NotifyChange("*")
	public void generarReportePedido(){
		
		if(fechaDesdeRep1 == null || fechaHastaRep1 == null) {
			Clients.showNotification("Debe ingresar las fechas!", "error", null, "top_center", 0);
			return;
		}
		
		if(fechaDesdeRep1.after(fechaHastaRep1)) {
			Clients.showNotification("La fecha inicial debe ser menor a la final!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_FECHA_DESDE_R1, fechaDesdeRep1);
		parametros.put(P_FECHA_HASTA_R1, fechaHastaRep1);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_PEDIDOS, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_PEDIDOS, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void generarReporteInventario(){
		
		if(tipoProductoRep3 == null) {
			Clients.showNotification("Debe seleccionar el tipo de producto!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_TIPO_PRODUCTO_R3, tipoProductoRep3);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_INVENTARIO, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_INVENTARIO, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void generarReporteVentasMeses(){
		
		if(anioRep2 < 2021) {
			Clients.showNotification("No existen registros de ese año!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_ANIO_R2, anioRep2);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_VENTAS_MESES, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_VENTAS_MESES, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void generarReporteVentasAnios(){
		
		if(anioDesdeRep2 > anioHastaRep2) {
			Clients.showNotification("El año inicial no debe ser mayor al final!", "error", null, "top_center", 0);
			return;
		}
		
		if(anioDesdeRep2 < 2021) {
			Clients.showNotification("No existen registros de ese año!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_ANIO_DESDE_R2, anioDesdeRep2);
		parametros.put(P_ANIO_HASTA_R2, anioHastaRep2);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_VENTAS_ANIOS, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_VENTAS_ANIOS, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void generarReporteComprasVentasFechas(){
		
		if(fechaDesdeRep4 == null || fechaHastaRep4 == null) {
			Clients.showNotification("Debe ingresar las fechas!", "error", null, "top_center", 0);
			return;
		}
		
		if(fechaDesdeRep4.after(fechaHastaRep4)) {
			Clients.showNotification("La fecha inicial debe ser menor a la final!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_FECHA_DESDE_R4, fechaDesdeRep4);
		parametros.put(P_FECHA_HASTA_R4, fechaHastaRep4);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_VENTAS_COMPRAS_FECHAS, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_VENTAS_COMPRAS_FECHAS, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void generarReporteComprasVentasMeses(){
		
		if(anioRep5 < 2022) {
			Clients.showNotification("No existen registros de ese año!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_ANIO_R5, anioRep5);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_VENTAS_COMPRAS_MESES, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_VENTAS_COMPRAS_MESES, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void generarProductosCategoria(){
		
		if(fechaDesdeRep5 == null || fechaHastaRep5 == null) {
			Clients.showNotification("Debe ingresar las fechas!", "error", null, "top_center", 0);
			return;
		}
		
		if(fechaDesdeRep5.after(fechaHastaRep5)) {
			Clients.showNotification("La fecha inicial debe ser menor a la final!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_FECHA_DESDE_R5, fechaDesdeRep5);
		parametros.put(P_FECHA_HASTA_R5, fechaHastaRep5);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_PRODUCTOS_CATEGORIAS, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_PRODUCTOS_CATEGORIAS, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void generarProductosTalla(){
		
		if(fechaDesdeRep6 == null || fechaHastaRep6 == null) {
			Clients.showNotification("Debe ingresar las fechas!", "error", null, "top_center", 0);
			return;
		}
		
		if(fechaDesdeRep6.after(fechaHastaRep6)) {
			Clients.showNotification("La fecha inicial debe ser menor a la final!", "error", null, "top_center", 0);
			return;
		}
				
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(P_FECHA_DESDE_R6, fechaDesdeRep6);
		parametros.put(P_FECHA_HASTA_R6, fechaHastaRep6);
		try {
			rutaReporte = "/tmp/" + ReporteUtil.generaReporte(pedidoDAO, REPORTE_PRODUCTOS_SEXO, null, Constantes.FORMATO_PDF, 
					parametros).getName();
			//ReporteUtil.ejecutaReporte(pedidoDAO, REPORTE_PRODUCTOS_SEXO, 
			//				null, 
			//				Constantes.FORMATO_PDF, parametros);
		} catch (SQLException | JRException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
