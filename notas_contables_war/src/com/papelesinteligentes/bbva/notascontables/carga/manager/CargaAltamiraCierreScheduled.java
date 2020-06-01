package com.papelesinteligentes.bbva.notascontables.carga.manager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.papelesinteligentes.bbva.notascontables.carga.dto.AsientoSalida;
import com.papelesinteligentes.bbva.notascontables.carga.dto.ISalida;
import com.papelesinteligentes.bbva.notascontables.carga.dto.NotasOKSalida;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RechazoSalida;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.facade.NotasContablesSession;
import com.papelesinteligentes.bbva.notascontables.facade.impl.NotasContablesSessionBean;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;

public class CargaAltamiraCierreScheduled {

	private static final String EXTENSION_PROCESO = "_PROC";
	private static final String EXTENSION_FINALIZADO = "_OK";
	private static final String EXTENSION_ERROR = "_ERROR";
	private static final String ASIENTOS_FILE_NAME = "ARCHIVO_ASIENTOS_NTCON.TXT";
	private static final String OK_FILE_NAME = "ARCHIVO_NOTASOK_NTCON.TXT";
	private static final String RECHAZOS_FILE_NAME = "ARCHIVO_RECHAZOS_NTCON.TXT";

	private final NotasContablesSession notasContablesSession = new NotasContablesSessionBean();

	enum TIPO {
		ASIENTO(ASIENTOS_FILE_NAME), NOTASOK(OK_FILE_NAME), RECHAZOS(RECHAZOS_FILE_NAME), OTRO("");

		String nombre;

		private TIPO(String nombre) {
			this.nombre = nombre;
		}

		public static TIPO getByName(String name) {
			if (name == null) {
				return OTRO;
			}
			if (name.trim().toLowerCase().equals(ASIENTOS_FILE_NAME.trim().toLowerCase())) {
				return ASIENTO;
			}

			if (name.trim().toLowerCase().equals(OK_FILE_NAME.trim().toLowerCase())) {
				return NOTASOK;
			}

			if (name.trim().toLowerCase().equals(RECHAZOS_FILE_NAME.trim().toLowerCase())) {
				return RECHAZOS;
			}
			return OTRO;
		}

	}

	/**
	 * la información debe estar formateada con:</p> datos originales separados por punto y coma, prefijo <code>Schedule,</code>, y datos de información separados por coma en el siguiente orden:
	 * <ul>
	 * <li>Fecha en formato yyyyMMddHH</li>
	 * <li>Periodo en milisegundos</li>
	 * <li>Si se debe eliminar la info completa; 0 para no, cualquier otro número para sí</li>
	 * <li>Si se debe ejecutar en un hilo independiente; 0 para no, cualquier otro número para sí</li>
	 * </ul>
	 */
	public void loadDatosAltamiraScheduled(File dir) throws Exception {
		String ls_linea;
		Map<TIPO, List<ISalida>> data = new HashMap<TIPO, List<ISalida>>();
		for (File f : dir.listFiles()) {
			try {
				boolean archivoValido = f.getName().equals(ASIENTOS_FILE_NAME) || f.getName().equals(RECHAZOS_FILE_NAME) || f.getName().equals(OK_FILE_NAME);
				if (!archivoValido || f.getName().endsWith(EXTENSION_PROCESO) || f.getName().endsWith(EXTENSION_ERROR) || f.getName().endsWith(EXTENSION_FINALIZADO)) {
					continue;
				}
				TIPO tipoArch = TIPO.getByName(f.getName());
				String lastPath = f.getAbsolutePath();
				File fDest = new File(f.getAbsolutePath() + EXTENSION_PROCESO);
				f.renameTo(fDest);
				f = new File(fDest.getAbsolutePath());

				FileInputStream lfis_stream = new FileInputStream(f);
				DataInputStream in = new DataInputStream(lfis_stream);
				BufferedReader lbr_bufferReader = new BufferedReader(new InputStreamReader(in));
				System.out.println("****************************** PROCESANDO ARCHIVO DE PRECIERRE/CIERRE ******************************");
				//System.out.println("  Inició: " + DateUtils.getTimestamp() + " : " + f.getName());
				while ((ls_linea = lbr_bufferReader.readLine()) != null) {
					try {
						procesar(ls_linea, tipoArch, data);
					} catch (Exception e) {
						System.out.println("****************************** ERROR PROCESANDO ARCHIVO DE PRECIERRE/CIERRE ******************************");
						e.printStackTrace();
						throw e;
					}
				}
				System.out.println("  Terminó: " + DateUtils.getTimestamp() + ": " + f.getName());
				lbr_bufferReader.close();
				in.close();
				lfis_stream.close();

				fDest = new File(lastPath + EXTENSION_FINALIZADO);
				f.renameTo(fDest);

			} catch (Exception e) {
				if (f != null) {
					File fDest = new File(f.getAbsolutePath() + EXTENSION_ERROR);
					f.renameTo(fDest);
				}
				System.err.println("Error: " + e.getMessage());
			}
		}
		procesarPrecierreCierre(data);

	}

