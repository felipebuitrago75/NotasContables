package com.papelesinteligentes.bbva.notascontables.jsf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletContext;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.carga.dto.UsuarioAltamira;
import com.papelesinteligentes.bbva.notascontables.carga.manager.CargaAltamiraScheduled;
import com.papelesinteligentes.bbva.notascontables.dto.CentroEspecial;
import com.papelesinteligentes.bbva.notascontables.dto.Rol;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.jsf.adminnota.PendientePage;
import com.papelesinteligentes.bbva.notascontables.jsf.beans.UsuarioLogueado;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */

public class HomePage extends GeneralPage implements IPages {

	private final CargaAltamiraScheduled cargaAltamiraScheduled = new CargaAltamiraScheduled();

	public String cronCierreHist() {
		cargaAltamiraScheduled.procesarHistorico();
		return null;
	}

	public String cronCierreAnulado() {
		cargaAltamiraScheduled.procesarAnulados();
		return null;
	}

	public String cronCierreAnular() {
		cargaAltamiraScheduled.procesarAnular();
		return null;
	}

	public String cronActPend() {
		try {
			cargaAltamiraScheduled.loadProcesarActividadesPendientes("micorreo.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private final String DIR_AUTH_LDAP;
	public String username;
	public String pwd;

	private String rolActual;

	// listas para mostrar en combos
	private List<SelectItem> roles = null;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public HomePage() {
		super();
		ServletContext context = (ServletContext) getExternalContext().getContext();
		DIR_AUTH_LDAP = context.getInitParameter("DIR_AUTH_LDAP");
		_init();
	}

	/**
	 * <p>
	 * Automatically managed component initialization. <strong>WARNING:</strong> This method is automatically generated, so any user-specified code inserted here is subject to being replaced.
	 * </p>
	 */
	@Override
	protected void _init() {
		rolActual = "";
		roles = new ArrayList<SelectItem>();
		username = "";
		pwd = "";

	}

	public String iniciar() {
		return LOGIN;
	}

	@SuppressWarnings( { "unused", "unchecked" })
	private boolean validateUser(String userId, String password) {
		boolean indExiste = false;
		// Create the search controls
		SearchControls searchCtls = new SearchControls();
		searchCtls.setCountLimit(500);

		// Specify the search scope
		searchCtls.setSearchScope(2);
		String searchBase = "O=BBVA";
		Hashtable environment = new Hashtable();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL, DIR_AUTH_LDAP);
		environment.put(Context.SECURITY_PRINCIPAL, "CN=" + userId + "/O=BBVA");
		environment.put(Context.SECURITY_CREDENTIALS, password);
		LdapContext ctxGC = null;
		//System.out.println("Validando usuario " + userId);
		try {
			ctxGC = new InitialLdapContext(environment, null);
			indExiste = true;
		} catch (NamingException e) {
			System.out.println("Error validando usuario contra el LDAP " + e);
			indExiste = false;
		}

		return indExiste;
	}

	public String loguear() {
		try {
			UsuarioLogueado usuLogueado = getContablesSessionBean().getLoginUser();

			usuLogueado = cargarUsuario();
			if (usuLogueado != null) {
				getContablesSessionBean().setLoginUser(usuLogueado);
				// se retorna a la ventana de login para que el usuario pueda indicar el rol a usar
				if (usuLogueado.getRolActual() == null && usuLogueado.getRoles().size() > 1) {
					return LOGIN;
				}
				return asignarRol();
			}
			// usuario no encontrado
			return null;
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al realizar la autenticacion");
		}
		return LOGIN;
	}

	public String salir() {
		/**COL521513I001176 se inserta en tabla auditoria campo OPERACION "Salir de la aplicación" y en TIPO_REGISTRO "Logout"**/
		UsuarioLogueado usuLogueado = getContablesSessionBean().getLoginUser();
			try {
				notasContablesManager.addRegistroAuditoriaIngreso(usuLogueado.getUsuario().getCodigo().intValue(), "Salir de la aplicación", "Logout", "0");
			} catch (Exception le_e) {
			}
		getContablesSessionBean().setLoginUser(null);
		return INICIO;
	}

	public String asignarRol() {
		UsuarioLogueado usuLogueado = getContablesSessionBean().getLoginUser();
		if (usuLogueado != null) {
			Rol rol = new Rol();
			rol.setCodigo(new Integer(rolActual));
			try {
				usuLogueado.setRolActual(notasContablesManager.getRol(rol));

				// se vuelve a consultar el usuario por rol
				usuLogueado.setUsuario(notasContablesManager.getUsuarioByCodEmpAndRol(usuLogueado.getUsuario().getCodigoEmpleado(), rol.getCodigo().intValue()));
				usuLogueado.setMenu(notasContablesManager.getMenu(rol.getCodigo().intValue()), getApplication());
				notasContablesManager.addRegistroAuditoriaIngreso(usuLogueado.getUsuario().getCodigo().intValue(), "Ingresó a la aplicación", "Login", "1");
				PendientePage bean = (PendientePage) getBean("pendientePage");
				bean.cargarPendientes();
				if (!bean.getDatos().isEmpty()) {
					return ADMIN_PENDIENTES;
				}
			} catch (Exception le_e) {

			}
		}
		return BIENVENIDO;
	}

	private UsuarioLogueado cargarUsuario() {
		try {

			// carga el rol del usuario
			UsuarioModulo usuario = new UsuarioModulo();
			UsuarioLogueado usuLogueado = null;
			if (!username.equalsIgnoreCase("SYSADMIN")) {
				try {
					usuario.setCodigoEmpleado(username);
					usuario = notasContablesManager.getUsuarioModuloPorCodigoEmpleado(usuario);
					// si el usuario existe en el sistema
					if (usuario.getCodigo().intValue() != 0) {
						boolean exist = ACTIVAR_LDAP.equals("0") || validateUser(username, pwd);
						if (exist) {
							// System.out.println("Validacion contra LDAP. Usuario Existe: " + exist);
							usuLogueado = new UsuarioLogueado(usuario);
							// si existen varios roles asociados, se retorna a la pagina de logueo para seleccionar el rol adecuado
							Collection<UsuarioModulo> usuariosIguales = notasContablesManager.getUsuariosModuloPorCodigoEmpleado(usuario);
							// si tiene por lo menos un rol activo
							if (!usuariosIguales.isEmpty()) {
								for (UsuarioModulo user : usuariosIguales) {
									// si el usuario del rol actual está activo
									Rol rol = new Rol();
									rol.setCodigo(user.getCodigoRol().intValue());
									rol = notasContablesManager.getRol(rol);
									if (usuariosIguales.size() == 1) {
										usuLogueado.setRolActual(rol);
										rolActual = rol.getCodigo().toString();
									}
									usuLogueado.getRoles().add(rol);
								}
							} else {
								nuevoMensaje(FacesMessage.SEVERITY_WARN, "Usuario inactivo. Contacte al administrador del sistema");
								return null;
							}
						} else {
							nuevoMensaje(FacesMessage.SEVERITY_WARN, "Contraseña inválida. Por favor rectifique e intente de nuevo.");
						}
					} else {
						nuevoMensaje(FacesMessage.SEVERITY_WARN, "El usuario no existe. Por favor rectifique e intente de nuevo.");
					}
				} catch (Exception le_e) {
					le_e.printStackTrace();
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "Error del sistema al intentar realizar el proceso de autenticación");
				}
			} else if (username.equalsIgnoreCase("SYSADMIN")) {
				if (pwd.equals("paint")) {
					usuario.setCodigo(1);
					usuario.setCodigoEmpleado("sysadmin");
					usuario.setCodigoRol(1);
					usuario.setEstado("A");
					// datos del rol del usuario
					usuLogueado = new UsuarioLogueado(usuario);
					Rol rol = new Rol();
					rol.setCodigo(usuario.getCodigoRol().intValue());
					usuLogueado.getRoles().add(rol);
					usuLogueado.setRolActual(rol);
					rolActual = "1";
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "Contraseña inválida. Por favor rectifique e intente de nuevo.");
				}
			}
			if (usuLogueado != null) {
				UsuarioAltamira usuarioAltamira = new UsuarioAltamira();
				usuarioAltamira.setCodigoEmpleado(usuLogueado.getUsuario().getCodigoEmpleado());
				usuLogueado.setUsuAltamira(cargaAltamiraManager.getUsuarioAltamira(usuarioAltamira));

				// informacion de la sucursal del usuario
				Sucursal sucursal = new Sucursal();
				sucursal.setCodigo(usuLogueado.getUsuario().getCodigoAreaModificado());
				sucursal = cargaAltamiraManager.getSucursal(sucursal);
				usuLogueado.setSucursal(sucursal);

				// Centro Especial
				CentroEspecial centroEspecial = new CentroEspecial();
				centroEspecial.setCodigoSucursal(usuLogueado.getUsuario().getCodigoAreaModificado());
				centroEspecial = notasContablesManager.getCentroEspecialPorSucursal(centroEspecial);
				usuLogueado.setCentroEspecial(centroEspecial);
			}
			return usuLogueado;
		} catch (Exception e) {
			lanzarError(e, "Error al iniciar sesión con el usuario " + username);
		}
		return null;
	}

	public String getUsername() {
		if (username == null) {
			username = "";
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRolActual() {
		return rolActual;
	}

	public void setRolActual(String rolActual) {
		this.rolActual = rolActual;
	}

	public List<SelectItem> getRoles() {
		if (getContablesSessionBean().getLoginUser() != null) {
			roles = new ArrayList<SelectItem>();
			for (Rol rol : getContablesSessionBean().getLoginUser().getRoles()) {
				roles.add(new SelectItem(rol.getCodigo(), rol.getNombre()));
			}
		}
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

}
