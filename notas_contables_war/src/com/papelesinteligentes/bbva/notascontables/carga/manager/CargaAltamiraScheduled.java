package com.papelesinteligentes.bbva.notascontables.carga.manager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.papelesinteligentes.bbva.notascontables.carga.dao.RechazoSalidaDAO;
import com.papelesinteligentes.bbva.notascontables.carga.dto.CierreMensual;
import com.papelesinteligentes.bbva.notascontables.carga.dto.EstructuraCarga;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RechazoSalida;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RegistroCarga;
import com.papelesinteligentes.bbva.notascontables.dao.ActividadRealizadaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.AnexoDAO;
import com.papelesinteligentes.bbva.notascontables.dao.InstanciaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.NotaContableCrucePartidaPendienteDAO;
import com.papelesinteligentes.bbva.notascontables.dao.NotaContableDAO;
import com.papelesinteligentes.bbva.notascontables.dao.NotaContableRegistroLibreDAO;
import com.papelesinteligentes.bbva.notascontables.dao.NotaContableTemaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.NotaContableTemaImpuestoDAO;
import com.papelesinteligentes.bbva.notascontables.dao.NotaContableTemaRiesgoDAO;
import com.papelesinteligentes.bbva.notascontables.dao.NotaContableTotalDAO;
import com.papelesinteligentes.bbva.notascontables.dao.ObservacionDAO;
import com.papelesinteligentes.bbva.notascontables.dao.ParametroDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKActividadRealizadaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKAnexoDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKInstanciaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKNotaContableCrucePartidaPendienteDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKNotaContableDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKNotaContableRegistroLibreDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKNotaContableTemaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKNotaContableTemaImpuestoDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKNotaContableTemaRiesgoDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKNotaContableTotalDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKObservacionDAO;
import com.papelesinteligentes.bbva.notascontables.dao.bk.BKRechazoSalidaDAO;
import com.papelesinteligentes.bbva.notascontables.dto.ActividadRealizada;
import com.papelesinteligentes.bbva.notascontables.dto.Anexo;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableCrucePartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableRegistroLibre;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTema;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTemaImpuesto;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTotal;
import com.papelesinteligentes.bbva.notascontables.dto.Observacion;
import com.papelesinteligentes.bbva.notascontables.dto.Parametro;
import com.papelesinteligentes.bbva.notascontables.dto.RiesgoOperacional;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.facade.NotasContablesSession;
import com.papelesinteligentes.bbva.notascontables.facade.impl.NotasContablesSessionBean;
import com.papelesinteligentes.bbva.notascontables.jsf.beans.UsuarioLogueado;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;
import com.papelesinteligentes.bbva.notascontables.util.StringUtils;
//import com.papelesinteligentes.bbva.notascontables.util.Log;

public class CargaAltamiraScheduled extends CargaAltamiraBase {
	//
	private static final String START_SKIP = "--";
	private static final String START_SCHEDULE = "Schedule,";
	private static final String SPLITTER_INTERN = "\\,";
	private static final String SPLITTER = "\\;";
	private static final String EXTENSION_PROCESO = "_PROC";
	private static final String EXTENSION_ERROR = "_ERROR";
	private static final String EXTENSION_OK = "_OK";

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");

