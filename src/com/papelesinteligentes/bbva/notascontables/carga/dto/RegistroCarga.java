package com.papelesinteligentes.bbva.notascontables.carga.dto;

import java.sql.Timestamp;

import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;

public class RegistroCarga extends CommonVO<RegistroCarga> implements java.io.Serializable {

	private static final long serialVersionUID = 7227890757063387387L;
	private int codigo = 0;
	private Timestamp fechaInicio = null;
	private Timestamp fechaFin = null;
	private String nombreArchivo = "";
	private int registrosCargados = 0;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Timestamp getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Timestamp getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public int getRegistrosCargados() {
		return registrosCargados;
	}

	public void setRegistrosCargados(int registrosCargados) {
		this.registrosCargados = registrosCargados;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

}
