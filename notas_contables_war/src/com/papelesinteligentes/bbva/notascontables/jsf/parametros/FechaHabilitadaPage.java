package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.FechaHabilitada;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad FechaHabilitada
 * </p>
 * 
 */
@KeepAlive
public class FechaHabilitadaPage extends GeneralParametrosPage<FechaHabilitada, FechaHabilitada> {

	private static final long serialVersionUID = 1L;
	private Boolean selectedAll = false;
	private Integer nuevosDias = 2;

	public FechaHabilitadaPage() {
		super(true);
	}

	/**
	 * retorna una instancia de FechaHabilitada
	 * 
	 * @return
	 */
	@Override
	protected FechaHabilitada _getInstance() {
		return new FechaHabilitada();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo FechaHabilitada
	 * 
	 * @return
	 */
	@Override
	public Collection<FechaHabilitada> _buscarTodos() throws Exception {
		return notasContablesManager.getFechasHabilitadas();
	}

	/**
	 * Realiza la busqueda de entidades de tipo FechaHabilitada filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<FechaHabilitada> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchFechaHabilitada(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo FechaHabilitada
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new FechaHabilitada();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addFechaHabilitada(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateFechaHabilitada(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			return false;
		}
	}

	public String actualizarTodos() {
		try {
			validarDias(nuevosDias);
			if (!hayMensajes()) {
				ArrayList<String> sucursales = new ArrayList<String>();
				for (FechaHabilitada f : getDatos()) {
					if (f.getSelected()) {
						sucursales.add(f.getCodigoSucursal());
					}
				}
				String[] newString = new String[sucursales.size()];
				newString = sucursales.toArray(newString);
				notasContablesManager.updateTodasFechasHabilitadas(nuevosDias, "", newString, getCodUsuarioLogueado());
				setDatos(new ArrayList<FechaHabilitada>(_buscarPorFiltro()));
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "Registros actualizados correctamente");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error al actualizar todas las fechas habilitadas");
		}
		return null;
	}

	@Override
	protected void _validar() throws Exception {
		validarDias(objActual.getDias());
	}

	private void validarDias(Number dias) throws Exception {
		if (dias == null) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Ingrese un valor válido para realizar la operación");
		} else {
			if (dias.intValue() <= 0) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El valor debe ser mayor a cero");
			} else {
				int diasDesdeCierre = notasContablesManager.getDiasHabilesDesdeUltimoCierre();
				if (dias.intValue() > diasDesdeCierre) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "El valor debe ser menor a los días desde el último cierre mensual: " + diasDesdeCierre);
				}
			}
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo FechaHabilitada
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		return false;
	}

	public String selecccionar() {
		selectedAll = !selectedAll;
		for (FechaHabilitada f : getDatos()) {
			f.setSelected(selectedAll);
		}
		return null;
	}

	public boolean getHaySeleccion() {
		for (FechaHabilitada f : getDatos()) {
			if (f.getSelected()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo FechaHabilitada
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		// notasContablesManager.deleteFechaHabilitada(objActual, getCodUsuarioLogueado());
		return false;
	}

	@Override
	protected String _getPage() {
		return FECHAS_HABILIDADAS;
	}

	public Boolean getSelectedAll() {
		return selectedAll;
	}

	public void setSelectedAll(Boolean selectedAll) {
		this.selectedAll = selectedAll;
	}

	public Integer getNuevosDias() {
		return nuevosDias;
	}

	public void setNuevosDias(Integer nuevosDias) {
		this.nuevosDias = nuevosDias;
	}

}
