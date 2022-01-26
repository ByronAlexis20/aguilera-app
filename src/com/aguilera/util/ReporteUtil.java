package com.aguilera.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

import com.aguilera.modeloDAO.ClaseDAO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class ReporteUtil {

	private static final int MAXIMOS_INTENTOS_OBTENER_ARCHIVO = 5; 
	private static final int SEGUNDOS_ESPERA_OBTENER_ARCHIVO = 1; 
	
	/**
	 * Retorna el path de la aplicacion.
	 * @return
	 */
	public static String getRealPath(String resource) {
		return Executions.getCurrent().getDesktop().getWebApp().getRealPath(resource);
	}

	/**
	 * Retorna el path de la aplicacion.
	 * @return
	 */
	public static String getAppPath() {
		return getRealPath(File.separator);
	}
	
	/**
	 * Para obtener la ruta limpia de un documento desde un directorio
	 * @param path
	 * @return
	 */
	public static String getPathLimpio(String path) {
		String retorno = path;
		retorno = retorno.replace("//", File.separator);
		retorno = retorno.replace("\\/", File.separator);
		retorno = retorno.replace("/\\", File.separator);
		retorno = retorno.replace("\\\\", File.separator);
		return retorno.trim();
	}
	
	/**
	 * Para validar que un documento desde un directorio existe
	 * @param pathArchivo
	 * @return
	 */
	public static boolean isExisteArchivo(String pathArchivo) {
		boolean existe = false;
		try {
			File archivo = new File(getPathLimpio(pathArchivo));
			existe = (archivo.exists() || archivo.getAbsoluteFile().exists()) && !archivo.isDirectory();
		} catch (Exception e) {
			existe = false;
		}
		return existe; 
	}
	
	/**
	 * Ejecuta un reporte y lo genera en el formato especificado, para luego enviarlo a la descarga.
	 * 
	 * @param pathReporte EL path del reporte (relativo a la raiz de la aplicación) incluyendo el nombre.
	 * @param formato El formato puede ser: GlobalConstant.FORMATO_PDF o GlobalConstant.FORMATO_XLS
	 * @param parametros Mapa de parametros del tipo: Map<String, Object> 
	 * @throws SQLException 
	 * @throws JRException 
	 * @throws IOException 
	 * @throws EdException 
	 * @throws InterruptedException 
	 */
	public static void ejecutaReporte(ClaseDAO claseDAO, String pathReporte, String nombre, String formato, Map<String, Object> parametros) throws SQLException, JRException, IOException, InterruptedException {
		Filedownload.save(generaReporte(claseDAO, pathReporte, nombre, formato, parametros), formato); 
	}

	/**
	 * Genera un reporte en el formato especificado retornando un objeto File del archivo generado.
	 * 
	 * @param pathReporte EL path del reporte (relativo a la raiz de la aplicación) incluyendo el nombre.
	 * @param formato El formato puede ser: GlobalConstant.FORMATO_PDF o GlobalConstant.FORMATO_XLS
	 * @param parametros Mapa de parametros del tipo: Map<String, Object> 
	 * @throws SQLException 
	 * @throws JRException 
	 * @throws IOException 
	 * @throws EdException 
	 * @throws InterruptedException 
	 */
	public static File generaReporte(ClaseDAO claseDAO, String pathReporte, String nombre, String formato, Map<String, Object> parametros) throws SQLException, JRException, IOException, InterruptedException {

		String nombreArchivo = null;

		Connection conexionBaseDatos = claseDAO.abreConexion();

		String pathAbsoluto = getAppPath();
		String archivoReporte = getPathLimpio(pathAbsoluto + pathReporte);
		String archivoLogo = getPathLimpio(pathAbsoluto + "/img/icono.png");
		
		if(nombre != null && nombre.length() > 0) {
			nombreArchivo = getPathLimpio(pathAbsoluto + File.separator + "tmp" + File.separator + nombre);
		}else {
			nombreArchivo = getPathLimpio(pathAbsoluto + File.separator + "tmp" + File.separator + UUID.randomUUID().toString());
		}
		
		if (parametros == null) {
			parametros = new HashMap<String, Object>();
		}

		// Coloca los parametros por defecto
		parametros.put("__RUTA_LOGO__", isExisteArchivo(archivoLogo) ? archivoLogo: null);
		parametros.put("__PATH_BASE__", pathAbsoluto);
					
		if (formato.equals(Constantes.FORMATO_PDF)) {
			nombreArchivo += ".pdf";
			byte[] b = null;
			b = JasperRunManager.runReportToPdf(archivoReporte, parametros, conexionBaseDatos);
			FileOutputStream fos = new FileOutputStream(nombreArchivo);
			fos.write(b);
			fos.close();
			// Para asegurar que el reporte este almacenado en el disco.
			fos.flush();
			// fos.getFD().sync();
		}else{
			nombreArchivo += ".xls";
	        JasperPrint jasperPrint = JasperFillManager.fillReport(archivoReporte, parametros, conexionBaseDatos);
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nombreArchivo));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}
	
		File retorno = null;
		long longitud = 0L;
		int repeticiones = 0;
		do {
			retorno = new File(nombreArchivo);
			longitud = retorno.length();
			if (longitud == 0) {
				repeticiones++;
				TimeUnit.SECONDS.sleep(SEGUNDOS_ESPERA_OBTENER_ARCHIVO);
			}
		}while (longitud == 0 && repeticiones < MAXIMOS_INTENTOS_OBTENER_ARCHIVO);
		
		if (longitud == 0) {
			throw new IOException("El tamaño del archivo es 0.");
		}

		return retorno; 
			
	}

}
