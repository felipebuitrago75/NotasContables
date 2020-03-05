package com.papelesinteligentes.bbva.notascontables.jsf.adminnota;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.papelesinteligentes.bbva.notascontables.dto.FechaHabilitada;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableCrucePartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableRegistroLibre;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTema;
import com.papelesinteligentes.bbva.notascontables.facade.CargaAltamiraSession;
import com.papelesinteligentes.bbva.notascontables.facade.NotasContablesSession;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;

public class FileGenerator {

	private final NotasContablesSession notasContablesManager;
	private final CargaAltamiraSession cargaAltamiraManager;
	private final int codigoUsuario;

	// private static int consecutivo = 0;

	public FileGenerator(NotasContablesSession notasContablesManager, CargaAltamiraSession cargaAltamiraManager, int codigoUsuario) {
		super();
		this.notasContablesManager = notasContablesManager;
		this.cargaAltamiraManager = cargaAltamiraManager;
		this.codigoUsuario = codigoUsuario;
	}

	public void generarPUC(String rutaArchivo) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		for (String str : cargaAltamiraManager.getPUCArchAltamira()) {
			pw.println(str);
		}
		pw.close();
	}

	public void generarTerceros(String rutaArchivo) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		for (String str : cargaAltamiraManager.getTercerosArch()) {
			pw.println(str);
		}
		pw.close();
	}
	/**GP10266 CREACION ARCHIVO VACIO INDICA EL PRECIERRE**/
	public void generarIndicaPrecierre(String rutaArchivo) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		String str="";
			pw.println(str);
		pw.close();
	}
	
	/**GP10266 CREACION ARCHIVO CON LETRA C DE CIERRE**/
	public void generarIndicaCierre(String rutaArchivo) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		String str="C";
			pw.println(str);
		pw.close();
	}
	

	public void generarInterfazContable(String rutaArchivo, final List<Instancia> instancias) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		ArrayList<java.util.Date> diasNoHabiles = new ArrayList<java.util.Date>(cargaAltamiraManager.getFestivosFecha());
		for (Instancia instancia : instancias) {
			NotaContable notaContable = instancia.getNC();
			if (notaContable.getTipoNota().equals(NotaContable.NORMAL)) {

				Collection<NotaContableTema> temasNota = notasContablesManager.getTemasPorNotaContable(notaContable.getCodigo().intValue());
				/******************INCIDENCIA GENERACION PRECIERRE SABADOS**************/
				for (NotaContableTema t : temasNota) {
					// si es necesario, se actualiza la fecha contable de la nota
					
					if (fechaInvalida(t.getCodigoSucursalDestinoPartida(), t.getFechaContable(), diasNoHabiles) || fechaInvalida(t.getCodigoSucursalDestinoContraPartida(), t.getFechaContable(), diasNoHabiles)) {
						t.setFechaContable(new Date(Calendar.getInstance().getTimeInMillis()));
						notasContablesManager.updateFechaNotaContableTema(t, codigoUsuario);
						break;
					}
					//fechaNota = t.getFechaContable();
					//existeDiaNoHabil = diasNoHabiles.contains(fechaNota);
				}
				/******************INCIDENCIA GENERACION PRECIERRE SABADOS**************
				if (update) {
					boolean existeDiaNoHabil;
					Date stamp = new Date(System.currentTimeMillis());
					Date fechaNota = new Date(stamp.getTime());  
					
					for (NotaContableTema t : temasNota) {
						fechaNota = t.getFechaContable();
							  existeDiaNoHabil = diasNoHabiles.contains(fechaNota);
							  if(existeDiaNoHabil){
								  t.setFechaContable(new Date(Calendar.getInstance().getTimeInMillis()-86400000));
								try {
									notasContablesManager.updateFechaNotaContableTema(t, codigoUsuario);
									break;
								} catch (Exception e) {
									e.printStackTrace();
								}
						  } 
					}
			}
				*/
				for (String str : notasContablesManager.getInfoGeneralArchAltamira(notaContable.getCodigo().intValue())) {
					pw.println(str);
				}
			} else if (notaContable.getTipoNota().equals(NotaContable.CRUCE_REFERENCIA)) {
				ArrayList<NotaContableCrucePartidaPendiente> temasNotaContable = new ArrayList<NotaContableCrucePartidaPendiente>(notasContablesManager.getCrucesPartidasPendientesPorNotaContable(notaContable.getCodigo().intValue()));
				boolean update = true;  //inicial false PARA REFERENCIA DE CRUCE LA FECHA SIEMPRE ES LA DEL DIA
				for (NotaContableCrucePartidaPendiente t : temasNotaContable) {
					 //'si es necesario, se actualiza la fecha contable de la nota
					if (fechaInvalida(t.getCodigoSucursalDestino(), new Date(t.getFechaContable().getTime()), diasNoHabiles)) {
						update = true;
						break;
					}
				}
				if (update) {
					for (NotaContableCrucePartidaPendiente t : temasNotaContable) {
						// 'si es necesario, se actualiza la fecha contable de la nota
						/******************INCIDENCIA GENERACION PRECIERRE SABADOS**************/
						//cambioFechaNoValida(t, diasNoHabiles);
						t.setFechaContable(new Timestamp(Calendar.getInstance().getTimeInMillis()));
						notasContablesManager.updateFechaNotaContableCruceRef(t, codigoUsuario);
					}
				} 
				for (String str : notasContablesManager.getInfoCruceArchAltamira(notaContable.getCodigo().intValue())) {
					pw.println(str);
				}
			} else if (notaContable.getTipoNota().equals(NotaContable.LIBRE)) {
				ArrayList<NotaContableRegistroLibre> temasNotaContable = new ArrayList<NotaContableRegistroLibre>(notasContablesManager.getRegistrosNotaContableLibre(notaContable.getCodigo()));
				boolean update = false;
				for (NotaContableRegistroLibre t : temasNotaContable) {
					// si es necesario, se actualiza la fecha contable de la nota
					if (fechaInvalida(t.getCodigoSucursalDestino(), t.getFechaContable(), diasNoHabiles)) {
						update = true;
						break;
					}
				}
				if (update) {
					for (NotaContableRegistroLibre t : temasNotaContable) {
						// si es necesario, se actualiza la fecha contable de la nota
						t.setFechaContable(new Date(Calendar.getInstance().getTimeInMillis()));
						notasContablesManager.updateFechaNotaContableRegLibre(t, codigoUsuario);
						/******************INCIDENCIA GENERACION PRECIERRE SABADOS**************/
						 //existeDiaNoHabil;
						/*  for(java.util.Date dd: diasNoHabiles){
							  existeDiaNoHabil = diasNoHabiles.contains(dd);
							  if(existeDiaNoHabil){
								  t.setFechaContable(new Date(Calendar.getInstance().getTimeInMillis()-86400000));
								try {
									notasContablesManager.updateFechaNotaContableRegLibre(t, codigoUsuario);
									break;
								} catch (Exception e) {
									e.printStackTrace();
								}
						  } 
					}*/
						//t.setFechaContable(new Date(Calendar.getInstance().getTimeInMillis()));
						//notasContablesManager.updateFechaNotaContableRegLibre(t, codigoUsuario);
					}
				}
				for (String str : notasContablesManager.getInfoLibreArchAltamira(notaContable.getCodigo().intValue())) {
					pw.println(str);
				}
			}
		}
		pw.close();
	}

	/**
	 * Verifica si es necesario cambiar la fecha segun los dias habilitados para cada sucursal
	 * 
	 * @param suc
	 * @param fechaContable
	 * @param diasNoHabiles
	 * @return
	 * @throws Exception
	 */
	private boolean fechaInvalida(String suc, Date fechaContable, ArrayList<java.util.Date> diasNoHabiles) throws Exception {
		FechaHabilitada fecha = new FechaHabilitada();
		fecha.setCodigoSucursal(suc);
		fecha = notasContablesManager.getFechaHabilitadaPorSucursal(fecha);

		java.util.Date minFechaSuc = DateUtils.getDateTodayBeforeDays(fecha.getDias().intValue(), diasNoHabiles);

		// si la fecha contable es menor a la minima permitida, se debe ajustar al día de hoy
		return fechaContable.getTime() < minFechaSuc.getTime();
	}
	
	/**************************INCIDENCIA SABADOS GENERACION PRECIERRE*******************************
	private void cambioFechaNoValida(NotaContableCrucePartidaPendiente t, ArrayList<java.util.Date> diasNoHabiles){
		  boolean existeDiaNoHabil;
		  for(java.util.Date dd: diasNoHabiles){
			  existeDiaNoHabil = diasNoHabiles.contains(dd);
			  if(existeDiaNoHabil){
				t.setFechaContable(new Timestamp(Calendar.getInstance().getTimeInMillis()-86400000));
				try {
					notasContablesManager.updateFechaNotaContableCruceRef(t, codigoUsuario);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			  } 
		  }
	}*/
}
