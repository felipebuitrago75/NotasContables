package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.ActividadEconomica;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@KeepAlive
public class ActividadEconomicaPage extends GeneralCargaPage<ActividadEconomica> {

	private static final long serialVersionUID = -2215153957937462919L;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public ActividadEconomicaPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<ActividadEconomica> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getActividadesEconomicas();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<ActividadEconomica> _buscarPorFiltro() throws Exception {
		return cargaAltamiraManager.searchActividadEconomica(param);
	}

	@Override
	protected String _getPage() {
		return ACTIVIDAD_ECONOMICA;
	}
}
