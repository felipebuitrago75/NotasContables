package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.util.Collection;

import javax.faces.bean.ViewScoped;

import com.papelesinteligentes.bbva.notascontables.carga.dto.PerdidaOperacionalClaseRiesgo;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@ViewScoped
public class PerdidaOperacionalClaseRiesgoTipoPerdidaPage extends GeneralCargaPage<PerdidaOperacionalClaseRiesgo> {

	private static final long serialVersionUID = -7767388810824414064L;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public PerdidaOperacionalClaseRiesgoTipoPerdidaPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<PerdidaOperacionalClaseRiesgo> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getPerdidaOperacionalClaseRiesgo();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<PerdidaOperacionalClaseRiesgo> _buscarPorFiltro() throws Exception {
		return cargaAltamiraManager.searchPerdidaOperacionalClaseRiesgo(param);
	}

	@Override
	protected String _getPage() {
		return PERD_OPER_CLAS_RIES_TI_PER;
	}
}
