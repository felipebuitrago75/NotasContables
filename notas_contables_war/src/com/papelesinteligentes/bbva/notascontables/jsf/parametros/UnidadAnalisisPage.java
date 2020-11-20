package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.dto.Padrino;
import com.papelesinteligentes.bbva.notascontables.dto.UnidadAnalisis;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad UnidadAnalisis
 * </p>
 * 
 */
@ViewScoped
public class UnidadAnalisisPage extends GeneralParametrosPage<UnidadAnalisis, UnidadAnalisis> {

	private static final long serialVersionUID = 1L;
	private List<SelectItem> sucursales;
	private List<SelectItem> autorizaciones;

	public UnidadAnalisisPage() {
		super(true);
	}

	/**
	 * retorna una instancia de UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	protected UnidadAnalisis _getInstance() {
		return new UnidadAnalisis();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	public Collection<UnidadAnalisis> _buscarTodos() throws Exception {
		return notasContablesManager.getUnidadesAnalisis();
	}

	/**
	 * Realiza la busqueda de entidades de tipo UnidadAnalisis filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<UnidadAnalisis> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchUnidadAnalisis(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new UnidadAnalisis();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addUnidadAnalisis(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateUnidadAnalisis(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe una Unidad de Análisis con la misma sucursal");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getCodigoSucursal(), "Sucursal");
		validador.validarRequerido(objActual.getAutorizaReferenciaCruce(), "Autorización");
		cambioValido();
	}

	protected boolean cambioValido() throws Exception {
		Padrino p = new Padrino();
		p.setCodigoUnidadAnalisis(objActual.getCodigo());
		p.setEstado("A");
		if (!notasContablesManager.getPadrinosPorUnidadAnalisisYEstado(p).isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se puede cambiar el estado o borrar la unidad de analisis dado que tiene padrinos asociados");
			return false;
		}
		return true;
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		if (cambioValido()) {
			notasContablesManager.changeEstadoUnidadAnalisis(notasContablesManager.getUnidadAnalisis(objActual), getCodUsuarioLogueado());
			return true;
		}
		return false;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		try {
			notasContablesManager.deleteUnidadAnalisis(objActual, getCodUsuarioLogueado());
		} catch (Exception e) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No puede eliminar la Unidad de Análisis porque contiene registros asociados");
			return false;
		}
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				sucursales = getSelectItemList(new TreeMap<String, String>(notasContablesManager.getCV(Sucursal.class)), true);
				Map<String, String> autMap = new TreeMap<String, String>();
				autMap.put("N", "No");
				autMap.put("S", "Si");
				autorizaciones = getSelectItemList(autMap, false);
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de montos autorizados");
			}
		}
	}

	@Override
	protected String _getPage() {
		return UNIDAD_ANALISIS;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getAutorizaciones() {
		return autorizaciones;
	}

	public void setAutorizaciones(List<SelectItem> autorizaciones) {
		this.autorizaciones = autorizaciones;
	}
}
