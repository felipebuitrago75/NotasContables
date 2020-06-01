package com.papelesinteligentes.bbva.notascontables.jsf.adminnota;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.servlet.ServletContext;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.ErrorValidacion;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RechazoSalida;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTotal;
import com.papelesinteligentes.bbva.notascontables.jsf.consultas.GeneralConsultaPage;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;
import com.papelesinteligentes.bbva.notascontables.util.ReportesExcel;
import com.papelesinteligentes.bbva.notascontables.util.StringUtils;

@KeepAlive
public class PrecierreCierrePage extends GeneralConsultaPage<Instancia> {

	private static final long serialVersionUID = -6709113217662690209L;

	protected final ServletContext context;
	private final FileGenerator fileGenerator;
	private boolean esPrecierre;
	protected List<NotaContableTotal> totales;
	protected String excelFileName = "";
	protected boolean mostrarArchAlt = false;
	protected boolean mostrarArchExc = false;

	public PrecierreCierrePage() {
		super();
		context = (ServletContext) getExternalContext().getContext();
		fileGenerator = new FileGenerator(notasContablesManager, cargaAltamiraManager, getUsuarioLogueado().getUsuario().getCodigo().intValue());
	}

	@Override
	protected void _init() {
		super._init();
	}

	public String mostrar() {
		return "";
	}

	public void cargarRegistros(boolean esPrecierre) {
		this.esPrecierre = esPrecierre;
		try {
			Collection<NotaContable> notasContables = notasContablesManager.getNotaContablesPrecierreCierre(esPrecierre);
			ArrayList<Instancia> instancias = new ArrayList<Instancia>();
			for (NotaContable nc : notasContables) {
				String rechazo = "";
				Collection<RechazoSalida> res = notasContablesManager.getRechazoSalidaByNotaContable(nc.getNumeroRadicacion());
				nc.setRechazos(new ArrayList<RechazoSalida>(res));
				if (!res.isEmpty()) {
					rechazo = "<table><th>Cuenta</th><th>Divisa</th><th>Destino</th><th>Error</th>";
					for (RechazoSalida rs : res) {
						ErrorValidacion rv = new ErrorValidacion();
						rv.setCodigo(StringUtils.getStringLeftPadding(rs.getCodError().toString(), 4, '0'));
						rv = cargaAltamiraManager.getErrorValidacion(rv);
						rechazo += "<tr><td>" + rs.getCuenta() + "</td> <td>" + rs.getDivisa() + "</td> <td>" + rs.getCeDestin() + "</td> <td>" + rv.getNombre() + "</td> </tr>";
					}
					rechazo += "</table>";
				}
				nc.setCausalDeRechazo(rechazo);
				Instancia instancia = new Instancia();
				instancia.setCodigoNotaContable(nc.getCodigo());
				instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
				instancia.setNC(nc);
				instancias.add(instancia);
			}

			totales = notasContablesManager.getDatosDeInstancias(instancias, true);
			setDatos(instancias);
			if (getDatos().isEmpty() && !hayMensajes()) {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "No hay notas " + (esPrecierre ? "en Precierre" : "cerradas"));
			}
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al realizar la consulta de Precierre");
		}
	}

	public String generarArchivoExcel(String path, String file, String title) {
		try {
			List<NotaContable> notas = new ArrayList<NotaContable>();
			excelFileName = file + StringUtils.getString(DateUtils.getTimestamp(), "ddMMyyyyhhmmss") + ".xls";
			for (Instancia i : getDatos()) {
				notas.add(i.getNC());
			}
			ReportesExcel reportesExcel = new ReportesExcel();
			reportesExcel.setStrPath(path);
			reportesExcel.setNombreArchivo(excelFileName);
			reportesExcel.getReporteGeneral(title, notas);
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "El archivo de excel ha sido generado y guardado en " + path + excelFileName);
			mostrarArchExc = true;
		} catch (Exception e) {
			lanzarError(e, "Ocurrio un error al generar el archivo de Excel");
		}
		return mostrar();
	}

	protected String generarArchivoAltamira(String path, String interfaz, String puc, String terceros,String indicaPc) {
		try {
			fileGenerator.generarInterfazContable(path + interfaz, getDatos());
			fileGenerator.generarPUC(path + puc);
			fileGenerator.generarTerceros(path + terceros);
			if(!esPrecierre)
				fileGenerator.generarIndicaCierre(path + indicaPc);
			else
			fileGenerator.generarIndicaPrecierre(path + indicaPc);
			mostrarArchAlt = true;
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "Los archivos se han generado correctamente");
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Ocurrio un error al generar el archivo de Interfaz Contable");
		}
		return mostrar();
	}

	@Override
	protected String _getPage() {
		return mostrar();
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {
		return new ArrayList<Instancia>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	public List<NotaContableTotal> getTotales() {
		return totales;
	}

	public void setTotales(List<NotaContableTotal> totales) {
		this.totales = totales;
	}

	public boolean isMostrarArchAlt() {
		return mostrarArchAlt;
	}

	public void setMostrarArchAlt(boolean mostrarArchAlt) {
		this.mostrarArchAlt = mostrarArchAlt;
	}

	public boolean isMostrarArchExc() {
		return mostrarArchExc;
	}

	public void setMostrarArchExc(boolean mostrarArchExc) {
		this.mostrarArchExc = mostrarArchExc;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public boolean isEsPrecierre() {
		return esPrecierre;
	}

	public void setEsPrecierre(boolean esPrecierre) {
		this.esPrecierre = esPrecierre;
	}

}
