package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.dto.EnteAutorizador;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.util.IRol;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad EnteAutorizador
 * </p>
 * 
 */
@ViewScoped
public class EnteAutorizadorPage extends GeneralParametrosPage<EnteAutorizador, EnteAutorizador> {

	private static final long serialVersionUID = 1L;

	private List<SelectItem> sucursales;
	private List<SelectItem> usuarios;

	private String usuarioSel;

	public EnteAutorizadorPage() {
		super(true);
	}

	/**
	 * retorna una instancia de EnteAutorizador
	 * 
	 * @return
	 */
	@Override
	protected EnteAutorizador _getInstance() {
		return new EnteAutorizador();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo EnteAutorizador
	 * 
	 * @return
	 */
	@Override
	public Collection<EnteAutorizador> _buscarTodos() throws Exception {
		return notasContablesManager.getEntesAutorizadores();
	}

	/**
	 * Realiza la busqueda de entidades de tipo EnteAutorizador filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<EnteAutorizador> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchEnteAutorizador(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo EnteAutorizador
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new EnteAutorizador();
		usuarios = new ArrayList<SelectItem>();
		usuarioSel = "";
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			objActual.setCodigo(null);
			objActual.setCodigoUsuarioModulo(new Integer(usuarioSel));
			notasContablesManager.addEnteAutorizador(objActual, getCodUsuarioLogueado());
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe un ente Autorizador con la misma sucursal y el mismo usuario");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getCodigoSucursal().endsWith("-")) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar una sucursal");
		}
		if (new Integer(usuarioSel) <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un usuario");
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo EnteAutorizador
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoEnteAutorizador(notasContablesManager.getEnteAutorizador(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo EnteAutorizador
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		try {
			notasContablesManager.deleteEnteAutorizador(objActual, getCodUsuarioLogueado());
		} catch (Exception e) {
			e.printStackTrace();
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No puede eliminar el Ente Autorizador porque contiene registros asociados");
			return false;
		}
		return true;
	}

	@Override
	protected String _getPage() {
		return ENTE_AUTORIZADOR;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				sucursales = getSelectItemList(new TreeMap<String, String>(notasContablesManager.getCV(Sucursal.class)));
				// usuarios = new ArrayList<SelectItem>();
				// usuarioSel = "";
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de entes autorizados");
			}
		}
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo EnteAutorizador
	 * 
	 * @return
	 */
	public String cambiarSucursal() throws Exception {
		usuarios = new ArrayList<SelectItem>();
		try {
			Collection<UsuarioModulo> lista = notasContablesManager.getUsuariosModuloPorAreaYRolYEstado(objActual.getCodigoSucursal(), IRol.AUTORIZADOR, "A");
			for (UsuarioModulo u : lista) {
				usuarios.add(new SelectItem(u.getCodigo().toString(), u.getCodigoEmpleado() + " - " + u.getUsuAlt().getNombreEmpleado()));
			}
			if (usuarios.isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontraron usuarios autorizadores activos para la sucursal indicada");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error al inicializar el módulo de administración de entes autorizados");
		}
		return null;
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

	public String getUsuarioSel() {
		return usuarioSel;
	}

	public void setUsuarioSel(String usuarioSel) {
		this.usuarioSel = usuarioSel;
	}

}
