package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.ClaseRiesgo;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@KeepAlive
public class PerdidaOperacionalClaseRiesgoPage extends GeneralCargaPage<ClaseRiesgo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8330009617976284212L;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public PerdidaOperacionalClaseRiesgoPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<ClaseRiesgo> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getClasesRiesgo();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<ClaseRiesgo> _buscarPorFiltro() throws Exception {
		return cargaAltamiraManager.searchClaseRiesgo(param);
	}

	@Override
	protected String _getPage() {
		return PERD_OPER_CLAS_RIES;
	}
}
