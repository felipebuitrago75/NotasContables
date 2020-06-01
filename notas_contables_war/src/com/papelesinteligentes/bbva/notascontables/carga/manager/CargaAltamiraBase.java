package com.papelesinteligentes.bbva.notascontables.carga.manager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import com.papelesinteligentes.bbva.notascontables.carga.dto.EstructuraCarga;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RegistroCarga;
import com.papelesinteligentes.bbva.notascontables.facade.CargaAltamiraSession;
import com.papelesinteligentes.bbva.notascontables.facade.NotasContablesSession;
import com.papelesinteligentes.bbva.notascontables.facade.impl.CargaAltamiraSessionBean;
import com.papelesinteligentes.bbva.notascontables.facade.impl.NotasContablesSessionBean;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;
//import com.papelesinteligentes.bbva.notascontables.util.Log;

public abstract class CargaAltamiraBase {

	protected CargaAltamiraSession cargaAltamiraManager = new CargaAltamiraSessionBean();
	protected NotasContablesSession notasContablesManager = new NotasContablesSessionBean();
	//protected Log escribir = new Log();

	protected void getDatos(String as_nombreTabla, File f, String as_nombreArchivo, EstructuraCarga[] estructurasCarga, boolean eliminar) throws Exception {
		RegistroCarga registroCarga = new RegistroCarga();
		String ls_linea = "";
		int contadorLineas = 0;

		registroCarga.setFechaInicio(DateUtils.getTimestamp());

		FileInputStream lfis_stream = null;
		DataInputStream in = null;
		BufferedReader lbr_bufferReader = null;
		try {
			lfis_stream = new FileInputStream(f);

			in = new DataInputStream(lfis_stream);
			lbr_bufferReader = new BufferedReader(new InputStreamReader(in));

			cargaAltamiraManager.beginLoad(as_nombreTabla, eliminar);
			while ((ls_linea = lbr_bufferReader.readLine()) != null) {
				try {
					cargaAltamiraManager.addRow(as_nombreTabla, ls_linea, estructurasCarga);
					//contadorLineas++;
					//System.out.println(contadorLineas);
					if (contadorLineas % 1000 == 0) {
						//System.out.println("Registros Cargados: " + contadorLineas);
						// MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE
						//Log.escribirLogInfo("Registros Cargados: " + contadorLineas);
					}
					
				} catch (Exception le_e) {
					//System.err.println("Error al procesar el registro número " + contadorLineas + "\tTabla: " + as_nombreTabla + "\tDEtalle: " + ls_linea);
					/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
					//Log.escribirLogInfo("Error al procesar el registro número " + contadorLineas + "\tTabla: " + as_nombreTabla + "\tDEtalle: " + ls_linea);
					le_e.printStackTrace();
				}
			}
			/**	
			if (as_nombreTabla.equals("NC_CLASE_RIESGO")) {
				cargaAltamiraManager.endLoadClaseRiesgo(as_nombreArchivo);
				/**} else if (as_nombreTabla.equals("NC_CLIENTE")) {
				cargaAltamiraManager.endLoadCliente(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_CONTRATO")) {
				cargaAltamiraManager.endLoadContrato(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_PERD_OPER_CLAS_RIES")) {
				cargaAltamiraManager.endLoadPerdidaOperacionalClaseRiesgo(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_PARTIDA_PENDIENTE")) {
				cargaAltamiraManager.endLoadPartidaPendiente(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_PERDIDA_OPERACION")) {
				cargaAltamiraManager.endLoadPerdidaOperacional(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_USUARIO_ALTAMIRA")) {
				cargaAltamiraManager.endLoadUsuarioAltamira(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_HADTAPL")) {
				cargaAltamiraManager.endLoadHADTAPL(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_PRODUCTO")) {
				cargaAltamiraManager.endLoadProducto(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_SUCURSAL")) {
				cargaAltamiraManager.endLoadSucursal(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_PUC")) {
				cargaAltamiraManager.endLoadPUC(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_DIVISA")) {
				cargaAltamiraManager.endLoadDivisa(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_TERCERO")) {
				cargaAltamiraManager.endLoadTercero(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_TIPO_IDENTIFICACION")) {
				cargaAltamiraManager.endLoadTipoIdentificacion(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_TIPO_TELEFONO")) {
				cargaAltamiraManager.endLoadTipoTelefono(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_TIPO_INDICATIVO")) {
				cargaAltamiraManager.endLoadTipoIndicativo(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_RIES_OPER_PROD")) {
				cargaAltamiraManager.endLoadRiesgoOperacionalProducto(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_RIES_OPER_PROC")) {
				cargaAltamiraManager.endLoadRiesgoOperacionalProceso(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_RIES_OPER_LINE_OPER")) {
				cargaAltamiraManager.endLoadRiesgoOperacionalLineaOperativa(as_nombreArchivo);
				/** Incidencia COL396113I000838
			}else if (as_nombreTabla.equals("NC_RIES_OPER_LINE_OPER")) {
					cargaAltamiraManager.endLoadRiesgoOperacionalLineaOperativa(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_MUNICIPIO")) {
				cargaAltamiraManager.endLoadMunicipio(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_ERROR_VALIDACION")) {
				cargaAltamiraManager.endLoadErrorValidacion(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_DEPARTAMENTO")) {
				cargaAltamiraManager.endLoadDepartamento(as_nombreArchivo);
			} else if (as_nombreTabla.equals("NC_ACTIVIDAD_ECO")) {
				cargaAltamiraManager.endLoadActividadEconomica(as_nombreArchivo);
			}**/

			registroCarga.setFechaFin(DateUtils.getTimestamp());
			registroCarga.setNombreArchivo(as_nombreArchivo);
			registroCarga.setRegistrosCargados(contadorLineas);
			/**BLOQUEO BASE DE DATOS **/
			//cargaAltamiraManager.addRegistroCarga(registroCarga);
			// se borra el archivo si todo fue ok
			f.delete();

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (lbr_bufferReader != null) {
					lbr_bufferReader.close();
				}
				if (in != null) {
					in.close();
				}
				if (lfis_stream != null) {
					lfis_stream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
