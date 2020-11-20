package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.dto.Concepto;
import com.papelesinteligentes.bbva.notascontables.dto.TemaAutorizacion;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Concepto
 * </p>
 * 
 */
@ViewScoped
public class ConceptoPage extends GeneralParametrosPage<Concepto, Concepto> {

	private static final long serialVersionUID = 1L;
	private String estado;

	private List<SelectItem> unidadesAnalisis;
	private List<SelectItem> temasAut;

	public ConceptoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de Concepto
	 * 
	 * @return
	 */
	@Override
	protected Concepto _getInstance() {
		return new Concepto();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	public Collection<Concepto> _buscarTodos() throws Exception {
		return notasContablesManager.getConceptos();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Concepto filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Concepto> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchConcepto(param, estado);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new Concepto();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			objActual.setAutorizacionTercero(getStringFromBool(objActual.getBoolAutorizacionTercero()));
			objActual.setCentrosAutAreasCentrales(getStringFromBool(objActual.getBoolCentrosAutAreasCentrales()));
			objActual.setCentrosAutCentroEspecial(getStringFromBool(objActual.getBoolCentrosAutCentroEspecial()));
			objActual.setCentrosAutSucursales(getStringFromBool(objActual.getBoolCentrosAutSucursales()));
			objActual.setOrigenDestino(getStringFromBool(objActual.getBoolOrigenDestino()));
			objActual.setSoportes(getStringFromBool(objActual.getBoolSoportes()));
			objActual.setVistoBuenoAnalisis(getStringFromBool(objActual.getBoolVistoBuenoAnalisis()));
			if (!objActual.getBoolVistoBuenoAnalisis()) {
				objActual.setCodigoUnidadAnalisis(0);
			}
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addConcepto(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateConcepto(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Error al guardar el concepto");
			return false;
		}
	}

	private String getStringFromBool(boolean bool) {
		return bool ? "S" : "N";
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getNombre(), "Nombre");
		if (objActual.getBoolVistoBuenoAnalisis()) {
			validador.validarRequerido(objActual.getCodigoUnidadAnalisis(), "Unidad de análisis");
		}
		if (objActual.getBoolCentrosAutAreasCentrales() || objActual.getBoolCentrosAutCentroEspecial() || objActual.getBoolCentrosAutSucursales()) {
			return;
		}
		nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar por lo menos un (1) Centro Autorizado");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoConcepto(notasContablesManager.getConcepto(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteConcepto(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return CONCEPTO;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				unidadesAnalisis = getSelectItemList(cargaAltamiraManager.getCVSucursal(), false);
				temasAut = getSelectItemList(notasContablesManager.getCV(TemaAutorizacion.class), false);
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de conceptos");
			}
		}
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<SelectItem> getUnidadesAnalisis() {
		return unidadesAnalisis;
	}

	public void setUnidadesAnalisis(List<SelectItem> unidadesAnalisis) {
		this.unidadesAnalisis = unidadesAnalisis;
	}

	public List<SelectItem> getTemasAut() {
		return temasAut;
	}

	public void setTemasAut(List<SelectItem> temasAut) {
		this.temasAut = temasAut;
	}

}
