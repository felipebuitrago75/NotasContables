package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.Padrino;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Padrino
 * </p>
 * 
 */
@KeepAlive
public class PadrinoPage extends GeneralParametrosPage<Padrino, Padrino> {

	private static final long serialVersionUID = 1L;
	private static final int cs_CODIGO_PADRINOS = 4;
	private List<SelectItem> sucursales;
	private List<SelectItem> usuarios;

	public PadrinoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de Padrino
	 * 
	 * @return
	 */
	@Override
	protected Padrino _getInstance() {
		return new Padrino();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	public Collection<Padrino> _buscarTodos() throws Exception {
		return notasContablesManager.getPadrinos();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Padrino filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Padrino> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchPadrino(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new Padrino();
		usuarios = new ArrayList<SelectItem>();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addPadrino(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updatePadrino(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un Padrino con los mismo datos");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getCodigoUsuario(), "Usuario");
		validador.validarRequerido(objActual.getCodigoUnidadAnalisis(), "Unidad de análisis");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoPadrino(notasContablesManager.getPadrino(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deletePadrino(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				sucursales = getSelectItemList(cargaAltamiraManager.getCVSucursal(), false);
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de padrinos");
			}
		}
	}

	public String buscarUsuarios() {
		try {
			usuarios = getSelectItemList(new TreeMap<String, String>(cargaAltamiraManager.getCVUsuarioAltamira(objActual.getCodigoUnidadAnalisis(), cs_CODIGO_PADRINOS, "A")), false);
			if (usuarios.isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se han encontrado usuarios en el sistema para la sucursal seleccionada");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error al iniciar los usuarios");
		}
		return null;
	}

	@Override
	protected String _getPage() {
		return PADRINO;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<SelectItem> usuarios) {
		this.usuarios = usuarios;
	}
}
