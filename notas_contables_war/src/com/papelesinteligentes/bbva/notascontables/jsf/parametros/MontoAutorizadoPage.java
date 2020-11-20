package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.dto.MontoAutorizado;
import com.papelesinteligentes.bbva.notascontables.dto.TemaAutorizacion;
import com.papelesinteligentes.bbva.notascontables.dto.TipoEvento;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad MontoAutorizado
 * </p>
 * 
 */
@ViewScoped
public class MontoAutorizadoPage extends GeneralParametrosPage<MontoAutorizado, MontoAutorizado> {

	private static final long serialVersionUID = 1L;

	private List<SelectItem> tiposEvento;
	private List<SelectItem> entesAut;
	private List<SelectItem> temasAut;

	public MontoAutorizadoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	protected MontoAutorizado _getInstance() {
		return new MontoAutorizado();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizado> _buscarTodos() throws Exception {
		return notasContablesManager.getMontosAutorizados();
	}

	/**
	 * Realiza la busqueda de entidades de tipo MontoAutorizado filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizado> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchMontoAutorizado(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new MontoAutorizado();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addMontoAutorizado(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateMontoAutorizado(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un ente Monto Autorizado con el mismo tipo de evento, ente autorizador y tema de autorización");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getCodigoTipoAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tipo de enveto");
		}
		if (objActual.getCodigoEnteAutorizador().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el ente Autorizador");
		}
		if (objActual.getCodigoTemaAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un tema de autorización");
		}
		validador.validarPositivo(objActual.getMonto(), "Límite");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoMontoAutorizado(notasContablesManager.getMontoAutorizado(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteMontoAutorizado(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
				temasAut = getSelectItemList(notasContablesManager.getCV(TemaAutorizacion.class), false);
				entesAut = getSelectItemList(notasContablesManager.getCVEntesAut(), false);
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de montos autorizados");
			}
		}
	}

	@Override
	protected String _getPage() {
		return MONTOS_AUTORIZADOS;
	}

	public List<SelectItem> getTiposEvento() {
		return tiposEvento;
	}

	public void setTiposEvento(List<SelectItem> tiposEvento) {
		this.tiposEvento = tiposEvento;
	}

	public List<SelectItem> getEntesAut() {
		return entesAut;
	}

	public void setEntesAut(List<SelectItem> entesAut) {
		this.entesAut = entesAut;
	}

	public List<SelectItem> getTemasAut() {
		return temasAut;
	}

	public void setTemasAut(List<SelectItem> temasAut) {
		this.temasAut = temasAut;
	}

}
