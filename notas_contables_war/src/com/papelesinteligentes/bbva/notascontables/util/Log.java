package com.papelesinteligentes.bbva.notascontables.util;
/**
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.papelesinteligentes.bbva.notascontables.util.Log;

public class Log {
	
	private static InputStream inputStream;
	private static Properties properties=null;
	private static Logger loggerINFO = null;
	private static Logger loggerERROR = null;
	public static boolean ESCRIBIR_SYSOUT=false;
	
	static {
		obtenerLoggers();
	}
	
	/**
	 * Obtiene la instancia de los Loggers
	 */
/**
	private static void obtenerLoggers() {
		try {
			inputStream = Log.class.getResourceAsStream("log4j.properties");
			properties = new Properties();
			properties.load(inputStream);
			String ruta = properties.getProperty("rutaLog");
			validaYCreaDirectorio(ruta);
			PropertyConfigurator.configure(properties);
			loggerERROR = Logger.getLogger("ERROR");
			loggerINFO = Logger.getLogger("INFO");
		} catch (IOException e) {
			Log.escribirLogError("Log:obtenerLoggers ", e);
			System.out.println("ERROR AL CONFIGURAR EL LOG:" + e.getMessage());
		}
	}
	
	public static void validaYCreaDirectorio(String ruta) {
		try {
			File folder = new File(ruta);
			if (!folder.exists()) {
				folder.mkdirs();
			}
		} catch (Exception e) {
			Log.escribirLogError("validaYCreaDirectorio ", e);
		}
	}
	
	public static void escribirLogError(String msj, Throwable e) {
		try {
			StringBuffer bf = new StringBuffer();
			//bf.append("(");
			//bf.append(obtenerFechaHora());
			//bf.append(") - ");
			bf.append(msj+" ");
			loggerERROR.error(bf.toString(), e);
			if(ESCRIBIR_SYSOUT){
				System.out.println(msj+" "+e.getMessage());
			}
		} catch (Exception e1) {
			Log.escribirLogError("Log:escribirLogError ", e);
			System.out.println("ERROR AL ESCRIBIR EN EL LOG: "+e);
		}
	}
	
	public static String getNombreLogError(){
		try {
			if(properties==null){
				obtenerLoggers();
			}
			return properties.getProperty("log4j.appender.ERROR.File").trim();
		} catch (Exception e) {
			Log.escribirLogError("Log:getNombreLogError ", e);
			return "";
		}
	}
	
	public static String getNombreLogInfo(){
		try {
			if(properties==null){
				obtenerLoggers();
			}
			return properties.getProperty("log4j.appender.INFO.File").trim();
		} catch (Exception e) {
			Log.escribirLogError("Log:getNombreLogInfo ", e);
			return "";
		}
	}
	
	public static void escribirLogInfo(String msj) {
		try {
			StringBuffer bf = new StringBuffer();
			//bf.append("(");
			//bf.append(obtenerFechaHora());
			//bf.append(") - ");
			bf.append(msj);
			loggerINFO.info(bf.toString());
			if(ESCRIBIR_SYSOUT){
				System.out.println("LogInf: "+msj);
			}
		} catch (Exception e1) {
			Log.escribirLogError("Log:escribirLogInfo ", e1);
			System.out.println("ERROR AL ESCRIBIR EN EL LOG: "+msj);
		}
	}

	/**
	 * Obtiene la fecha actual
	 * 
	 * @return fecha actual en forma de String
	 
	private static String obtenerFechaHora() {
		java.util.Date fechaActual = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(fechaActual);
	}
	
}
**/