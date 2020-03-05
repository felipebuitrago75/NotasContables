package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.ActividadEconomica;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Departamento;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Municipio;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Pais;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Tercero;
import com.papelesinteligentes.bbva.notascontables.carga.dto.TipoIdentificacion;
import com.papelesinteligentes.bbva.notascontables.carga.dto.TipoIndicativo;
import com.papelesinteligentes.bbva.notascontables.carga.dto.TipoTelefono;
import com.papelesinteligentes.bbva.notascontables.jsf.parametros.GeneralParametrosPage;
import com.papelesinteligentes.bbva.notascontables.util.StringUtils;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@KeepAlive
public class TerceroPage extends GeneralParametrosPage<Tercero, Tercero> {

	private static final long serialVersionUID = -8330009617976284212L;

	private List<SelectItem> tiposIdentificacion;
	private List<SelectItem> municipios;
	private List<SelectItem> departamentos;
	private List<SelectItem> actividades;
	private List<SelectItem> tiposTelefono;
	private List<SelectItem> indicativos;
	private List<SelectItem> paises;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.mmmmmm");
	private String actividad;

	private Collection<TipoIdentificacion> tiposIdentificacionList;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public TerceroPage() {
		super(false);
	}

	public void cargarMunicipios() {
		cargarMunicipios(objActual.getDepartamento());
	}

	public boolean isMostrarListaIndicativos1() {
		if (objActual == null || objActual.getTipoTelefono() == null || objActual.getTipoTelefono().isEmpty()) {
			return true;
		}
		for (SelectItem i : tiposTelefono) {
			if (i.getLabel().trim().equalsIgnoreCase("celular")) {
				if (i.getValue().toString().equals(objActual.getTipoTelefono().toString())) {
					return false;
				}
				return true;
			}
		}
		return true;
	}

	public boolean isMostrarListaIndicativos2() {
		if (objActual == null || objActual.getTipoTelefono2() == null || objActual.getTipoTelefono2().isEmpty()) {
			return true;
		}
		for (SelectItem i : tiposTelefono) {
			if (i.getLabel().trim().equalsIgnoreCase("celular")) {
				if (i.getValue().toString().equals(objActual.getTipoTelefono2().toString())) {
					return false;
				}
				return true;
			}
		}
		return true;
	}

	public String buscarActividades() {
		try {
			actividades = new ArrayList<SelectItem>();
			if (actividad.length() >= 4) {
				for (ActividadEconomica ti : cargaAltamiraManager.searchActividadEconomica(actividad)) {
					actividades.add(new SelectItem(ti.getCodigo(), ti.getCodigo() + " - " + ti.getNombre()));
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "Por favor ingrese un filtro de búsqueda de 4 caracteres o mas");
			}
		} catch (Exception e) {
			lanzarError(e, "Error consultando las actividades económicas ");
		}
		return null;
	}

	public void cargarDepartamentos() {
		cargarDepartamentos(objActual.getPais());
	}

