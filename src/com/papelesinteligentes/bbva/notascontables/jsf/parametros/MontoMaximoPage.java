package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;

import javax.faces.application.FacesMessage;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.MontoMaximo;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad MontoMaximo
 * </p>
 * 
 */
@KeepAlive
public class MontoMaximoPage extends GeneralParametrosPage<MontoMaximo, MontoMaximo> {

	private static final long serialVersionUID = 1L;

	public MontoMaximoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de MontoMaximo
	 * 
	 * @return
	 */
	@Override
	protected MontoMaximo _getInstance() {
		return new MontoMaximo();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoMaximo> _buscarTodos() throws Exception {
		return notasContablesManager.getMontoMaximos();
	}

	/**
	 * Realiza la busqueda de entidades de tipo MontoMaximo filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoMaximo> _buscarPorFiltro() throws Exception {
		return notasContablesManager.getMontoMaximos();

	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new MontoMaximo();
	}

	@Override
	protected boolean _guardar() throws Exception {
		int codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo() <= 0) {
				notasContablesManager.addMontoMaximo(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateMontoMaximo(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un Monto Máximo con el mismo tipo de moneda");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getDivisa().equals("-1")) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar una divisa");
		}

	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoMontoMaximo(notasContablesManager.getMontoMaximo(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteMontoMaximo(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return MONTO_MAXIMO;
	}
}
