package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;

import javax.faces.application.FacesMessage;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.Rol;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Rol
 * </p>
 * 
 */
@KeepAlive
public class RolPage extends GeneralParametrosPage<Rol, Rol> {

	private static final long serialVersionUID = 1L;

	public RolPage() {
		super(true);
	}

	/**
	 * retorna una instancia de Rol
	 * 
	 * @return
	 */
	@Override
	protected Rol _getInstance() {
		return new Rol();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Rol
	 * 
	 * @return
	 */
	@Override
	public Collection<Rol> _buscarTodos() throws Exception {
		return notasContablesManager.getRoles();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Rol filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Rol> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchRol(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Rol
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		// TODO agregar funcionalidad de edicion de datos
		// no se debería permitir la creacion o el borrado de roles
	}

	@Override
	protected boolean _guardar() throws Exception {
		notasContablesManager.updateRol(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getNombre().trim().isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe indicar un nombre para el rol");
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Rol
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoRol(notasContablesManager.getRol(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Rol
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		// notasContablesManager.deleteRol(objActual, getCodUsuarioLogueado());

		// no se debería permitir la creacion o el borrado de roles
		return true;
	}

	@Override
	protected String _getPage() {
		return ROLES;
	}
}
