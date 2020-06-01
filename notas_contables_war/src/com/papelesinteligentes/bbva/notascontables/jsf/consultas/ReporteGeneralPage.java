package com.papelesinteligentes.bbva.notascontables.jsf.consultas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Divisa;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.dto.Concepto;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTotal;
import com.papelesinteligentes.bbva.notascontables.dto.Tema;
import com.papelesinteligentes.bbva.notascontables.dto.TipoEvento;
import com.papelesinteligentes.bbva.notascontables.jsf.adminnota.PrecierreCierrePage;

@KeepAlive
public class ReporteGeneralPage extends GeneralConsultaPage<Instancia> {

	private static final long serialVersionUID = -6709113217662690209L;

	// variables para combos
	protected List<SelectItem> sucursales = new ArrayList<SelectItem>();
	protected List<SelectItem> conceptos = new ArrayList<SelectItem>();
	protected List<SelectItem> temas = new ArrayList<SelectItem>();
	protected List<SelectItem> tiposEvento = new ArrayList<SelectItem>();
	protected List<SelectItem> divisas = new ArrayList<SelectItem>();

	// variables para guardar los datos del filtro
	protected String tipoNota;
	protected String sucOrigen;
	protected String sucDestino;
	protected Integer concepto;
	protected Integer tema;
	protected Integer tipoEvento;
	protected String divisa;
	protected String partida;
	protected String numIdentificacion;
	protected Date desde;
	protected Date hasta;
	protected String estado;
	protected String descripcion;

	protected List<NotaContableTotal> totales = new ArrayList<NotaContableTotal>();

	/**
	 * indica el numero de pagina del scroller de actividades
	 */
	public Integer scrollPageAct = 1;

	public ReporteGeneralPage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		if (sucursales == null || sucursales.isEmpty()) {
			try {
				sucursales = getSelectItemList(notasContablesManager.getCV(Sucursal.class));
				conceptos = getSelectItemList(notasContablesManager.getCV(Concepto.class), false);
				temas = new ArrayList<SelectItem>();
				tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
				divisas = getSelectItemList(notasContablesManager.getCV(Divisa.class));
			} catch (Exception e) {
				lanzarError(e, "Error inicializando filtro de busqueda ");
			}
		}
	}

	public String generarArchivoExcel() {
		PrecierreCierrePage bean = (PrecierreCierrePage) getBean("precierreCierrePage");
		try {
			for (Instancia ins : getDatos()) {
				NotaContable nc = new NotaContable();
				nc.setCodigo(ins.getCodigoNotaContable());
				nc = notasContablesManager.getNotaContable(nc);
				ins.setNC(nc);
			}
			bean.setDatos(getDatos());
			return bean.generarArchivoExcel(DIR_REPORTES_EXCEL, "NC_PRECIERRE_", "Reporte");
		} catch (Exception e) {
			lanzarError(e, "Error generando el reporte");
		}
		return null;
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {
		Collection<Instancia> instancias = new ArrayList<Instancia>();
		java.sql.Date desdeD = desde != null ? new java.sql.Date(desde.getTime()) : null;
		java.sql.Date hastaD = hasta != null ? new java.sql.Date(hasta.getTime()) : null;
		if (hastaD != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(hastaD);
			c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.MILLISECOND, 0);
			hastaD.setTime(c.getTimeInMillis());
		}
		if (sucOrigen == null) {
			sucOrigen = "";
		}
		if (tipoNota.trim().equals("")) {
			instancias = notasContablesManager.getInstanciasPor(sucOrigen, sucDestino, concepto, tema, tipoEvento, partida, numIdentificacion, desdeD, hastaD, divisa, estado, descripcion, getUsuarioLogueado().getSucursal().getCodigo(),
					getUsuarioLogueado().getRolActual().getCodigo().intValue());
			instancias.addAll(notasContablesManager.getInstanciasRegLibrePor(sucOrigen, sucDestino, partida, numIdentificacion, desdeD, hastaD, divisa, estado, descripcion, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado()
					.getRolActual().getCodigo().intValue()));
			instancias.addAll(notasContablesManager.getInstanciasCruceRefPor(sucOrigen, sucDestino, partida, desdeD, hastaD, divisa, estado, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		} else {
			if (tipoNota.trim().equals(NotaContable.NORMAL)) {
				instancias = notasContablesManager.getInstanciasPor(sucOrigen, sucDestino, concepto, tema, tipoEvento, partida, numIdentificacion, desdeD, hastaD, divisa, estado, descripcion, getUsuarioLogueado().getSucursal().getCodigo(),
						getUsuarioLogueado().getRolActual().getCodigo().intValue());
			} else if (tipoNota.trim().equals(NotaContable.LIBRE)) {
				instancias = notasContablesManager.getInstanciasRegLibrePor(sucOrigen, sucDestino, partida, numIdentificacion, desdeD, hastaD, divisa, estado, descripcion, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado()
						.getRolActual().getCodigo().intValue());
			} else {// if (tipoNota.equals(NotaContable.CRUCE_REFERENCIA)) {
				instancias = notasContablesManager.getInstanciasCruceRefPor(sucOrigen, sucDestino, partida, desdeD, hastaD, divisa, estado, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue());
			}
		}
		totales = notasContablesManager.getDatosDeInstancias(instancias, true);
		return instancias;
	}

	public String buscarTemas() {
		temas = new ArrayList<SelectItem>();
		if (concepto.intValue() > 0) {
			try {
				Tema row = new Tema();
				row.setCodigoConcepto(concepto.intValue());
				for (Tema t : notasContablesManager.getTemasPorConcepto(row)) {
					temas.add(new SelectItem(t.getCodigo(), t.getNombre()));
				}
			} catch (Exception e) {
				lanzarError(e, "Error Consultando los temas asociados al concepto seleccionado");
			}
		}
		return null;
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return REPORTE_GENERAL;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<SelectItem> conceptos) {
		this.conceptos = conceptos;
	}

	public List<SelectItem> getTemas() {
		return temas;
	}

	public void setTemas(List<SelectItem> temas) {
		this.temas = temas;
	}

	public List<SelectItem> getTiposEvento() {
		return tiposEvento;
	}

	public void setTiposEvento(List<SelectItem> tiposEvento) {
		this.tiposEvento = tiposEvento;
	}

	public List<SelectItem> getDivisas() {
		return divisas;
	}

	public void setDivisas(List<SelectItem> divisas) {
		this.divisas = divisas;
	}

	public String getSucOrigen() {
		return sucOrigen;
	}

	public void setSucOrigen(String sucOrigen) {
		this.sucOrigen = sucOrigen == null ? "" : sucOrigen;
	}

	public String getSucDestino() {
		return sucDestino;
	}

	public void setSucDestino(String sucDestino) {
		this.sucDestino = sucDestino == null ? "" : sucDestino;
	}

	public Integer getConcepto() {
		return concepto;
	}

	public void setConcepto(Integer concepto) {
		this.concepto = concepto;
	}

	public Integer getTema() {
		return tema;
	}

	public void setTema(Integer tema) {
		this.tema = tema;
	}

	public Integer getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(Integer tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa == null ? "" : divisa;
	}

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida == null ? "" : partida;
	}

	public String getNumIdentificacion() {
		return numIdentificacion;
	}

	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion == null ? "" : numIdentificacion;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado == null ? "" : estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion == null ? "" : descripcion;
	}

	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota == null ? "" : tipoNota;
	}

}