	/**
	 * Se está guardando solo el primero, con su info de fecha y esas cosas. Además falta también actualizar la instancia
	 * 
	 * @param data
	 */
	private void procesarPrecierreCierre(Map<TIPO, List<ISalida>> data) {
		TreeMap<String, Instancia> notas = new TreeMap<String, Instancia>();
		
		//Ajuste Incidencia COL563417I856584 archivo ascientos se toma unicamente para cierres contables
		//if (data.keySet().contains(TIPO.RECHAZOS)) {
		if (data.keySet().contains(TIPO.NOTASOK)) {
			Map<String, List<ISalida>> notasOk = producirMapa(data.get(TIPO.NOTASOK));
			Map<String, List<ISalida>> notasRechazos = producirMapa(data.get(TIPO.RECHAZOS));
			for (String str : notasOk.keySet()) {
				if (!notasRechazos.containsKey(str)) {
					try {
						siguienteActividad(notasOk.get(str).get(0), notas);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			for (String str : notasRechazos.keySet()) {
				try {
					addError((RechazoSalida) notasRechazos.get(str).get(0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (data.keySet().contains(TIPO.ASIENTO)) {// si hay solo asientos puede tratarse de un precierre con todo ok o de un cierrre
			Map<String, List<ISalida>> notasAsientos = producirMapa(data.get(TIPO.ASIENTO));
			for (String str : notasAsientos.keySet()) {
				try {
					for (ISalida salida : notasAsientos.get(str)) {
						siguienteActividad(salida, notas);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void addError(RechazoSalida rs) throws RemoteException, Exception {
		notasContablesSession.addRechazoSalida(rs, 0);
	}

	/**
	 * Este metodo deberia tener control de transaccionalidad.... pero :$
	 * 
	 * @param salida
	 * @param notas
	 * @throws RemoteException
	 * @throws Exception
	 */
	private void siguienteActividad(ISalida salida, TreeMap<String, Instancia> notas) throws Exception {
		notasContablesSession.siguienteActividad(salida, notas);
	}

	private Map<String, List<ISalida>> producirMapa(List<ISalida> list) {
		Map<String, List<ISalida>> notas = new TreeMap<String, List<ISalida>>();
		if (list == null) {
			return notas;
		}
		for (ISalida s : list) {
			if (!notas.containsKey(s.getNumNota())) {
				notas.put(s.getNumNota(), new ArrayList<ISalida>());
			}
			notas.get(s.getNumNota()).add(s);
		}
		return notas;
	}

	private void procesar(String line, TIPO tipo, Map<TIPO, List<ISalida>> data) throws Exception {
		if (tipo == TIPO.ASIENTO) {
			if (!data.containsKey(TIPO.ASIENTO)) {
				data.put(TIPO.ASIENTO, new ArrayList<ISalida>());
			}
			AsientoSalida as = new AsientoSalida();
			as.produceFromString(line);
			data.get(TIPO.ASIENTO).add(as);
		}
		if (tipo == TIPO.NOTASOK) {
			if (!data.containsKey(TIPO.NOTASOK)) {
				data.put(TIPO.NOTASOK, new ArrayList<ISalida>());
			}
			NotasOKSalida as = new NotasOKSalida();
			as.produceFromString(line);
			data.get(TIPO.NOTASOK).add(as);
		}
		if (tipo == TIPO.RECHAZOS) {
			if (!data.containsKey(TIPO.RECHAZOS)) {
				data.put(TIPO.RECHAZOS, new ArrayList<ISalida>());
			}
			RechazoSalida as = new RechazoSalida();
			as.produceFromString(line);
			data.get(TIPO.RECHAZOS).add(as);
		}
	}
}
