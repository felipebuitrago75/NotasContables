package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;

import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;
import com.papelesinteligentes.bbva.notascontables.jsf.GeneralPage;
import com.papelesinteligentes.bbva.notascontables.jsf.IPages;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
public abstract class GeneralCargaPage<T extends CommonVO<T>> extends GeneralPage implements IPages, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4039536732981625179L;

	/**
	 * usado para filtrar los datos a mostrar
	 */
	public String param = "";

	/**
	 * indica el numero de pagina del scroller
	 */
	public Integer scrollPage = 1;

	/**
	 * Lista de datos a mostrar en la tabla
	 */
	private List<T> datos = new ArrayList<T>();

	protected abstract Collection<T> _buscarTodos() throws Exception;

	protected abstract Collection<T> _buscarPorFiltro() throws Exception;

	protected abstract String _getPage();

	private Comparator<T> pkComparator;

	private boolean buscarTodos = false;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public GeneralCargaPage(boolean buscarTodos) {
		super();
		this.buscarTodos = buscarTodos;
		_init();
		try {
			//long time = System.currentTimeMillis();
			//println("Ejecutando el constructor de la clase " + getClass().getSimpleName());
			// solo se realiza la busqueda si se est� en la ultima fase del ciclo de vida de faces y se quieren buscar todos los datos
			if (esUltimaFase() && buscarTodos /* && (getDatos() == null || getDatos().isEmpty()) */) {
				setDatos(new ArrayList<T>(_buscarTodos()));
			}
			//println("Finalizado el constructor en " + (System.currentTimeMillis() - time));
			initComparators();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initComparators() {
		pkComparator = new Comparator<T>() {

			@Override
			public int compare(T object1, T object2) {
				try {
					Double n1 = Double.valueOf(object1.getPK().toString());
					Double n2 = Double.valueOf(object2.getPK().toString());
					return n1.compareTo(n2);
				} catch (Exception e) {
					return object1.toString().compareTo(object2.toString());
				}
			}
		};
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
		param = (param == null || param.equalsIgnoreCase("null")) ? "" : param;
		scrollPage = scrollPage == null ? 1 : scrollPage;
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	public String buscarTodos() {
		try {
			//long time = System.currentTimeMillis();
			//println("Ejecutando el m�todo buscarTodos en la clase " + getClass().getSimpleName());
			// solo se realiza la busqueda si se est� en la ultima fase del ciclo de vida de faces
			if (esUltimaFase()) {
				datos = new ArrayList<T>(_buscarTodos());

			}
			//println("Finalizado buscarTodos en " + (System.currentTimeMillis() - time));
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al realizar la consulta ");
		}
		return _getPage();
	}

	public String iniciar() {
		datos = new ArrayList<T>();
		return _getPage();
	}

	/**
	 * Realiza la busqueda por filtro
	 * 
	 * @return
	 */
	public String buscarPorFiltro() {
		try {
			if (!buscarTodos) {
				if (param == null || param.isEmpty()) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "El filtro no puede ser vac�o" + param);
					return _getPage();
				} else if (param != null && param.length() < 4) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "Se deben ingresar m�nimo 4 caracteres en el filtro de b�squeda " + param);
					return _getPage();
				}
			}
			//long time = System.currentTimeMillis();
			println("Ejecutando el m�todo buscarPorFiltro en la clase " + getClass().getSimpleName());
			datos = new ArrayList<T>(_buscarPorFiltro());
			if (datos.isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron resultados para el filtro: " + param);
			}
			//println("Finalizado buscarPorFiltro en " + (System.currentTimeMillis() - time));
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al realizar la consulta por filtro ");
		}
		return _getPage();
	}

	public String getParam() {
		if (param == null) {
			param = "";
		}
		return param;
	}

	public void setParam(String param) {
		this.param = param;
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

	public Comparator<T> getPkComparator() {
		return pkComparator;
	}

	public void setPkComparator(Comparator<T> pkComparator) {
		this.pkComparator = pkComparator;
	}
}
