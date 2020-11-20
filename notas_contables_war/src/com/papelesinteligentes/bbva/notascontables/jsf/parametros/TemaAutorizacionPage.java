package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;

import javax.faces.bean.ViewScoped;

import com.papelesinteligentes.bbva.notascontables.dto.TemaAutorizacion;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad TemaAutorizacion
 * </p>
 * 
 */
@ViewScoped
public class TemaAutorizacionPage extends GeneralParametrosPage<TemaAutorizacion, TemaAutorizacion> {

	private static final long serialVersionUID = 1L;

	public TemaAutorizacionPage() {
		super(true);
	}

	/**
	 * retorna una instancia de TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	protected TemaAutorizacion _getInstance() {
		return new TemaAutorizacion();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	public Collection<TemaAutorizacion> _buscarTodos() throws Exception {
		return notasContablesManager.getTemasAutorizacion();
	}

	/**
	 * Realiza la busqueda de entidades de tipo TemaAutorizacion filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<TemaAutorizacion> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchTemaAutorizacion(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new TemaAutorizacion();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			// si se trata de un objeto nuevo
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addTemaAutorizacion(objActual, getCodUsuarioLogueado());
			} else {// si se trata de una actualizacion y el cambio el valido
				notasContablesManager.updateTemaAutorizacion(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un tema de autorización con ese nombre");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getNombre(), "nombre");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoTemaAutorizacion(notasContablesManager.getTemaAutorizacion(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteTemaAutorizacion(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return TEMA_AUTORIZACION;
	}
}