	public void cargarDepartamentos(String pais) {
		try {
			departamentos.clear();
			if (pais.equals("COL")) {
				for (Departamento ti : cargaAltamiraManager.getDepartamentos()) {
					departamentos.add(new SelectItem(ti.getCodigo(), ti.getNombre()));
				}
			}
			if (objActual != null) {
				if (departamentos.isEmpty()) {
					objActual.setDepartamento("");
				} else {
					objActual.setDepartamento(departamentos.get(0).getValue().toString());
				}
			}
			if (objActual != null) {
				cargarMunicipios();
			} else {
				if (!departamentos.isEmpty()) {
					cargarMunicipios(departamentos.get(0).getValue().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cargarMunicipios(String dep) {
		try {
			municipios.clear();
			for (Municipio ti : cargaAltamiraManager.getMunicipiosPorDepartamento(dep)) {
				municipios.add(new SelectItem(ti.getCodigoMunicipio(), ti.getNombre()));
			}
			if (objActual != null) {
				if (municipios.isEmpty()) {
					objActual.setMunicipio("");
				} else {
					objActual.setMunicipio(municipios.get(0).getValue().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void _init() {
		super._init();
		if (tiposIdentificacion == null || tiposIdentificacion.isEmpty()) {
			try {
				tiposIdentificacion = new ArrayList<SelectItem>();
				municipios = new ArrayList<SelectItem>();
				departamentos = new ArrayList<SelectItem>();
				actividades = new ArrayList<SelectItem>();
				tiposTelefono = new ArrayList<SelectItem>();
				indicativos = new ArrayList<SelectItem>();
				paises = new ArrayList<SelectItem>();
				tiposIdentificacionList = cargaAltamiraManager.getTiposIdentificacion();
				for (TipoIdentificacion ti : tiposIdentificacionList) {
					tiposIdentificacion.add(new SelectItem(ti.getCodigo(), ti.getNombre()));
				}

				actividad = "";
				actividades = new ArrayList<SelectItem>();

				tiposTelefono.add(new SelectItem("", "Seleccione uno..."));
				for (TipoTelefono ti : cargaAltamiraManager.getTiposTelefonos()) {
					tiposTelefono.add(new SelectItem(ti.getCodigo(), ti.getNombre()));
				}

				indicativos.add(new SelectItem("", "Seleccione uno..."));
				for (TipoIndicativo ti : new TreeSet<TipoIndicativo>(cargaAltamiraManager.getTiposIndicativos())) {
					indicativos.add(new SelectItem(ti.getIndicativo(), ti.getIndicativo()));
				}
				for (Pais p : cargaAltamiraManager.getPaises()) {
					paises.add(new SelectItem(p.getPrefijo(), p.getNombre()));
				}

				if (objActual != null) {
					objActual.setPais(paises.get(0).getValue().toString());
					cargarDepartamentos();
				} else {
					cargarDepartamentos(paises.get(0).getValue().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				tiposIdentificacion = new ArrayList<SelectItem>();
			}
		}
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<Tercero> _buscarTodos() throws Exception {
		return new ArrayList<Tercero>();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<Tercero> _buscarPorFiltro() throws Exception {
		Collection<Tercero> terceros = cargaAltamiraManager.searchTercero(param);
		if (tiposIdentificacionList != null) {
			for (TipoIdentificacion ti : tiposIdentificacionList) {
				for (Tercero t : terceros) {
					if (ti.getCodigo().equals(t.getTipoIdentificacion())) {
						t.setTipoIdentificacionStr(ti.getNombre());
					}
				}
			}
		}
		return terceros;
	}

	@Override
	protected String _getPage() {
		return TERCERO;
	}

	@Override
	protected boolean _borrar() throws Exception {
		throw new UnsupportedOperationException("El método _borrar de TerceroPage no está implementado");
	}

	@Override
	protected boolean _cambiarEstado() throws Exception {
		throw new UnsupportedOperationException("El método _cambiarEstado de TerceroPage no está implementado");
	}

	@Override
	protected void _editar() throws Exception {
		objActual = new Tercero();
		objActual.setPais("COL");
		actividades = new ArrayList<SelectItem>();
		actividad = "";
		cargarDepartamentos();
	}

	@Override
	protected Tercero _getInstance() {
		return new Tercero();
	}

	@Override
	protected boolean _guardar() throws Exception {
		String docInicial = objActual.getNumeroIdentificacion();
		String tipoDocInicial = objActual.getTipoIdentificacion();
		try {
			objActual.setUsuario(getUsuarioLogueado().getUsuAltamira().getCodigoEmpleado());
			objActual.setFecha(sdf.format(Calendar.getInstance().getTime()));
			objActual.setNumeroIdentificacion(StringUtils.getStringLeftPaddingFixed(docInicial, 15, '0'));
			cargaAltamiraManager.addTercero(objActual, getCodUsuarioLogueado());
			actividad = "";
			objActual = new Tercero();
		} catch (Exception e) {
			objActual.setNumeroIdentificacion(docInicial);
			objActual.setTipoIdentificacion(tipoDocInicial);
			lanzarError(e, "Ya existe un Tercero con la misma identificación");
			return false;
		}
		return true;
	}

	public String cancelar() {
		objActual = new Tercero();
		return null;
	}

	@Override
	protected void _validar() throws Exception {
		objActual.setMoneda("COP");
		objActual.setIngresoApp(1);
		validador.validarRequerido(objActual.getTipoIdentificacion(), "Tipo de Documento");
		validador.validarRequerido(objActual.getNumeroIdentificacion(), "Número de Identificación");
		if (objActual.getTipoIdentificacion().equals("3") || objActual.getTipoIdentificacion().equals("9")) {
			validador.validarRequerido(objActual.getDigitoVerificacion(), "Dígito de Verificación");
		} else {
			objActual.setDigitoVerificacion("0");
		}
		validador.validarRequerido(objActual.getPrimerApellido(), "Primer Apellido");
		// validador.validarRequerido(objActual.getSegundoApellido(), "Segundo Apellido");
		validador.validarRequerido(objActual.getPrimerNombre(), "Primer Nombre");
		// validador.validarRequerido(objActual.getSegundoNombre(), "Segundo Nombre");
		validador.validarRequerido(objActual.getDireccion(), "Dirección");
		validador.validarRequerido(objActual.getActividadEconomica(), "Actividad Económica");
		validador.validarRequerido(objActual.getPais(), "País");
		if (objActual.getPais() == null || objActual.getPais().equals("COL")) {
			validador.validarRequerido(objActual.getMunicipio(), "Municipio");
			validador.validarRequerido(objActual.getDepartamento(), "Departamento");
			validador.validarRequerido(objActual.getTipoTelefono(), "Tipo de Teléfono");
			validador.validarRequerido(objActual.getIndicativo(), "Indicativo");
			validador.validarRequerido(objActual.getTelefono(), "Teléfono");
			// validador.validarRequerido(objActual.getExtension(), "Extensión");
			// validador.validarRequerido(objActual.getTipoTelefono2(), "Tipo de Teléfono 2");
			// validador.validarRequerido(objActual.getIndicativo2(), "Indicativo 2");
			// validador.validarRequerido(objActual.getTelefono2(), "Teléfono 2");
			// validador.validarRequerido(objActual.getExtension2(), "Extensión 2");
		}
		validador.validarRequerido(objActual.getMoneda(), "Moneda");
		// validador.validarRequerido(objActual.getIndicadorNotasContables(), "Indicador de Notas Contables");
		if (objActual.getEMail() != null && !objActual.getEMail().isEmpty() && !objActual.getEMail().contains("@")) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "El correo electrónico debe contener un signo @");
		}
		// validador.validarRequerido(objActual.getRegimenTributario(), "Régimen Tributario");
		validador.validarRequerido(objActual.getSexo(), "Sexo");

		validador.validarLongitud(objActual.getTelefono(), "Teléfono", 7);
		// validador.validarLongitud(objActual.getTelefono2(), "Teléfono 2", 7);
		// validador.validarEMail(objActual.getEMail(), "Correo electrónico");

	}

	public void crearTercero() {
		try {
			objActual = cargaAltamiraManager.getTerceroPorTipoYNumeroIdentificacion(objActual);
			actividades = new ArrayList<SelectItem>();
			actividad = "";
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error procesando la solicitud");
		}
	}

	public List<SelectItem> getTiposIdentificacion() {
		return tiposIdentificacion;
	}

	public void setTiposIdentificacion(List<SelectItem> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}

	public List<SelectItem> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<SelectItem> municipios) {
		this.municipios = municipios;
	}

	public List<SelectItem> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<SelectItem> departamentos) {
		this.departamentos = departamentos;
	}

	public List<SelectItem> getActividades() {
		return actividades;
	}

	public void setActividades(List<SelectItem> actividades) {
		this.actividades = actividades;
	}

	public List<SelectItem> getTiposTelefono() {
		return tiposTelefono;
	}

	public void setTiposTelefono(List<SelectItem> tiposTelefono) {
		this.tiposTelefono = tiposTelefono;
	}

	public List<SelectItem> getIndicativos() {
		return indicativos;
	}

	public void setIndicativos(List<SelectItem> indicativos) {
		this.indicativos = indicativos;
	}

	public List<SelectItem> getPaises() {
		return paises;
	}

	public void setPaises(List<SelectItem> paises) {
		this.paises = paises;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

}
