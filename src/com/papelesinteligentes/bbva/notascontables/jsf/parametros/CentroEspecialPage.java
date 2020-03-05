package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.dto.CentroEspecial;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad CentroEspecial
 * </p>
 * 
 */
@KeepAlive
public class CentroEspecialPage extends GeneralParametrosPage<CentroEspecial, CentroEspecial> {

	private static final long serialVersionUID = 1L;
	private List<SelectItem> sucursales;

	public CentroEspecialPage() {
		super(true);
	}

	/**
	 * retorna una instancia de CentroEspecial
	 * 
	 * @return
	 */
	@Override
	protected CentroEspecial _getInstance() {
		return new CentroEspecial();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo CentroEspecial
	 * 
	 * @return
	 */
	@Override
	public Collection<CentroEspecial> _buscarTodos() throws Exception {
		return notasContablesManager.getCentrosEspeciales();
	}

	/**
	 * Realiza la busqueda de entidades de tipo CentroEspecial filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<CentroEspecial> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchCentroEspecial(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo CentroEspecial
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new CentroEspecial();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addCentroEspecial(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateCentroEspecial(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un Centro Especial con la misma sucursal");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getCodigoSucursal(), "Sucursal");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo CentroEspecial
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoCentroEspecial(notasContablesManager.getCentroEspecial(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo CentroEspecial
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		try {
			notasContablesManager.deleteCentroEspecial(objActual, getCodUsuarioLogueado());
		} catch (Exception e) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No puede eliminar el Centro Especial porque contiene registros asociados");
			return false;
		}
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				sucursales = getSelectItemList(new TreeMap<String, String>(notasContablesManager.getCV(Sucursal.class)), true);
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de montos autorizados");
			}
		}
	}

	@Override
	protected String _getPage() {
		return CENTRO_ESPECIAL;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}
}
