package com.papelesinteligentes.bbva.notascontables.carga.dto;

import java.sql.Timestamp;

import com.papelesinteligentes.bbva.notascontables.anotacion.Columna;
import com.papelesinteligentes.bbva.notascontables.anotacion.Tabla;
import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;

@Tabla(nombreTabla = "NC_DIVISA")
public class Divisa extends CommonVO<Divisa> implements java.io.Serializable, Comparable<Divisa> {

	private static final long serialVersionUID = -34425606193180486L;
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private String codigo = "";
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstadoCarga() {
		return estadoCarga;
	}

	public void setEstadoCarga(String estadoCarga) {
		this.estadoCarga = estadoCarga;
	}

	public Timestamp getFechaUltimaCarga() {
		return fechaUltimaCarga;
	}

	public void setFechaUltimaCarga(Timestamp fechaUltimaCarga) {
		this.fechaUltimaCarga = fechaUltimaCarga;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public int compareTo(Divisa o) {
		return codigo.compareTo(o.getCodigo());
	}

}
