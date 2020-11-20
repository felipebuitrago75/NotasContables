package com.papelesinteligentes.bbva.notascontables.jsf.adminnota;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.dto.Rol;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioInstancias;
import com.papelesinteligentes.bbva.notascontables.jsf.consultas.GeneralConsultaPage;

@ViewScoped
public class CargaUsuarioPage extends GeneralConsultaPage<UsuarioInstancias> {

	private static final long serialVersionUID = -6709113217662690209L;

	private List<SelectItem> sucursales = new ArrayList<SelectItem>();
	private List<SelectItem> roles = new ArrayList<SelectItem>();

	private String estado = "";
	private String sucursal = "";
	private Integer rol;

	public CargaUsuarioPage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		cargarFiltros();
	}

	public void cargarFiltros() {
		try {
			if (sucursales == null || sucursales.isEmpty()) {
				sucursales = getSelectItemList(notasContablesManager.getCV(Sucursal.class));
				roles = getSelectItemList(notasContablesManager.getCV(Rol.class), false);
			}
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al inicializar el módulo de carga por usuario");
		}
	}

	public String mostrar() {
		return _getPage();
	}

	@Override
	protected Collection<UsuarioInstancias> _buscar() throws Exception {
		return notasContablesManager.getInstanciasPorEstadoYSucursalOrigenYRol(estado, sucursal, rol);
	}

	@Override
	protected void _validar() throws Exception {
		// validador.validarSeleccion(sucursal, "Sucursal Origen");
		// validador.validarSeleccion(estado, "Estado");
		// if (rol.intValue() < 0) {
		// nuevoMensaje(FacesMessage.SEVERITY_WARN, "Seleccione una opción para el campo 'Rol'");
		// }
	}

	@Override
	protected String _getPage() {
		return CARGA_USUARIO;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getRol() {
		return rol;
	}

	public void setRol(Integer rol) {
		this.rol = rol;
	}

}
