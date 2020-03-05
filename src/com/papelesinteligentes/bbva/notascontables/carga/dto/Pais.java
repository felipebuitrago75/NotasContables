package com.papelesinteligentes.bbva.notascontables.carga.dto;

import java.io.Serializable;

import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;

public class Pais extends CommonVO<Pais> implements Serializable {

	private static final long serialVersionUID = -6568299162456667984L;
	private String nombre;
	private String prefijo;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrefijo() {
		return prefijo;
	}

	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}

	@Override
	public Object getPK() {
		return prefijo;
	}

}