	private final BKActividadRealizadaDAO bkActividadRealizadaDAO = new BKActividadRealizadaDAO();
	private final BKAnexoDAO bkAnexoDAO = new BKAnexoDAO();
	private final BKInstanciaDAO bkInstanciaDAO = new BKInstanciaDAO();
	private final BKNotaContableCrucePartidaPendienteDAO bkNotaContableCrucePartidaPendienteDAO = new BKNotaContableCrucePartidaPendienteDAO();
	private final BKNotaContableDAO bkNotaContableDAO = new BKNotaContableDAO();
	private final BKNotaContableRegistroLibreDAO bkNotaContableRegistroLibreDAO = new BKNotaContableRegistroLibreDAO();
	private final BKNotaContableTemaDAO bkNotaContableTemaDAO = new BKNotaContableTemaDAO();
	private final BKNotaContableTemaImpuestoDAO bkNotaContableTemaImpuestoDAO = new BKNotaContableTemaImpuestoDAO();
	private final BKNotaContableTemaRiesgoDAO bkNotaContableTemaRiesgoDAO = new BKNotaContableTemaRiesgoDAO();
	private final BKNotaContableTotalDAO bkNotaContableTotalDAO = new BKNotaContableTotalDAO();
	private final BKObservacionDAO bkObservacionDAO = new BKObservacionDAO();
	private final BKRechazoSalidaDAO bkRechazoSalidaDAO = new BKRechazoSalidaDAO();

	private final ActividadRealizadaDAO actividadRealizadaDAO = new ActividadRealizadaDAO();
	private final AnexoDAO anexoDAO = new AnexoDAO();
	private final InstanciaDAO instanciaDAO = new InstanciaDAO();
	private final NotaContableCrucePartidaPendienteDAO notaContableCrucePartidaPendienteDAO = new NotaContableCrucePartidaPendienteDAO();
	private final NotaContableDAO notaContableDAO = new NotaContableDAO();
	private final NotaContableRegistroLibreDAO notaContableRegistroLibreDAO = new NotaContableRegistroLibreDAO();
	private final NotaContableTemaDAO notaContableTemaDAO = new NotaContableTemaDAO();
	private final NotaContableTemaImpuestoDAO notaContableTemaImpuestoDAO = new NotaContableTemaImpuestoDAO();
	private final NotaContableTemaRiesgoDAO notaContableTemaRiesgoDAO = new NotaContableTemaRiesgoDAO();
	private final NotaContableTotalDAO notaContableTotalDAO = new NotaContableTotalDAO();
	private final ObservacionDAO observacionDAO = new ObservacionDAO();
	private final RechazoSalidaDAO rechazoSalidaDAO = new RechazoSalidaDAO();

	private final ParametroDAO parametroDAO = new ParametroDAO();

	private final NotasContablesSession notasContablesSession = new NotasContablesSessionBean();

