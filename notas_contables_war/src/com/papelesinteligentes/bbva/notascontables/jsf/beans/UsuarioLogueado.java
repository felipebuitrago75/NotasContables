package com.papelesinteligentes.bbva.notascontables.jsf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;  
import java.util.TreeSet;
 
import javax.el.MethodExpression; 
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.richfaces.component.html.HtmlMenuItem;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.carga.dto.UsuarioAltamira;
import com.papelesinteligentes.bbva.notascontables.dto.CentroEspecial;
import com.papelesinteligentes.bbva.notascontables.dto.Menu;
import com.papelesinteligentes.bbva.notascontables.dto.Rol;
import com.papelesinteligentes.bbva.notascontables.dto.SubMenu;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;

public class UsuarioLogueado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7935040122801785429L;

	private UsuarioModulo usuario;
	private UsuarioAltamira usuAltamira;
	private Sucursal sucursal;
	private CentroEspecial centroEspecial;
	private Rol rolActual;

	private TreeMap<Menu, TreeSet<SubMenu>> menu;
	private ArrayList<MenuVisual> opcionesMenu;
	private Collection<Rol> roles;

	private Application application;

	public UsuarioLogueado(UsuarioModulo usuario) {
		this.usuario = usuario;
		usuAltamira = new UsuarioAltamira();
		rolActual = null;
		roles = new ArrayList<Rol>();
		menu = new TreeMap<Menu, TreeSet<SubMenu>>();
		sucursal = new Sucursal(); 
	}

	public UsuarioModulo getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioModulo usuario) {
		this.usuario = usuario;
	}

	public Rol getRolActual() {
		return rolActual;
	}

	public void setRolActual(Rol rolActual) {
		this.rolActual = rolActual;
	} 

	public Collection<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Rol> roles) {
		this.roles = roles;
	}

	public UsuarioAltamira getUsuAltamira() {
		return usuAltamira;
	}

	public void setUsuAltamira(UsuarioAltamira usuAltamira) {
		this.usuAltamira = usuAltamira;
	}

	public TreeMap<Menu, TreeSet<SubMenu>> getMenu() {
		return menu;
	}

	public void setMenu(TreeMap<Menu, TreeSet<SubMenu>> menu, Application application) {
		this.menu = menu;
		this.application = application;
	}

	public ArrayList<MenuVisual> getOpcionesMenu() {
		if (opcionesMenu == null || opcionesMenu.isEmpty()) {
			opcionesMenu = new ArrayList<MenuVisual>();
			for (Menu m : menu.keySet()) {
				MenuVisual menuVisual = new MenuVisual(m);
				menuVisual.setMenuItems(getMenuList(menu.get(m)));
				opcionesMenu.add(menuVisual);
			}
		}
		return opcionesMenu;
	} 
  
	private List<HtmlMenuItem> getMenuList(TreeSet<SubMenu> opciones) {
		LinkedList<HtmlMenuItem> menuList = new LinkedList<HtmlMenuItem>();
		for (SubMenu sm : opciones) {
			// make binding
			HtmlMenuItem htmlMenuItem = new HtmlMenuItem();
			Class<?>[] params = {};
			MethodExpression actionExpression = application.getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), sm.getAccion(), String.class, params);
			htmlMenuItem.setActionExpression(actionExpression);  
			htmlMenuItem.setDisabled(false);
			htmlMenuItem.setValue(sm.getNombre());
			menuList.add(htmlMenuItem);
		}
		return menuList;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public CentroEspecial getCentroEspecial() {
		return centroEspecial;
	}

	public void setCentroEspecial(CentroEspecial centroEspecial) {
		this.centroEspecial = centroEspecial;
	}

}
