package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.MontoAutorizadoGeneral;
import com.papelesinteligentes.bbva.notascontables.dto.Rol;
import com.papelesinteligentes.bbva.notascontables.dto.TemaAutorizacion;
import com.papelesinteligentes.bbva.notascontables.dto.TipoEvento;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad MontoAutorizadoGeneral
 * </p>
 * 
 */
@KeepAlive
public class MontoAutorizadoGeneralPage extends GeneralParametrosPage<MontoAutorizadoGeneral, MontoAutorizadoGeneral> {

	private static final long serialVersionUID = 1L;

	private List<SelectItem> tiposEvento;
	private List<SelectItem> roles;
	private List<SelectItem> temasAut;

	public MontoAutorizadoGeneralPage() {
		super(true);
	}

	/**
	 * retorna una instancia de MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	protected MontoAutorizadoGeneral _getInstance() {
		return new MontoAutorizadoGeneral();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizadoGeneral> _buscarTodos() throws Exception {
		return notasContablesManager.getMontosAutorizadosGenerales();
	}

	/**
	 * Realiza la busqueda de entidades de tipo MontoAutorizadoGeneral filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizadoGeneral> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchMontoAutorizadoGeneral(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new MontoAutorizadoGeneral();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addMontoAutorizadoGeneral(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateMontoAutorizadoGeneral(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un registro con los mismos datos.");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarPositivo(objActual.getMonto(), "Límite");
		if (objActual.getCodigoTipoAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un tipo de enveto");
		}
		if (objActual.getCodigoRol().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un rol");
		}
		if (objActual.getCodigoTemaAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un tema de autorización");
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoMontoAutorizadoGeneral(notasContablesManager.getMontoAutorizadoGeneral(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteMontoAutorizadoGeneral(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
				temasAut = getSelectItemList(notasContablesManager.getCV(TemaAutorizacion.class), false);
				roles = getSelectItemList(notasContablesManager.getCV(Rol.class), false);
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de montos autorizados generales");
			}
		}
	}

	@Override
	protected String _getPage() {
		return MONTOS_AUTORIZADOS_GENERALES;
	}

	public List<SelectItem> getTiposEvento() {
		return tiposEvento;
	}

	public void setTiposEvento(List<SelectItem> tiposEvento) {
		this.tiposEvento = tiposEvento;
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public List<SelectItem> getTemasAut() {
		return temasAut;
	}

	public void setTemasAut(List<SelectItem> temasAut) {
		this.temasAut = temasAut;
	}

}
