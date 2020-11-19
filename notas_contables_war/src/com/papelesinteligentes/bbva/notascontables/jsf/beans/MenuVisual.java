package com.papelesinteligentes.bbva.notascontables.jsf.beans;

import java.util.ArrayList;
import java.util.List;

import com.papelesinteligentes.bbva.notascontables.dto.Menu;

public class MenuVisual implements java.io.Serializable, Comparable<MenuVisual> {

	private static final long serialVersionUID = -4411745472093058822L;

	private int codigo = 0;
	private int ordenVisual = 0;
	private String nombre = "";

	private List<String> menuItems = new ArrayList<String>();

	public MenuVisual(Menu m) {
		this.codigo = m.getCodigo();
		this.nombre = m.getNombre();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getOrdenVisual() {
		return ordenVisual;
	}

	public void setOrdenVisual(int ordenVisual) {
		this.ordenVisual = ordenVisual;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int compareTo(MenuVisual o) {
		return o.getOrdenVisual() - getOrdenVisual();
	}

	public List<String> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<String> menuItems) {
		this.menuItems = menuItems;
	}

}
