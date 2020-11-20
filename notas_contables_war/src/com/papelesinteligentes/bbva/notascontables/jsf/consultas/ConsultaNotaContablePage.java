package com.papelesinteligentes.bbva.notascontables.jsf.consultas;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;

@ViewScoped
public class ConsultaNotaContablePage extends GeneralConsultaPage<Instancia> {

	private static final long serialVersionUID = -6709113217662690209L;

	private List<SelectItem> tiposCriterio = new ArrayList<SelectItem>();
	private String numRadicacion;
	private String criterio;
	private String asientoContable;
	private Date fecha;
	
	//gp12833 - aseguramiento anexos
	private static boolean recuperarAnexos;
	// Fin - gp12833 - aseguramiento anexos
		
	/**
	 * indica el numero de pagina del scroller de actividades
	 */
	public Integer scrollPageAct = 1;

	public ConsultaNotaContablePage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		if (tiposCriterio == null || tiposCriterio.isEmpty()) {
			tiposCriterio = new ArrayList<SelectItem>();
			fecha = Calendar.getInstance().getTime();
			criterio = "1";
			tiposCriterio.add(new SelectItem("1", "Número de radicación"));
			tiposCriterio.add(new SelectItem("2", "Asiento contable Altamira"));
			tiposCriterio.add(new SelectItem("3", "Fecha Radicación Módulo"));
		}
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {
		NotaContable notaContable = new NotaContable();
		switch (Integer.valueOf(criterio.trim())) {
		case 1:
			notaContable.setNumeroRadicacion(numRadicacion);
			return new ArrayList<Instancia>(notasContablesManager.getInstanciasPorNumeroRadicacion(notaContable, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		case 2:
			notaContable.setAsientoContable(asientoContable);
			notaContable.setFechaRegistroAltamiraTs(new Timestamp(fecha.getTime()));
			return new ArrayList<Instancia>(notasContablesManager.getInstanciasPorAsientoContableAndFecha(notaContable, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		case 3:
			notaContable.setFechaRegistroAltamiraTs(new Timestamp(fecha.getTime()));
			return new ArrayList<Instancia>(notasContablesManager.getInstanciasPorNotaContable(notaContable, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		}
		return new ArrayList<Instancia>();
	}

	@Override
	protected void _validar() throws Exception {
		switch (Integer.valueOf(criterio.trim())) {
		case 1:
			validador.validarRequerido(numRadicacion, "Número de radicación");
			break;
		case 2:
			validador.validarRequerido(asientoContable, "Asiento contable");
		case 3:
			validador.validarRequerido(fecha, "Fecha");
			break;
		}
	}

	@Override
	protected String _getPage() {
		return CONSULTA_NOTA_CONTABLE;
	}
	
	//gp12833 - aseguramiento anexos
	public String iniciar() {
		recuperarAnexos = false;
		System.out.println(recuperarAnexos);
		System.out.println("...");
		System.out.println(" ");
		return super.iniciar();
	}
	
	public String recuperarAnexos() {
		String vista = iniciar();
		recuperarAnexos = true;
		System.out.println(recuperarAnexos);
		System.out.println("...");
		System.out.println(" ");
		return vista;
	}
	
	public boolean getProcesoRecuperarAnexos() {
		System.out.println("Recuperando valor procesoRecuperarAnexos.");
		System.out.println(recuperarAnexos);
		System.out.println("...");
		System.out.println(" ");
		return recuperarAnexos;
	}
	// Fin - gp12833 - aseguramiento anexos
	
	public List<SelectItem> getTiposCriterio() {
		return tiposCriterio;
	}

	public void setTiposCriterio(List<SelectItem> tiposCriterio) {
		this.tiposCriterio = tiposCriterio;
	}

	public String getNumRadicacion() {
		return numRadicacion;
	}

	public void setNumRadicacion(String numRadicacion) {
		this.numRadicacion = numRadicacion;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	public String getAsientoContable() {
		return asientoContable;
	}

	public void setAsientoContable(String asientoContable) {
		this.asientoContable = asientoContable;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
