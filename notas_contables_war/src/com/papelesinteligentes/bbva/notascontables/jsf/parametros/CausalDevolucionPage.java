package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.CausalDevolucion;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad CausalDevolucion
 * </p>
 * 
 */
@KeepAlive
public class CausalDevolucionPage extends GeneralParametrosPage<CausalDevolucion, CausalDevolucion> {

	private static final long serialVersionUID = 1L;

	public CausalDevolucionPage() {
		super(true);
	}

	/**
	 * retorna una instancia de CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	protected CausalDevolucion _getInstance() {
		return new CausalDevolucion();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	public Collection<CausalDevolucion> _buscarTodos() throws Exception {
		return notasContablesManager.getCausalesDevolucion();
	}

	/**
	 * Realiza la busqueda de entidades de tipo CausalDevolucion filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<CausalDevolucion> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchCausalDevolucion(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new CausalDevolucion();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			// si se trata de un objeto nuevo
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addCausalDevolucion(objActual, getCodUsuarioLogueado());
			} else {// si se trata de una actualizacion y el cambio el valido
				notasContablesManager.updateCausalDevolucion(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe otra causal de devolución con la misma información");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getNombre(), "nombre");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoCausalDevolucion(notasContablesManager.getCausalDevolucion(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteCausalDevolucion(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return CAUSAL_DEVOLUCION;
	}
}
