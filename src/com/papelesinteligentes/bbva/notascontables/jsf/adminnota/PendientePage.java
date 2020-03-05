package com.papelesinteligentes.bbva.notascontables.jsf.adminnota;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.jsf.consultas.GeneralConsultaPage;

@KeepAlive
public class PendientePage extends GeneralConsultaPage<Instancia> {

	private static final long serialVersionUID = -6709113217662690209L;

	private Instancia instancia;

	public PendientePage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		cargarPendientes();
	}

	public void cargarPendientes() {
		try {
			instancia = new Instancia();
			instancia.setCodigoUsuarioActual(getUsuarioLogueado().getUsuario().getCodigo());
			ArrayList<Instancia> instancias = new ArrayList<Instancia>(notasContablesManager.getInstanciasPorUsuario(instancia));
			setDatos(instancias);
			if (esUltimaFase() && getDatos().isEmpty() && !hayMensajes()) {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "Usted no tiene pendientes");
			}
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al realizar la consulta de Pendientes");
		}
	}

	public String mostrar() {
		return ADMIN_PENDIENTES;
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {
		return new ArrayList<Instancia>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return ADMIN_PENDIENTES;
	}

}
