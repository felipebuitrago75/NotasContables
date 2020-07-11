package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Perfil;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.carga.dto.UsuarioAltamira;
import com.papelesinteligentes.bbva.notascontables.dao.ActividadRealizadaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.AuditoriaDAO;
import com.papelesinteligentes.bbva.notascontables.dao.IAuditoriaSentence;
import com.papelesinteligentes.bbva.notascontables.dao.SuperDAO;
import com.papelesinteligentes.bbva.notascontables.dto.EnteAutorizador;
import com.papelesinteligentes.bbva.notascontables.dto.Padrino;
import com.papelesinteligentes.bbva.notascontables.dto.Rol;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.util.IRol;
import com.papelesinteligentes.bbva.notascontables.util.StringUtils;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@KeepAlive
public class UsuarioPage extends GeneralParametrosPage<UsuarioModulo, UsuarioModulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2612093503479337549L;

	private List<SelectItem> roles;
	private List<SelectItem> sucursales;
	private List<SelectItem> perfiles;
	protected final SuperDAO actividadSuperDAO = new SuperDAO(null);
	private String rolSel;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public UsuarioPage() {
		super(true);
	}

	@Override
	protected UsuarioModulo _getInstance() {
		return new UsuarioModulo();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de un nuevo usuario
	 * 
	 * @return
	 */
	public String buscarUsuarioAltamira() {
		String codEmpleado = objActual.getCodigoEmpleado().trim();
		objActual = new UsuarioModulo();
		try {
			if (codEmpleado.length() > 0) {
				// se busca el usuario altamira
				UsuarioAltamira usuarioAltamira = new UsuarioAltamira();
				usuarioAltamira.setCodigoEmpleado(codEmpleado);
				usuarioAltamira = cargaAltamiraManager.getUsuarioAltamira(usuarioAltamira);

				// si se encuentra el usuario
				if (usuarioAltamira != null && usuarioAltamira.getCodigoEmpleado().trim().length() > 0) {
					objActual.setCodigoEmpleado(usuarioAltamira.getCodigoEmpleado());
					objActual.setCodigoPerfilModificado(usuarioAltamira.getCodigoPerfil());
					objActual.setNombrePerfilModificado(usuarioAltamira.getNombrePerfil());
					objActual.setCodigoAreaModificado(StringUtils.getStringLeftPadding("" + usuarioAltamira.getCodigoArea(), 4, '0'));
					objActual.setNombreAreaModificado(usuarioAltamira.getNombreArea());
					objActual.setEMailModificado(usuarioAltamira.getCorreoElectronico());
					objActual.setUsuAlt(usuarioAltamira);
					setRolSel("-1");
				} else {
					objActual.setCodigoEmpleado(codEmpleado);
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontró el usuario asociado al código " + codEmpleado);
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe indicar el código completo del usuario a buscar");
			}
			consultarListasAuxiliares();

		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "Error al inicializar el editor de creación de usuarios");
		}
		return null;
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<UsuarioModulo> _buscarTodos() throws Exception {
		return notasContablesManager.getUsuariosModulo();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<UsuarioModulo> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchUsuarioModulo(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de un nuevo usuario
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new UsuarioModulo();
		rolSel = "-1";
		consultarListasAuxiliares();
	}
	
	@Override
	protected boolean _guardar() throws Exception {
		try {
			objActual.setCodigoRol(new Integer(rolSel));
			// si se trata de un usuario nuevo
			if (objActual.getCodigo().intValue() <= 0) {

				// chambonazo para tapar las brutalidades del desarrollador anterior... (como no normaliza los datos??????)
				for (SelectItem s : sucursales) {
					if (s.getValue().toString().equals(objActual.getCodigoAreaModificado())) {
						objActual.setNombreAreaModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
						break;
					}
				}
				for (SelectItem s : perfiles) {
					if (s.getValue().toString().equals(objActual.getCodigoPerfilModificado())) {
						objActual.setNombrePerfilModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
						break;
					}
				}
				notasContablesManager.addUsuarioModulo(objActual, getCodUsuarioLogueado());
			} //else {
				//int Usuariosalida = 0;
				//int usuarioEntrada = 0;
				//try {
				/**COL521513I001176 VALIDA SI USUARIO ESTA LOGUEADO PARA REALIZAR CAMBIOS EN PERFIL**/
				//objActual.setCodigoRol(new Integer(rolSel));
					//Connection con = actividadSuperDAO.getConexion();
					//String consultaEstadoUsuarioSalida = IAuditoriaSentence.SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSALIDA;
					//String consultaEstadoUsuarioIngreso = IAuditoriaSentence.SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSINGRESO;
					//Object codigoEmpleado = (Integer) objActual.getCodigo();
					//usuarioEntrada = Integer.parseInt(actividadSuperDAO.getConsultaGeneral(con,consultaEstadoUsuarioIngreso,codigoEmpleado));
					//Usuariosalida = Integer.parseInt(actividadSuperDAO.getConsultaGeneral(con,consultaEstadoUsuarioSalida,codigoEmpleado));
				//} catch (Exception e) {
					//Usuariosalida = 0;
				//}	
					//if (usuarioEntrada > Usuariosalida){
						//nuevoMensaje(FacesMessage.SEVERITY_WARN, "Usuario logueado en el aplicativo, espere que salga del sistema para realizar los cambios");
						//return false;
					//}
		else{
				
				UsuarioModulo usuarioModuloOriginal = notasContablesManager.getUsuarioModulo(objActual);
				// si se trata de un cambio de rol
				boolean cambioRol = usuarioModuloOriginal.getCodigoRol().intValue() != objActual.getCodigoRol().intValue();
				if (cambioRol && !canUpdate()) {// si se trata de una actualizacion y el cambio es valido
					return false;
				}else{
					for (SelectItem s : sucursales) {
						if (s.getValue().toString().equals(objActual.getCodigoAreaModificado())) {
							objActual.setNombreAreaModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
							break;
						}
					}
					for (SelectItem s : perfiles) {
 						if (s.getValue().toString().equals(objActual.getCodigoPerfilModificado())) {
							objActual.setNombrePerfilModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
							break;
						}
					}
					canUpdate();
				}
				notasContablesManager.updateUsuarioModulo(objActual, getCodUsuarioLogueado(), cambioRol && objActual.getEstado().equals("A"));
			}
				//}
				return true;
		} catch (Exception e) {
			lanzarError(e, "Ya existe el usuario con el mismo Rol");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getEMailModificado(), "Correo electrónico");

		if (new Integer(objActual.getCodigoAreaModificado()) <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar la sucursal");
		}
		if (objActual.getCodigoPerfilModificado().equals("-1")) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el perfil");
		}
		if (new Integer(rolSel) <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un rol");
		}
	}

	private boolean canUpdate() throws Exception {
		// se consulta la información anterior del usuario para verificar si se cambió de rol
		UsuarioModulo usuarioModuloOriginal = notasContablesManager.getUsuarioModulo(objActual);
		// se verifica el cambio de rol solo para padrinos y autorizadores
		if (usuarioModuloOriginal.getCodigoRol().intValue() == IRol.PADRINOS || usuarioModuloOriginal.getCodigoRol().intValue() == IRol.AUTORIZADOR) {
			Padrino padrino = new Padrino();
			padrino.setCodigoUsuario(usuarioModuloOriginal.getCodigo().intValue());
			padrino = notasContablesManager.getPadrinoPorUsuario(padrino);

			// si no esta asociado a un padrino se evalua si es ente autorizador
			if (padrino.getCodigo().intValue() == 0 || padrino.getEstado().equals("I")) {

				EnteAutorizador enteAutorizador = new EnteAutorizador();
				enteAutorizador.setCodigoUsuarioModulo(usuarioModuloOriginal.getCodigo().intValue());
				enteAutorizador = notasContablesManager.getEnteAutorizadorPorUsuario(enteAutorizador);

				// es ente autorizador
				if (enteAutorizador.getCodigo().intValue() != 0) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se puede cambiar el rol del usuario porque está asociado a un Ente Autorizador");
					return false;
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se puede cambiar el rol del usuario porque está asociado a un Padrino");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean _cambiarEstado() throws Exception {
		
		try {
			String consultaEstadoUsuarioSalida = IAuditoriaSentence.SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSALIDA;
			String consultaEstadoUsuarioIngreso = IAuditoriaSentence.SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSINGRESO;
			/**
			//int Usuariosalida = 0;
			//int usuarioEntrada = 0;
			try {
			//COL521513I001176 VALIDA SI USUARIO ESTA LOGUEADO PARA REALIZAR CAMBIOS EN PERFIL
			objActual.setCodigoRol(new Integer(rolSel));
				Connection con = actividadSuperDAO.getConexion();
				String consultaEstadoUsuarioSalida = IAuditoriaSentence.SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSALIDA;
				String consultaEstadoUsuarioIngreso = IAuditoriaSentence.SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSINGRESO;
				Object codigoEmpleado = (Integer) objActual.getCodigo();
				//usuarioEntrada = Integer.parseInt(actividadSuperDAO.getConsultaGeneral(con,consultaEstadoUsuarioIngreso,codigoEmpleado));
				//Usuariosalida = Integer.parseInt(actividadSuperDAO.getConsultaGeneral(con,consultaEstadoUsuarioSalida,codigoEmpleado));
			} catch (Exception e) {
				//Usuariosalida = 0;
			}
				if (usuarioEntrada > Usuariosalida){
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "Usuario logueado en el aplicativo, espere que salga del sistema para realizar los cambios");
					return false;
				}else{**/
			notasContablesManager.changeEstadoUsuarioModulo(notasContablesManager.getUsuarioModulo(objActual), getCodUsuarioLogueado());
			return true;
		//}
				} catch (Exception e) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No existe otro Usario con el mismo Rol para este Centro");
		}
		return false;
	}

	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteUsuarioModulo(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				roles = getSelectItemList(notasContablesManager.getCV(Rol.class), false);
				sucursales = getSelectItemList(notasContablesManager.getCV(Sucursal.class));
				perfiles = getSelectItemList(notasContablesManager.getCV(Perfil.class));
			} catch (Exception e) {
				e.printStackTrace();
				lanzarError(e, "Error al inicializar el módulo de administración de usuarios");
			}
		}
	}

	@Override
	protected String _getPage() {
		return USUARIO;
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<SelectItem> perfiles) {
		this.perfiles = perfiles;
	}

	public String getRolSel() {
		return rolSel;
	}

	public void setRolSel(String rolSel) {
		this.rolSel = rolSel;
	}
}
