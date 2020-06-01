package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalLineaOperativa;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@KeepAlive
public class RiesgoOperacionalLineaOperativaPage extends GeneralCargaPage<RiesgoOperacionalLineaOperativa> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8330009617976284212L;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public RiesgoOperacionalLineaOperativaPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<RiesgoOperacionalLineaOperativa> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getRiesgosOperacionalesLineasOperativas();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<RiesgoOperacionalLineaOperativa> _buscarPorFiltro() throws Exception {
		return cargaAltamiraManager.searchRiesgoOperacionalLineaOperativa(param);
	}

	@Override
	protected String _getPage() {
		return RIES_OPER_LIN_OPER;
	}
}
