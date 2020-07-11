package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.TipoEvento;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad TipoEvento
 * </p>
 * 
 */
@KeepAlive
public class TipoEventoPage extends GeneralParametrosPage<TipoEvento, TipoEvento> {

	private static final long serialVersionUID = 1L;

	public TipoEventoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de TipoEvento
	 * 
	 * @return
	 */
	@Override
	protected TipoEvento _getInstance() {
		return new TipoEvento();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo TipoEvento
	 * 
	 * @return
	 */
	@Override
	public Collection<TipoEvento> _buscarTodos() throws Exception {
		return notasContablesManager.getTiposEvento();
	}

	/**
	 * Realiza la busqueda de entidades de tipo TipoEvento filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<TipoEvento> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchTipoEvento(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo TipoEvento
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new TipoEvento();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			// si se trata de un objeto nuevo
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addTipoEvento(objActual, getCodUsuarioLogueado());
			} else {// si se trata de una actualizacion y el cambio el valido
				notasContablesManager.updateTipoEvento(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un tipo de evento con el mismo nombre");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getNombre(), "nombre");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo TipoEvento
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoTipoEvento(notasContablesManager.getTipoEvento(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo TipoEvento
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteTipoEvento(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return TIPO_EVENTO;
	}
}
