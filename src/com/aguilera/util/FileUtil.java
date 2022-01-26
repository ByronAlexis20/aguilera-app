package com.aguilera.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Filedownload;

import com.aguilera.modelo.Archivo;

/**
 * 
 * @author Sabino Chalen
 * @date Dec 22, 2019
 */

@SuppressWarnings("unused")
public class FileUtil {
	
public static Archivo cargaArchivo(Media media) throws Exception, IOException {
		
		if (media == null) {
			throw new Exception("El archivo a cargar no puede ser nulo.");
		}

		String path = cargaArchivoRepositorio(media);
		
		if (path == null) {
			throw new Exception("El archivo no se pudo cargar al servidor.");
		}
		
		Archivo resultado = new Archivo();
		
		resultado.setFormato(media.getFormat());
		resultado.setMimeType(media.getContentType());
		//resultado.setNombre(media.getName());
		resultado.setDireccion(getPath(path));
		resultado.setNombre(getFileName(path));
		
		return resultado;
	}
	
	private static String getFileName(String pathCompleto) {
		if (pathCompleto == null) {
			return null;
		}
		
		boolean esPathWindows = !pathCompleto.contains("/");
		String separadorDirectorios;
		String retorno = null;
		if (esPathWindows) {
			separadorDirectorios = "\\";
		}else{
			separadorDirectorios = "/";
		}
		int posicionNombre = pathCompleto.lastIndexOf(separadorDirectorios.charAt(0));
		
		if (posicionNombre >= 0) {
			retorno = pathCompleto.substring(posicionNombre + 1);
		}else{
			retorno = pathCompleto;
		}
				
		return retorno;
	}
	
	private static String getPath(String pathCompleto) {
		if (pathCompleto == null) {
			return null;
		}
		
		boolean esPathWindows = !pathCompleto.contains("/");
		String separadorDirectorios;
		String retorno = null;
		if (esPathWindows) {
			separadorDirectorios = "\\";
		}else{
			separadorDirectorios = "/";
		}
		int posicionNombre = pathCompleto.lastIndexOf(separadorDirectorios.charAt(0));
		
		if (posicionNombre >= 0) {
			retorno = pathCompleto.substring(0, posicionNombre + 1);
		}
				
		return retorno;
	}
	
	private static String cargaArchivoRepositorio(Media media) throws IOException {
		
		String pathArchivo;
		String pathRepositorio;
		String nombreArchivo;
		String pathArchivoCargado = null; 
		String extension = getFileExtension(media.getName());
		
		// Obtiene la fecha del dia para definir el directorio donde se depositara el archivo.
		Calendar hoy = Calendar.getInstance();
		int anio = hoy.get(Calendar.YEAR);
		int mes = hoy.get(Calendar.MONTH);
		int dia = hoy.get(Calendar.DAY_OF_MONTH);

		pathArchivo = anio + "/" + mes + "/" + dia + "/";

		// Obtiene la ruta del archivo digital.
		pathRepositorio = "/disenios-aguilera/ad/";

		// Define el path definitivo donde se depositara el archivo
		// Si no existe, lo crea.
		
		File baseDir = new File(pathRepositorio + pathArchivo);
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}

		// Obtiene el nombre del archivo.
		nombreArchivo = UUID.randomUUID().toString();
		if (extension != null && !extension.isEmpty()) {
			nombreArchivo = nombreArchivo + "." + extension;
		}

		// Construye el path completo del archivo.
		pathArchivoCargado = pathRepositorio + pathArchivo + nombreArchivo;
		
		// Copia el archivo en la ruta definitiva.
		if (media.isBinary()) {
			Files.copy(new File(pathArchivoCargado), media.getStreamData());
		}else{
			Files.copy(new File(pathArchivoCargado), media.getReaderData(), null);
		}

		return pathArchivoCargado;
	}

	private static String getFileExtension(String fileName) {
	    if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	    return fileName.substring(fileName.lastIndexOf(".")+1);
	    else return "";
	}
	
	public static void descargarArchivo(String nombreArchivo) {
		try {
			Filedownload.save(new File(nombreArchivo), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public static void descargaArchivo(Archivo archivo) {
		descargarArchivo(archivo.toString());
	}
	
	public static AImage getImagenTamanoFijo(AImage imagen, int ancho, int alto) {
		AImage retorno = null; 
		BufferedImage imagenOriginal;
		Image imagenEscalada;
		BufferedImage nuevaImagen;
		ByteArrayOutputStream salidaImagen = new ByteArrayOutputStream();
		try {
			imagenOriginal = ImageIO.read(imagen.getStreamData()); 
			imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

			nuevaImagen = new BufferedImage(imagenEscalada.getWidth(null), imagenEscalada.getHeight(null), imagenOriginal.getType());
			Graphics2D g = nuevaImagen.createGraphics();
			g.drawImage(imagenEscalada, 0, 0, null);
			g.dispose();	
			
			ImageIO.write(nuevaImagen, imagen.getFormat(),  salidaImagen);

			retorno = new AImage("", salidaImagen.toByteArray());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
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
	
	public static String getPathLimpio(String path) {
		String retorno = path;
		retorno = retorno.replace("//", File.separator);
		retorno = retorno.replace("\\/", File.separator);
		retorno = retorno.replace("/\\", File.separator);
		retorno = retorno.replace("\\\\", File.separator);
		return retorno.trim();
	}
}
