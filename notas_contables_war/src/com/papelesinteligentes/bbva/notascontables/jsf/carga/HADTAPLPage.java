package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.util.Collection;

import javax.faces.bean.ViewScoped;

import com.papelesinteligentes.bbva.notascontables.carga.dto.HADTAPL;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@ViewScoped
public class HADTAPLPage extends GeneralCargaPage<HADTAPL> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8330009617976284212L;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public HADTAPLPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<HADTAPL> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getHADTAPL();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<HADTAPL> _buscarPorFiltro() throws Exception {
		return cargaAltamiraManager.searchHADTAPL(param);
	}

	@Override
	protected String _getPage() {
		return HADTAPL;
	}
}
