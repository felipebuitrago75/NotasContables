package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.Auditoria;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Auditoria
 * </p>
 * 
 */
@KeepAlive
public class AuditoriaPage extends GeneralParametrosPage<Auditoria, Auditoria> {

	private static final long serialVersionUID = 1L;

	private Auditoria auditoria;

	public AuditoriaPage() {
		super(true);
		auditoria = new Auditoria();
	}

	/**
	 * retorna una instancia de Auditoria
	 * 
	 * @return
	 */
	@Override
	protected Auditoria _getInstance() {
		return new Auditoria();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	public Collection<Auditoria> _buscarTodos() throws Exception {
		return notasContablesManager.getRegistrosAuditoria();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Auditoria filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Auditoria> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchRegistrosAuditoria(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		// TODO agregar funcionalidad de edicion de datos
	}

	@Override
	protected boolean _guardar() throws Exception {
		// TODO agregar funcionalidad de guardado o actualizacion
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		// TODO logica de validacion
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		return true;
	}

	public String consultarDetalle() {
		try {
			auditoria = notasContablesManager.getRegistroAuditoria(auditoria.getCodigo().intValue());
			auditoria.setDetalle(notasContablesManager.getDetalleAuditoriaPorCodigoAuditoria(auditoria.getCodigo().intValue()));
			UsuarioModulo us = new UsuarioModulo();
			us.setCodigo(auditoria.getCodigoUsuario().intValue());
			auditoria.setUsuario(notasContablesManager.getUsuarioModulo(us));
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Hubo un error al consultar la auditoría en el sistema");
		}
		return null;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		return true;
	}

	@Override
	protected String _getPage() {
		return AUDITORIA;
	}

	public Auditoria getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}
}
