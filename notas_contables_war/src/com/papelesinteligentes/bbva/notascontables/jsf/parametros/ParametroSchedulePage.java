package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import com.papelesinteligentes.bbva.notascontables.dto.Parametro;
import com.papelesinteligentes.bbva.notascontables.jsf.GeneralPage;
import com.papelesinteligentes.bbva.notascontables.jsf.IPages;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */

public class ParametroSchedulePage extends GeneralPage implements IPages {

	List<Parametro> parametros = null;

	@Override
	protected void _init() throws Exception {
		// TODO Auto-generated method stub
		if (parametros == null) {
			parametros = new ArrayList<Parametro>(notasContablesManager.getParametros());
		}
	}

	public String inicializar() {
		return PARAM_SCHEDULE;
	}

	public String guardar() {
		try {
			notasContablesManager.updateParametros(parametros, getUsuarioLogueado().getUsuario().getCodigo().intValue());
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "La información ha sido actualizada");
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error guardando los parámetros");
		}
		return null;
	}

	public Parametro getCopiaSeguridadPar() {
		for (Parametro p : parametros) {
			if (p.getNombre().equalsIgnoreCase(Parametro.CIERRE_MENSUAL)) {
				return p;
			}
		}
		return new Parametro();
	}

	public Parametro getDeltaBorradoPar() {
		for (Parametro p : parametros) {
			if (p.getNombre().equalsIgnoreCase(Parametro.DELTA_BORRADO_ANULACION)) {
				return p;
			}
		}
		return new Parametro();
	}

}
