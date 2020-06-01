package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;

import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;
import com.papelesinteligentes.bbva.notascontables.jsf.carga.GeneralCargaPage;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
public abstract class GeneralParametrosPage<T extends CommonVO<T>, D> extends GeneralCargaPage<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4039536732981625179L;

	protected D objActual;

	/**
	 * Crea una instnacia del objeto a manejar
	 * 
	 */
	protected abstract D _getInstance();

	/**
	 * Logica adicional para motrar el editor. Propia de cada página
	 * 
	 * @throws Exception
	 */
	protected abstract void _editar() throws Exception;

	/**
	 * Logica propia de guardado de informacion
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean _guardar() throws Exception;

	/**
	 * Validacion propia de informacion
	 * 
	 * @throws Exception
	 */
	protected abstract void _validar() throws Exception;

	/**
	 * Logica propia de cambio de estado
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean _cambiarEstado() throws Exception;

	/**
	 * Logica propia de borrado de datos
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean _borrar() throws Exception;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public GeneralParametrosPage(boolean buscarTodos) {
		super(buscarTodos);
	}

	/**
	 * <p>
	 * Automatically managed component initialization. <strong>WARNING:</strong> This method is automatically generated, so any user-specified code inserted here is subject to being replaced.
	 * </p>
	 * 
	 * @param buscarTodos
	 */
	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Metodo que enmascara la logica de validacion de informacion antes de guardar o actualizar
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean datosValidos() throws Exception {
		_validar();
		return !getFacesContext().getMessages().hasNext();
	}

	/**
	 * Metodo que enmascara la logica encargada de inicializar el editor de informacion
	 * 
	 * @return
	 */
	public String editar() {
		try {
			_editar();
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error al inicializar el editor");
		}
		return null;
	}

	/**
	 * Metodo que enmascara la logica encargada de guardar o actualizar la informacion del registro objActual
	 * 
	 * @return
	 */
	public String guardar() {
		try {
			if (datosValidos() && _guardar()) {
				setDatos(new ArrayList<T>(_buscarPorFiltro()));
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La información ha sido guardada correctamente");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error guardar la información");
		}
		return null;
	}

	/**
	 * Metodo que enmascara la logica encargada de cambiar el estado del objActual
	 * 
	 * @return
	 */
	public String cambiarEstado() {
		try {
			if (_cambiarEstado()) {
				setDatos(new ArrayList<T>(_buscarPorFiltro()));
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "El estado ha sido cambiado correctamente");
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No es posible cambiar el estado de este registro");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error al cambiar el estado");
		}
		return null;
	}

	/**
	 * Metodo que enmascara la logica encargada del borrado de la informacion del registro objActual
	 * 
	 * @return
	 */
	public String borrar() {
		try {
			if (_borrar()) {
				setDatos(new ArrayList<T>(_buscarPorFiltro()));
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La información ha sido borrada correctamente");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error al borrar la información");
		}
		return null;
	}

	public D getObjActual() {
		if (objActual == null) {
			objActual = _getInstance();
		}

		return objActual;
	}

	public void setObjActual(D objActual) {
		this.objActual = objActual;
	}

}
