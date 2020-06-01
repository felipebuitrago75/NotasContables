package com.papelesinteligentes.bbva.notascontables.jsf.consultas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;

import com.papelesinteligentes.bbva.notascontables.jsf.GeneralPage;
import com.papelesinteligentes.bbva.notascontables.jsf.IPages;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
public abstract class GeneralConsultaPage<T> extends GeneralPage implements IPages, Serializable {

	private static final long serialVersionUID = 4721210842962039085L;

	protected T objActual;

	/**
	 * indica el numero de pagina del scroller
	 */
	public Integer scrollPage = 1;

	/**
	 * Lista de datos a mostrar en la tabla
	 */
	private List<T> datos = new ArrayList<T>();

	protected abstract Collection<T> _buscar() throws Exception;

	protected abstract String _getPage();

	/**
	 * Validacion propia de informacion
	 * 
	 * @throws Exception
	 */
	protected abstract void _validar() throws Exception;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public GeneralConsultaPage() {
		super();
		_init();
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
		scrollPage = scrollPage == null ? 1 : scrollPage;
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	public String buscar() {
		try {
			println("Ejecutando el método buscar en la clase " + getClass().getSimpleName());
			// solo se realiza la busqueda si se está en la ultima fase del ciclo de vida de faces
			if (datosValidos()) {
				datos = new ArrayList<T>(_buscar());
			} else {
				datos = new ArrayList<T>();
			}
			if (datos.isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron resultados para las características seleccionadas");
			}
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al realizar la consulta ");
		}
		return _getPage();
	}

	protected boolean datosValidos() throws Exception {
		_validar();
		return !getFacesContext().getMessages().hasNext();
	}

	public String iniciar() {
		datos = new ArrayList<T>();
		return _getPage();
	}

	public List<T> getDatos() {
		if (datos == null) {
			datos = new ArrayList<T>();
		}
		return datos;
	}

	public void setDatos(List<T> datos) {
		this.datos = datos;
	}

	public Integer getScrollPage() {
		if (scrollPage == null) {
			scrollPage = 1;
		}
		return scrollPage;
	}

	public void setScrollPage(Integer srollPage) {
		this.scrollPage = srollPage;
	}

	public int getTotalFilas() {
		return datos == null ? 0 : datos.size();
	}

	public T getObjActual() {
		return objActual;
	}

	public void setObjActual(T objActual) {
		this.objActual = objActual;
	}
}