	/**
	 * la información debe estar formateada con:</p> datos originales separados por punto y coma, prefijo <code>Schedule,</code>, y datos de información separados por coma en el siguiente orden:
	 * <ul>
	 * <li>Fecha en formato yyyyMMddHH</li>
	 * <li>Periodo en milisegundos</li>
	 * <li>Si se debe eliminar la info completa; 0 para no, cualquier otro número para sí</li>
	 * <li>Si se debe ejecutar en un hilo independiente; 0 para no, cualquier otro número para sí</li>
	 * </ul>
	 */
	public synchronized void loadDatosAltamiraScheduled(File file) throws Exception {
		EstructuraCarga estructuraCarga = new EstructuraCarga();
		String ls_linea;
		int li_contador = 0;
		int li_numeroDatos = 0;
		final String as_rutaArchivo = file.getParent() + "/";
		try {
			FileInputStream lfis_stream = new FileInputStream(file);
			final boolean[] dataToProcess = new boolean[3];

			DataInputStream in = new DataInputStream(lfis_stream);
			BufferedReader lbr_bufferReader = new BufferedReader(new InputStreamReader(in));
			while ((ls_linea = lbr_bufferReader.readLine()) != null) {
				if (!ls_linea.startsWith(START_SKIP)) {
					li_numeroDatos = cargaAltamiraManager.getNumeroDatos(ls_linea);
					final EstructuraCarga[] estructurasCarga = new EstructuraCarga[li_numeroDatos];

					final String ls_nombreTabla = ls_linea.substring(0, ls_linea.indexOf(';'));
					ls_linea = ls_linea.substring(ls_linea.indexOf(';') + 1);

					final String ls_nombreArchivo = ls_linea.substring(0, ls_linea.indexOf(';'));
					ls_linea = ls_linea.substring(ls_linea.indexOf(';') + 1);

					li_contador = 0;
					for (String str : ls_linea.split(SPLITTER)) {
						if (!str.startsWith(START_SCHEDULE)) {
							estructuraCarga = new EstructuraCarga();
							estructuraCarga.setTipo(str.substring(0, 1));
							estructuraCarga.setLongitud(Integer.parseInt(str.substring(2)));

							estructurasCarga[li_contador] = estructuraCarga;
							li_contador++;
						} else {
							boolean tmp[] = process(str, ls_nombreArchivo);
							dataToProcess[0] = tmp[0];
							dataToProcess[1] = tmp[1];
							dataToProcess[2] = tmp[2];
						}
					}
					if (dataToProcess[0]) {// indica si es adecuado procesar el archivo
						File f = new File(as_rutaArchivo + ls_nombreArchivo);
						File fDest = new File(as_rutaArchivo + ls_nombreArchivo + EXTENSION_PROCESO);
						if (f.exists()) {
							f.renameTo(fDest);
							final File fileMod = new File(as_rutaArchivo + ls_nombreArchivo + EXTENSION_PROCESO);

							if (!dataToProcess[2]) {// proceso en serie
								
								//System.out.println("******************************");
								System.out.println("  Inició Carga Archivo: " + DateUtils.getTimestamp() + ": " + ls_nombreArchivo);
								/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
								//Log.escribirLogInfo("******************************");
								//Log.escribirLogInfo("  Inició: " + DateUtils.getTimestamp() + ": " + ls_nombreArchivo);
								try {
									getDatos(ls_nombreTabla, fileMod, ls_nombreArchivo, estructurasCarga, dataToProcess[1]);
									// esto se hace temporalmente para verificar el procesado de los datos
									 File fOK = new File(as_rutaArchivo + ls_nombreArchivo + EXTENSION_OK);
									 fileMod.renameTo(fOK);
								} catch (Exception e) {
									//System.out.println("Error procesando el archivo " + as_rutaArchivo + ls_nombreArchivo);
									/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
									//Log.escribirLogInfo("Error procesando el archivo " + as_rutaArchivo + ls_nombreArchivo);
									File fOrg = new File(as_rutaArchivo + ls_nombreArchivo + EXTENSION_ERROR + "_" + StringUtils.getString(new Date(), "yyMMddhhmmss"));
									fileMod.renameTo(fOrg);
								}
								System.out.println("  Terminó Carga Archivo : " + DateUtils.getTimestamp() + " : " + ls_nombreArchivo);
								/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
								//Log.escribirLogInfo("  Terminó: " + DateUtils.getTimestamp() + " : " + ls_nombreArchivo);
							} else {// proceso en hilos
								//System.out.println("******************************");
								//System.out.println("  Inició en hilo: " + DateUtils.getTimestamp() + " : " + ls_nombreArchivo);
								/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
								//Log.escribirLogInfo("******************************");
								//Log.escribirLogInfo("  Inició en hilo: " + DateUtils.getTimestamp() + " : " + ls_nombreArchivo);
								
								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											getDatos(ls_nombreTabla, fileMod, ls_nombreArchivo, estructurasCarga, dataToProcess[1]);
											// esto se hace temporalmente para verificar el procesado de los datos
											 File fOK = new File(as_rutaArchivo + ls_nombreArchivo + EXTENSION_OK);
											 fileMod.renameTo(fOK);

										} catch (Exception e) {
											//System.out.println("Error procesando el archivo " + as_rutaArchivo + ls_nombreArchivo);
											//System.out.println("Renombrando el archivo como error");
											/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
											//Log.escribirLogInfo("Error procesando el archivo " + as_rutaArchivo + ls_nombreArchivo);
											//Log.escribirLogInfo("Renombrando el archivo como error");
											
											File fOrg = new File(as_rutaArchivo + ls_nombreArchivo + "_" + StringUtils.getString(new Date(), "yyMMddhhmmss"));
											fileMod.renameTo(fOrg);
										}
										//System.out.println("  Terminó: " + DateUtils.getTimestamp() + " : " + ls_nombreArchivo);
										//Log.escribirLogInfo("  Terminó: " + DateUtils.getTimestamp() + " : " + ls_nombreArchivo);
									}
								}).start();
							}
							// } else if (!fDest.exists()) {
							// System.out.println("No se encontró el archivo " + as_rutaArchivo + ls_nombreArchivo);
						}
					}
				}
			}
			lbr_bufferReader.close();
			in.close();
			lfis_stream.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * 
	 * @param ls_linea
	 * @return 1. si se debe procesar 2. si debe eliminar la info anterior 3. si debe ejecutarse en hilo
	 */
	private boolean[] process(String ls_linea, String ls_nombreArchivo) {
		try {
			boolean ret[] = { false, false, false };
			String line = ls_linea.substring(START_SCHEDULE.length());
			String[] data = line.split(SPLITTER_INTERN);
			Date from = sdf.parse(data[0].trim());
			Calendar actCal = Calendar.getInstance();
			long periodo = Long.valueOf(data[1].trim()) * 60000;// periodo en minutos, se multiplica para pasar el tiempo a milisegundos
			ret[1] = Integer.valueOf(data[2].trim()) != 0;// borrar todos
			ret[2] = Integer.valueOf(data[3].trim()) != 0;// correr en hilo
			RegistroCarga registroCarga = cargaAltamiraManager.getRegistroCargaPorNombreArchivo(ls_nombreArchivo);
			// se deberá procesar si el último registro de carga fue ejecutado desde hace más de x tiempo, siendo x = periodo
			if (registroCarga == null || registroCarga.getCodigo() == 0) {
				// solo se procesa si la fecha de inicio ya pasó
				ret[0] = from.before(actCal.getTime());
			} else {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(registroCarga.getFechaInicio().getTime() + periodo);
				// solo se procesa si la fecha de último proceso mas el periodo han sido de antes de ahora
				ret[0] = c.before(actCal);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return new boolean[] { false, false, false };
		}
	}

	public void procesarAnulados() {
		try {
			int cantMeses = 1;
			Parametro p = new Parametro();
			p.setNombre(Parametro.DELTA_BORRADO_ANULACION);
			p = parametroDAO.findByPrimaryKey(p);

			cantMeses = p.getValor().intValue();

			for (NotaContable nc : notaContableDAO.findByAnulados(cantMeses)) {
				for (RechazoSalida rs : rechazoSalidaDAO.findByNotaContable(nc.getNumeroRadicacion())) {
					rechazoSalidaDAO.delete(rs, 0);
				}
				for (NotaContableCrucePartidaPendiente ncc : notaContableCrucePartidaPendienteDAO.findByNotaContable(nc.getCodigo().intValue())) {
					notaContableCrucePartidaPendienteDAO.delete(ncc, 0);
				}
				for (NotaContableRegistroLibre ncr : notaContableRegistroLibreDAO.findByNotaContable(nc.getCodigo().intValue())) {
					notaContableRegistroLibreDAO.delete(ncr, 0);
				}
				for (NotaContableTemaImpuesto nct : notaContableTemaImpuestoDAO.findByNotaContable(nc.getCodigo().intValue())) {
					notaContableTemaImpuestoDAO.delete(nct, 0);
				}
				for (RiesgoOperacional nct : notaContableTemaRiesgoDAO.findByNotaContable(nc.getCodigo().intValue())) {
					notaContableTemaRiesgoDAO.delete(nct, 0);
				}
				for (NotaContableTema nct : notaContableTemaDAO.findByNotaContable(nc.getCodigo().intValue())) {
					notaContableTemaDAO.delete(nct, 0);
				}
				for (NotaContableTotal nct : notaContableTotalDAO.findByNotaContable(nc.getCodigo().intValue())) {
					notaContableTotalDAO.delete(nct, 0);
				}
				Instancia i = new Instancia();
				i.setCodigoNotaContable(nc.getCodigo());
				i = instanciaDAO.findByNotaContable(i);
				for (Anexo ane : anexoDAO.findByCodigoInstancia(i.getCodigo().intValue())) {
					anexoDAO.delete(ane, 0);
				}
				for (Observacion obs : observacionDAO.findByCodigoInstancia(i.getCodigo().intValue())) {
					observacionDAO.delete(obs, 0);
				}
				ActividadRealizada ar = new ActividadRealizada();
				ar.setCodigoInstancia(i.getCodigo());
				for (ActividadRealizada obs : actividadRealizadaDAO.findByCodigoInstancia(ar)) {
					actividadRealizadaDAO.delete(obs, 0);
				}
				instanciaDAO.delete(i, 0);				

				notaContableDAO.delete(nc, 0);
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void procesarHistorico() {
		Calendar c = Calendar.getInstance();
		CierreMensual cm = new CierreMensual();
		cm.setMes(c.get(Calendar.MONTH) + 1);//02
		cm.setYear(c.get(Calendar.YEAR));//2020
		try {
			cm = cargaAltamiraManager.getCierreMensual(cm);
			
			
			if (cm == null || cm.getFechaUltimaCarga() == null) {
				return;
			}
			c.setTime(cm.getFechaUltimaCarga());
			Calendar c2 = Calendar.getInstance();
			if (c2.get(Calendar.DAY_OF_MONTH) != c.get(Calendar.DAY_OF_MONTH)) {
				return;
			}
			int cantMeses = 1;
			Parametro p = new Parametro();
			p.setNombre(Parametro.CIERRE_MENSUAL);
			p = parametroDAO.findByPrimaryKey(p);			

			cantMeses = p.getValor().intValue();

			for (NotaContable nc : notaContableDAO.findToBackup(cantMeses)) {
			//for (NotaContable nc : notaContableDAO.findToBackup(5)) {
				
				bkNotaContableDAO.add(nc, 0);				
				
				for (RechazoSalida rs : rechazoSalidaDAO.findByNotaContable(nc.getNumeroRadicacion())) {
					bkRechazoSalidaDAO.add(rs, 0);
					rechazoSalidaDAO.delete(rs, 0);
				}
				for (NotaContableCrucePartidaPendiente ncc : notaContableCrucePartidaPendienteDAO.findByNotaContable(nc.getCodigo().intValue())) {
					bkNotaContableCrucePartidaPendienteDAO.add(ncc, 0);
					notaContableCrucePartidaPendienteDAO.delete(ncc, 0);
				}
				for (NotaContableRegistroLibre ncr : notaContableRegistroLibreDAO.findByNotaContable(nc.getCodigo().intValue())) {
					bkNotaContableRegistroLibreDAO.add(ncr, 0);
					notaContableRegistroLibreDAO.delete(ncr, 0);
				}
				for (NotaContableTemaImpuesto nct : notaContableTemaImpuestoDAO.findByNotaContable(nc.getCodigo().intValue())) {
					bkNotaContableTemaImpuestoDAO.add(nct, 0);
					notaContableTemaImpuestoDAO.delete(nct, 0);
				}
				for (RiesgoOperacional nct : notaContableTemaRiesgoDAO.findByNotaContable(nc.getCodigo().intValue())) {
					bkNotaContableTemaRiesgoDAO.add(nct, 0);
					notaContableTemaRiesgoDAO.delete(nct, 0);
				}
				for (NotaContableTema nct : notaContableTemaDAO.findByNotaContable(nc.getCodigo().intValue())) {
					nct.setCodigo(nct.getCodigo().intValue());
					nct.setCodigoTema(nct.getCodigoTema().intValue());
					nct.setCodigoNotaContable(nct.getCodigoNotaContable().intValue());
					nct.setMonto(nct.getMonto().doubleValue());
					bkNotaContableTemaDAO.add(nct, 0);
					notaContableTemaDAO.delete(nct, 0);
				}
				for (NotaContableTotal nct : notaContableTotalDAO.findByNotaContable(nc.getCodigo().intValue())) {
					bkNotaContableTotalDAO.add(nct, 0);
					notaContableTotalDAO.delete(nct, 0);
				}
				Instancia i = new Instancia();
				i.setCodigoNotaContable(nc.getCodigo());
				i = instanciaDAO.findByNotaContable(i);
				bkInstanciaDAO.add(i, 0);
				for (Anexo ane : anexoDAO.findByCodigoInstancia(i.getCodigo().intValue())) {
					ane.setCodigo(ane.getCodigo().intValue());
					ane.setCodigoInstancia(ane.getCodigoInstancia().intValue());
					ane.setCodigoTema(ane.getCodigoTema().intValue());
					ane.setCodigoUsuario(ane.getCodigoUsuario().intValue());
					bkAnexoDAO.add(ane, 0);
					anexoDAO.delete(ane, 0);
				}
				for (Observacion obs : observacionDAO.findByCodigoInstancia(i.getCodigo().intValue())) {
					bkObservacionDAO.add(obs, 0);
					observacionDAO.delete(obs, 0);
				}
				ActividadRealizada ar = new ActividadRealizada();
				ar.setCodigoInstancia(i.getCodigo());
				for (ActividadRealizada obs : actividadRealizadaDAO.findByCodigoInstancia(ar)) {
					obs.setCodigo(obs.getCodigo().intValue());
					obs.setCodigoInstancia(obs.getCodigoInstancia().intValue());
					obs.setCodigoUsuario(obs.getCodigoUsuario().intValue());
					obs.setDuracionActividad(obs.getDuracionActividad().intValue());
					bkActividadRealizadaDAO.add(obs, 0);
					actividadRealizadaDAO.delete(obs, 0);
				}
				instanciaDAO.delete(i, 0);

				notaContableDAO.delete(nc, 0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void procesarAnular() {
		Calendar c = Calendar.getInstance();
		CierreMensual cm = new CierreMensual();
		cm.setMes(c.get(Calendar.MONTH) + 1);
		cm.setYear(c.get(Calendar.YEAR));
		try {
			cm = cargaAltamiraManager.getCierreMensual(cm);
			if (cm == null || cm.getFechaUltimaCarga() == null) {
				return;
			}
			c.setTime(cm.getFechaUltimaCarga());
			Calendar c2 = Calendar.getInstance();
			if (c2.get(Calendar.DAY_OF_MONTH) != c.get(Calendar.DAY_OF_MONTH)) {
				return;
			}
			int cantDias = 1;
			Parametro p = new Parametro();
			p.setNombre(Parametro.DELTA_BORRADO_ANULACION);
			p = parametroDAO.findByPrimaryKey(p);

			cantDias = p.getValor().intValue();

			for (NotaContable nc : notaContableDAO.findToAnular(cantDias)) {
				Instancia i = new Instancia();
				i.setCodigoNotaContable(nc.getCodigo());
				i = instanciaDAO.findByNotaContable(i);
				notasContablesManager.anularNotaContable(i, 0);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadProcesarActividadesPendientes(String mail) throws Exception {
		UsuarioModulo um = new UsuarioModulo();
		um.setEMailModificado(mail);
		UsuarioLogueado ul = new UsuarioLogueado(um);

		notasContablesSession.processSendMail(new ArrayList<Instancia>(notasContablesSession.getInstanciasPendientes()), ul);
	}
}
